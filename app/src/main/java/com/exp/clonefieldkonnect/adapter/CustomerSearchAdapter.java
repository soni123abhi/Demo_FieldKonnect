package com.exp.clonefieldkonnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.exp.clonefieldkonnect.R;
import com.exp.clonefieldkonnect.model.DistriutorModel;
import java.util.ArrayList;
import java.util.List;

public class CustomerSearchAdapter extends ArrayAdapter<DistriutorModel.Datum> {

    private Context context;
    private List<DistriutorModel.Datum> products = new ArrayList<DistriutorModel.Datum>();
    private List<DistriutorModel.Datum> filteredProducts = new ArrayList<DistriutorModel.Datum>();

    public CustomerSearchAdapter(Context context, List<DistriutorModel.Datum> products) {
        super(context, R.layout.adapter_customer_search, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return filteredProducts.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new ProductFilter(this, products);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        DistriutorModel.Datum product = filteredProducts.get(position);
        view = LayoutInflater.from(context).inflate(R.layout.adapter_customer_search, parent, false);
        TextView tvRetailer = (TextView) view.findViewById(R.id.tvRetailer);

        tvRetailer.setText(product.getName());
        return view;
    }

    private class ProductFilter extends Filter {

        CustomerSearchAdapter productListAdapter;
        List<DistriutorModel.Datum> originalList;
        List<DistriutorModel.Datum> filteredList;

        public ProductFilter(CustomerSearchAdapter productListAdapter, List<DistriutorModel.Datum> originalList) {
            super();
            this.productListAdapter = productListAdapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final DistriutorModel.Datum product : originalList) {
                    if (product.getName().toLowerCase().contains(filterPattern)||
                            product.getMobile().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productListAdapter.filteredProducts.clear();
            productListAdapter.filteredProducts.addAll((List) results.values);
            productListAdapter.notifyDataSetChanged();
        }
    }

}