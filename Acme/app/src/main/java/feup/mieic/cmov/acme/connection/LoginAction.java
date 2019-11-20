package feup.mieic.cmov.acme.connection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import feup.mieic.cmov.acme.HomeActivity;
import feup.mieic.cmov.acme.security.Cryptography;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;

public class LoginAction extends AsyncTask<String, Void, Boolean> {

    private JSONObject res;

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

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    res = new JSONObject(line);
                }
                return true;
            } else if(code == HTTPInfo.UNAUTHORIZED_CODE){
                ERROR_MSG = "Please check your credentials.\nThese are incorrect.";
                return false;
            } else {
                throw new IOException("Invalid response from server: " + code);
            }
        } catch(ConnectException exc){
            ERROR_MSG = "No connectivity.";
            return false;
        } catch (Exception e) {
            Log.e("LOGIN ACTION", e.getMessage());
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(!success) {
            builder.setTitle("Error Message");
            builder.setMessage(ERROR_MSG);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            try{
                SharedPrefsHolder.updateCurrentUser(res.getString("username"), res.getString("UUID"), res.getString("acmePK"), weakActivity.get());

                // KeyInstance.generateKeyPair(weakActivity.get(), SharedPrefsHolder.getUsername(weakActivity.get()));

                Log.e("username", SharedPrefsHolder.getUsername(weakActivity.get()));

                weakActivity.get().startActivity(new Intent(weakActivity.get(), HomeActivity.class));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
