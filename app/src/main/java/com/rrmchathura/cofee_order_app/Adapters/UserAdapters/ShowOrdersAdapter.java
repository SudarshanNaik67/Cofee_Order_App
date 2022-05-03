package com.rrmchathura.cofee_order_app.Adapters.UserAdapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rrmchathura.cofee_order_app.Model.CartItemsModel;
import com.rrmchathura.cofee_order_app.Model.OrdersModel;
import com.rrmchathura.cofee_order_app.R;
import com.rrmchathura.cofee_order_app.Show_Order_Details_Activity;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowOrdersAdapter extends RecyclerView.Adapter<ShowOrdersAdapter.OrdersViewHolder> {

    Context context;
    ArrayList<OrdersModel> ordersList;

    public ShowOrdersAdapter(Context context, ArrayList<OrdersModel> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_show_orders, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        OrdersModel ordersModel = ordersList.get(position);


        String email = ordersModel.getEmail();
        String orderDate = ordersModel.getOrderDate();
        String amount = ordersModel.getFinalPrice();
        String mobile = ordersModel.getMobile();
        String address = ordersModel.getAddress();
        String username = ordersModel.getUsername();


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderDate));
        String formatDate = DateFormat.format("dd/MM/yyyy hh:mm a", calendar).toString();

        holder.emailTv.setText(email);
        holder.orderDateTv.setText(formatDate);
        holder.orderIdTv.setText(orderDate);
        holder.amountTv.setText("Rs."+amount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Show_Order_Details_Activity.class);
                intent.putExtra("username",username);
                intent.putExtra("email",email);
                intent.putExtra("orderDate",orderDate);
                intent.putExtra("amount",amount);
                intent.putExtra("mobile",mobile);
                intent.putExtra("address",address);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdTv,emailTv,orderDateTv,amountTv;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            emailTv = itemView.findViewById(R.id.emailTv);
            orderDateTv = itemView.findViewById(R.id.orderDateTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            orderIdTv = itemView.findViewById(R.id.orderIdTv);
        }
    }
}
