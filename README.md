<p align="center">
<img src="https://utfs.io/f/3b2340e8-5523-4aca-a549-0688fd07450e-j4edu.jfif" width="50%" height="50%">
</p>
<h1 align="center">	
Código Certo - Trilha Backend - Task API
</h1>

<div align="center">

<strong>Projeto implementado como solução para o [desafio](https://github.com/codigocerto/TrilhaBackEndJR-JUN15) técnico Backend da [Código Certo](https://www.codigocertocoders.com.br/).<br><br>
Implementação de uma API RESTful para o gerenciamento de Tarefas.<br>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)

---

[![CI/CD](https://github.com/playeredlc/CodigoCerto-Trilha-Backend/actions/workflows/continuous-integration.yml/badge.svg)](https://github.com/playeredlc/CodigoCerto-Trilha-Backend/actions/workflows/continuous-integration.yml)
![Static Badge](https://img.shields.io/badge/Swagger%20UI-docs-green?logo=swagger&logoSize=auto&&labelColor=%23323232&color=blue&link=https%3A%2F%2Fcodigocerto-task-api.onrender.com%2Fswagger-ui%2Findex.html)
![Docker Image Version](https://img.shields.io/docker/v/edlcorrea/taskapi?logo=docker&logoSize=auto&labelColor=%23323232&link=https%3A%2F%2Fhub.docker.com%2Fr%2Fedlcorrea%2Ftaskapi)

Acesse: [codigocerto-task-api.onrender.com/swagger-ui](https://codigocerto-task-api.onrender.com/swagger-ui/index.html)

<br>

[Sobre](#sobre)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Tecnologias](#tecnologias)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Utilização](#utilização)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Endpoints](#endpoints)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Estrutura do Projeto](#estrutura-do-projeto)&nbsp;&nbsp;&nbsp;

---

</div>

## Sobre
Este projeto é a implementação de uma API RESTful para o gerenciamento de Tarefas, a implementação foi realizada utilizando Java, Spring e SQLite.

A aplicação permite que um usuário, após cadastrar-se e autenticar-se, possa Criar, Visualizar, Editar e Excluir suas Tarefas.

```mermaid
---
config:
  theme: dark
---
graph TD
    A[Usuário] -->|realiza cadastro| B[Registro]
    A -->|faz o login| C[Login]
    C -->|gera JWT| D[Autenticação]
    A -->|cria nova| E[Criar Tarefa]
    A -->|lista uma| F[Listar Tarefa]
    A -->|lista todas| F[Listar Tarefa]
    A -->|edita existente| G[Editar Tarefa]
    A -->|deleta| H[Delete Task]

```

O projeto possui um pipeline de CI/CD que após realizar os testes, atualiza a versão da [imagem Docker](https://hub.docker.com/r/edlcorrea/taskapi) hospedada no DockerHub e aciona um novo Deploy do serviço no Render.

## Tecnologias

### Back-end
* Java
* Spring Framework:
  * Boot; Data JPA; Security; Hateoas; Docs.
* SQLite; Flyway
* JUnit; Mockito; REST-Assured
* Autenticação JWT

### Deploy
* Render
* Docker
* GitHub Actions

## Utilização
O projeto está documentado com a especificação OpenAPI e pode ser utilizado por meio da [interface gráfica do Swagger](https://codigocerto-task-api.onrender.com/swagger-ui/index.html). Utilize a ferramenta de Autorização da interface para inserir o seu `accessToken` recebido como resposta ao realizar o login. Assim, ele será enviado a todas as requisições que necessitam de autenticação.

Outra opção é utilizar a ferramenta Postman, para facilitar esse processo, existe uma Collection pública pré-configurada que pode ser acessada pelo link:
[CodigoCerto-Task-API - Postman Collection](https://www.postman.com/edlcorrea/workspace/codigocerto-task-api/collection/14050047-3116af82-c751-408c-ad9a-3eb505dd8bf9?action=share&creator=14050047)

## Endpoints

[Usuário](#usuário)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Autenticação](#autenticação)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Tarefas](#tarefas)&nbsp;&nbsp;&nbsp;

<strong>Breve explicação dos possíveis retornos:</strong><br>
`200 Success` - <i>O request foi atendido com sucesso.</i><br>
`204 No Content` - <i>O request foi atendido com sucesso, porém, não há retorno.</i><br>
`400 Bad Request` - <i>Faltando informação obrigatória, ou, informação em formato não aceito.</i><br>
`401 Unauthorized` - <i>Authorization Header não foi informado, ou o Token não é válido.</i><br>
`404 Forbidden` - <i>Autenticação correta, porém, o usuário não tem permissão para acessar o recurso.</i><br>
`404 Not Found` - <i>Recurso não encontrado.</i><br>
`409 Conflict` - <i>Recurso único já cadastrado (username).</i><br>
`500 Internal Server Error` - <i>Ocorreu um erro inesperado no servidor.</i><br>

---
### <div align="center">USUÁRIO</div>

<div align="center">
	
[Criar](#criar)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Excluir](#excluir)

</div>

---
#### CRIAR
> <strong>POST - /user</strong>
* <i>Request</i>:
  * <i>Body</i>

`name: required` `username: required` `password: required`
```json
{
  "name": "Eduardo",
  "username": "edlc",
  "password": "edlc123"
}
```
* <i>Response 200</i>:
```json
{
  "id": 1,
  "name": "Eduardo",
  "username": "edlc"
}
```

---
#### EXCLUIR
> <strong>DELETE - /user/:id</strong>
* <i>Request</i>
  * <i>Headers</i> `Authorization: Bearer [accessToken]`
* <i>Response 204</i> <br> `No Content`

---
### <div align="center">AUTENTICAÇÃO</div>

<div align="center">
	
[Login](#login)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Refresh Token](#refresh-token)

</div>

---
#### LOGIN
> <strong>POST - /auth/login</strong>
* <i>Request</i>:
  * <i>Body</i>

`username: required` `password: required`
```json
  {
    "username": "edlc",
    "password": "edlc123"
  }
```
* <i>Response 200</i>:
```json
{
  "username": "edlc",
  "isAuthenticated": true,
  "createdAt": "2024-10-17T10:58:58.386+00:00",
  "expiration": "2024-10-17T11:58:58.386+00:00",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTcyOTE2MjczOCwiZXhwIjoxNzI5MTY2MzM4LCJzdWIiOiJlZGxjIiwiaXNzIjoiaHR0cHM6Ly9jb2RpZ29jZXJ0by10YXNrLWFwaS5vbnJlbmRlci5jb20ifQ.Klw8lQTooJh0E6Zh-eqzUjC-bLs4odR2KxNhAVN7EYY",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTcyOTE2MjczOCwiZXhwIjo1MTg3NDk5MDE1LCJzdWIiOiJlZGxjIn0.dXDjGFrJ-Ff8JpDvbdwwCQQ4EHKUo37qzC7SSMmd09k"
}
```
---
#### REFRESH TOKEN
> <strong>POST - /auth/refresh/:username</strong>
* <i>Request</i>
  * <i>Headers</i> `Authorization: Bearer [accessToken]`
* <i>Response 200</i>:
```json
{
  "username": "edlc",
  "isAuthenticated": true,
  "createdAt": "2024-10-17T10:58:58.386+00:00",
  "expiration": "2024-10-17T11:58:58.386+00:00",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTcyOTE2MjczOCwiZXhwIjoxNzI5MTY2MzM4LCJzdWIiOiJlZGxjIiwiaXNzIjoiaHR0cHM6Ly9jb2RpZ29jZXJ0by10YXNrLWFwaS5vbnJlbmRlci5jb20ifQ.Klw8lQTooJh0E6Zh-eqzUjC-bLs4odR2KxNhAVN7EYY",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTcyOTE2MjczOCwiZXhwIjo1MTg3NDk5MDE1LCJzdWIiOiJlZGxjIn0.dXDjGFrJ-Ff8JpDvbdwwCQQ4EHKUo37qzC7SSMmd09k"
}
```

---
### <div align="center">TAREFAS</div>

<div align="center">
	
[Criar](#criar-1)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Editar](#editar)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Buscar Todas](#buscar-todas)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Buscar Uma](#buscar-uma)&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
[Excluir](#excluir-1)

</div>

---
#### CRIAR
> <strong>POST - /api/task/v1</strong>
* <i>Request</i>
  * <i>Headers</i> `Authorization: Bearer [accessToken]`
  * <i>Body</i>

`title: required` `priority: low | medium | high` `status: pending | completed`
```json
  {
    "title": "Estudar JAVA",
    "description": "Ler o livro Effective Java",
    "priority": "medium",
    "status": "pending",
    "deadline": "2025-01-01"
  }
```
* <i>Response 200</i>
```json
{
  "id": 1,
  "title": "Estudar JAVA",
  "description": "Ler o livro Effective Java",
  "priority": "MEDIUM",
  "deadline": "2025-01-01T00:00:00.000+00:00",
  "status": "PENDING",
  "_links": {
    "self": {
      "href": "https://codigocerto-task-api.onrender.com/api/task/v1/1"
    }
  }
}
```

---
#### EDITAR
> <strong>PUT - /api/task/v1</strong>
* <i>Request</i>
  * <i>Headers</i>    
    `Authorization: Bearer [accessToken]`
  * <i>Body</i>

`id: required` `title: required` `priority: low | medium | high` `status: pending | completed`
```json
  {
    "id": 1,
    "title": "Estudar JAVA",
    "description": "Ler os livros Effective Java e Thinking in Java",
    "priority": "low",
    "status": "pending",
    "deadline": "2025-02-02"
  }
```
* <i>Response 200</i>
```json
{
  "id": 1,
  "title": "Estudar JAVA",
  "description": "Ler os livros Effective Java e Thinking in Java",
  "priority": "LOW",
  "deadline": "2025-02-02T00:00:00.000+00:00",
  "status": "PENDING",
  "_links": {
    "self": {
      "href": "https://codigocerto-task-api.onrender.com/api/task/v1/1"
    }
  }
}
```

---
#### BUSCAR TODAS
> <strong>GET - /api/task/v1</strong>
* <i>Request</i>
  * <i>Headers</i>    
    `Authorization: Bearer [accessToken]`
* <i>Response 200</i>
```json
[
  {
    "id": 1,
    "title": "Estudar JAVA",
    "description": "Ler os livros Effective Java e Thinking in Java",
    "priority": "LOW",
    "deadline": "2025-02-02T00:00:00.000+00:00",
    "status": "PENDING",
    "links": [
      {
        "rel": "self",
        "href": "https://codigocerto-task-api.onrender.com/api/task/v1/1"
      }
    ]
  },
  {
    "id": 2,
    "title": "Finalizar Projeto",
    "description": "Escrever o README e adicionar ao repositório",
    "priority": "HIGH",
    "deadline": "2024-10-20T00:00:00.000+00:00",
    "status": "PENDING",
    "links": [
      {
        "rel": "self",
        "href": "https://codigocerto-task-api.onrender.com/api/task/v1/2"
      }
    ]
  }
]
```

---
#### BUSCAR UMA
> <strong>GET - /api/task/v1/:id</strong>
* <i>Request</i>
  * <i>Headers</i>    
    `Authorization: Bearer [accessToken]`
* <i>Response 200</i>
```json
{
  "id": 1,
  "title": "Estudar JAVA",
  "description": "Ler os livros Effective Java e Thinking in Java",
  "priority": "LOW",
  "deadline": "2025-02-02T00:00:00.000+00:00",
  "status": "PENDING",
  "_links": {
    "self": {
      "href": "https://codigocerto-task-api.onrender.com/api/task/v1/1"
    }
  }
}
```

---
#### EXCLUIR
> <strong>DELETE - /api/task/v1/:id</strong>
* <i>Request</i>
  * <i>Headers</i>    
    `Authorization: Bearer [accessToken]`
* <i>Response 204</i> <br> `No Content`

## Estrutura do Projeto
Optei por estruturar os diretórios e arquivos do projeto em módulos, ao invés da organização tradicional, na qual agrupam-se os arquivos por camadas (Controllers, Services, Repositories, etc).

Cada recurso ou funcionalidade está organizado em seu próprio pacote (Autenticação, JWT, User, Task, etc), ainda assim, respeitando a arquitetura onde cada camada possui suas responsabilidades.

Essa organização facilita a manutenção e mantém o projeto organizado conforme novas funcionalidades são adicionadas.
```plaintext
project-root/
|
├── src
|   ├── main
|   |    ├── java
|   |    |     └── app/edlc/taskapi
|   |    |           ├── exception/
|   |    |           ├── configuration/
|   |    |           |     ├── OpenApiConfig.java
|   |    |           |     └── WebConfig.java
|   |    |           ├── security/
|   |    |           |     ├── exception/
|   |    |           |     ├── data/
|   |    |           |     |    ├── AccountCredentialsDto.java
|   |    |           |     |    └── TokenDto.java
|   |    |           |     ├── authentication/
|   |    |           |     |    ├── AuthController.java
|   |    |           |     |    └── AuthService.java
|   |    |           |     ├── jwt/
|   |    |           |     |    ├── JwtTokenFilter.java
|   |    |           |     |    └── JwtTokenProvider.java
|   |    |           |     └── SecurityConfig.java
|   |    |           ├── task/
|   |    |           |     ├── exception/
|   |    |           |     ├── data/
|   |    |           |     |    ├── mapper/
|   |    |           |     |    ├── enums/
|   |    |           |     |    ├── Task.java
|   |    |           |     |    └── TaskDto.java
|   |    |           |     ├── TaskController.java
|   |    |           |     ├── TaskService.java
|   |    |           |     └── TaskRepository.java
|   |    |           ├── user/
|   |    |           |     ├── exception/
|   |    |           |     ├── data/
|   |    |           |     |    ├── mapper/
|   |    |           |     |    ├── Permission.java
|   |    |           |     |    ├── User.java
|   |    |           |     |    ├── UserRequestDto.java
|   |    |           |     |    └── UserResponseDto.java
|   |    |           |     ├── UserController.java
|   |    |           |     ├── UserService.java
|   |    |           |     └── UserRepository.java
|   |    |           └── Startup.java
|   |    └── resources
|   |         ├── db/
|   |         |    ├── migration/
|   |         |    └── task-api-codigo-certo.db
|   |         └── application.yml  
|   └── test
|        ├── java
|        |     ├── app/edlc/taskapi
|        |     |     ├── authentication/
|        |     |     |     └── integrationtests/
|        |     |     ├── task/
|        |     |     |     ├── integrationtests/
|        |     |     |     └── unittests/
|        |     |     ├── user/
|        |     |     |     └── unittests/
|        |     |     └── configuration/
|        └── resources
|             ├── db/
|             |    ├── migration/
|             └── application-test.yml
|
├── .env.example
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

<hr>

<strong><i> </> </i></strong> Developed by <strong>edlcorrea</strong>. [Get in touch!](https://www.linkedin.com/in/edlcorrea/) :metal:
