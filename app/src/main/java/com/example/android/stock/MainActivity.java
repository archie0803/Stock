package com.example.android.stock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements onDownloadCompleteListener {

    ArrayList<Stock> stockArrayList;
    ArrayList<String> stockSymbol;
    ArrayList<String> stockChange;
    ListView stockListView;
    StockAdapter stockAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockArrayList = new ArrayList<>();
        stockSymbol = new ArrayList<>();
        stockChange = new ArrayList<>();
        stockAdapter = new StockAdapter(this, stockArrayList);
        stockListView = (ListView) findViewById(R.id.stock_list_view);
        stockListView.setAdapter(stockAdapter);

        fetchStock();
    }

    public void fetchStock() {
        String urlString = "https://query.yahooapis.com/v1/public/yql?q=select+%2A+from+" +
                "yahoo.finance.quotes+where+symbol+in+%28%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22%29" +
                "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback";
        StockAsyncTask stockAsyncTask = new StockAsyncTask();
        stockAsyncTask.execute(urlString);
        stockAsyncTask.setOnDownloadCompleteListener(this);


    }

    @Override
    public void onDownloadComplete(ArrayList<Stock> stockList) {
        stockArrayList.clear();
        stockArrayList.addAll(stockList);
        for(int i = 0; i<stockArrayList.size(); i++){
            stockSymbol.add(stockArrayList.get(i).symbol);
            stockChange.add(stockArrayList.get(i).change);
        }
        stockAdapter.notifyDataSetChanged();

    }
}
