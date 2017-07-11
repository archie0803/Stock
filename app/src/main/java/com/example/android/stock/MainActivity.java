package com.example.android.stock;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.format;
import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity implements onDownloadCompleteListener, onCompleteListener {

    ArrayList<Stock> stockArrayList;
    ArrayList<String> stockSymbol;
    ArrayList<String> stockChange;
    ListView stockListView;
    StockAdapter stockAdapter;
    FloatingActionButton fab;
    ArrayList<SearchStock> getSymbol;
    ArrayList<String> searchName;
    ArrayList<String> searchSymbol;


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
        getSymbol = new ArrayList<>();
        searchName = new ArrayList<>();
        searchSymbol = new ArrayList<>();
        String urlString = "https://query.yahooapis.com/v1/public/yql?q=select+%2A+from+" +
                "yahoo.finance.quotes+where+symbol+in+%28%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22" +
                "%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback";

        fab = (FloatingActionButton) findViewById(R.id.Fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Search");
                builder.setCancelable(false);
                View v = getLayoutInflater().inflate(R.layout.search_view, null);
                TextView tv = v.findViewById(R.id.searchViewText);
                tv.setText("Enter the Stock Name");
                final EditText searchNameEdit = v.findViewById(R.id.searchViewEdit);
                builder.setView(v);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String searchText = searchNameEdit.getText().toString();
                        if (searchText.equals("")) {
                            dialog.dismiss();
                        } else {
                            String urlString = "https://stocksearchapi.com/api/?api_key=" +
                                    "a6a94a9508b6713037a1e39152dba2ea22d9c9e6&format=api&" +
                                    "search_text=" + searchText;
                            SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                            searchAsyncTask.execute(urlString);
                            searchAsyncTask.setOnCompleteListener(MainActivity.this);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        fetchStock(urlString);
    }

    public void fetchStock(String urlString) {


        StockAsyncTask stockAsyncTask = new StockAsyncTask();
        stockAsyncTask.execute(urlString);
        stockAsyncTask.setOnDownloadCompleteListener(this);


    }

    @Override
    public void onDownloadComplete(ArrayList<Stock> stockList) {
        stockArrayList.clear();
        stockArrayList.addAll(stockList);
        for (int i = 0; i < stockArrayList.size(); i++) {
            stockSymbol.add(stockArrayList.get(i).symbol);
            stockChange.add(stockArrayList.get(i).change);
        }
        stockAdapter.notifyDataSetChanged();

    }

    @Override
    public void onComplete(ArrayList<SearchStock> nameSymbol) {
        getSymbol.clear();
        searchName.clear();
        searchSymbol.clear();
        getSymbol.addAll(nameSymbol);
        for (int i = 0; i < getSymbol.size(); i++) {
            searchName.add(getSymbol.get(i).name);
            searchSymbol.add(getSymbol.get(i).symbol);
        }
        AlertDialog.Builder buildAgain = new AlertDialog.Builder(this);
        buildAgain.setTitle("Select");
        final ArrayAdapter<String> displayResult = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, searchName);
        displayResult.notifyDataSetChanged();
        buildAgain.setAdapter(displayResult, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String symbol = searchSymbol.get(i);
                String urlString = "https://query.yahooapis.com/v1/public/yql?q=select+%2A+from+" +
                        "yahoo.finance.quotes+where+symbol+in+%28%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22%2C%22" + symbol + "%22"
                        + "%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback";
                fetchStock(urlString);
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = buildAgain.create();
        dialog.show();
    }
}
