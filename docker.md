...
3. Autores
docker run -name app-authors -p 9090:9090 -e QUARKUS_DATASOURCE_USERNAME=postgres -e POSTGRES_DB=distribuida -d postgres:17.2-alpine3.21
...
 

docker run -name app-authors -p 9090:9090 -e QUARKUS_DATASOURCE_USERNAME=postgres -e QUARKUS_DATASOURCE_PASSWORD=postgres -e QUARKUS_DATASOURCE_JDBC_URL=JBDC:POSTGRESQL://172.17.0.2:5432/distribuida -d app-authors


docker compose up