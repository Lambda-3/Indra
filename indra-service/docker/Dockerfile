FROM openjdk:8-jre

ENV INDRA_JAVA_OPTS "-Xmx4g -Dindra.http.host=0.0.0.0"

WORKDIR /usr/share/indra
ADD indra-service-${project.version}-distribution.tar.gz .
WORKDIR /usr/share/indra/bin

EXPOSE 8916
CMD ["./start"]
