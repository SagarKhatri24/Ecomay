package info.ecomay.ui.home;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

import info.ecomay.ConstantSp;
import info.ecomay.ProductDetailActivity;
import info.ecomay.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public ProductAdapter(Context context, ArrayList<ProductList> productArrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = productArrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    public void updateList(ArrayList<ProductList> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView,wishlist,minus,plus;
        TextView name,newPrice,oldPrice,discount,unit,addItem,qty,edit,delete;
        RelativeLayout cartLayout;
        CardView cartCard;
        LinearLayout editLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            newPrice = itemView.findViewById(R.id.custom_product_new_price);
            oldPrice = itemView.findViewById(R.id.custom_product_old_price);
            discount = itemView.findViewById(R.id.custom_product_discount);
            unit = itemView.findViewById(R.id.custom_product_unit);
            addItem = itemView.findViewById(R.id.custom_product_add_item);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);

            minus = itemView.findViewById(R.id.custom_product_minus);
            plus = itemView.findViewById(R.id.custom_product_plus);
            qty = itemView.findViewById(R.id.custom_product_qty);
            cartLayout = itemView.findViewById(R.id.custom_product_cart_layout);

            cartCard = itemView.findViewById(R.id.custom_product_cart_card);

            edit = itemView.findViewById(R.id.custom_product_edit);
            delete = itemView.findViewById(R.id.custom_product_delete);
            editLayout = itemView.findViewById(R.id.custom_product_edit_layout);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if(sp.getString(ConstantSp.USERTYPE,"").equalsIgnoreCase("admin")){
            holder.editLayout.setVisibility(VISIBLE);
        }
        else{
            holder.editLayout.setVisibility(GONE);
        }

        holder.name.setText(arrayList.get(position).getName());
        holder.newPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getNewPrice());
        holder.oldPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.discount.setText(arrayList.get(position).getDiscount()+"% off");
        holder.unit.setText(arrayList.get(position).getUnit());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        if(sp.getString(ConstantSp.USERTYPE,"").equalsIgnoreCase("admin")){
            holder.wishlist.setVisibility(GONE);
            holder.cartCard.setVisibility(GONE);
        }
        else{
            holder.wishlist.setVisibility(VISIBLE);
            holder.cartCard.setVisibility(VISIBLE);
        }

        if(arrayList.get(position).getWishlistId()==0){
            holder.wishlist.setImageResource(R.drawable.wishlist_blank);
        }
        else{
            holder.wishlist.setImageResource(R.drawable.wishlist_fill);
        }

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.get(position).getWishlistId()==0){
                    String insertQuery = "INSERT INTO WISHLIST VALUES (NULL,'"+sp.getString(ConstantSp.USERID,"")+"','"+arrayList.get(position).getProductId()+"')";
                    db.execSQL(insertQuery);
                    holder.wishlist.setImageResource(R.drawable.wishlist_fill);

                    String selectQuery = "SELECT MAX(WISHLISTID) FROM WISHLIST LIMIT 1";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        while (cursor.moveToNext()){
                            doUpdateList(position, Integer.parseInt(cursor.getString(0)));
                        }
                    }
                }
                else{
                    String deleteQuery = "DELETE FROM WISHLIST WHERE WISHLISTID='"+arrayList.get(position).getWishlistId()+"'";
                    db.execSQL(deleteQuery);
                    holder.wishlist.setImageResource(R.drawable.wishlist_blank);
                    doUpdateList(position,0);
                }
            }
        });

        if(arrayList.get(position).getCartId()==0){
            holder.addItem.setVisibility(VISIBLE);
            holder.cartLayout.setVisibility(GONE);
        }
        else{
            holder.addItem.setVisibility(GONE);
            holder.cartLayout.setVisibility(VISIBLE);
        }

        holder.qty.setText(String.valueOf(arrayList.get(position).getQty()));

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty = 1;
                int iTotal = Integer.parseInt(arrayList.get(position).getNewPrice())* iQty;
                String insertQuery = "INSERT INTO CART VALUES(NULL,'0','"+sp.getString(ConstantSp.USERID,"")+"','"+arrayList.get(position).getProductId()+"','"+arrayList.get(position).getNewPrice()+"','"+iQty+"','"+iTotal+"')";
                db.execSQL(insertQuery);

                int iCartId = 0;
                String selectQuery = "SELECT MAX(CARTID) FROM CART LIMIT 1";
                Cursor cursor = db.rawQuery(selectQuery,null);
                if(cursor.getCount()>0){
                    while (cursor.moveToNext()){
                        iCartId = Integer.parseInt(cursor.getString(0));
                    }
                }
                holder.qty.setText(String.valueOf(iQty));
                doUpdateCartList(position,iCartId,iQty);
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    doUpdateCartList(position,0,0);
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
        ProductList list = new ProductList();
        list.setProductId(arrayList.get(position).getProductId());
        list.setSubCategoryId(arrayList.get(position).getSubCategoryId());
        list.setName(arrayList.get(position).getName());
        list.setDescription(arrayList.get(position).getDescription());
        list.setOldPrice(arrayList.get(position).getOldPrice());
        list.setNewPrice(arrayList.get(position).getNewPrice());
        list.setDiscount(arrayList.get(position).getDiscount());
        list.setUnit(arrayList.get(position).getUnit());
        list.setImage(arrayList.get(position).getImage());
        list.setWishlistId(arrayList.get(position).getWishlistId());
        list.setCartId(cartId);
        list.setQty(qty);
        arrayList.set(position,list);
        notifyItemChanged(position);
    }

    private void doUpdateList(int position, int wishlistId) {
        ProductList list = new ProductList();
        list.setProductId(arrayList.get(position).getProductId());
        list.setSubCategoryId(arrayList.get(position).getSubCategoryId());
        list.setName(arrayList.get(position).getName());
        list.setDescription(arrayList.get(position).getDescription());
        list.setOldPrice(arrayList.get(position).getOldPrice());
        list.setNewPrice(arrayList.get(position).getNewPrice());
        list.setDiscount(arrayList.get(position).getDiscount());
        list.setUnit(arrayList.get(position).getUnit());
        list.setImage(arrayList.get(position).getImage());
        list.setWishlistId(wishlistId);
        list.setCartId(arrayList.get(position).getCartId());
        list.setQty(arrayList.get(position).getQty());
        arrayList.set(position,list);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
