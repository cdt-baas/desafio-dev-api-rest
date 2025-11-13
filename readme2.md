# 🚀 API Digital Account - FastAPI e Docker

Este projeto **simula uma API** para gerenciamento de contas digitais e seus titulares.  
O objetivo é demonstrar o funcionamento e lógica do serviço de forma simples.  
A API foi construída em Python usando FastAPI, e preparada para rodar com container (Docker).  
Existem pontos de melhoria a implementar, por exemplo: autenticação, logs, implementação real com banco de dados, melhoria de testes. 


## 1️⃣ Construir a imagem Docker
docker build -t minha-api .

## 2️⃣ Rodar o container
docker run -d -p 8000:8000 --name meu-container-api minha-api

## 3️⃣ Acessar API e Documentação
Para acessar a API e executar requisições é recomendável o uso do Insomnia.  
Serviço disponível em: http://localhost:8000  
Documentação disponível em: http://localhost:8000/docs

## 4️⃣ Ver logs do container
docker logs -f meu-container-api

## 5️⃣ Parar e remover o container
docker stop meu-container-api
docker rm meu-container-api

## 6️⃣ Atualizar a aplicação
Após alterar o código, reconstrua e rode o container:

docker build -t minha-api .  
docker stop meu-container-api  
docker rm meu-container-api  
docker run -d -p 8000:8000 --name meu-container-api minha-api

## 7️⃣ Desenvolvimento com hot reload (opcional)
Este comando sincroniza o código local com o container em tempo real.  
Para evitar rebuilds constantes durante o desenvolvimento, monte um volume:  

docker run -d -p 8000:8000 -v $(pwd)/app:/app --name meu-container-api minha-api
