package feup.mieic.cmov.acme.validation;

import java.security.MessageDigest;

public class Sha256Hashing {

    private String encrypted;

    public Sha256Hashing(String msg) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        byte[] digest = md.digest();

        StringBuffer converted = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {
            converted.append(Integer.toHexString(0xFF & digest[i]));
        }
        encrypted = converted.toString();
    }

    public String getResult(){
        return encrypted;
    }
}
