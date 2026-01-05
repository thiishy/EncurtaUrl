# ✂️ EncurtaUrl - encurtador de URLs simples desenvolvido em Spring Boot

![Imagem demonstrando o funcionamento da API, informando uma URL alvo e recebendo a URL encurtada](https://i.imgur.com/VZukhck.png)

## 📦 Pré-requisitos
- Java JDK 17+ [Download](https://openjdk.org/)
- Maven [Download](https://maven.apache.org/download.cgi)
- PostgreSQL 15+ [Download](https://www.postgresql.org/download/)

## ⚙️ Configuração
- Você deve editar o **application.properties** *(localizado em src/main/resources)* e alterar o valor da propriedade **encurtaurl.base-url** para a URL base do seu servidor *(já está configurado para localhost por padrão)*. 
- Altere também os dados da conexão com o banco de dados.

## 📍 Lista de endpoints da API

Para mais detalhes, acesse o Swagger UI em http://127.0.0.1:8080/swagger-ui (substitua localhost pelo seu domínio, caso esteja hospedado)

![Imagem demonstrando a API documentada com Swagger UI](https://i.imgur.com/44713M2.png)

### 1. Listar todas as URLs

*   **Método:** `GET`
*   **Caminho:** `/urls?page=0 (padrão: 0)`
*   **Descrição:** Lista todas as URLs registradas/encurtadas divididas em páginas com 10 registros cada por padrão.
*   **Resposta:**
    *   `200 OK`

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
- SpringDoc OpenAPI Starter WebMVC UI
- Flyway

Projeto inicializado com Maven - confira o **pom.xml** para mais detalhes.

## 📋 A fazer
- [ ] Dockerfile
- [ ] Autenticação
- [ ] Caching
- [ ] Rate limiting

*Aceito sugestões!*
