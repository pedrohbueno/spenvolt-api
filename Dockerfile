
# Etapa 1: Compilar a aplicação com Maven e Java 17
FROM maven:3.9-eclipse-temurin-17 AS build

LABEL authors="pedrohbueno"

WORKDIR /app

# Copia os arquivos necessários para compilar o projeto
COPY pom.xml .
COPY src ./src

# Compila e gera o JAR
RUN mvn clean package -DskipTests

# Etapa 2: Executar a aplicação com Java 17
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia o JAR gerado na etapa de compilação
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Porta padrão da aplicação
EXPOSE 8080

# Inicia a API Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
