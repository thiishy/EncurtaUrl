# ✂️ EncurtaUrl - encurtador de URLs simples desenvolvido em Spring Boot

![Imagem demonstrando o funcionamento da API, informando uma URL alvo e recebendo a URL encurtada](https://i.imgur.com/VZukhck.png)

## 💻 Ambiente de teste
- Linux Mint 22.2
- OpenJDK 24.0.2 (versão mínima: 17)
- Spring Boot 3.5.7
- IntelliJ IDEA
- PostgreSQL 15 em um container Docker

## ⚙️ Configuração
- Você deve editar o **application.properties** *(localizado em src/main/resources)* e alterar o valor da propriedade **encurtaurl.base-url** para a URL base do seu servidor *(já está configurado para localhost por padrão)*. 
- Altere também os dados da conexão com o banco de dados.

## 📍 Lista de endpoints da API

### 1. Listar todas as URLs

*   **Método:** `GET`
*   **Caminho:** `/urls?page=0 (padrão: 0)`
*   **Descrição:** Lista todas as URLs registradas/encurtadas divididas em páginas com 10 registros cada por padrão.
*   **Resposta:**
    *   `200 OK`

        ```json
        {
            "content": [
                {
                    "id": 17,
                    "targetUrl": "http://n.com",
                    "shortenedUrl": "http://127.0.0.1:8080/hRzyZC",
                    "createdAt": "2025-11-08T18:40:13.93834"
                },
                {
                    "id": 16,
                    "targetUrl": "http://m.com",
                    "shortenedUrl": "http://127.0.0.1:8080/huFp2b",
                    "createdAt": "2025-11-08T18:40:09.284384"
                },
                ...
            ],
            "pageable": {
                "pageNumber": 0,
                "pageSize": 10,
                "sort": {
                    "sorted": true,
                    "empty": false,
                    "unsorted": false
                },
                "offset": 0,
                "paged": true,
                "unpaged": false
            },
            "totalPages": 2,
            "totalElements": 15,
            "last": false,
            "size": 10,
            "number": 0,
            "numberOfElements": 10,
            "sort": {
                "sorted": true,
                "empty": false,
                "unsorted": false
            },
            "first": true,
            "empty": false
        }
        ```

### 2. Registrar/encurtar uma URL

*   **Método:** `POST`
*   **Caminho:** `/register`
*   **Descrição:** Recebe a URL alvo (enviada no corpo da requisição em formato JSON) e a encurta.
*   **Exemplo de requisição:**

       ```json
            {
                "targetUrl": "https://google.com"
            }
    
*   **Resposta:**
    *   `201 Created`

        ```json
            {
                "id": 1,
                "targetUrl": "https://google.com",
                "shortenedUrl": "http://127.0.0.1:8080/xBU5XX",
                "createdAt": "2025-11-06T03:25:02.833156"
            }

### 3. Excluir uma URL encurtada

*   **Método:** `DELETE`
*   **Caminho:** `/delete/{id}`
*   **Descrição:** Recebe o ID da URL encurtada e realiza a exclusão caso ela exista.
*   **Resposta:**
    *   `204 No Content`

### 4. Redirecionamento

*   **Método:** `GET`
*   **Caminho:** `/{shortCode}`
*   **Resposta:**
    *   `302 Found`

## 🧩 Dependências
- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Lombok

Projeto inicializado com Maven - confira o **pom.xml** para mais detalhes.

## 📋 A fazer
- [ ] Rate limiting
- [ ] Autenticação
- [ ] Caching

*Aceito sugestões!*
