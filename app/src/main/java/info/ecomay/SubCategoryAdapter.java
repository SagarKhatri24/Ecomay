package info.ecomay;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {

    Context context;
    ArrayList<SubCategoryList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryList> arrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sub_category,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,edit,delete;
        LinearLayout editLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_sub_category_name);
            imageView = itemView.findViewById(R.id.custom_sub_category_image);

            edit = itemView.findViewById(R.id.custom_sub_category_edit);
            delete = itemView.findViewById(R.id.custom_sub_category_delete);
            editLayout = itemView.findViewById(R.id.custom_sub_category_edit_layout);

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
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.SUBCATEGORYID, String.valueOf(arrayList.get(position).getSubCategoryId())).commit();
                Intent intent = new Intent(context, ProductActivity.class);
                context.startActivity(intent);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.SUBCATEGORYID, String.valueOf(arrayList.get(position).getSubCategoryId())).commit();
                sp.edit().putString(ConstantSp.SUBCATEGORYNAME, arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantSp.SUBCATEGORYIMAGE, arrayList.get(position).getImage()).commit();
                Intent intent = new Intent(context, AddSubCategoryActivity.class);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM SUBCATEGORY WHERE SUBCATEGORYID='"+arrayList.get(position).getSubCategoryId()+"'";
                db.execSQL(deleteQuery);
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
