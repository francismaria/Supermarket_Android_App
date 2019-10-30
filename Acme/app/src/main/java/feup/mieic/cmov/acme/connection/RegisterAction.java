package feup.mieic.cmov.acme.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterAction extends AsyncTask<String, Void, Boolean>  {

    private static final String REGISTER_PATH = "http://10.0.2.2:8080/AcmeServer/acme/api/register";

    // HTTP response codes
    private static final int SUCCESS_CODE = 200;
    private static final int UNAUTHORIZED_CODE = 401;

    @Override
    protected void onPreExecute(){}

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        //String username = params[0], password = params[1];

        try {
            URL url = new URL(RegisterAction.REGISTER_PATH);
            JSONObject obj = new JSONObject();
            obj.put("username", "example");
            //obj.put("password", password);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(obj.toString().getBytes("UTF-8"));
            os.close();

            Log.i("REGISTER ACTION", "request POST sent");

            int code = urlConnection.getResponseCode();

            if (code == RegisterAction.SUCCESS_CODE) {
                Log.i("LOGIN ACTION", "OK");
            } else {
                Log.i("LOGIN ACTION", "ERROR - username already exists");
            }
        } catch(Exception e){

        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean error) {

    }
}
