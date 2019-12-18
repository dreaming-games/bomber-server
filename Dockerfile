FROM ubuntu

RUN apt update -y && apt upgrade -y
RUN apt install -y bash vim
RUN apt install -y maven

ENTRYPOINT ["bash"]
