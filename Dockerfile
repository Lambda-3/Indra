FROM openjdk:8-jre

ARG INDRA_VERSION

WORKDIR /usr/share/indra
ADD indra-service/target/indra-service-${INDRA_VERSION}-distribution.tar.gz .

WORKDIR /usr/share/indra/bin

EXPOSE 8916
CMD ["./start"]