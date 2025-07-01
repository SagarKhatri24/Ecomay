package info.ecomay;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import info.ecomay.ui.home.ProductList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    Context context;
    ArrayList<CartList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public CartAdapter(Context context, ArrayList<CartList> productArrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = productArrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    public void updateList(ArrayList<CartList> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart,parent,false);
        return new CartAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView,minus,plus;
        TextView name,newPrice,oldPrice,discount,unit,qty;
        RelativeLayout cartLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_cart_image);
            name = itemView.findViewById(R.id.custom_cart_name);
            newPrice = itemView.findViewById(R.id.custom_cart_new_price);
            oldPrice = itemView.findViewById(R.id.custom_cart_old_price);
            discount = itemView.findViewById(R.id.custom_cart_discount);
            unit = itemView.findViewById(R.id.custom_cart_unit);
            
            minus = itemView.findViewById(R.id.custom_cart_minus);
            plus = itemView.findViewById(R.id.custom_cart_plus);
            qty = itemView.findViewById(R.id.custom_cart_qty);
            cartLayout = itemView.findViewById(R.id.custom_cart_cart_layout);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.newPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getNewPrice());
        holder.oldPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.discount.setText(arrayList.get(position).getDiscount()+"% off");
        holder.unit.setText(arrayList.get(position).getUnit());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.qty.setText(String.valueOf(arrayList.get(position).getQty()));

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.iTotal += Integer.parseInt(arrayList.get(position).getNewPrice());
                CartActivity.cartTotal.setText(ConstantSp.PRICE_SYMBOL+CartActivity.iTotal);
                int iQty = arrayList.get(position).getQty()+1;
                int iTotal = Integer.parseInt(arrayList.get(position).getNewPrice())* iQty;
                String insertQuery = "UPDATE CART SET PRICE='"+arrayList.get(position).getNewPrice()+"',QTY='"+iQty+"',TOTAL='"+iTotal+"' WHERE CARTID='"+arrayList.get(position).getCartId()+"'";
                db.execSQL(insertQuery);

                holder.qty.setText(String.valueOf(iQty));
                doUpdateCartList(position,arrayList.get(position).getCartId(),iQty);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.iTotal -= Integer.parseInt(arrayList.get(position).getNewPrice());
                CartActivity.cartTotal.setText(ConstantSp.PRICE_SYMBOL+CartActivity.iTotal);

                int iQty = arrayList.get(position).getQty()-1;
                int iTotal = Integer.parseInt(arrayList.get(position).getNewPrice())* iQty;
                if(iQty>0) {
                    String insertQuery = "UPDATE CART SET PRICE='" + arrayList.get(position).getNewPrice() + "',QTY='" + iQty + "',TOTAL='" + iTotal + "' WHERE CARTID='" + arrayList.get(position).getCartId() + "'";
                    db.execSQL(insertQuery);
                    holder.qty.setText(String.valueOf(iQty));
                    doUpdateCartList(position,arrayList.get(position).getCartId(),iQty);
                }
                else{
                    String deleteQuery = "DELETE FROM CART WHERE CARTID='"+arrayList.get(position).getCartId()+"'";
                    db.execSQL(deleteQuery);
                    holder.qty.setText(String.valueOf(iQty));
                    arrayList.remove(position);
                    notifyDataSetChanged();
                    //doUpdateCartList(position,0,0);
                }
            }
        });

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

    private void doUpdateCartList(int position, int cartId, int qty) {
        CartList list = new CartList();
        list.setProductId(arrayList.get(position).getProductId());
        list.setSubCategoryId(arrayList.get(position).getSubCategoryId());
        list.setName(arrayList.get(position).getName());
        list.setDescription(arrayList.get(position).getDescription());
        list.setOldPrice(arrayList.get(position).getOldPrice());
        list.setNewPrice(arrayList.get(position).getNewPrice());
        list.setDiscount(arrayList.get(position).getDiscount());
        list.setUnit(arrayList.get(position).getUnit());
        list.setImage(arrayList.get(position).getImage());
        list.setCartId(cartId);
        list.setQty(qty);
        arrayList.set(position,list);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

