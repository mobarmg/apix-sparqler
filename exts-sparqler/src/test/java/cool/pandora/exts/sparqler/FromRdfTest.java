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

import static org.junit.Assert.assertEquals;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.utils.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class FromRdfTest {

    @Test
    public void testFromRdf0001() throws IOException, JsonLdError {
        InputStream n3is = getClass().getResourceAsStream("/test1.n3");
        String ntriples = streamToString(n3is);
        final String framed = FromRdf.toJsonLd(ntriples);

        InputStream out = getClass().getResourceAsStream("/test1-out.json");
        final String expected = streamToString(out);
        System.out.println(JsonUtils.toPrettyString(framed));
        assertEquals(expected, framed);
    }

    @Test(expected = IOException.class)
    public void testFromRdf0002() throws IOException, JsonLdError {
        InputStream n3is = getClass().getResourceAsStream("/test2.n3");
        String ntriples = streamToString(n3is);
        final String framed = FromRdf.toJsonLd(ntriples);
    }

    private String streamToString(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
