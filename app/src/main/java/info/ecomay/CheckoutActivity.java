package info.ecomay;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    EditText name, email, contact, address, pincode;
    RadioGroup payVia;
    Spinner country;
    Button payNow;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String[] countryArray = {"Select Country", "India", "USA", "UK", "Canada"};

    SQLiteDatabase db;

    String sPaymentType, sCountry;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
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

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS ORDER_TABLE (ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,USERID VARCHAR(10),NAME VARCHAR(50),EMAIL VARCHAR(50),CONTACT VARCHAR(10),ADDRESS TEXT,PINCODE VARCHAR(6),COUNTRY VARCHAR(20),PAYMENTMODE VARCHAR(20),TRANSACTIONID VARCHAR(50),TOTAL VARCHAR(20),STATUS VARCHAR(20))";
        db.execSQL(orderTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        name = findViewById(R.id.checkout_name);
        email = findViewById(R.id.checkout_email);
        contact = findViewById(R.id.checkout_contact);
        address = findViewById(R.id.checkout_address);
        pincode = findViewById(R.id.checkout_pincode);
        payVia = findViewById(R.id.checkout_payment_type);
        country = findViewById(R.id.checkout_country);
        payNow = findViewById(R.id.checkout_pay_now);

        name.setText(sp.getString(ConstantSp.NAME, ""));
        email.setText(sp.getString(ConstantSp.EMAIL, ""));
        contact.setText(sp.getString(ConstantSp.CONTACT, ""));

        ArrayAdapter adapter = new ArrayAdapter(CheckoutActivity.this, android.R.layout.simple_list_item_1, countryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        country.setAdapter(adapter);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCountry = countryArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        payVia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                sPaymentType = rb.getText().toString();
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().trim().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (address.getText().toString().trim().equals("")) {
                    address.setError("Address Required");
                } else if (pincode.getText().toString().trim().equals("")) {
                    pincode.setError("Pincode Required");
                } else if (pincode.getText().toString().trim().length() < 6) {
                    pincode.setError("Min. 6 Char Pincode Required");
                } else if (country.getSelectedItemPosition() <= 0) {
                    Toast.makeText(CheckoutActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
                } else if (payVia.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CheckoutActivity.this, "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else {
                    if (sPaymentType.equalsIgnoreCase("Cash")) {
                        if (sp.getString(ConstantSp.ORDER_TYPE, "").equalsIgnoreCase("BuyNow")) {
                            int iQty = 1;
                            int iTotal = Integer.parseInt(sp.getString(ConstantSp.PRODUCT_NEWPRICE, "")) * iQty;
                            String insertQuery = "INSERT INTO CART VALUES(NULL,'0','" + sp.getString(ConstantSp.USERID, "") + "','" + sp.getString(ConstantSp.PRODUCTID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NEWPRICE, "") + "','" + iQty + "','" + iTotal + "','Pending')";
                            db.execSQL(insertQuery);
                            doOrder("");
                        } else {
                            doOrder("");
                        }
                    } else if (sPaymentType.equalsIgnoreCase("Online")) {
                        startPayment();
                    }
                }
            }
        });

    }

    private void startPayment() {
        Activity activity = this;
        Checkout co = new Checkout();
        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", getResources().getString(R.string.app_name));
            jsonObject.put("description", "Multi Product Order");
            jsonObject.put("send_sms_hash", true);
            jsonObject.put("allow_rotation", true);
            jsonObject.put("image", R.mipmap.ic_launcher);
            jsonObject.put("currancy", "INR");
            jsonObject.put("amount", String.valueOf(Integer.parseInt(sp.getString(ConstantSp.CART_TOTAL, "")) * 100));

            JSONObject prefill = new JSONObject();
            prefill.put("email", sp.getString(ConstantSp.EMAIL, ""));
            prefill.put("contact", sp.getString(ConstantSp.CONTACT, ""));

            jsonObject.put("prefill", prefill);
            co.open(activity, jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d("RESPONSE_SUCCESS", s);
        //Toast.makeText(CheckoutActivity.this, "Payment Successfully", Toast.LENGTH_SHORT).show();
        if (sp.getString(ConstantSp.ORDER_TYPE, "").equalsIgnoreCase("BuyNow")) {
            int iQty = 1;
            int iTotal = Integer.parseInt(sp.getString(ConstantSp.PRODUCT_NEWPRICE, "")) * iQty;
            String insertQuery = "INSERT INTO CART VALUES(NULL,'0','" + sp.getString(ConstantSp.USERID, "") + "','" + sp.getString(ConstantSp.PRODUCTID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NEWPRICE, "") + "','" + iQty + "','" + iTotal + "','Pending')";
            db.execSQL(insertQuery);
            doOrder(s);
        } else {
            doOrder(s);
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d("RESPONSE_FAIL", s);
        Toast.makeText(CheckoutActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }

    private void doOrder(String sTransactionId) {
        String insertQuery = "INSERT INTO ORDER_TABLE VALUES (NULL,'" + sp.getString(ConstantSp.USERID, "") + "','" + name.getText().toString() + "','" + email.getText().toString() + "','" + contact.getText().toString() + "','" + address.getText().toString() + "','" + pincode.getText().toString() + "','" + sCountry + "','" + sPaymentType + "','" + sTransactionId + "','" + sp.getString(ConstantSp.CART_TOTAL, "") + "')";
        db.execSQL(insertQuery);

        String selectQuery = "SELECT MAX(ORDERID) FROM ORDER_TABLE LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String sOrderId = cursor.getString(0);
                String cartQuery = "UPDATE CART SET ORDERID='" + sOrderId + "' WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND ORDERID='0'";
                db.execSQL(cartQuery);
            }
        }

        Toast.makeText(CheckoutActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}