# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using Tomcat
FROM tomcat:10.1.19-jdk21-temurin-jammy
# Remove default apps to keep it clean
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file from the build stage to the Tomcat webapps directory
# naming it ROOT.war makes it available at the root context /
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (Render expects this)
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
