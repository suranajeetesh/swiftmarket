package com.swiftmarket.ui.adapter;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.swiftmarket.data.LocalProductsModel;
import com.swiftmarket.R;
import com.swiftmarket.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements Filterable {
    private ArrayList<LocalProductsModel> productsList;
    private ArrayList<LocalProductsModel> productsListFull;
    private ItemClickListener mItemClickListener;

    public ProductsAdapter(ArrayList<LocalProductsModel> productsList, ItemClickListener itemClickListener) {
        this.productsList = productsList;
        this.productsListFull = new ArrayList<>(productsList);
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalProductsModel product = productsList.get(position);
        holder.bindData(product);
        Glide.with(holder.itemView.getContext())
                .load(product.getProductImage()).centerCrop()
                .error(R.drawable.ic_profile_holder)
                .into(holder.binding.imgProductItem);
        holder.binding.txtPrice.setText(holder.itemView.getContext().getResources().getString(R.string.dollar_value,product.getProductPrice()));
        holder.itemView.setOnClickListener(v -> mItemClickListener.itemClick(position, product));
        holder.binding.imgEdit.setOnClickListener(v -> mItemClickListener.updateItemClick(position,product));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEmployeeList(ArrayList<LocalProductsModel> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    private Filter productsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LocalProductsModel> filteredList = new ArrayList<>();

            if (TextUtils.isEmpty(constraint)) {
                filteredList.addAll(productsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (LocalProductsModel product : productsListFull) {
                    if (product.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productsList.clear();
            productsList.addAll((List<LocalProductsModel>) results.values);
            notifyDataSetChanged();

            if (mItemClickListener != null) {
                mItemClickListener.onSearchResultsChanged(productsList.isEmpty());
            }
        }
    };

    // Add this method to set the original full list
    public void setOriginalProductList(ArrayList<LocalProductsModel> originalList) {
        this.productsListFull = new ArrayList<>(originalList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;

        public ViewHolder(ItemProductBinding productBinding) {
            super(productBinding.getRoot());
            this.binding = productBinding;
        }

        public void bindData(LocalProductsModel localProductsModel) {
            this.binding.setMData(localProductsModel);
            binding.executePendingBindings();
        }
    }

    public interface ItemClickListener {
        void itemClick(int position, LocalProductsModel productsModel);
        void updateItemClick(int position, LocalProductsModel productsModel);

        void onSearchResultsChanged(boolean isEmpty);

    }
}
