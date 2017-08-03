FROM pandorasystems/karaf:4.0.9

MAINTAINER Christopher Johnson <chjohnson39@gmail.com>
LABEL description = "Provides a Karaf container configured with an Sparqler service"

ENV DEBUG_PORT 5008
ENV JAVA_DEBUG_PORT=${DEBUG_PORT} \
    TOOLBOX_VERSION=LATEST \
    KARAF_VERSION=LATEST \
    CAMEL_VERSION=LATEST \
    PANDORA_EXTS_VERSION=LATEST \
    FCREPO_HOST=fcrepo \
    FCREPO_PORT=8080 \
    FCREPO_CONTEXT_PATH=/fcrepo \
    MAVEN_REPO=/build/repository

ENV FCREPO_BASEURI=http://${FCREPO_HOST}:${FCREPO_PORT}/${FCREPO_CONTEXT_PATH}/rest
ENV KARAF_RUNTIME /opt/karaf
ENV KARAF_VERSION 4.0.8

RUN mkdir -p ${MAVEN_REPO}
ADD repository/ ${MAVEN_REPO}

WORKDIR ${KARAF_RUNTIME}/apache-karaf-${KARAF_VERSION}

RUN echo "pandora=mvn:cool.pandora/pandora-karaf/${PANDORA_EXTS_VERSION}/xml/features" >> etc/org.apache.karaf.features.repos.cfg

RUN sed -e "s:^\(    mvn\:org.apache.karaf.features/enterprise/${KARAF_VERSION}/xml/features\):\1, \\\
    mvn\:cool.pandora/pandora-karaf/${PANDORA_EXTS_VERSION}/xml/features \\\:" \
        -i etc/org.apache.karaf.features.cfg

RUN \
  echo "eval 'alias mvn=\"mvn -Dmaven.repo.local=\${MAVEN_REPO}\"'" \
    >> /etc/skel/.bashrc && \
  echo "eval 'alias mvn=\"mvn -Dmaven.repo.local=\${MAVEN_REPO}\"'" \
    >> ~/.bashrc

RUN bin/start && \
    bin/client -r 10 -d 5  "feature:repo-add camel ${CAMEL_VERSION}" && \
    bin/client -r 10 -d 5  "feature:repo-add mvn:cool.pandora/pandora-karaf/0.0.1/xml/features" && \
    bin/client -r 10 -d 5  "feature:repo-add mvn:org.fcrepo.camel/fcrepo-camel/4.5.0/xml/features" && \
    ##bin/client -r 10 -d 5  "feature:install exts-sparqler" && \
    sleep 15 && \
    bin/stop

RUN sed -e "s/osgi:/stdout, osgi:/" -i etc/org.ops4j.pax.logging.cfg

COPY repository/cool/pandora/*.cfg etc/

WORKDIR ${KARAF_RUNTIME}/apache-karaf-${KARAF_VERSION}
COPY repository/cool/pandora/entrypoint.sh /entrypoint.sh

RUN chmod 700 /entrypoint.sh

EXPOSE ${DEBUG_PORT}

ENTRYPOINT [ "/entrypoint.sh" ]

# Launch Karaf with no console, and debugging enabled by default.

CMD [ "server", "debug" ]
