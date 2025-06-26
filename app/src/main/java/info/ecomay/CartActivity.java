package info.ecomay;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import info.ecomay.ui.home.ProductAdapter;
import info.ecomay.ui.home.ProductList;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CartList> productArrayList;

    ImageView defaultImage;

    SearchView searchView;

    SharedPreferences sp;

    SQLiteDatabase db;
    CartAdapter adapter;

    public static TextView cartTotal;
    TextView checkout;
    RelativeLayout dataLayout;

    public static int iTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = openOrCreateDatabase("Ecomay.db",MODE_PRIVATE,null);
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

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART (CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID VARCHAR(10), USERID VARCHAR(10),PRODUCTID VARCHAR(10),PRICE VARCHAR(20), QTY VARCHAR(10), TOTAL VARCHAR(20))";
        db.execSQL(cartTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        defaultImage = findViewById(R.id.cart_image);

        searchView = findViewById(R.id.cart_search);

        recyclerView = findViewById(R.id.cart_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        recyclerView.setNestedScrollingEnabled(false);

        productArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM CART WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND ORDERID='0'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                CartList list = new CartList();
                list.setCartId(Integer.parseInt(cursor.getString(0)));
                list.setQty(Integer.parseInt(cursor.getString(5)));

                String selectProductQuery = "SELECT * FROM PRODUCT WHERE PRODUCTID='"+cursor.getString(3)+"'";
                Cursor productCursor = db.rawQuery(selectProductQuery,null);
                if(productCursor.getCount()>0){
                    while (productCursor.moveToNext()){
                        list.setProductId(Integer.parseInt(productCursor.getString(0)));
                        list.setSubCategoryId(Integer.parseInt(productCursor.getString(1)));
                        list.setName(productCursor.getString(2));
                        list.setDescription(productCursor.getString(4));
                        list.setOldPrice(productCursor.getString(5));
                        list.setNewPrice(productCursor.getString(6));
                        list.setDiscount(productCursor.getString(7));
                        list.setUnit(productCursor.getString(8));
                        list.setImage(productCursor.getString(3));

                        iTotal += Integer.parseInt(cursor.getString(5))* Integer.parseInt(productCursor.getString(6));
                    }
                }
                else{
                    list.setProductId(0);
                    list.setSubCategoryId(0);
                    list.setName("");
                    list.setDescription("");
                    list.setOldPrice("");
                    list.setNewPrice("");
                    list.setDiscount("");
                    list.setUnit("");
                    list.setImage("");
                }
                productArrayList.add(list);
            }
        }
        adapter = new CartAdapter(CartActivity.this, productArrayList, db);
        recyclerView.setAdapter(adapter);

        cartTotal = findViewById(R.id.cart_total);
        checkout = findViewById(R.id.cart_checkout);
        dataLayout = findViewById(R.id.cart_data_layout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.ORDER_TYPE,"").commit();
                sp.edit().putString(ConstantSp.CART_TOTAL, String.valueOf(iTotal)).commit();
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });

        cartTotal.setText(ConstantSp.PRICE_SYMBOL+iTotal);

        if(productArrayList.size()>0){
            defaultImage.setVisibility(GONE);
            dataLayout.setVisibility(VISIBLE);
        }
        else{
            defaultImage.setVisibility(VISIBLE);
            dataLayout.setVisibility(GONE);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                productFilter(s);
                return false;
            }
        });

        Glide
                .with(CartActivity.this)
                .asGif()
                .load("https://assets-v2.lottiefiles.com/a/0e30b444-117c-11ee-9b0d-0fd3804d46cd/BkQxD7wtnZ.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

    }

    private void productFilter(String s) {
        ArrayList<CartList> tempList = new ArrayList<>();
        for(CartList list : productArrayList){
            if(list.getName().toLowerCase().contains(s.toLowerCase())){
                tempList.add(list);
            }
        }
        adapter.updateList(tempList);
    }
}