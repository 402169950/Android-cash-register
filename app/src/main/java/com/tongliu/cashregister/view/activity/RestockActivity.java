package com.tongliu.cashregister.view.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tongliu.cashregister.R;
import com.tongliu.cashregister.data.entity.ItemEntity;
import com.tongliu.cashregister.view.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestockActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText restockQuantityEt;
    private RecyclerView restockListRv;
    private ProductAdapter adapter;
    private List<ItemEntity> itemLst;
    private int selectedPos = -1;

    private class BundleExtra {
        public static final String SELECTED_POS = "selected_pos";
    }

    private class Flag {
        public static final int NO_SELECTION = -1;
        public static final int NO_QUANTITY = -2;
        public static final int ZERO_QUANTITY = -3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
        restoreState(savedInstanceState);
    }

    private void initView() {
        restockQuantityEt = findViewById(R.id.restock_quantity_et);
        findViewById(R.id.restock_confirm_btn).setOnClickListener(this);
        findViewById(R.id.restock_cancel_btn).setOnClickListener(this);
        restockListRv = findViewById(R.id.restock_list_rv);
    }

    private void initData() {
        itemLst = getIntent().getParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST);
        adapter = new ProductAdapter(this, itemLst);
        restockListRv.setLayoutManager(new LinearLayoutManager(this));
        restockListRv.setAdapter(adapter);
        adapter.setItemClickListener(new ProductAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(ItemEntity item, int position) {
                selectedPos = position;
            }
        });
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null && !savedInstanceState.isEmpty()){
           selectedPos =  savedInstanceState.getInt(BundleExtra.SELECTED_POS);
            adapter.setSelectedPos(selectedPos);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BundleExtra.SELECTED_POS, selectedPos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restock_confirm_btn:
                int flag = validate();
                if (flag == Flag.NO_SELECTION)
                    Toast.makeText(this, getString(R.string.restock_no_selection), Toast.LENGTH_LONG).show();
                else if (flag == Flag.NO_QUANTITY)
                    Toast.makeText(this, getString(R.string.restock_no_quantity), Toast.LENGTH_LONG).show();
                else if (flag == Flag.ZERO_QUANTITY)
                    Toast.makeText(this, getString(R.string.restock_zero_quantity), Toast.LENGTH_LONG).show();
                else {
                    ItemEntity curItem = itemLst.get(selectedPos);
                    curItem.setQuantity(curItem.getQuantity() + Integer.parseInt(restockQuantityEt.getText().toString()));
                    Toast.makeText(this, getString(R.string.restock_restocked), Toast.LENGTH_LONG).show();
                    adapter.setDataAndNotify(itemLst);
                    restockQuantityEt.setText("");
                }
                break;
            case R.id.restock_cancel_btn:
                selectedPos = -1;
                restockQuantityEt.setText("");
                adapter.setDataAndNotify(itemLst);
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    private int validate() {
        if (selectedPos == -1)
            return Flag.NO_SELECTION;
        else if (TextUtils.isEmpty(restockQuantityEt.getText()))
            return Flag.NO_QUANTITY;
        else if (Integer.parseInt(restockQuantityEt.getText().toString()) == 0)
            return Flag.ZERO_QUANTITY;
        else
            return 0;
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

    public static void startActivity(Context context, ActivityResultLauncher<Intent> activityResultLauncher, List<ItemEntity> itemList) {
        Intent intent = new Intent(context, RestockActivity.class);
        intent.putParcelableArrayListExtra(CashRegisterActivity.BundleExtra.ITEM_LIST, (ArrayList) itemList);
        activityResultLauncher.launch(intent);
    }

}