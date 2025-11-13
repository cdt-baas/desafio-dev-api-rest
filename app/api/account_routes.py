from datetime import datetime, time
from fastapi import APIRouter, status
from app.models.account_model import Account, AccountCreate
from app.models.enums import AccountStatus
from app.repositories.account_repository import AccountRepository
from app.repositories.transaction_repository import TransactionRepository
from app.services.account_service import AccountService
from app.services.transaction_service import TransactionService
from app.api.holder_routes import _holder_service


router = APIRouter()

_account_service = AccountService(AccountRepository(), _holder_service,
                                 TransactionService(TransactionRepository()) )

@router.post("/", response_model=Account, status_code=status.HTTP_201_CREATED)
async def create_account(account: AccountCreate):
    return _account_service.create(account)

@router.post("/{account_number}/deposit", status_code=status.HTTP_200_OK)
async def deposit(account_number: int, amount: float):
    return _account_service.deposit(account_number, amount)

@router.post("/{account_number}/withdraw", status_code=status.HTTP_200_OK)
async def withdraw(account_number: int, amount: float):
    return _account_service.withdraw(account_number, amount)

@router.get("/{account_number}/statement", status_code=status.HTTP_200_OK)
async def get_account_statement(account_number: int, start_date: str, end_date: str):
    start_date = datetime.combine(datetime.fromisoformat(start_date), time.min)
    end_date = datetime.combine(datetime.fromisoformat(end_date), time.max)
    return _account_service.get_account_statement(account_number, start_date, end_date)

@router.post("/{account_number}/close", status_code=status.HTTP_200_OK)
async def close_account(account_number: int):
    return _account_service.change_account_status(account_number, AccountStatus.INACTIVE)

@router.post("/{account_number}/open", status_code=status.HTTP_200_OK)
async def close_account(account_number: int):
    return _account_service.change_account_status(account_number, AccountStatus.ACTIVE)

@router.post("/{account_number}/block", status_code=status.HTTP_200_OK)
async def block_account(account_number: int):
    return _account_service.change_account_blocked(account_number, True)

@router.post("/{account_number}/unblock", status_code=status.HTTP_200_OK)
async def unblock_account(account_number: int):
    return _account_service.change_account_blocked(account_number, False)

@router.get("/", status_code=status.HTTP_200_OK)
async def get():
    return _account_service.all()