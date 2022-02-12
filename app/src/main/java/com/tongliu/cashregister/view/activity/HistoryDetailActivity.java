package com.tongliu.cashregister.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TimeUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.tongliu.cashregister.R;
import com.tongliu.cashregister.data.entity.ItemEntity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryDetailActivity extends AppCompatActivity {
    private TextView historyDetailProductTv, historyDetailDateTv, historyDetailPriceTv;
    private class BundleExtra{
        public static final String SELECTED_POS = "selected_pos";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();

    }
    private void initView(){
        historyDetailProductTv = findViewById(R.id.history_detail_product_tv);
        historyDetailDateTv = findViewById(R.id.history_detail_date_tv);
        historyDetailPriceTv = findViewById(R.id.history_detail_price_tv);
    }
    private void initData(){
        ItemEntity historyEntity = getIntent().getParcelableExtra(HistoryActivity.BundleExtra.HISTORY_ITEM);
        historyDetailProductTv.setText(historyEntity.getName());
        Date date = new Date(historyEntity.getDate());
        DateFormat formatter = DateFormat.getDateTimeInstance();
        historyDetailDateTv.setText(formatter.format(date));
        historyDetailPriceTv.setText(String.format("%.2f", (historyEntity.getPrice() * historyEntity.getQuantity()))+ getString(R.string.currency_symbol));
    }
    public static void startActivity(Context context, ItemEntity historyItem){
        Intent intent = new Intent(context, HistoryDetailActivity.class);
        intent.putExtra(HistoryActivity.BundleExtra.HISTORY_ITEM, historyItem);
        context.startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}