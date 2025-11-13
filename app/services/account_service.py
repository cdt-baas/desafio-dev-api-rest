from uuid import uuid4
from datetime import datetime
from typing import List
from fastapi import HTTPException, status
from app.models.enums import AccountStatus, TransactionType
from app.models.account_model import Account, AccountCreate
from app.models.transaction_model import Transaction
from app.repositories.account_repository import AccountRepository
from app.services.holder_service import HolderService
from app.services.transaction_service import TransactionService
from app.utils import utils
from app import config


class AccountService:
    def __init__(self, repository: AccountRepository, holder_service: HolderService,
                 transaction_service: TransactionService):
        self._repository = repository
        self._holder_service = holder_service
        self._transaction_service = transaction_service

    def create(self, account: AccountCreate) -> Account:
        """
        Create the account in the repository
        Args:
            account: account to create
        Returns:
            the account created
        """
        if not utils.is_valid_cpf(account.holder_cpf):
            raise HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Invalid CPF.")
        if not self._holder_service.get_holder_by_cpf(account.holder_cpf):
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="The holder isn't registered in the system base.")

        new_acc = Account(agency=account.agency, holder_cpf=account.holder_cpf, number=utils.create_random_account_number(),
                      balance=0.0, status=AccountStatus.ACTIVE, blocked=False, created_at=datetime.now())

        return self._repository.save(new_acc)

    def change_account_status(self, account_number: int, new_status: AccountStatus):
        """
        Change the status of the account
        Args:
            account_number: account number
            new_status: new account status
        Returns:
            the account changed
        """
        account = self._repository.get_account_by_number(account_number)
        if account:
            account.status = new_status
            return self._repository.save(account)
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="The account doesn't exist.")

    def change_account_blocked(self, account_number: int, blocked: bool):
        """
        Block or unblock the account
        Args:
            account_number: account number
            blocked: boolean flag
        Returns:
            the account changed
        """
        account = self._repository.get_account_by_number(account_number)
        if account:
            account.blocked = blocked
            return self._repository.save(account)
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="The account doesn't exist.")

    def deposit(self, account_number: int, amount: float):
        """
        Makes a deposit into the account and creates a new transaction
        Args:
            account_number: account number
            amount: amount to deposit
        Returns:
            the account changed
        """
        account = self._repository.get_account_by_number(account_number)
        if account:
            if self.__is_valid_account(account):
                account.balance += amount
                self._repository.save(account)
                self._transaction_service.save(account_number, Transaction(id=str(uuid4()), type=TransactionType.DEPOSIT,
                                                                           amount=amount, date=datetime.now()))
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="The account doesn't exist.")
        return account

    def withdraw(self, account_number: int, amount: float):
        """
        Makes a withdrawal into the account and creates a new transaction
        Args:
            account_number: account number
            amount: amount to withdraw
        Returns:
            the account changed
        """
        account = self._repository.get_account_by_number(account_number)
        if account:
            if self.__is_valid_account(account) and self.__is_withdrawal_permitted(account, amount):
                account.balance -= amount
                self._repository.save(account)
                self._transaction_service.save(account_number, Transaction(id=str(uuid4()), type=TransactionType.WITHDRAWAL,
                                                                           amount=amount, date=datetime.now()))
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="The account doesn't exist.")
        return account

    def get_account_statement(self, account_number: int, start_date: datetime, end_date: datetime) -> List[Transaction]:
        """
        Gets a list of all accounts' transactions by period
        Args:
            account_number: account number
            start_date: period start date
            end_date: period end date
        Returns:
            a list with all transactions in the period
        """
        account = self._repository.get_account_by_number(account_number)
        if account:
            return self._transaction_service.get_transactions(account_number, start_date, end_date)
        else:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="The account doesn't exist.")

    def __get_total_withdrawal_amount(self, account_number: int, start_date: datetime, end_date: datetime) -> float:
        """
        Gets the total amount of withdrawals for the current day
        Args:
            account_number: account number
            start_date: start date
            end_date: end date
        Returns:
            a value(float) of the total amount of withdrawals for the current day
        """
        withdrawals = self._transaction_service.get_withdrawal_transactions(account_number, start_date, end_date)
        return sum(t.amount for t in withdrawals)

    def __is_withdrawal_permitted(self, account: Account, amount: float) -> bool:
        """
        Verify if the account is withdrawable or not
        Args:
            account: account to verify
            amount: amount to verify
        Returns:
            returns True if the account is withdrawable or not
        """
        if (account.balance - amount) < 0:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Insufficient funds.")
        total = self.__get_total_withdrawal_amount(account.number, utils.day_start(), utils.day_end())
        if (total + amount) > config.WITHDRAWAL_LIMIT:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Daily limit exceeded.")
        return True

    @staticmethod
    def __is_valid_account(account: Account) -> bool:
        """
        Verify if the account is valid
        Args:
            account: account to verify
        Returns:
            true if the account is valid
        """
        if account.status == AccountStatus.INACTIVE or account.blocked:
            raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail="Account inactive or blocked.")
        return True

    def all(self) -> List[Account]:
        """
        Get all accounts
        Returns:
            a list of accounts
        """
        return self._repository.all()