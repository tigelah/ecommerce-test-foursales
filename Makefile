# -------- Config --------
APP_NAME ?= ecommerce
PORT ?= 8080
COMPOSE ?= docker compose

# Serviços do compose (ajuste se seus nomes diferirem)
APP_SVC ?= app
DB_SVC ?= mysql
ES_SVC ?= elasticsearch
KAFKA_SVC ?= kafka

# -------- Help --------
.PHONY: help
help:
	@echo "Targets:"
	@echo "  make build           - mvn clean package (skipTests)"
	@echo "  make test            - mvn test"
	@echo "  make java-run        - roda app local (precisa de MySQL/ES/Kafka rodando)"
	@echo "  make run             - docker compose up -d --build"
	@echo "  make stop            - docker compose down"
	@echo "  make logs            - logs do app"
	@echo "  make ps              - lista serviços do compose"
	@echo "  make es-health       - verifica estado do ES"
	@echo "  make es-indices      - lista indices do ES"
	@echo "  make kafka-consume   - consome topico (TOPIC=order.paid)"
	@echo "  make tokens          - pega tokens admin/user via curl"
	@echo "  make seed-product    - cria um produto exemplo (requer ADMIN_TOKEN)"

# -------- Maven / Local --------
.PHONY: build test java-run
build:
	./mvnw -DskipTests clean package

test:
	./mvnw test

java-run:
	./mvnw spring-boot:run

# -------- Docker Compose --------
.PHONY: run stop logs ps restart
run:
	$(COMPOSE) up -d --build

stop:
	$(COMPOSE) down

logs:
	$(COMPOSE) logs -f $(APP_SVC)

ps:
	$(COMPOSE) ps

restart:
	$(COMPOSE) restart $(APP_SVC)

# -------- Elasticsearch utils --------
.PHONY: es-health es-indices
es-health:
	curl -s http://localhost:9200/_cluster/health | jq .

es-indices:
	curl -s http://localhost:9200/_cat/indices?v

# -------- Kafka utils --------
# Consome mensagens do tópico (Bitnami). Use: make kafka-consume TOPIC=order.paid
TOPIC ?= order.paid
.PHONY: kafka-consume
kafka-consume:
	$(COMPOSE) exec -T $(KAFKA_SVC) bash -lc '\
	  if [ -x /opt/bitnami/kafka/bin/kafka-console-consumer.sh ]; then KBIN=/opt/bitnami/kafka/bin; \
	  else KBIN=/usr/bin; fi; \
	  $$KBIN/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic $(TOPIC) --from-beginning'

# -------- Tokens / Seed --------
.PHONY: tokens seed-product
tokens:
	@echo "ADMIN_TOKEN:"
	@curl -s -X POST http://localhost:$(PORT)/auth/login -H "Content-Type: application/json" \
	  -d '{"email":"admin@acme.com","password":"admin123"}' | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p'
	@echo ""
	@echo "USER_TOKEN:"
	@curl -s -X POST http://localhost:$(PORT)/auth/login -H "Content-Type: application/json" \
	  -d '{"email":"user@acme.com","password":"user123"}' | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p'

# Exemplo de seed de produto (requer var ADMIN_TOKEN no ambiente)
seed-product:
	curl -s -X POST http://localhost:$(PORT)/products \
	  -H "Authorization: Bearer $$ADMIN_TOKEN" -H "Content-Type: application/json" \
	  -d '{"name":"Teclado","description":"RGB","price":199.90,"category":"perifericos","stock":50}'
