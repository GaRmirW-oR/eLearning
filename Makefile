DOCKER_COMPOSE = docker-compose
MAVEN = mvn
PROFILE ?= sqlite

.PHONY: all up down build run run-postgres test clean swarm-init swarm-deploy

# --- Infrastructure ---
up:
	@echo "Démarrage de l'infrastructure Docker..."
	$(DOCKER_COMPOSE) up -d

down:
	@echo "Arrêt de l'infrastructure Docker..."
	$(DOCKER_COMPOSE) down

build:
	@echo "Compilation des projets..."
	$(MAVEN) clean package -DskipTests

# --- Application ---
# Usage: make run (default sqlite) or make run PROFILE=postgres
run:
	@echo "Lancement des microservices (Profil: $(PROFILE))..."
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) -f discovery-service/pom.xml &
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) -f auth-service/pom.xml &
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) -f catalog-service/pom.xml &
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) -f progress-service/pom.xml &
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) -f gateway-service/pom.xml &
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) -f gateway-service/pom.xml &
	@echo "Tous les services démarrent en arrière-plan."

stop:
	@echo "Arrêt des microservices Java..."
	@-pkill -f 'java.*spring-boot' || true
	@echo "Services arrêtés."

restart: stop run

# Raccourci pour Postgres
run-postgres:
	$(MAKE) run PROFILE=postgres

# --- Qualité ---
test:
	@echo "Lancement des tests ..."
	$(MAVEN) verify

# --- Orchestration (Swarm) ---
swarm-init:
	docker swarm init || true

swarm-deploy: swarm-init
	@echo "Déploiement de la stack ..."
	docker stack deploy -c docker-compose.yml eduCatalog
	docker stack services eduCatalog

clean:
	$(MAVEN) clean
	$(DOCKER_COMPOSE) down -v 

