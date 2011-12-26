package util.connection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class StringHelper {
    
    public static String sha1(String message) {
        MessageDigest md;
        byte[] buffer, digest;
        String hash = "";

        try {
            buffer = message.getBytes("UTF-8");
            md = MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        md.update(buffer);
        digest = md.digest();

        for(byte aux : digest) {
            int b = aux & 0xff;
            String s = Integer.toHexString(b);
            if (s.length() == 1) hash += "0";
            hash += s;
        }

        return hash;
    }
    
    public static String urlEncode(String s) {
    	try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // should never happen
		}
    }

}
