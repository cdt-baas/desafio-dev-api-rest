from datetime import datetime
from pydantic import BaseModel, Field, constr
from app.models.enums import AccountStatus


class AccountCreate(BaseModel):
    """Model that represents a digital account (Create)"""
    agency: int = Field(..., description="Account agency")
    holder_cpf: constr(regex=r'^\d{11}$') = Field(..., description="Account holder CPF, only numbers (11 digits)")


class Account(AccountCreate):
    """Model that represents a digital account"""
    number: int = Field(..., description="Account number")
    balance: float = Field(default=0.0, description="Current balance")
    status: AccountStatus = Field(default=True, description="Indicates whether the account is active")
    blocked: bool = Field(default=False, description="Indicates whether the account is blocked")
    created_at: datetime = Field(default_factory=datetime.now(), description="Creation datetime")

    class Config:
        from_attributes = True
        json_schema_extra = {
            "example": {
                "number": 123456,
                "agency": 1234,
                "balance": 2500.75,
                "status": AccountStatus.ACTIVE,
                "blocked": False,
                "holder_cpf": "12345678901",
                "created_at": datetime(year=2021, month=9, day=27)
            }
        }
