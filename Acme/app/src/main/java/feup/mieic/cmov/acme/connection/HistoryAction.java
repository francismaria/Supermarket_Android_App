package feup.mieic.cmov.acme.connection;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import feup.mieic.cmov.acme.security.SharedPrefsHolder;
import feup.mieic.cmov.acme.ui.history.HistoryViewModel;

public class HistoryAction extends AsyncTask<String, Void, Boolean> {

    private JSONObject res;
    private JSONObject req;
    private HistoryViewModel model;

    public HistoryAction(HistoryViewModel model){
        this.model = model;
    }

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
    protected Boolean doInBackground(String... params) {

        HttpURLConnection urlConnection = null;

        setRequestBody(params[0]);

        if(req == null) return false;

        try {
            URL url = new URL(HTTPInfo.HISTORY_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            Log.i("HISTORY ACTION", "request POST sent" + req.toString());

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("HISTORY ACTION", "OK");
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    res = new JSONObject(line);
                    Log.i("HISTORY", res.toString());
                }
            } else {
                return false;
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
            try {
                model.setRequestResult(res.getJSONArray("items"));
            } catch (JSONException e) {
                e.printStackTrace();
                model.setRequestResult(null);
            }
        } else {
            model.setRequestResult(null);
        }
    }
}
