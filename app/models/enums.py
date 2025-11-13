from enum import Enum


class AccountStatus(str, Enum):
    ACTIVE = "active"
    INACTIVE = "inactive"


class TransactionType(str, Enum):
    DEPOSIT = "deposit"
    WITHDRAWAL = "withdrawal"
