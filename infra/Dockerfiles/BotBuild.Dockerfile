FROM openjdk:17.0.1

WORKDIR /TelgramBot

COPY ./target/TelegramBot-1.0.jar ./TelegramBotJar.jar

RUN mkdir data

CMD ["java" ,"-jar" , "TelegramBotJar.jar" ]