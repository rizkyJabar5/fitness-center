FROM amazoncorretto:17.0.6-al2023
LABEL authors="jabar"

ARG JAR_FILE=build/libs/*.jar
COPY $JAR_FILE application.jar
EXPOSE 9090
RUN java -Djarmode=layertools -jar application.jar extract

#FROM amazoncorretto:17.0.6-al2023
#COPY --from=builder dependencies/ ./
#COPY --from=builder snapshot-dependencies/ ./
#COPY --from=builder spring-boot-loader/ ./
#COPY --from=builder application/ ./
#
#ENV API_SECRET_KEY=place_holder

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]