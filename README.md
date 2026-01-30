# eduCatalog - Plateforme e-learning - TPs Java Spring Boot

Plateforme e-learning basée sur une architecture microservices avec Spring Boot, Kafka et Docker.

## Architecture

| Service | Port | Description |
| ----------------- | ms---- | ------------------------------------------ |
| **Discovery** | `8761` | Eureka Server (Service Registry) |
| **Gateway** | `8080` | Point d'entrée unique (API Gateway) |
| **Auth** | `8081` | Authentification JWT RS256 |
| **Catalog** | `8082` | Gestion des cours (Kafka Producer) |
| **Progress** | `8083` | Suivi d'avancement (Kafka Consumer + Feign)|
| **Infrastructure**| `9092` | Kafka, PostgreSQL |

## Démarrage

### Prérequis

- Java 17+
- Docker & Docker Compose
- Maven

### Utilisation du Makefile

Le projet inclut un `Makefile` pour simplifier les opérations courantes.

1. **Démarrer l'infrastructure (Kafka, Eureka, Postgres)**

   ```bash
   make up
   ```

2. **Lancer les services (Profil SQLite par défaut)**

   ```bash

   _Pour utiliser PostgreSQL :_ `make run-postgres`

   ```

3. **Redémarrer l'infrastructure**

   ```bash
   make restart
   ```

4. **Arrêter l'infrastructure**

   ```bash
   make down

   ```

## Tests

Les tests d'intégration utilisent **Testcontainers** pour valider le fonctionnement réel avec Kafka et PostgreSQL.

```bash
make test
```

## Docker Swarm

Le projet est compatible avec Docker Swarm pour l'orchestration.

```bash
# Initialiser et déployer la stack
make swarm-deploy
```

## Vérification

1. **Eureka Dashboard** : [http://localhost:8761](http://localhost:8761)
2. **Login (Auth)** : `POST /api/auth/login` (admin/password)
3. **Créer Cours (Catalog)** : `POST /api/lessons`
4. **Voir Progrès (Progress)** : `GET /api/progress/me`
