package feup.mieic.cmov.acme.connection;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VouchersCheckoutAction extends AsyncTask<String, Void, Integer>{

    private JSONObject res;
    private JSONObject req;

    @Override
    protected void onPreExecute(){ }

    private void setRequestBody(String uuid){
        req = new JSONObject();

        try {
            req.put("UUID", uuid);
        } catch (JSONException e) {
            e.printStackTrace();
            req = null;
        }
    }

    @Override
    protected Integer doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        setRequestBody(params[0]);

        if(req == null) return -1;

        try {
            URL url = new URL(HTTPInfo.VOUCHERS_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            Log.i("VOUCHERS ACTION", "request POST sent" + req.toString());

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    res = new JSONObject(line);
                }
                return res.getInt("vouchers");
            } else {
                return -1;
            }
        } catch(Exception e){
            return -1;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Integer numVouchers) {
        super.onPostExecute(numVouchers);
    }
}