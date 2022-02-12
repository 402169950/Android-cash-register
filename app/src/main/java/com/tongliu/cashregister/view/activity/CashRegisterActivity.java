package com.tongliu.cashregister.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tongliu.cashregister.R;
import com.tongliu.cashregister.data.entity.ItemEntity;
import com.tongliu.cashregister.view.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class CashRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private NumberPicker cashregQuantityNp;
    private RecyclerView cashregProductListRv;
    private TextView cashregProdTypeTv, cashregTotalTv, cashregQuantityTv;
    private ProductAdapter productListAdapter;
    private List<ItemEntity> itemLst;
    private List<ItemEntity> historyLst;
    private int selectItemPos = -1;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData().hasExtra(BundleExtra.ITEM_LIST)) {
                            itemLst = result.getData().getParcelableArrayListExtra(BundleExtra.ITEM_LIST);
                            productListAdapter.setDataAndNotify(itemLst);
                            updateView(selectItemPos, cashregQuantityNp.getValue());
                        }
                    }
                }
            });

    public static class BundleExtra {
        //Saved instance
        public static final String SELECTED_ITEM_POS = "selected_item";
        public static final String SELECTED_QUANTITY = "selected_quantity";
        public static final String ITEM_LIST = "item_list";
        public static final String HISTORY_LIST = "history_list";

    }

    private class Flags {
        public static final int VALID_QUANTITY = 1;
        public static final int NO_SELECTION_QUANTITY = -1;
        public static final int OUT_OF_STOCK = -2;
        public static final int NO_SELECTION_PRODUCT = -3;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BundleExtra.SELECTED_QUANTITY, cashregQuantityNp.getValue());
        outState.putInt(BundleExtra.SELECTED_ITEM_POS, selectItemPos);
        outState.putParcelableArrayList(BundleExtra.ITEM_LIST, (ArrayList) itemLst);
        outState.putParcelableArrayList(BundleExtra.HISTORY_LIST, (ArrayList)historyLst);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_regsiter_layout);
        initView();
        initData();
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            itemLst = savedInstanceState.getParcelableArrayList(BundleExtra.ITEM_LIST);
            historyLst = savedInstanceState.getParcelableArrayList(BundleExtra.HISTORY_LIST);
            productListAdapter.setDataAndNotify(itemLst);
            updateView(savedInstanceState.getInt(BundleExtra.SELECTED_ITEM_POS), savedInstanceState.getInt(BundleExtra.SELECTED_QUANTITY));
            productListAdapter.setSelectedPos(selectItemPos);
        }

    }

    private void initData() {
        historyLst = new ArrayList<>();
        itemLst = new ArrayList<>();
        itemLst.add(new ItemEntity("Pante", 10, 20.44f));
        itemLst.add(new ItemEntity("Shoes", 100, 20.44f));
        itemLst.add(new ItemEntity("Hats", 30, 5.9f));
        productListAdapter = new ProductAdapter(this,itemLst);
        cashregProductListRv.setLayoutManager(new LinearLayoutManager(this));
        cashregProductListRv.setAdapter(productListAdapter);
        cashregQuantityNp.setMinValue(0);
        cashregQuantityNp.setMaxValue(0);
        productListAdapter.setItemClickListener((item, position) -> {
            selectItemPos = position;
            updateView(position, 0);
        });

    }

    private void initView() {

        findViewById(R.id.cashreg_manager_btn).setOnClickListener(this);

        cashregQuantityNp = findViewById(R.id.cashreg_quantity_np);
        cashregProductListRv = findViewById(R.id.cashreg_product_list_rv);
        cashregProdTypeTv = findViewById(R.id.cashreg_prod_type_tv);
        cashregTotalTv = findViewById(R.id.cashreg_total_tv);
        cashregQuantityTv = findViewById(R.id.cashreg_quantity_tv);
        findViewById(R.id.cashreg_manager_btn).setOnClickListener(this);
        findViewById(R.id.cashreg_buy_btn).setOnClickListener(this);
        cashregQuantityNp.setOnValueChangedListener((picker, oldVal, newVal) -> updateView(selectItemPos, newVal));

    }


    private void updateView(int selectedPos, int selectedQuantity) {
        if (selectedPos == -1)
            return;
        selectItemPos = selectedPos;
        ItemEntity selectedItem = itemLst.get(selectItemPos);
        cashregQuantityNp.setMaxValue(selectedItem.getQuantity());
        cashregQuantityNp.setValue(selectedQuantity);
        cashregQuantityTv.setText(String.valueOf(selectedQuantity));
        cashregTotalTv.setText(String.format("%.2f", selectedQuantity * selectedItem.getPrice()));
        cashregProdTypeTv.setText(selectedItem.getName());
    }

    private int validate() {
        int quantity = Integer.parseInt(cashregQuantityTv.getText().toString());
        if (selectItemPos == -1)
            return Flags.NO_SELECTION_PRODUCT;
        else if (quantity == 0) {
            if (itemLst.get(selectItemPos).getQuantity() == 0)
                return Flags.OUT_OF_STOCK;
            else
                return Flags.NO_SELECTION_QUANTITY;
        } else
            return Flags.VALID_QUANTITY;

    }

    private void promptPurchase() {
        ItemEntity purchasedItem = itemLst.get(selectItemPos);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setPositiveButton(R.string.dialog_negative, (dialog, which) -> {
            dialog.dismiss();
        });
        dialogBuilder.setCancelable(true);
        StringBuilder sb = new StringBuilder(getString(R.string.purchase_confirmation_msg_prefix));
        sb.append(cashregQuantityNp.getValue()).append(' ');
        sb.append(purchasedItem.getName()).append(' ');
        sb.append(getString(R.string.purchase_confirmation_msg_preposition)).append(' ');
        sb.append(String.format("%.2f", purchasedItem.getPrice() * cashregQuantityNp.getValue()));
        dialogBuilder.setMessage(sb.toString());
        dialogBuilder.show();
    }

    private void afterPurchase() {
        int quantityPurchased = cashregQuantityNp.getValue();
        ItemEntity itemPurchased = itemLst.get(selectItemPos);
        createHistory(itemPurchased.getName(), quantityPurchased, itemPurchased.getPrice());
        itemPurchased.setQuantity(itemPurchased.getQuantity() - quantityPurchased);
        cashregQuantityNp.setMaxValue(itemPurchased.getQuantity());
        cashregQuantityNp.setValue(0);
        cashregQuantityTv.setText(String.valueOf(0));
        productListAdapter.notifyItemChanged(selectItemPos);
        cashregTotalTv.setText(String.format("%.2f", 0.00));
    }

    private void createHistory(String name, int stock, float price) {
        historyLst.add(new ItemEntity(name, stock, price, System.currentTimeMillis()));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cashreg_manager_btn:
                ManagertActivity.startActivity(this, activityResultLauncher, itemLst, historyLst);
                break;
            case R.id.cashreg_buy_btn:

                int flag = validate();
                if (flag == Flags.VALID_QUANTITY) {
                    promptPurchase();
                    afterPurchase();
                } else if (flag == Flags.NO_SELECTION_QUANTITY)
                    Toast.makeText(this, getString(R.string.no_selection_quantity_prompt), Toast.LENGTH_LONG).show();
                else if (flag == Flags.OUT_OF_STOCK)
                    Toast.makeText(this, getString(R.string.selection_out_of_stock), Toast.LENGTH_LONG).show();
                else if (flag == Flags.NO_SELECTION_PRODUCT) {
                    Toast.makeText(this, getString(R.string.no_selection_product_prompt), Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
    }

}
