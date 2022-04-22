package com.rrmchathura.cofee_order_app.Filters;

import android.widget.Filter;

import com.rrmchathura.cofee_order_app.Adapters.CoffeeAdapter;
import com.rrmchathura.cofee_order_app.Model.CoffeeModel;

import java.util.ArrayList;

public class CoffeeFilter extends Filter {

    CoffeeAdapter coffeeAdapter;
    ArrayList<CoffeeModel> coffeeModelList;

    public CoffeeFilter(CoffeeAdapter coffeeAdapter, ArrayList<CoffeeModel> coffeeModelList) {
        this.coffeeAdapter = coffeeAdapter;
        this.coffeeModelList = coffeeModelList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results =new FilterResults();
        if (charSequence !=null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<CoffeeModel> filterModel = new ArrayList<>();
            for (int i=0; i<coffeeModelList.size(); i++){

                if (coffeeModelList.get(i).getCoffee_name().toUpperCase().contains(charSequence)){

                    filterModel.add(coffeeModelList.get(i));

                }
            }
            results.count = filterModel.size();
            results.values = filterModel;
        }
        else {
            results.count = coffeeModelList.size();
            results.values = coffeeModelList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        coffeeAdapter.coffeeList = (ArrayList<CoffeeModel>) filterResults.values;
        coffeeAdapter.notifyDataSetChanged();
    }
}
