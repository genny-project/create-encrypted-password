FROM java:8 
COPY ./app.sh / 
ADD target/create-encrypted-password-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar   
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["/app.sh"]
CMD []
