# Use a versão LTS do OpenJDK
FROM eclipse-temurin:17-jdk-alpine

# Instala o Gradle
RUN apk add --no-cache gradle

# Diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Gera o JAR
RUN gradle build -x test

# Expõe a porta que a aplicação vai usar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "build/libs/gestao-residuos-0.0.1-SNAPSHOT.jar"] 