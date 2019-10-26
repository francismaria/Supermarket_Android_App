package feup.mieic.cmov.acme.connect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAction extends AsyncTask<String, Void, Void > {

    private static final String LOGIN_PATH = "http://10.0.2.2:8080/AcmeServer/acme/api/login";

    private static final int SUCCESS_CODE = 200;
    private static final int UNAUTHORIZED_CODE = 401;

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(LoginAction.LOGIN_PATH);
            JSONObject obj = new JSONObject();
            obj.put("username", "example");
            obj.put("password", "123456");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(obj.toString().getBytes("UTF-8"));
            os.close();

            Log.i("LOGIN ACTION", "request POST send");


            int code = urlConnection.getResponseCode();

            if (code == LoginAction.SUCCESS_CODE) {
                Log.i("LOGIN ACTION", "OK");

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    JSONObject jsonObject = new JSONObject(line);
                    Log.i("LOGIN", jsonObject.toString());
                }
            } else if(code == LoginAction.UNAUTHORIZED_CODE){
                Log.e("LOGIN ACTION", "The credentials are wrong.");
            } else {
                throw new IOException("Invalid response from server: " + code);
            }
        } catch(ConnectException exc){
            Log.e("LOGIN ACTION", "There is no connectivity to the server. Is it down?");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    protected void onProgressUpdate(String... params) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
        Log.i("INFORMATION_RESULT", "finished");
    }
}
