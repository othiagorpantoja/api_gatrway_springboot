FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o arquivo pom.xml
COPY pom.xml .

# Baixar dependências (para cache do Docker)
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar a aplicação
RUN ./mvnw clean package -DskipTests

# Expor a porta
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/api-gateway-1.0.0.jar"]
