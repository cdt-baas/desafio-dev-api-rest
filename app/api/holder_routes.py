from fastapi import APIRouter, HTTPException, status
from fastapi.responses import JSONResponse
from app.models.holder_model import Holder
from app.repositories.holder_repository import HolderRepository
from app.services.holder_service import HolderService

router = APIRouter()

_holder_service = HolderService(HolderRepository())

@router.post("/", response_model=Holder, status_code=status.HTTP_201_CREATED)
async def save(holder: Holder):
    return _holder_service.save(holder)

@router.delete("/{cpf}", status_code=status.HTTP_204_NO_CONTENT)
async def delete(cpf: str):
    return _holder_service.delete(cpf)

@router.get("/", status_code=status.HTTP_200_OK)
async def get():
    return _holder_service.all()