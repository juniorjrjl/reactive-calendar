gradle clean
gradle bootJar
#java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*5005 -jar build/libs/reactive-calendar-1.0.0.jar
java -jar build/libs/reactive-calendar-1.0.0.jar