package feup.mieic.cmov.terminal.connection;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CheckoutAction extends AsyncTask<String, Void, Boolean> {

    private JSONObject req;
    private Toast successfulPaymentToast;
    private WeakReference<Context> weakActivity;

    public CheckoutAction(Context c){
        weakActivity = new WeakReference<>(c);
    }

    @Override
    protected void onPreExecute(){ }

    @Override
    protected Boolean doInBackground(String... params) {

        String msg = params[0];
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(HTTPInfo.CHECKOUT_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.close();

            Log.i("CHECKOUT ACTION", "request POST sent");

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {

                Log.i("CHECKOUT ACTION", "OK");
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    JSONObject res = new JSONObject(line);
                    Log.i("CHECKOUT RESULT", res.toString());
                }
            } else {
                Log.e("CHECKOUT Action", "Server error");
                return false;
            }
        } catch(Exception e){
            Log.e("CHECKOUT Action", "Exception occurred");
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return true;
    }

    private void showSuccessfulPaymentToast(){
        successfulPaymentToast = Toast.makeText(weakActivity.get(), null, Toast.LENGTH_SHORT);
        successfulPaymentToast.setText("PAYMENT WAS SUCCESSFUL");
        successfulPaymentToast.show();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            Log.i("onpostexecute", "success");
            showSuccessfulPaymentToast();
        } else {
            Log.i("onpostexecute", "error");
        }
    }
}
