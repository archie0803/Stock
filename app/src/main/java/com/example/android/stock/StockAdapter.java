package com.example.android.stock;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Archita on 10-07-2017.
 */

public class StockAdapter extends ArrayAdapter<Stock> {


    Context context;
    ArrayList<Stock> stockArrayList;

    public StockAdapter(@NonNull Context context, ArrayList<Stock> stockArrayList) {
        super(context, 0);
        this.context = context;
        this.stockArrayList = stockArrayList;
    }

    @Override
    public int getCount(){
        return stockArrayList.size();
    }

    static class StockViewHolder {
        TextView symbol;
        TextView percentageChange;

        public StockViewHolder(TextView symbol, TextView percentageChange) {
            this.symbol = symbol;
            this.percentageChange = percentageChange;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            TextView symbol = (TextView) convertView.findViewById(R.id.stock_symbol);
            TextView percentageChange = (TextView) convertView.findViewById(R.id.stock_percent_change);
            StockViewHolder stockViewHolder = new StockViewHolder(symbol, percentageChange);
            convertView.setTag(stockViewHolder);
        }
        StockViewHolder stockViewHolder = (StockViewHolder) convertView.getTag();
        Stock s = stockArrayList.get(position);
        stockViewHolder.symbol.setText(s.symbol);
        stockViewHolder.percentageChange.setText(s.change);

        return convertView;
    }
}
