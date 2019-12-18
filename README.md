# bomber-server

## Compile and run?
```
mvn clean compile exec:java
```

## Are you Martin?
```
docker build -t ubuntu_bomber .
docker run -v $(pwd):/bomber -it ubuntu_bomber
```

In the container move to /bomber and run the compile and run command above
