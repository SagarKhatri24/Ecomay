package info.ecomay;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyHolder> {
    
    Context context;
    ArrayList<CartList> arrayList;
    SharedPreferences sp;
    
    public OrderDetailAdapter(Context context, ArrayList<CartList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_detail,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,newPrice,oldPrice,discount,unit,qty;
        RelativeLayout cartLayout;
        
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_order_detail_image);
            name = itemView.findViewById(R.id.custom_order_detail_name);
            newPrice = itemView.findViewById(R.id.custom_order_detail_new_price);
            oldPrice = itemView.findViewById(R.id.custom_order_detail_old_price);
            discount = itemView.findViewById(R.id.custom_order_detail_discount);
            unit = itemView.findViewById(R.id.custom_order_detail_unit);
            
            qty = itemView.findViewById(R.id.custom_order_detail_qty);
            cartLayout = itemView.findViewById(R.id.custom_order_detail_cart_layout);
            
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.newPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getNewPrice());
        holder.oldPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.discount.setText(arrayList.get(position).getDiscount()+"% off");
        holder.unit.setText(arrayList.get(position).getUnit());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.qty.setText("Qty : "+String.valueOf(arrayList.get(position).getQty()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCTID, String.valueOf(arrayList.get(position).getProductId())).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NAME,arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_OLDPRICE,arrayList.get(position).getOldPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NEWPRICE,arrayList.get(position).getNewPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DISCOUNT,arrayList.get(position).getDiscount()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_UNIT,arrayList.get(position).getUnit()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_IMAGE,arrayList.get(position).getImage()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESCRIPTION,arrayList.get(position).getDescription()).commit();

                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    
}
