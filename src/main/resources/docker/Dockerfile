FROM pandorasystems/karaf:${KARAF_VERSION}

MAINTAINER Christopher Johnson <christopher_hanna.johnson@uni-leipzig.de>
LABEL description = "Provides a Karaf container configured with an Sparqler service"

ENV FCREPO_CAMEL_VERSION=${fcrepoCamelVersion}
ENV CAMEL_VERSION=${camelVersion}
ENV PANDORA_EXTS_VERSION=${version}

ENV DEBUG_PORT=${DEBUG_PORT}
ENV JAVA_DEBUG_PORT=${DEBUG_PORT}
ENV FCREPO_HOST=fcrepo
ENV REST_HOST=${REST_HOST}
ENV REST_PORT=${REST_PORT}
ENV REST_PREFIX=${REST_PREFIX}
ENV MAVEN_REPO=/build/repository
ENV EXTENSION_LOAD_URI=${EXTENSION_LOAD_URI}


EXPOSE ${DEBUG_PORT}
EXPOSE ${REST_PORT}

RUN mkdir -p ${mavenRepo}
ADD repository/ ${mavenRepo}

RUN echo "pandora=mvn:cool.pandora.exts/sparqler-karaf/${version}/xml/features" >> etc/org.apache.karaf.features.repos.cfg

RUN bin/start && \
    bin/client -r 10 -d 5  "feature:repo-add camel ${camelVersion}" && \
    bin/client -r 10 -d 5  "feature:repo-add mvn:cool.pandora.exts/sparqler-karaf/${version}/xml/features" && \
    bin/client -r 10 -d 5  "feature:repo-add mvn:org.fcrepo.camel/fcrepo-camel/${fcrepoCamelVersion}/xml/features" && \
    bin/client -r 10 -d 5  "feature:install exts-sparqler" && \
    sleep 15 && \
    bin/stop

COPY repository/cool/pandora/exts/*.cfg etc/
COPY repository/cool/pandora/exts/entrypoint.sh /entrypoint.sh

RUN chmod 700 /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]

CMD [ "server" ]
