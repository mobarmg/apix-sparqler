/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.exts.sparqler;

import static java.net.URLEncoder.encode;
import static org.apache.camel.Exchange.HTTP_CHARACTER_ENCODING;
import static org.apache.camel.Exchange.HTTP_PATH;
import static org.apache.camel.Exchange.HTTP_METHOD;
import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.fcrepo.camel.FcrepoHeaders.FCREPO_IDENTIFIER;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.component.jetty.JettyHttpComponent;

import org.slf4j.Logger;

/**
 * EventRouter.
 *
 * @author @christopher-johnson
 */
public class EventRouter extends RouteBuilder {
    private static final String SPARQL_QUERY = "type";
    private static final String NODE_SET = "node";
    private static final String HTTP_ACCEPT = "Accept";
    private static final Logger LOGGER = getLogger(EventRouter.class);

    /**
     * Configure the message route workflow.
     */
    public void configure() throws Exception {
        JettyHttpComponent jettyComponent = getContext().getComponent("jetty", JettyHttpComponent.class);
        jettyComponent.setHttpClientMinThreads(16);
        jettyComponent.setHttpClientMaxThreads(32);
        HttpComponent httpComponent = getContext().getComponent("http4", HttpComponent.class);
        httpComponent.setConnectionsPerRoute(100);

        from("jetty:http://{{rest.host}}:{{rest.port}}{{rest.prefix}}?"
                + "optionsEnabled=true&matchOnUriPrefix=true&sendServerVersion=false"
                + "&httpMethodRestrict=GET,OPTIONS")
                .routeId("Sparqler")
                .process(e -> e.getIn().setHeader(FCREPO_IDENTIFIER,
                        e.getIn().getHeader("Apix-Ldp-Resource-Path",
                                e.getIn().getHeader(HTTP_PATH))))
                .removeHeaders(HTTP_ACCEPT)
                .choice()
                .when(header(HTTP_METHOD).isEqualTo("GET"))
                .to("direct:sparql")
                .when(header(HTTP_METHOD).isEqualTo("OPTIONS"))
                .to("direct:options");
        from("direct:options")
                .routeId("SparqlerOptions")
                .setHeader(CONTENT_TYPE).constant("text/turtle")
                .setHeader("Allow").constant("GET,OPTIONS")
                .to("language:simple:resource:classpath:options.ttl");
        from("direct:sparql")
                .routeId("SparqlerGet")
                .choice()
                .when(header(SPARQL_QUERY).isEqualTo("manifest"))
                .setHeader(HTTP_METHOD).constant("POST")
                .setHeader(CONTENT_TYPE).constant("application/x-www-form-urlencoded; "
                + "charset=utf-8")
                .setHeader(HTTP_ACCEPT).constant("application/n-triples")
                .process(e -> e.getIn().setBody(sparqlSelect(Query.getManifestQuery(
                        "cool/pandora/exts/sparqler/manifest.rq", getSet(e)))))
                .log(LoggingLevel.INFO, LOGGER, String.valueOf(body()))
                .to("{{triplestore.baseUrl}}?useSystemProperties=true&bridgeEndpoint=true")
                .filter(header(HTTP_RESPONSE_CODE).isEqualTo(200))
                .setHeader(CONTENT_TYPE).constant("application/n-triples")
                .convertBodyTo(String.class)
                .log(LoggingLevel.INFO, LOGGER, "Getting query results as n-triples")
                .to("direct:toJsonLd");
        from("direct:toJsonLd")
                .routeId("JsonLd")
                .log(LoggingLevel.INFO, LOGGER, "Serializing n-triples as Json-Ld")
                .process(e -> e.getIn().setBody(FromRDF.toJsonLd(e.getIn().getBody().toString())))
                .removeHeader(HTTP_ACCEPT)
                .setHeader(HTTP_METHOD).constant("GET")
                .setHeader(HTTP_CHARACTER_ENCODING).constant("UTF-8")
                .setHeader(CONTENT_TYPE).constant("application/ld+json");
    }

    private static String sparqlSelect(final String command) {
        try {
            return "query=" + encode(command, "UTF-8");
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static String getSet(final Exchange e) {
        final Object optHdr = e.getIn().getHeader(NODE_SET);

        if (optHdr instanceof Collection) {
            return String.join(" ", new HashSet<>((Collection<String>) optHdr));
        } else if (optHdr != null) {
            return (String) optHdr;
        }
        return "";
    }
}
