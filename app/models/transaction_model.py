import uuid
from datetime import datetime
from pydantic import BaseModel, Field
from app.models.enums import TransactionType


class Transaction(BaseModel):
    """Model that represents a bank transaction."""
    id: str = Field(default_factory=uuid.uuid4(), description="Transaction ID")
    type: TransactionType = Field(..., description="Transaction type, ex: 'deposit', 'withdrawal'")
    amount: float = Field(..., description="Transaction amount, must be positive")
    date: datetime = Field(default_factory=datetime.now(), description="Transaction datetime")
