FROM openjdk

COPY ./out/artifacts/TelegramBotNew_jar/TelegramBotNew.jar /TelegramBotJar.jar

CMD ["java" ,"-jar" , "TelegramBotJar.jar" ]