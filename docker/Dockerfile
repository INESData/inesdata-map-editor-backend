FROM eclipse-temurin:17-jre-alpine

# System env vars
ENV USER_NAME=mapper
ENV USER_UID=200
ENV APP_HOME=/opt/mapper-backend

# Add the user who executes the app. The user belongs to the group with gui 0
RUN adduser -G root -s /bin/sh -D -u ${USER_UID} ${USER_NAME}

# Create the app directory
RUN mkdir -p ${APP_HOME}

# Copy the script of the entrypoint and resources
COPY ./docker/docker-entrypoint.sh ${APP_HOME}/docker-entrypoint.sh

# Sets the minimum rights
RUN chown -R ${USER_NAME}:0 ${APP_HOME} \
    && chmod -R g=u ${APP_HOME} \
    && chmod ug+x ${APP_HOME}/docker-entrypoint.sh

# Define the port of the container
EXPOSE 8080

# User by default
USER ${USER_NAME}

# Copy the fatjar
COPY --chown="${USER_NAME}:0" ./target/mapper-backend-*.jar ${APP_HOME}/mapper-backend.jar

# Script entrypoint for the container
WORKDIR ${APP_HOME}
ENTRYPOINT ["./docker-entrypoint.sh"]
