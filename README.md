# ✂️ EncurtaUrl - encurtador de URLs simples desenvolvido em Spring Boot

![Imagem demonstrando o funcionamento da API, informando uma URL alvo e recebendo a URL encurtada](https://i.imgur.com/6rkAqac.png)

## 💻 Ambiente de teste
- Linux Mint 22.2
- OpenJDK 24.0.2 (versão mínima: 17)
- Spring Boot 3.5.7
- IntelliJ IDEA
- PostgreSQL 15 em um container Docker

## ⚙️ Configuração
- Você deve editar o **application.properties** *(localizado em src/main/resources)* e alterar o valor da propriedade **encurtaurl.base-url** para a URL base do seu servidor *(já está configurado para localhost por padrão)*. 
- Altere também os dados da conexão com o banco de dados.

## 📋 Lista de endpoints da API

### 1. Registrar/encurtar uma URL

*   **Método:** `POST`
*   **Caminho:** `/register`
*   **Descrição:** Recebe a URL alvo (enviada no corpo da requisição em formato JSON) e a encurta.
*   **Resposta:**
    *   `201 Created`: Retorna a URL encurtada e a timestamp da criação.

        ```json
        {
            "shortenedUrl": "http://127.0.0.1:8080/OMUwE0",
            "createdAt": "2025-11-05T21:08:36.060444315"
        }

### 2. Listar todas as URLs

*   **Método:** `GET`
*   **Caminho:** `/urls`
*   **Descrição:** Lista todas as URLs registradas/encurtadas.
*   **Resposta:**
    *   `200 OK`: Retorna todas as URLs registradas/encurtadas (juntamente com o ID, URL alvo e timestamp da criação).

        ```json
        [
            {
                "id": 1,
                "targetUrl": "https://google.com",
                "shortenedUrl": "http://127.0.0.1:8080/xBU5XX",
                "createdAt": "2025-11-06T03:25:02.833156"
            },
            {
                "id": 2,
                "targetUrl": "https://github.com/thiimont",
                "shortenedUrl": "http://127.0.0.1:8080/jZkKKE",
                "createdAt": "2025-11-06T03:26:05.239215"
            }
        ]

### 3. Redirecionamento

*   **Método:** `GET`
*   **Caminho:** `/{shortCode}`
*   **Descrição:** Recebe o código da URL encurtada, pesquisa a URL alvo no banco de dados e realiza o redirecionamento, caso encontrada.
*   **Resposta:**
    *   `302 Found`: Redireciona para a URL alvo.

## 🧩 Dependências
- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Lombok
