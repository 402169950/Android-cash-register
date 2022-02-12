package com.tongliu.cashregister.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tongliu.cashregister.R;
import com.tongliu.cashregister.data.entity.ItemEntity;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<ItemEntity> itemLst;
    private ItemClickListener mItemClickListener;
    private Context mContext;

    public HistoryAdapter(Context context,List <ItemEntity> itemLst) {
        this.itemLst = itemLst;
        mContext = context;
    }

    public void setDataAndNotify(List<ItemEntity> itemLst) {
        this.itemLst = itemLst;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cash_item_list_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cashItemNameTv.setText(itemLst.get(position).getName());
        holder.cashItemQuantityTv.setText(String.format( "%.2f" , itemLst.get(position).getPrice() * itemLst.get(position).getQuantity()) + mContext.getResources().getString(R.string.currency_symbol));
        holder.cashItemPriceTv.setText(String.valueOf(itemLst.get(position).getQuantity()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null)
                mItemClickListener.onItemClicked(itemLst.get(holder.getAdapterPosition()),holder.getAdapterPosition());
            }
        });

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return itemLst == null ? 0 : itemLst.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cashItemNameTv, cashItemQuantityTv, cashItemPriceTv;
        private ItemClickListener mItemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClicked(itemLst.get(getAdapterPosition()),getAdapterPosition());
        }

        ItemEntity getItem(int position) {
            return itemLst == null ? null : itemLst.get(position);
        }

        public void setItemClickListener(ItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

        private void initView(View itemView) {
            cashItemNameTv = itemView.findViewById(R.id.cash_item_name_tv);
            cashItemQuantityTv = itemView.findViewById(R.id.cash_item_quantity_tv);
            cashItemPriceTv = itemView.findViewById(R.id.cash_item_price_tv);
        }
    }

    public interface ItemClickListener {
        void onItemClicked(ItemEntity item, int position);
    }
}
