FROM gradle:7.5.1-jdk17

RUN apt-get update && apt-get install -qq -y --no-install-recommends

ENV INSTALL_PATH /reactive-calendar

RUN mkdir -p $INSTALL_PATH

WORKDIR $INSTALL_PATH

COPY . .