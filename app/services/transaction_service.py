from typing import List, Optional
from app.models.transaction_model import Transaction
from app.repositories.transaction_repository import TransactionRepository


class TransactionService:
    def __init__(self, repository: TransactionRepository):
        self._repository = repository

    def save(self, account_number: int, transaction: Transaction) -> Transaction:
        """
        Save the transaction in the repository.
        Args:
            account_number: account number
            transaction: transaction model
        Returns:
            the transaction saved
        """
        return self._repository.save(account_number, transaction)

    def get_transactions(self, account_number: int, start_date, end_date) -> Optional[List[Transaction]]:
        """
        Gets a list of transactions.
        Args:
            account_number: account number
            start_date: period start date
            end_date: period end date
        Returns:
            a list with all transactions from an account
        """
        return self._repository.get_transactions(account_number, start_date, end_date)

    def get_withdrawal_transactions(self, account_number: int, start_date, end_date) -> Optional[
        List[Transaction]]:
        """
        Gets all withdrawal transactions from an account.
        Args:
            account_number: account number
            start_date: period start date
            end_date: period end date
        Returns:
            a list of all withdrawal transactions
        """
        return self._repository.get_withdrawal_transactions(account_number, start_date, end_date)
