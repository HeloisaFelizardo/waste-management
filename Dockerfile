# Use a versão LTS do OpenJDK
FROM eclipse-temurin:17-jdk-alpine

# Diretório de trabalho
WORKDIR /app

# Copia os arquivos de configuração
COPY src/main/resources/application-prod.yml /app/src/main/resources/

# Copia o arquivo JAR do diretório build/libs
COPY build/libs/gestao-residuos-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação vai usar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"] 