package info.ecomay.ui.orders;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import info.ecomay.ConstantSp;
import info.ecomay.OrderDetailActivity;
import info.ecomay.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    Context context;
    ArrayList<OrderList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public OrderAdapter(Context context, ArrayList<OrderList> arrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OrderAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order,parent,false);
        return new OrderAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView orderNo,name,contact,address,payment,status,cancel;
        CardView cancelCard;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderNo = itemView.findViewById(R.id.custom_order_no);
            name = itemView.findViewById(R.id.custom_order_name);
            contact = itemView.findViewById(R.id.custom_order_contact);
            address = itemView.findViewById(R.id.custom_order_address);
            payment = itemView.findViewById(R.id.custom_order_payment);
            status = itemView.findViewById(R.id.custom_order_status);

            cancelCard = itemView.findViewById(R.id.custom_cancel_card);
            cancel = itemView.findViewById(R.id.custom_cancel);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyHolder holder, int position) {
        if(arrayList.get(position).getStatus().equalsIgnoreCase("Pending")) {
            holder.cancelCard.setVisibility(VISIBLE);
            holder.status.setText("In Process");
            holder.status.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        else if(arrayList.get(position).getStatus().equalsIgnoreCase("Cancelled")) {
            holder.cancelCard.setVisibility(GONE);
            holder.status.setText(arrayList.get(position).getStatus());
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }
        else{
            holder.cancelCard.setVisibility(GONE);
            holder.status.setText(arrayList.get(position).getStatus());
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUpdateStatus("Cancelled",position);
            }
        });

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

    private void doUpdateStatus(String sStatus, int position) {
        String updateQuery = "UPDATE ORDER_TABLE SET STATUS='"+sStatus+"' WHERE ORDERID='"+arrayList.get(position).getOrderId()+"'";
        db.execSQL(updateQuery);

        OrderList list = new OrderList();
        list.setOrderId(arrayList.get(position).getOrderId());
        list.setName(arrayList.get(position).getName());
        list.setEmail(arrayList.get(position).getEmail());
        list.setContact(arrayList.get(position).getContact());
        list.setAddress(arrayList.get(position).getAddress());
        list.setPincode(arrayList.get(position).getPincode());
        list.setCountry(arrayList.get(position).getCountry());
        list.setPaymentMode(arrayList.get(position).getPaymentMode());
        list.setTransactionId(arrayList.get(position).getTransactionId());
        list.setTotal(arrayList.get(position).getTotal());
        list.setStatus(sStatus);
        arrayList.set(position,list);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
