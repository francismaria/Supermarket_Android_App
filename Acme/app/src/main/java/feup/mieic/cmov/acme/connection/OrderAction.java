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
import java.util.UUID;

import feup.mieic.cmov.acme.ui.order.OrderViewModel;

public class OrderAction extends AsyncTask<Void, Void, Boolean> {

    private String userID;
    private String orderID;

    private JSONObject res;
    private JSONObject req;

    private OrderViewModel model;

    public OrderAction(OrderViewModel model, String userID, String orderID){
        this.userID = userID;
        this.orderID = orderID;
        this.model = model;
    }

    @Override
    protected void onPreExecute(){ }

    private void setRequestBody(){
        req = new JSONObject();

        try {
            req.put("UUID", userID);
            req.put("orderID", orderID);
        } catch (JSONException e) {
            e.printStackTrace();
            req = null;
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        setRequestBody();

        if(req == null) return false;

        try {
            URL url = new URL(HTTPInfo.GET_ORDER_INFO_PATH);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(req.toString().getBytes("UTF-8"));
            os.close();

            Log.i("ORDER ACTION", "request POST sent" + req.toString());

            int code = urlConnection.getResponseCode();

            if (code == HTTPInfo.SUCCESS_CODE) {
                Log.i("ORDER ACTION", "OK");
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    res = new JSONObject(line);
                    Log.i("ORDER", res.toString());
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

    private String roundDouble() throws JSONException {
        Double totalCostValue = res.getDouble("total_cost");
        totalCostValue = Math.round(totalCostValue * 100.0) / 100.0;
        return Double.toString(totalCostValue);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            try {
                model.setRequestResult(res.getString("orderID"), res.getString("date"),
                    roundDouble(), res.getString("vouchers"),
                    res.getJSONArray("products"));
            } catch (JSONException e) {
                e.printStackTrace();
                model.flagError();
            }
        } else {
            model.flagError();
        }
    }
}