package info.ecomay;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddCategoryActivity extends AppCompatActivity {

    TextView title;
    EditText name,image;
    Button submit;

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = openOrCreateDatabase("Ecomay.db", MODE_PRIVATE, null);
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

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS ORDER_TABLE (ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,USERID VARCHAR(10),NAME VARCHAR(50),EMAIL VARCHAR(50),CONTACT VARCHAR(10),ADDRESS TEXT,PINCODE VARCHAR(6),COUNTRY VARCHAR(20),PAYMENTMODE VARCHAR(20),TRANSACTIONID VARCHAR(50),TOTAL VARCHAR(20))";
        db.execSQL(orderTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        name = findViewById(R.id.add_category_name);
        image = findViewById(R.id.add_category_image);
        submit = findViewById(R.id.add_category_button);
        title = findViewById(R.id.add_category_title);

        if(sp.getString(ConstantSp.CATEGORYID,"").equalsIgnoreCase("")){
            name.setText("");
            image.setText("");
            title.setText("Add Category");
        }
        else{
            name.setText(sp.getString(ConstantSp.CATEGORYNAME,""));
            image.setText(sp.getString(ConstantSp.CATEGORYIMAGE,""));
            title.setText("Update Category");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equalsIgnoreCase("")){
                    name.setError("Name Required");
                }
                else if(image.getText().toString().trim().equalsIgnoreCase("")){
                    image.setError("Image Required");
                }
                else{
                    if(sp.getString(ConstantSp.CATEGORYID,"").equalsIgnoreCase("")){
                        String selectQuery = "SELECT * FROM CATEGORY WHERE NAME='"+name.getText().toString()+"'";
                        Cursor cursor = db.rawQuery(selectQuery,null);
                        if(cursor.getCount()>0){
                            Toast.makeText(AddCategoryActivity.this, "Category Already Exists", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String insertQuery = "INSERT INTO CATEGORY VALUES(NULL,'"+name.getText().toString()+"','"+image.getText().toString()+"')";
                            db.execSQL(insertQuery);
                            Toast.makeText(AddCategoryActivity.this, "Category Added Successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                    else{
                        String updateQuery = "UPDATE CATEGORY SET NAME='"+name.getText().toString()+"',IMAGE='"+image.getText().toString()+"' WHERE CATEGORYID='"+sp.getString(ConstantSp.CATEGORYID,"")+"'";
                        db.execSQL(updateQuery);
                        Toast.makeText(AddCategoryActivity.this, "Category Updated Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }
        });

    }
}