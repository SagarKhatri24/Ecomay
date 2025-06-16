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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int[] subCategoryIdArray = {1,2,3,4,5,6,7};
    int[] categoryIdArray = {1,1,1,8,8,6,6};
    String[] nameArray = {"Staples","Snacks & Beverages","Packaged Food","Personal & Baby Care","Household Care","Dairy & Eggs","Home & Kitchen"};
    String[] imageArray = {
            "https://rukminim2.flixcart.com/flap/128/128/image/50474c.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/9fbd36.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/ac8550.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/7670e2.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/b7ade9.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/8014b1.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/e6e0ecc56771471a.png?q=100"
    };

    ArrayList<SubCategoryList> arrayList;
    SharedPreferences sp;

    ImageView defaultImage;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub_category);
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

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        defaultImage = findViewById(R.id.sub_category_image);

        recyclerView = findViewById(R.id.sub_category_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        arrayList = new ArrayList<>();
        /*for(int i=0;i<nameArray.length;i++){
            if(sp.getString(ConstantSp.CATEGORYID,"").equals(String.valueOf(categoryIdArray[i]))) {
                SubCategoryList list = new SubCategoryList();
                list.setCategoryId(categoryIdArray[i]);
                list.setSubCategoryId(subCategoryIdArray[i]);
                list.setName(nameArray[i]);
                list.setImage(imageArray[i]);
                arrayList.add(list);
            }
        }*/
        String selectQuery = "SELECT * FROM SUBCATEGORY WHERE CATEGORYID='"+sp.getString(ConstantSp.CATEGORYID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                SubCategoryList list = new SubCategoryList();
                list.setCategoryId(Integer.parseInt(cursor.getString(1)));
                list.setSubCategoryId(Integer.parseInt(cursor.getString(0)));
                list.setName(cursor.getString(2));
                list.setImage(cursor.getString(3));
                arrayList.add(list);
            }
        }
        SubCategoryAdapter adapter = new SubCategoryAdapter(SubCategoryActivity.this,arrayList);
        recyclerView.setAdapter(adapter);

        if(arrayList.size()>0){
            defaultImage.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }
        else{
            defaultImage.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        }

        Glide
                .with(SubCategoryActivity.this)
                .asGif()
                .load("https://assets-v2.lottiefiles.com/a/0e30b444-117c-11ee-9b0d-0fd3804d46cd/BkQxD7wtnZ.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

    }
}