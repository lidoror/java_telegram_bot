FROM openjdk:17.0.1

WORKDIR /TelgramBot

COPY ./out/artifacts/TelegramBotNew_jar/TelegramBotNew.jar ./TelegramBotJar.jar

CMD ["java" ,"-jar" , "TelegramBotJar.jar" ]