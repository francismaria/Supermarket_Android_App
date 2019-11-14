package feup.mieic.cmov.acme.connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import feup.mieic.cmov.acme.HomeActivity;
import feup.mieic.cmov.acme.security.Cryptography;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;

public class RegisterAction extends AsyncTask<JSONObject, Void, Boolean>  {

    private JSONObject res;
    private WeakReference<Context> weakActivity;

    // Security Keys
    private PrivateKey pri;
    private PublicKey pub;
    private static final int KEY_SIZE = 512;
    private static final String KEY_ALGO = "RSA";
    private static final int CERT_SERIAL = 12121212;
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public RegisterAction(Context context){
        weakActivity = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute(){ }

    @Override
    protected Boolean doInBackground(JSONObject... params) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(HTTPInfo.REGISTER_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(params[0].toString().getBytes("UTF-8"));
            os.close();

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("REGISTER ACTION", "OK");
            } else {
                Log.i("REGISTER ACTION", "ERROR " + Integer.toString(code));
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                res = new JSONObject(line);
                Log.i("REGISTER", res.toString());
            }

        } catch(Exception e){
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            try{

                Log.e("testencrypt", Arrays.toString(Cryptography.encrypt("ahahahahaha".getBytes(StandardCharsets.ISO_8859_1), SharedPrefsHolder.getAcmePublicKey(weakActivity.get()))));

/*

                // TODO : SAVE PUBLIC KEY
                // this converted to decimal is the same key as the server
                byte[] pub = Base64.decode(res.getString("acmePK"), Base64.DEFAULT);
                X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(pub);
                KeyFactory kf = KeyFactory.getInstance("RSA");

                PublicKey pk = kf.generatePublic(X509publicKey);


                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, pk);

                byte[] bs = "ahahahahaha".getBytes(StandardCharsets.ISO_8859_1);


                bs = cipher.doFinal(bs);


                Log.e("testencrypt", Arrays.toString(bs));

*/
                SharedPrefsHolder.updateCurrentUser(res.getString("username"), res.getInt("UUID"), res.getString("acmePK"), weakActivity.get());


                weakActivity.get().startActivity(new Intent(weakActivity.get(), HomeActivity.class));
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            // todo: show toast error
        }
    }
}
