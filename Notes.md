

Build Spring Boot Docker Image
$ docker build -t springdockertest .

Docker Compose
$ docker-compose up


Note that running the image as container individualy not work. This is because cannot connect with database from different network's docker container