# Use a imagem oficial do OpenJDK para criar uma imagem base
FROM openjdk:17-alpine

# Variáveis de ambiente para a aplicação
ARG JAR_FILE=target/*.jar

# Copia o arquivo jar para o container
COPY ${JAR_FILE} app.jar

# Expõe a porta 8080 para acessar a aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java","-jar","/app.jar"]