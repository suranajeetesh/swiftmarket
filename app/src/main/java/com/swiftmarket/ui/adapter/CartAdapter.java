package com.swiftmarket.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.swiftmarket.R;
import com.swiftmarket.data.LocalProductsModel;
import com.swiftmarket.databinding.ItemCartBinding;
import com.swiftmarket.databinding.ItemProductBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Sam.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<LocalProductsModel> productsList;
    private CartAdapter.ItemClickListener mItemClickListener;
    private DecimalFormat decimalFormat= new DecimalFormat("##.##");

    public CartAdapter(ArrayList<LocalProductsModel> productsList, CartAdapter.ItemClickListener itemClickListener) {
        this.productsList = productsList;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        LocalProductsModel product = productsList.get(position);
        holder.bindData(product);
        Glide.with(holder.itemView.getContext())
                .load(product.getProductImage()).centerCrop()
                .error(R.drawable.ic_profile_holder)
                .into(holder.binding.imgProductItem);

        float amount = Float.parseFloat(product.getProductPrice()) * Float.parseFloat(product.getProductQuantity());
        holder.binding.txtNumber.setText(holder.itemView.getContext().getResources().getString(R.string.dollar_value,decimalFormat.format(amount)));
        holder.binding.txtItemName.setText(product.getProductName());
        holder.binding.txtItemType.setText(product.getProductCategory());
        holder.binding.includeButton.itemNumber.setText(product.getProductQuantity());

        holder.binding.includeButton.imgAddition.setOnClickListener(v -> {
            if (mItemClickListener!=null){
                Toast.makeText(holder.itemView.getContext(), "In development", Toast.LENGTH_SHORT).show();
                mItemClickListener.itemClickIncrease(position,product);
            }
        });
        holder.binding.includeButton.imgSubtraction.setOnClickListener(v -> {
            if (mItemClickListener!=null){
                Toast.makeText(holder.itemView.getContext(), "In development", Toast.LENGTH_SHORT).show();
                mItemClickListener.itemClickDecrease(position,product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCartBinding binding;

        public ViewHolder(ItemCartBinding cartBinding) {
            super(cartBinding.getRoot());
            this.binding = cartBinding;
        }

        public void bindData(LocalProductsModel localProductsModel) {
            this.binding.setMData(localProductsModel);
            binding.executePendingBindings();
        }
    }


    public interface ItemClickListener {
        void itemClickIncrease(int position, LocalProductsModel productsModel);
        void itemClickDecrease(int position, LocalProductsModel productsModel);
    }
}
