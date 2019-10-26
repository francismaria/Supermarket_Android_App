package feup.mieic.cmov.acme.connect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAction extends AsyncTask<String, Void, Void > {

    private static final String LOGIN_PATH = "http://10.0.2.2:8080/AcmeServer/acme/api/login";

    private static final int SUCCESS_CODE = 200;
    private static final int UNAUTHORIZED_CODE = 401;

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String username = params[0], password = params[1];

        try {
            URL url = new URL(LoginAction.LOGIN_PATH);
            JSONObject obj = new JSONObject();
            obj.put("username", username);
            obj.put("password", password);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(obj.toString().getBytes("UTF-8"));
            os.close();

            Log.i("LOGIN ACTION", obj.toString());
            Log.i("LOGIN ACTION", "request POST sent");

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
            Log.e("LOGIN ACTION", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    protected Void onProgressUpdate(String... params) {
        //setProgressPercent(progress[0]);
        return null;
    }

    protected Void onPostExecute() {
        //showDialog("Downloaded " + result + " bytes");
        Log.i("INFORMATION_RESULT", "finished");
        return null;
    }
}
