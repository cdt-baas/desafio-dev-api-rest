import secrets
import string
from validate_docbr import CPF
from datetime import datetime, time


def is_valid_cpf(cpf: str) -> bool:
    validator = CPF()
    return validator.validate(cpf)

def create_random_account_number() -> int:
    return int(''.join(secrets.choice(string.digits) for i in range(6)))

def day_start():
    now = datetime.now()
    start = datetime.combine(now.date(), time.min)  # 00:00:00
    return start

def day_end():
    now = datetime.now()
    end = datetime.combine(now.date(), time.max)     # 23:59:59.999999
    return end
