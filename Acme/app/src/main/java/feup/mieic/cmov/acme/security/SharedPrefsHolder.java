package feup.mieic.cmov.acme.security;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class serves as an interface for the app's SharedPreferences.
 */
public class SharedPrefsHolder {

    private static final String SETTINGS = "settings";

    public SharedPrefsHolder(){ }

    public static void updateCurrentUser(String username, Integer uuid, String acmePK, Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("username",username);
        editor.putString("uuid", Integer.toString(uuid));
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

    public static String getAcmePublicKey(Context c){
        SharedPreferences settings = c.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getString("acmePK", null);
    }
}
