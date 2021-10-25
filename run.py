import os
from app import app
from database import criar_base

# funçao que verificar se tem base criada, caso necessario cria
criar_base()

if __name__ == '__main__':
    port = int(os.environ.get("PORT", 5000))
    # app.run(host='0.0.0.0', port=port)
    app.run()
