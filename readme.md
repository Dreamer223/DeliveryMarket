***Keycloak***
>docker run --name delivery-keycloak -p 8082:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v ./config/keycloak/import:/opt/keycloak/data/import quay.io/keycloak/keycloak:23.0.4 start-dev --import-realm
> 
***PostgreSQL***
>_docker run --name delivery-db -p 5432:5432 -e POSTGRES_USER=delivery -e POSTGRES_PASSWORD=delivery -e POSTGRES_DB=catalogue postgres:16_
> 
***MongoDB***
>docker run --name delivery-feedback-db -p 27017:27017 mongo:7