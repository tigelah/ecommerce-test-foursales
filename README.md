# 🛒 E-commerce — Clean Architecture

> Sistema de pedidos e produtos com **JWT**, **CRUD**, **Busca ES**, **Pedidos + Outbox Kafka**, **Consumer de estoque** e **Relatórios** — tudo em **Java 21 + Spring Boot 3.3**.

---

## 🧰 Tecnologias

- ☕ **Java 21**, **Spring Boot 3.3.x**
  - Spring Web, Security, Data JPA, Validation, Actuator
  - Spring for Apache Kafka
- 🐬 **MySQL 8** (persistência principal)
- 🔎 **Elasticsearch 7.x** (busca e filtros — fuzzy, categoria, faixa de preço)
- 📨 **Kafka 3.x** (eventos `order.paid`)
- 🧱 **Lombok**
- 🕒 **Jackson JSR-310** (datas Java Time)
- 🐳 **Docker & Docker Compose**
- 🧪 **JUnit 5** + Mockito (TDD casos de uso)

---

## 🧱 Arquitetura (Clean Architecture)

ecommerce/
└─ src/main/java/com/tigelah/ecommerce
├─ domain/ # Regras puras (Entidades, VOs, Enums, Eventos)
│ ├─ product/ Product, ...
│ └─ order/ Order, OrderItem, OrderStatus, events/OrderPaidEvent
├─ application/ # Casos de uso + Ports (interfaces)
│ ├─ security/ usecase AuthUseCase, ...
│ ├─ product/ ports, dto, usecase (CRUD + Search)
│ ├─ order/ ports, dto, usecase (CreateOrder, PayOrder) + Outbox
│ ├─ inventory/ (ProcessOrderPaidUseCase)
│ └─ reports/ ports, dto, usecase (TopCustomers, AvgTicket, MonthlyRevenue)
├─ infrastructure/ # Adapters (JPA, ES, Kafka, Security)
│ ├─ persistence/
│ │ ├─ jpa/entity (OrderEntity, ProductEntity, Outbox, ProcessedEvent)
│ │ ├─ jpa/repo (Spring Data + queries nativas)
│ │ └─ jpa/adapters (implementações dos ports)
│ ├─ search/ ProductSearchEsAdapter (Elasticsearch client)
│ ├─ kafka/ (producer, NewTopic, listener OrderPaidListener)
│ └─ security/ (JwtTokenServiceImpl, BCryptPasswordHasher)
└─ entrypoints/
└─ http/ (AuthController, ProductController, OrderController, ReportsController)


**Padrões**: Ports & Adapters, Repository, CQRS leve (MySQL write / ES read), Outbox Pattern, Idempotência no consumer, Lock pessimista para estoque.

---

## 🔐 Segurança

- **JWT** com roles:
  - `ADMIN`: CRUD de produtos, relatórios.
  - `USER`: criar & pagar pedidos, visualizar produtos.
- **Rotas públicas**: `GET /products/**`, `GET /products/search`, `GET /health`, `GET /actuator/**`.
- Usuários seed:
  - admin → `admin@acme.com` / `admin123`
  - user  → `user@acme.com` / `user123`

---

> No Docker Compose, internamente usamos `mysql`, `elasticsearch`, `kafka` como hostnames.

---

## ▶️ Como rodar

```bash
# Sobe tudo com Docker (app, mysql, es, kafka)
make run

# Ver logs do app
make logs

# Derrubar tudo
make stop

make build
make java-run

make test
./mvnw test
```



# Smoke tests (curl)

ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@acme.com","password":"admin123"}' \
  | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')

USER_TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@acme.com","password":"user123"}' \
  | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')

📦 Produtos (ADMIN)
CREATE_PRODUCT_RESP=$(curl -s -X POST http://localhost:8080/products \
-H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
-d '{"name":"Teclado","description":"RGB","price":199.9,"category":"perifericos","stock":50}')
PRODUCT_ID=$(echo "$CREATE_PRODUCT_RESP" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')

# atualizar
curl -s -X PUT http://localhost:8080/products/$PRODUCT_ID \
-H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
-d '{"name":"Teclado Pro","description":"ABNT2 RGB","price":249.9,"category":"perifericos","stock":60}'

🔎 Busca (pública / ES)
curl -s "http://localhost:8080/products/search?name=teclado&minPrice=150&maxPrice=300&page=0&size=10"

🧾 Pedidos (USER)
ORDER_REQ='{"items":[{"productId":"'"$PRODUCT_ID"'","quantity":2}]}'

CREATE_ORDER_RESP=$(curl -s -X POST http://localhost:8080/orders \
-H "Authorization: Bearer $USER_TOKEN" -H "Content-Type: application/json" \
-d "$ORDER_REQ")
ORDER_ID=$(echo "$CREATE_ORDER_RESP" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')

# pagar (gera outbox -> kafka -> consumer baixa estoque)
curl -s -X POST http://localhost:8080/orders/$ORDER_ID/pay \
-H "Authorization: Bearer $USER_TOKEN"

📈 Relatórios (ADMIN)
curl -s "http://localhost:8080/reports/top-customers?start=2025-09-01T00:00:00Z&end=2025-10-01T00:00:00Z&limit=5" \
-H "Authorization: Bearer $ADMIN_TOKEN"

curl -s "http://localhost:8080/reports/avg-ticket?start=2025-09-01T00:00:00Z&end=2025-10-01T00:00:00Z" \
-H "Authorization: Bearer $ADMIN_TOKEN"

curl -s "http://localhost:8080/reports/monthly-revenue?year=2025&month=9" \
-H "Authorization: Bearer $ADMIN_TOKEN"
