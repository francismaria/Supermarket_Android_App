package feup.mieic.cmov.acme.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

import feup.mieic.cmov.acme.HomeActivity;
import feup.mieic.cmov.acme.KeyInstance;

public class RegisterAction extends AsyncTask<JSONObject, Void, Boolean>  {

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

    private boolean generateKeyPair(String keyname) {

        keyname = keyname + "_key";

        try {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            // set the end date to 20 years from now
            end.add(Calendar.YEAR, 20);

            KeyPairGenerator kgen = null;
            kgen = KeyPairGenerator.getInstance(KEY_ALGO, ANDROID_KEYSTORE);

            AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(this.weakActivity.get())
                    .setKeySize(KEY_SIZE)
                    .setAlias("example")                    // this can not be hardcoded
                    .setSubject(new X500Principal("CN=" + keyname))
                    .setSerialNumber(BigInteger.valueOf(CERT_SERIAL))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            kgen.initialize(spec);

            KeyPair kp = kgen.generateKeyPair();
            pri = kp.getPrivate();
            pub = kp.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

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
                JSONObject jsonObject = new JSONObject(line);
                Log.i("LOGIN", jsonObject.toString());
                // TODO: get supermarket key
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
            weakActivity.get().startActivity(new Intent(weakActivity.get(), HomeActivity.class));
        } else {
            // todo: show toast error
        }
    }
}
