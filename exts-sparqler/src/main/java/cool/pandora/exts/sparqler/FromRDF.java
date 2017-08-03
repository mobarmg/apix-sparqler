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

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

class FromRDF {

    static String toJsonLd(String ntriples) throws IOException, JsonLdError {
        Object ctxobj;
        Object frame;
        Object outobj;
        Object compactobj;
        Object frameobj;
        final JsonLdOptions opts = new JsonLdOptions("");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("cool/pandora/exts/sparqler/context.json");
        ctxobj = JsonUtils.fromInputStream(is);
        RDFFormat sesameInputFormat = RDFFormat.NTRIPLES;
        final String graph = Deskolemize.convertSkolem(ntriples);
        final Model inModel = Rio.parse(new StringReader(graph), "",
                sesameInputFormat);
        outobj = JsonLdProcessor.fromRDF(inModel, new RDF4JJSONLDRDFParser());
        compactobj = JsonLdProcessor.compact(outobj, ctxobj, opts);
        InputStream fs = classloader.getResourceAsStream("cool/pandora/exts/sparqler/frame.json");
        frame = JsonUtils.fromInputStream(fs);
        frameobj= JsonLdProcessor.frame(compactobj, frame, opts);
        //System.out.println(JsonUtils.toPrettyString(frameobj));
        //Files.write(Paths.get("output.json"), JsonUtils.toPrettyString(compactobj).getBytes());
        return JsonUtils.toPrettyString(frameobj);
    }
}
