package feup.mieic.cmov.acme.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * This class serves as an interface for the app's SharedPreferences.
 */
public class SharedPrefsHolder {

    private static final String SETTINGS = "settings";

    public SharedPrefsHolder(){ }

    public static void updateCurrentUser(String username, String uuid, String acmePK, Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("username",username);
        editor.putString("uuid", uuid);
        editor.putString("acmePK", acmePK);

        editor.commit();
    }

    public static void removeCurrentUser(Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.remove("username");
        editor.remove("uuid");
        editor.remove("acmePK");

        editor.commit();
    }

    public static String getUsername(Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getString("username", null);
    }

    public static String getUUID(Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getString("uuid", null);
    }


    public static String getAcmeRawPublicKey(Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getString("acmePK", null);
    }

    public static PublicKey getAcmePublicKey(Context c) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String rawPK = settings.getString("acmePK", null);

        byte[] pub = Base64.decode(rawPK, Base64.DEFAULT);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(pub);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PublicKey pk = kf.generatePublic(X509publicKey);
        return pk;
    }
}
