package cool.pandora.exts.sparqler;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;

class FromRDF {

    static String toJsonLd(String ntriples) throws IOException, JsonLdError {
        Object ctxobj;
        Object outobj;
        Object compactobj;
        final JsonLdOptions opts = new JsonLdOptions("");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("cool/pandora/exts/sparqler/context.json");
        ctxobj = JsonUtils.fromInputStream(is);
        RDFFormat sesameInputFormat = RDFFormat.NTRIPLES;
        final String graph = Deskolemize.convertSkolem(ntriples);
        final Model inModel = Rio.parse(new StringReader(graph), "",
                sesameInputFormat);
        outobj = JsonLdProcessor.fromRDF(inModel, opts, new RDF4JJSONLDRDFParser());
        compactobj = JsonLdProcessor.compact(outobj, ctxobj, opts);
        //System.out.println(JsonUtils.toPrettyString(compactobj));
        //Files.write(Paths.get("output.json"), JsonUtils.toPrettyString(compactobj).getBytes());
        return JsonUtils.toPrettyString(compactobj);
    }
}
