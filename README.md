# Baozi Store API

## PostgreSQL local para Insomnia

Com o Docker Desktop em execução, inicie o banco com `docker compose up -d postgres`.
A API usa, por padrão, `jdbc:postgresql://localhost:5432/baozi_store`, usuário `postgres` e senha `postgres`.
Após o banco ficar saudável, inicie a aplicação com `mvn -pl baozi-store-core spring-boot:run` e faça as requisições no Insomnia em `http://localhost:8080`.

API REST para o trabalho de Desenvolvimento Web Back End. A Baozi Store e tratada como um atelie de encomendas de baos com retirada programada, e nao como uma loja generica.

## Arquitetura

- `baozi-store-core`: aplicacao Spring Boot, controllers, services e repositories.
- `baozi-store-model-domain`: DTOs e enums.
- `baozi-store-model-entity`: entidades JPA.
- `baozi-store-model-mapper`: conversoes DTO-entidade com MapStruct.

## Requisitos locais

- JDK 21
- PostgreSQL para a execucao local
- Docker Desktop para a suite de integracao, que usa somente PostgreSQL via Testcontainers

## Virtual threads

O Spring Boot habilita virtual threads em `application.properties` com `spring.threads.virtual.enabled=true`.

## JSON contract

All JSON fields are in English. `documentNumber` is a textual identifier, preserving leading zeros and allowing future alphanumeric document formats.

```json
{
  "name": "Gabriel",
  "documentNumber": "12345678910",
  "areaPhoneCode": 12,
  "phoneNumber": "912345678"
}
```

The `documentNumber` is unique. A duplicate client returns `409 Conflict`.
An active client can be retrieved by document number with `GET /client/document-number/{documentNumber}`.

The phone number can be updated with `PUT /client/document-number/{documentNumber}/phone-number`:

```json
{
  "areaPhoneCode": 11,
  "phoneNumber": "998765432"
}
```

Products can be fully updated with `PUT /product/{id}`:

```json
{
  "name": "Pork Baozi Premium",
  "price": 14.90,
  "available": false
}
```

```json
{
  "name": "Baozi de Cogumelos",
  "price": 18.50,
  "available": true
}
```

```json
{
  "clientId": 1,
  "productId": 1,
  "quantity": 2
}
```

## Casos de teste

Cada caso de integracao possui seus arquivos JSON em `baozi-store-core/src/test/resources/cases`. A suite usa apenas um container PostgreSQL via Testcontainers e valida tanto a resposta HTTP em JSON quanto os registros persistidos.

## Executar testes

```powershell
..\apache-maven-3.9.16\bin\mvn.cmd test
```
