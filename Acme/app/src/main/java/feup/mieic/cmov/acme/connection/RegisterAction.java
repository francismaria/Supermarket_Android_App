package feup.mieic.cmov.acme.connection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;

import feup.mieic.cmov.acme.HomeActivity;

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
