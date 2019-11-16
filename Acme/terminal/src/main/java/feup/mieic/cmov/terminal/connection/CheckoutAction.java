package feup.mieic.cmov.terminal.connection;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckoutAction extends AsyncTask<String, Void, Boolean> {

    @Override
    protected void onPreExecute(){ }

    @Override
    protected Boolean doInBackground(String... params) {

        String msg = params[0];
        HttpURLConnection urlConnection = null;

        if(msg == null) return false;

        try {
            URL url = new URL(HTTPInfo.CHECKOUT_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(msg.getBytes("UTF-8"));
            os.close();

            Log.i("CHECKOUT ACTION", "request POST sent" + msg);

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {


            } else {
                Log.e("Contact Action", "Server error");
                return false;
            }
        } catch(Exception e){
            Log.e("Contact Action", "Exception occurred");
            e.printStackTrace();
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
            Log.i("onpostexecute", "success");
        } else {
            Log.i("onpostexecute", "error");
        }
    }
}
