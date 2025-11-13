from fastapi import FastAPI
from app.api.holder_routes import router as holder_router
from app.api.account_routes import router as account_router

app = FastAPI(
    title="Digital Account API",
    description="API for managing digital accounts and holders",
    version="1.0.0"
)

app.include_router(holder_router, prefix="/holders", tags=["Holders"])
app.include_router(account_router, prefix="/accounts", tags=["Accounts"])
