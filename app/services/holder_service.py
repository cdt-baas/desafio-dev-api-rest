from typing import List, Optional
from fastapi import HTTPException, status
from app.models.holder_model import Holder
from app.repositories.holder_repository import HolderRepository
from app.utils.utils import is_valid_cpf


class HolderService:
    def __init__(self, repository: HolderRepository):
        self._repository = repository

    def save(self, holder: Holder) -> Holder:
        """
        Save the holder in the repository.
        Args:
            holder: holder model
        Returns:
            the holder model saved
        """
        if not is_valid_cpf(holder.cpf):
            raise HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail="Invalid CPF.")
        if self._repository.get_holder_by_cpf(holder.cpf):
            raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail="CPF already exists.")

        return self._repository.save(holder)

    def delete(self, cpf: str) -> bool:
        """
        Delete a holder by cpf
        Args:
            cpf: holder CPF
        Returns:
            True if a holder was deleted, returns False otherwise
        """
        if self._repository.get_holder_by_cpf(cpf) is not None:
            self._repository.delete(cpf)
            return True
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="CPF not registered.")

    def get_holder_by_cpf(self, cpf: str) -> Optional[Holder]:
        """
        Get a holder by cpf
        Args:
            cpf: holder cpf
        Returns:
            a holder if exists, None otherwise
        """
        holder = self._repository.get_holder_by_cpf(cpf)
        if holder:
            return holder
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="CPF not registered.")

    def all(self) -> List[Holder]:
        """
        Get all holders
        Returns:
            a list of holders
        """
        return self._repository.all()
