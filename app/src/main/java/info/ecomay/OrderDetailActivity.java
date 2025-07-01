package info.ecomay;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    TextView orderNo,name,email,contact,address,payment,status,cancel;
    CardView cancelCard;
    RecyclerView recyclerView;
    ArrayList<CartList> arrayList;

    SharedPreferences sp;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);
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

        orderNo = findViewById(R.id.order_detail_no);
        name = findViewById(R.id.order_detail_name);
        email = findViewById(R.id.order_detail_email);
        contact = findViewById(R.id.order_detail_contact);
        address = findViewById(R.id.order_detail_address);
        payment = findViewById(R.id.order_detail_payment);
        status = findViewById(R.id.order_detail_status);
        cancel = findViewById(R.id.order_detail_cancel);
        cancelCard = findViewById(R.id.order_detail_cancel_card);
        
        String selectQuery = "SELECT * FROM ORDER_TABLE WHERE ORDERID='"+sp.getString(ConstantSp.ORDER_ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                orderNo.setText("Order No : "+cursor.getString(0));
                name.setText(cursor.getString(2));
                email.setText(cursor.getString(3));
                contact.setText(cursor.getString(4));
                address.setText(cursor.getString(5)+","+cursor.getString(7)+","+cursor.getString(6));
                if(cursor.getString(8).equalsIgnoreCase("Cash")) {
                    payment.setText(ConstantSp.PRICE_SYMBOL+cursor.getString(10)+" ("+ cursor.getString(8) +")");
                }
                else{
                    payment.setText(ConstantSp.PRICE_SYMBOL+cursor.getString(10)+" ("+ cursor.getString(8) +" - "+cursor.getString(9)+")");
                }
                setStaus(cursor.getString(11));
            }
        }

        recyclerView = findViewById(R.id.order_detail_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));

        String cartQuery = "SELECT * FROM CART WHERE ORDERID='"+sp.getString(ConstantSp.ORDER_ID,"")+"'";
        Cursor cartCursor = db.rawQuery(cartQuery,null);
        if(cartCursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cartCursor.moveToNext()){
                CartList list = new CartList();
                list.setCartId(Integer.parseInt(cartCursor.getString(0)));
                list.setProductId(Integer.parseInt(cartCursor.getString(3)));
                list.setQty(Integer.parseInt(cartCursor.getString(5)));

                String productQuery = "SELECT * FROM PRODUCT WHERE PRODUCTID='"+cartCursor.getString(3)+"'";
                Cursor productCursor = db.rawQuery(productQuery,null);
                if(productCursor.getCount()>0){
                    while (productCursor.moveToNext()){
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
            OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetailActivity.this,arrayList);
            recyclerView.setAdapter(adapter);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateQuery = "UPDATE ORDER_TABLE SET STATUS='Cancelled' WHERE ORDERID='"+sp.getString(ConstantSp.ORDER_ID,"")+"'";
                db.execSQL(updateQuery);
                setStaus("Cancelled");
            }
        });

    }

    private void setStaus(String sStatus) {
        if(sStatus.equalsIgnoreCase("Pending")) {
            cancelCard.setVisibility(VISIBLE);
            status.setText("In Process");
            status.setTextColor(getResources().getColor(R.color.yellow));
        }
        else if(sStatus.equalsIgnoreCase("Cancelled")) {
            cancelCard.setVisibility(GONE);
            status.setText(sStatus);
            status.setTextColor(getResources().getColor(R.color.red));
        }
        else{
            cancelCard.setVisibility(GONE);
            status.setText(sStatus);
        }
    }
}