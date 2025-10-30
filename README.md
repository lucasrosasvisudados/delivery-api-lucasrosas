# Delivery Tech API

![Project Banner](Eventualmente terá uma imagem)

Sistema de delivery desenvolvido com Spring Boot e Java 21./Delivery system developed with Spring Boot and Java 21.

## 🚀 Tecnologias / Tech Stack

-   **Java 21 LTS** (versão mais recente)
-   Spring Boot 3.5.7
-   Spring Web
-   Spring Data JPA
-   H2 Database
-   Maven

## ⚡ Recursos Modernos Utilizados / Resources used

-   Records (Java 14+)
-   Text Blocks (Java 15+)
-   Pattern Matching (Java 17+)
-   Virtual Threads (Java 21)

## 🏃‍♂️ Como executar / How to run

1.  **Pré-requisitos:** JDK 21 instalado. / Pre-requisites: Java 21 installed.
2.  Clone o repositório. / Clone the repo.
3.  Execute / Run: `./mvnw spring-boot:run` 
4.  Acesse / Acess in browser(or CURL): `http://localhost:8080/health`

## 📋 Endpoints

### Gerais/General
-   `GET /health` - Status da aplicação. / App status
-   `GET /info` - Informações da aplicação. / App info
-   `GET /h2-console` - Console do banco H2. / H2 database console (login initially) 

### Clientes
-   `POST /clientes` - Cadastrar novo cliente. / Add new customer
-   `GET /clientes` - Listar todos os clientes ativos. / List all customers
-   `GET /clientes/{id}` - Buscar cliente por ID. / Search customer by Id.
-   `PUT /clientes/{id}` - Atualizar cliente. / Update customer.
-   `DELETE /clientes/{id}` - Inativar cliente (soft delete). / Inactivate customer
-   `GET /clientes/buscar` - Buscar clientes por nome (`?nome=...`). / Search customer by name
-   `GET /clientes/email/{email}` - Buscar cliente por email. / Search customer by email

## 🔧 Configuração/Configuration

-   **Porta / Port:** 8080 
-   **Banco / DB :** H2 em memória
-   **Profile:** development

## 👨‍💻 Desenvolvedor/Developer

**Lucas Rosas da Cunha**

Desenvolvido com JDK 21 e Spring Boot 3.5.7
