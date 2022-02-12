package com.tongliu.cashregister.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tongliu.cashregister.R;
import com.tongliu.cashregister.data.entity.ItemEntity;
import com.tongliu.cashregister.view.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.ItemClickListener {
    private List<ItemEntity> historyLst;
    private RecyclerView historyRv;
    private HistoryAdapter adapter;

    @Override
    public void onItemClicked(ItemEntity item, int position) {
       HistoryDetailActivity.startActivity(this, item);
    }

    public static class BundleExtra{
        public static final String HISTORY_ITEM = "history_item";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }
    private void initView(){
        historyRv = findViewById(R.id.history_rv);
    }
    private void initData(){
        historyLst = getIntent().getParcelableArrayListExtra(CashRegisterActivity.BundleExtra.HISTORY_LIST);
        adapter = new HistoryAdapter(this,historyLst);
        historyRv.setLayoutManager(new LinearLayoutManager(this));
        historyRv.setAdapter(adapter);
        adapter.setItemClickListener(this);
    }


    public static void startActivity(Context context, List<ItemEntity> historyList) {
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putParcelableArrayListExtra(CashRegisterActivity.BundleExtra.HISTORY_LIST, (ArrayList<ItemEntity>) historyList);
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