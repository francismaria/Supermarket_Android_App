package feup.mieic.cmov.acme.connection;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAction extends AsyncTask<String, Void, Boolean> {

    private WeakReference<Context> weakActivity;

    private AlertDialog.Builder builder;
    private String ERROR_MSG;

    public LoginAction(Context context){
        weakActivity = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        builder = new AlertDialog.Builder(weakActivity.get());

    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String username = params[0], password = params[1];

        try {
            URL url = new URL(HTTPInfo.LOGIN_PATH);
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

            Log.i("LOGIN ACTION", "request POST sent");

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("LOGIN ACTION", "OK");

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    JSONObject jsonObject = new JSONObject(line);
                    // TODO: UPDATE SHARED PREFS WITH UUID AND ACME PK
                    Log.i("LOGIN", jsonObject.toString());
                }
            } else if(code == HTTPInfo.UNAUTHORIZED_CODE){
                Log.e("LOGIN ACTION", "The credentials are wrong.");
                ERROR_MSG = "Please check your credentials.\nThese are incorrect.";
                return true;
            } else {
                throw new IOException("Invalid response from server: " + code);
            }
        } catch(ConnectException exc){
            Log.e("LOGIN ACTION", "There is no connectivity to the server. Is it down?");
            ERROR_MSG = "No connectivity.";
            return true;


        } catch (Exception e) {
            Log.e("LOGIN ACTION", e.getMessage());
            return true;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean error) {
        if(error) {
            builder.setTitle("Error Message");
            builder.setMessage(ERROR_MSG);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
