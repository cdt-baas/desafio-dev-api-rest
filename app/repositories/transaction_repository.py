from typing import List, Dict, Optional
from collections import defaultdict
from app.models.enums import TransactionType
from app.models.transaction_model import Transaction


class TransactionRepository:
    def __init__(self):
        # In-memory database simulation
        #self.__transactions: defaultdict(list)
        self.__transactions: defaultdict[str, List[Transaction]] = defaultdict(list)

    def save(self, account_number: int, transaction: Transaction) -> Transaction:
        """
        Save the transaction in the repository.
        Args:
            account_number: account number
            transaction: transaction model
        Returns:
            the transaction saved
        """
        self.__transactions[str(account_number)].append(transaction)
        return transaction

    def get_transactions(self, account_number: int, start_date, end_date) -> Optional[List[Transaction]]:
        """
        Get a list of transactions.
        Args:
            account_number: account number
            start_date: start date
            end_date: end date
        Returns:
            a list with all transactions from an account
        """
        transactions = self.__transactions.get(str(account_number))
        return list(event for event in transactions if start_date <= event.date <= end_date)

    def get_withdrawal_transactions(self, account_number: int, start_date, end_date) -> Optional[List[Transaction]]:
        """
        Get all withdrawal transactions from an account.
        Args:
            account_number: account number
            start_date: start date
            end_date: end date
        Returns:
            a list of all withdrawal transactions
        """
        transactions = self.__transactions.get(str(account_number))
        return list(event for event in transactions if start_date <= event.date <= end_date
                    and event.type == TransactionType.WITHDRAWAL)
