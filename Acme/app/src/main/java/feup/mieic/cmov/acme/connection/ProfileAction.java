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

public class ProfileAction extends AsyncTask<Void, Void, Boolean> {

    private JSONObject req;

    @Override
    protected void onPreExecute(){ }

    private void setRequestBody(){
        req = new JSONObject();

        try {
            req.put("UUID", 1);
            req.put("publicKey", "example???");
        } catch (JSONException e) {
            e.printStackTrace();
            req = null;
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        setRequestBody();

        if(req == null) return false;     // error

        try {
            URL url = new URL(HTTPInfo.PROFILE_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            Log.i("REGISTER ACTION", "request POST sent" + req.toString());

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("REGISTER ACTION", "OK");
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    JSONObject jsonObject = new JSONObject(line);
                    Log.i("PROFILE", jsonObject.toString());
                }
            } else {
                return false;
            }
        } catch(Exception e){
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {

    }
}
