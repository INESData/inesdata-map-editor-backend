# Mapper Backend

- [Description](#description)
- [Docker](#docker)

# Description
Service responsible for saving a data source mapping and generating the corresponding R2RML / RML mapping file

# Docker
For building the image, a mandatory argument needs to be provided. `INDEX_URL` refers to the repository from downloading the dependencies. For example: https://nexus-repo/repository/public_pypi/simple

Run:
```
docker build -f docker/Dockerfile --build-arg INDEX_URL= --tag com.inesdata-map/mapper-backend .
```

A volume is needed to store the datasource's files. It is important that it is mounted into the same directory as defined on the properties `DATASOURCEPATHS_DATAINPUT` and `DATASOURCEPATHS_DATAOUTPUT`.
 
For starting a container, run the following command:
```
docker run --name mapper-backend -p 8080:8080 -d --env-file ./docker/env.list -v $(pwd)/inesdata-map-data:/data -d com.inesdata-map/mapper-backend:latest
```
