package info.ecomay.ui.home;

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

import info.ecomay.AddCategoryActivity;
import info.ecomay.ConstantSp;
import info.ecomay.R;
import info.ecomay.SubCategoryActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    Context context;
    ArrayList<CategoryList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public CategoryAdapter(Context context, ArrayList<CategoryList> arrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,edit,delete;
        LinearLayout editLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.custom_category_name);
            imageView = itemView.findViewById(R.id.custom_category_image);

            edit = itemView.findViewById(R.id.custom_category_edit);
            delete = itemView.findViewById(R.id.custom_category_delete);
            editLayout = itemView.findViewById(R.id.custom_category_edit_layout);

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
                sp.edit().putString(ConstantSp.CATEGORYID, String.valueOf(arrayList.get(position).getCategoryId())).commit();
                Intent intent = new Intent(context, SubCategoryActivity.class);
                context.startActivity(intent);
            }
        });

        //data:image/jpeg;base64,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

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.CATEGORYID, String.valueOf(arrayList.get(position).getCategoryId())).commit();
                sp.edit().putString(ConstantSp.CATEGORYNAME,arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantSp.CATEGORYIMAGE,arrayList.get(position).getImage()).commit();

                Intent intent = new Intent(context, AddCategoryActivity.class);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM CATEGORY WHERE CATEGORYID='"+arrayList.get(position).getCategoryId()+"'";
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
