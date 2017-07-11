package com.example.android.stock;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Archita on 10-07-2017.
 */

public class StockAsyncTask extends AsyncTask<String, Void, ArrayList<Stock>> {

    onDownloadCompleteListener mListener;

    void setOnDownloadCompleteListener(onDownloadCompleteListener listener) {
        this.mListener = listener;
    }

    @Override
    protected ArrayList<Stock> doInBackground(String... strings) {
        String urlString = strings[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scan = new Scanner(inputStream);
            String str = "";
            while (scan.hasNext()) {
                str += scan.nextLine();
            }

            return parseStock(str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private ArrayList<Stock> parseStock(String str) {
        try {
            JSONObject stocksJSON = new JSONObject(str);
            JSONObject query = stocksJSON.getJSONObject("query");
            JSONObject result = query.getJSONObject("results");
            JSONArray quote = result.getJSONArray("quote");
            ArrayList<Stock> stockList = new ArrayList<>();
            for (int i = 0; i < quote.length(); i++) {
                JSONObject stockJSON = (JSONObject) quote.get(i);
                String symbol = stockJSON.getString("symbol");
                String changePercentage = stockJSON.getString("Change_PercentChange");
                Stock stock = new Stock(symbol, changePercentage);
                stockList.add(stock);
            }

            return stockList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Stock> stocks) {
        super.onPostExecute(stocks);
        if (mListener != null) {
            mListener.onDownloadComplete(stocks);
        }
    }
}

interface onDownloadCompleteListener {
    void onDownloadComplete(ArrayList<Stock> stockList);
}