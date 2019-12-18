# bomber-server

## Compile and run?
```
mvn clean compile exec:java
```

## Are you Martin?
```
xhost local:root
docker build -t ubuntu_bomber .
docker run -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v $(pwd):/bomber -it ubuntu_bomber
```

In the container move to /bomber and run the compile and run command above
