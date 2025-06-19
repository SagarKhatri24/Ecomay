package info.ecomay;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import info.ecomay.ui.home.ProductList;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView defaultImage;
    SharedPreferences sp;
    SQLiteDatabase db;

    ArrayList<ProductList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

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

        recyclerView = findViewById(R.id.wishlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));

        defaultImage = findViewById(R.id.wishlist_default);

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            recyclerView.setVisibility(VISIBLE);
            defaultImage.setVisibility(GONE);

            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                ProductList list = new ProductList();
                list.setWishlistId(Integer.parseInt(cursor.getString(0)));
                list.setProductId(Integer.parseInt(cursor.getString(2)));

                String selectProductQuery = "SELECT * FROM PRODUCT WHERE PRODUCTID='"+cursor.getString(2)+"'";
                Cursor productCursor = db.rawQuery(selectProductQuery,null);
                if(productCursor.getCount()>0){
                    while (productCursor.moveToNext()){
                        list.setSubCategoryId(Integer.parseInt(productCursor.getString(1)));
                        list.setName(productCursor.getString(2));
                        list.setImage(productCursor.getString(3));
                        list.setDescription(productCursor.getString(4));
                        list.setOldPrice(productCursor.getString(5));
                        list.setNewPrice(productCursor.getString(6));
                        list.setDiscount(productCursor.getString(7));
                        list.setUnit(productCursor.getString(8));
                    }
                }
                else{
                    list.setSubCategoryId(0);
                    list.setName("");
                    list.setImage("");
                    list.setDescription("");
                    list.setOldPrice("");
                    list.setNewPrice("");
                    list.setDiscount("");
                    list.setUnit("");
                }
                arrayList.add(list);
            }
            WishlistAdapter adapter = new WishlistAdapter(WishlistActivity.this,arrayList,db);
            recyclerView.setAdapter(adapter);
        }
        else{
            recyclerView.setVisibility(GONE);
            defaultImage.setVisibility(VISIBLE);
        }

        Glide
                .with(WishlistActivity.this)
                .asGif()
                .load("https://assets-v2.lottiefiles.com/a/0e30b444-117c-11ee-9b0d-0fd3804d46cd/BkQxD7wtnZ.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

    }
}