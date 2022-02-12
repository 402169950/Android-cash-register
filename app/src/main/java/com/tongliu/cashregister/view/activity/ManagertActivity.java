package com.tongliu.cashregister.view.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;

import com.tongliu.cashregister.R;
import com.tongliu.cashregister.data.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ManagertActivity extends AppCompatActivity implements View.OnClickListener {
    private List<ItemEntity> itemLst;
    private List<ItemEntity> historyLst;



    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData().hasExtra(CashRegisterActivity.BundleExtra.ITEM_LIST)) {
                            itemLst = result.getData().getParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }

    private void initData() {
        itemLst = getIntent().getParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST);
        historyLst = getIntent().getParcelableArrayListExtra(CashRegisterActivity.BundleExtra.HISTORY_LIST);
    }

    private void initView() {
        findViewById(R.id.manager_history_btn).setOnClickListener(this);
        findViewById(R.id.manager_restock_btn).setOnClickListener(this);
    }

    public static void startActivity(Context context, ActivityResultLauncher<Intent> activityLauncher, List<ItemEntity> itemList, List<ItemEntity> historyLst) {
        Intent intent = new Intent(context, ManagertActivity.class);
        intent.putParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST, (ArrayList) itemList);
        intent.putParcelableArrayListExtra(CashRegisterActivity.BundleExtra.HISTORY_LIST, (ArrayList) historyLst);
        activityLauncher.launch(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST, (ArrayList) itemLst);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST, (ArrayList) itemLst);
        setResult(RESULT_OK, intent);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manager_history_btn:
                HistoryActivity.startActivity(this, historyLst);
                break;
            case R.id.manager_restock_btn:
                RestockActivity.startActivity(this, activityResultLauncher, itemLst);
                break;
        }
    }
}