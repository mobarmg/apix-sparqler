package cool.pandora.exts.sparqler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deskolemize {

    public boolean isWellKnownURI(String v) {
        Pattern p = Pattern.compile("^.+?#genid.+?$");
        Matcher m = p.matcher(v);
        return m.find();
    }

    /**
     * Converts a SkolemIRI to a BNode
     *
     * @param input the SkolemIRI to convert.
     *
     * @return a BNode.
     */
    public static String convertSkolem(String input) {
        Pattern p = Pattern.compile("http://([^>]+)#genid([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})");
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer(input.length());
        while (m.find()) {
            String id = m.group(2);
            String bnode = "_:b" + id;
            m.appendReplacement(sb, Matcher.quoteReplacement(bnode));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
