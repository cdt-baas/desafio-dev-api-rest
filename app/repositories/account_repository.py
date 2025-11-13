from typing import List, Dict, Optional

from app.models.account_model import Account


class AccountRepository:
    def __init__(self):
        # In-memory database simulation
        self.__accounts: Dict[str, Account] = {}

    def save(self, account: Account) -> Account:
        """
        Save the account in the repository.
        Args:
            account: account model
        Returns:
            the account saved
        """
        self.__accounts[str(account.number)] = account
        return account

    def get_account_by_number(self, account_number: int) -> Optional[Account]:
        """
        Gets an account by number
        Args:
            account_number: account number
        Returns:
            an account if exists, None otherwise
        """
        try:
            return self.__accounts.get(str(account_number))
        except KeyError:
            return None

    def get_account_by_holder_cpf(self, cpf: str) -> List[Account]:
        """
        Gets all accounts associated with a holder
        Args:
            cpf: holder cpf
        Returns:
            list of all holders' accounts if exists, None otherwise
        """
        return [account for account in self.__accounts.values() if account.holder_cpf == cpf]

    def delete(self, account_number: str) -> None:
        """
        Delete an account by number
        Args:
            account_number: account number
        """
        del self.__accounts[account_number]

    def all(self) -> List[Account]:
        """
        Gets all accounts
        Returns:
            A list of all accounts
        """
        return list(self.__accounts.values())
