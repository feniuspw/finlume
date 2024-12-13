# Use uma imagem base com JDK moderno (ajuste para o JDK 23, se necessário)
FROM openjdk:21-jdk-slim

# Instale o Kotlin Compiler e ferramentas básicas
RUN apt-get update && \
    apt-get install -y wget unzip && \
    wget https://github.com/JetBrains/kotlin/releases/download/v2.1.0/kotlin-compiler-2.1.0.zip && \
    unzip kotlin-compiler-2.1.0.zip -d /opt/kotlin && \
    ln -sf /opt/kotlin/kotlinc/bin/kotlinc /usr/bin/kotlinc && \
    ln -sf /opt/kotlin/kotlinc/bin/kotlin /usr/bin/kotlin && \
    apt-get clean

ENV PATH="/opt/kotlin/kotlinc/bin:${PATH}"

# Define o diretório de trabalho dentro do container
WORKDIR /app
