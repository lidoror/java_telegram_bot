FROM openjdk:17.0.1

WORKDIR /TelgramBot

COPY ./target/TelegramBotNew-1.0-jar-with-dependencies.jar ./TelegramBotJar.jar

CMD ["java" ,"-jar" , "TelegramBotJar.jar" ]