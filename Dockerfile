FROM java:8 
ADD target/create-encrypted-password-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar   
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar $my_params" ]
