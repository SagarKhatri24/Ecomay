package info.ecomay;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddSubCategoryActivity extends AppCompatActivity {

    TextView title;
    EditText name,image;
    Button submit;

    SQLiteDatabase db;
    SharedPreferences sp;

    Spinner spinner;
    ArrayList<String> catNameArray;
    ArrayList<String> catIdArray;

    int iCatId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_sub_category);
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

        spinner = findViewById(R.id.add_sub_category_spinner);

        catIdArray = new ArrayList<>();
        catNameArray = new ArrayList<>();

        String catQuery = "SELECT * FROM CATEGORY";
        Cursor cursor = db.rawQuery(catQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                catIdArray.add(cursor.getString(0));
                catNameArray.add(cursor.getString(1));
            }
        }
        ArrayAdapter catAdapter = new ArrayAdapter(AddSubCategoryActivity.this, android.R.layout.simple_list_item_1,catNameArray);
        spinner.setAdapter(catAdapter);
        int iCatPosition = 0;
        for(int i=0;i<catIdArray.size();i++){
            if(sp.getString(ConstantSp.CATEGORYID,"").equalsIgnoreCase(catIdArray.get(i))){
                iCatPosition = i;
                break;
            }
        }
        spinner.setSelection(iCatPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iCatId = Integer.parseInt(catIdArray.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        name = findViewById(R.id.add_sub_category_name);
        image = findViewById(R.id.add_sub_category_image);
        submit = findViewById(R.id.add_sub_category_button);
        title = findViewById(R.id.add_sub_category_title);

        if(sp.getString(ConstantSp.SUBCATEGORYID,"").equalsIgnoreCase("")){
            name.setText("");
            image.setText("");
            title.setText("Add Sub Category");
        }
        else{
            name.setText(sp.getString(ConstantSp.SUBCATEGORYNAME,""));
            image.setText(sp.getString(ConstantSp.SUBCATEGORYIMAGE,""));
            title.setText("Update Sub Category");
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
                    if(sp.getString(ConstantSp.SUBCATEGORYID,"").equalsIgnoreCase("")){
                        String selectQuery = "SELECT * FROM SUBCATEGORY WHERE NAME='"+name.getText().toString()+"' AND CATEGORYID='"+sp.getString(ConstantSp.CATEGORYID,"")+"'";
                        Cursor cursor = db.rawQuery(selectQuery,null);
                        if(cursor.getCount()>0){
                            Toast.makeText(AddSubCategoryActivity.this, "Sub Category Already Exists", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String insertQuery = "INSERT INTO SUBCATEGORY VALUES(NULL,'"+iCatId+"','"+name.getText().toString()+"','"+image.getText().toString()+"')";
                            db.execSQL(insertQuery);
                            Toast.makeText(AddSubCategoryActivity.this, "Sub Category Added Successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                    else{
                        String updateQuery = "UPDATE SUBCATEGORY SET CATEGORYID='"+iCatId+"',NAME='"+name.getText().toString()+"',IMAGE='"+image.getText().toString()+"' WHERE SUBCATEGORYID='"+sp.getString(ConstantSp.SUBCATEGORYID,"")+"'";
                        db.execSQL(updateQuery);
                        Toast.makeText(AddSubCategoryActivity.this, "Sub Category Updated Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }
        });
    }
}