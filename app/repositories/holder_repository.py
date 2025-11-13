from typing import List, Dict, Optional

from app.models.holder_model import Holder


class HolderRepository:
    def __init__(self):
        # In-memory database simulation
        self.__holders: Dict[str, Holder] = {}

    def save(self, holder) -> Holder:
        """
        Save the holder in the repository.
        Args:
            holder: holder model
        Returns:
            the holder saved
        """
        self.__holders[holder.cpf] = holder
        return holder

    def delete(self, cpf: str) -> None:
        """
        Delete a holder by cpf.
        Args:
            cpf: holder cpf
        """
        del self.__holders[cpf]

    def get_holder_by_cpf(self, cpf: str) -> Optional[Holder]:
        """
        Get a holder by cpf.
        Args:
            cpf: holder cpf
        Returns:
            a holder if exists, None otherwise
        """
        try:
            return self.__holders[cpf]
        except KeyError:
            return None

    def all(self) -> List[Holder]:
        """
        Get all holders.
        Returns:
            A list of all holders
        """
        return list(self.__holders.values())