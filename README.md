# Mapper Backend

- [Description](#description)
- [Docker](#docker)

# Description
Service responsible for saving a data source mapping and generating the corresponding R2RML / RML mapping file

# Docker
Run:
```
docker build -f docker/Dockerfile --tag com.inesdata-map/mapper-backend .
```

A volume is needed to store the datasource's files. It is important that it is mounted into the same directory as defined on the properties `APP_DATAPROCESSINGPATH` and must be a directory with write permissions (for example, the home folder).
 
For starting a container, run the following command:
```
docker run --name mapper-backend -p 8080:8080 -d --env-file ./docker/env.list -v $(pwd)/inesdata-map-data:/home/mapper/data -d com.inesdata-map/mapper-backend:latest
```
