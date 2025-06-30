package info.ecomay.ui.orders;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import info.ecomay.ConstantSp;
import info.ecomay.OrderDetailActivity;
import info.ecomay.ProductDetailActivity;
import info.ecomay.R;
import info.ecomay.ui.home.ProductAdapter;
import info.ecomay.ui.home.ProductList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    Context context;
    ArrayList<OrderList> arrayList;
    SharedPreferences sp;

    public OrderAdapter(Context context, ArrayList<OrderList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OrderAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order,parent,false);
        return new OrderAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView orderNo,name,contact,address,payment;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderNo = itemView.findViewById(R.id.custom_order_no);
            name = itemView.findViewById(R.id.custom_order_name);
            contact = itemView.findViewById(R.id.custom_order_contact);
            address = itemView.findViewById(R.id.custom_order_address);
            payment = itemView.findViewById(R.id.custom_order_payment);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyHolder holder, int position) {
        holder.orderNo.setText("Order No : "+arrayList.get(position).getOrderId());
        holder.name.setText(arrayList.get(position).getName());
        holder.contact.setText(arrayList.get(position).getContact());
        holder.address.setText(arrayList.get(position).getAddress()+" , "+arrayList.get(position).getCountry()+" , "+arrayList.get(position).getPincode());
        if(arrayList.get(position).getPaymentMode().equalsIgnoreCase("Cash")){
            holder.payment.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getTotal()+" ( "+arrayList.get(position).getPaymentMode()+" )");
        }
        else{
            holder.payment.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getTotal()+" ( "+arrayList.get(position).getPaymentMode()+" - "+arrayList.get(position).getTransactionId()+" )");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.ORDER_ID,arrayList.get(position).getOrderId()).commit();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
