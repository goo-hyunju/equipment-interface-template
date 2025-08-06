FROM openjdk:21-slim
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=dev
COPY equipment-interface-templatejar /app/equipment-interface-templatejar
ENTRYPOINT ["java", "-jar", "equipment-interface-templatejar"]