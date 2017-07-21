package cool.pandora.exts.sparqler;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {
    public static void main(String[] args) throws Exception {
        System.out.println(getManifestQuery("cool/pandora/exts/sparqler/manifest.rq", "http:/test"));
    }

    private static String replaceNode(String query, String node) {
        Pattern p = Pattern.compile("\\?node");
        Matcher m = p.matcher(query);
        StringBuffer sb = new StringBuffer(query.length());
        while (m.find()) {
            m.appendReplacement(sb, Matcher.quoteReplacement(node));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    static String getManifestQuery(final String qname, final String setName) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(qname);
        String out = readFile(is);
        return replaceNode(out, setName);
    }

    private static String readFile(InputStream in) throws IOException {
        StringBuilder inobj = new StringBuilder();
        try (BufferedReader buf = new BufferedReader(
                new InputStreamReader(in, "UTF-8"))) {
            String line;
            while ((line = buf.readLine()) != null) {
                inobj.append(line).append("\n");
            }
        }
        return inobj.toString();
    }
}
