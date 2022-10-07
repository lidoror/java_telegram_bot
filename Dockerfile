FROM openjdk

WORKDIR /TelgramBot

COPY ./out/artifacts/TelegramBotNew_jar/TelegramBotNew.jar /.

CMD ["java" ,"-jar" , "TelegramBotJar.jar" ]