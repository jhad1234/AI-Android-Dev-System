import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    HOST: str = os.getenv("HOST", "127.0.0.1")
    PORT: int = int(os.getenv("PORT", 8000))
    OLLAMA_BASE_URL: str = os.getenv("OLLAMA_BASE_URL", "http://127.0.0.1:11434")
    MODEL_NAME: str = os.getenv("MODEL_NAME", "llama3")
    SSH_TUNNEL_URL: str = os.getenv("SSH_TUNNEL_URL", "ssh.localhost.run")
    LOCAL_TUNNEL_SUBDOMAIN: str = os.getenv("LOCAL_TUNNEL_SUBDOMAIN", "ai-os-hub")

    class Config:
        env_file = ".env"

settings = Settings()
