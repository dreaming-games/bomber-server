FROM ubuntu

RUN apt update -y && apt upgrade -y
RUN apt install -y bash vim
RUN apt install -y maven


ENV QT_X11_NO_MITSHM=1

ENTRYPOINT ["bash"]

