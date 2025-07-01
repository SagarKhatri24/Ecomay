package info.ecomay.ui.orders;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import info.ecomay.ConstantSp;
import info.ecomay.ProductActivity;
import info.ecomay.R;
import info.ecomay.databinding.FragmentOrderBinding;
import info.ecomay.ui.home.ProductAdapter;
import info.ecomay.ui.home.ProductList;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;

    ArrayList<OrderList> arrayList;

    SharedPreferences sp;

    SQLiteDatabase db;
    OrderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = getActivity().openOrCreateDatabase("Ecomay.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(100), EMAIL VARCHAR(100), CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY (CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categoryTableQuery);

        String subCategoryTableQuery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY (SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subCategoryTableQuery);

        String productTableQuery = "CREATE TABLE IF NOT EXISTS PRODUCT (PRODUCTID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,OLDPRICE VARCHAR(10),NEWPRICE VARCHAR(10),DISCOUNT VARCHAR(10),UNIT VARCHAR(10))";
        db.execSQL(productTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST (WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID VARCHAR(10),PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTableQuery);

        sp = getActivity().getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        binding.orderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.orderRecycler.setNestedScrollingEnabled(false);

        arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM ORDER_TABLE WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' ORDER BY ORDERID DESC";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                OrderList list = new OrderList();
                list.setOrderId(cursor.getString(0));
                list.setName(cursor.getString(2));
                list.setEmail(cursor.getString(3));
                list.setContact(cursor.getString(4));
                list.setAddress(cursor.getString(5));
                list.setPincode(cursor.getString(6));
                list.setCountry(cursor.getString(7));
                list.setPaymentMode(cursor.getString(8));
                list.setTransactionId(cursor.getString(9));
                list.setTotal(cursor.getString(10));
                list.setStatus(cursor.getString(11));
                arrayList.add(list);
            }
        }
        adapter = new OrderAdapter(getActivity(), arrayList,db);
        binding.orderRecycler.setAdapter(adapter);

        if(arrayList.size()>0){
            binding.productImage.setVisibility(GONE);
            binding.orderRecycler.setVisibility(VISIBLE);
        }
        else{
            binding.productImage.setVisibility(VISIBLE);
            binding.orderRecycler.setVisibility(GONE);
        }

        Glide
                .with(getActivity())
                .asGif()
                .load("https://assets-v2.lottiefiles.com/a/0e30b444-117c-11ee-9b0d-0fd3804d46cd/BkQxD7wtnZ.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.productImage);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}