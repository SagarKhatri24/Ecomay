package info.ecomay.ui.home;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import info.ecomay.ConstantSp;
import info.ecomay.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> arrayList;

    public ProductAdapter(Context context, ArrayList<ProductList> productArrayList) {
        this.context = context;
        this.arrayList = productArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,newPrice,oldPrice,discount,unit,addItem;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            newPrice = itemView.findViewById(R.id.custom_product_new_price);
            oldPrice = itemView.findViewById(R.id.custom_product_old_price);
            discount = itemView.findViewById(R.id.custom_product_discount);
            unit = itemView.findViewById(R.id.custom_product_unit);
            addItem = itemView.findViewById(R.id.custom_product_add_item);
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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
