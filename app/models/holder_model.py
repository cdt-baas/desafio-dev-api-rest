from pydantic import BaseModel, Field, constr


class Holder(BaseModel):
    """Model that represents a digital account holder"""
    cpf: constr(regex=r'^\d{11}$') = Field(..., description="Holder CPF")
    name: str = Field(..., description="Holder full name")

    class Config:
        from_attributes = True
        json_schema_extra = {
            "example": {
                "cpf": "12345678901",
                "name": "Holder Full Name"
            }
        }
