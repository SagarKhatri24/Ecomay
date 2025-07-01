package info.ecomay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    SharedPreferences sp;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
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

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS ORDER_TABLE (ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,USERID VARCHAR(10),NAME VARCHAR(50),EMAIL VARCHAR(50),CONTACT VARCHAR(10),ADDRESS TEXT,PINCODE VARCHAR(6),COUNTRY VARCHAR(20),PAYMENTMODE VARCHAR(20),TRANSACTIONID VARCHAR(50),TOTAL VARCHAR(20),STATUS VARCHAR(20))";
        db.execSQL(orderTableQuery);

        /*String statusQuery = "ALTER TABLE ORDER_TABLE ADD COLUMN STATUS VARCHAR(20)";
        db.execSQL(statusQuery);*/

        /*String statusUpdateQuery = "UPDATE ORDER_TABLE SET STATUS='Pending'";
        db.execSQL(statusUpdateQuery);*/

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        imageView = findViewById(R.id.splash_image);
        Glide
                .with(SplashActivity.this)
                .asGif()
                .load("https://i.pinimg.com/originals/a7/95/25/a79525c01bbf0f2f84c60fa19d4b942c.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        doCategory();
        doSubCategory();
        doProduct();

        doSplash();

    }

    private void doProduct() {
        int[] subCategoryIdArray = {1,1,2,3,3,2};

        String[] productImageArray = {
                "https://rukminim2.flixcart.com/image/280/280/kfyasnk0/pulses/7/m/n/1-toor-dal-toor-dal-flipkart-supermart-classic-original-imafwaxgjm9rymzz.jpeg?q=70",
                "https://rukminim2.flixcart.com/image/280/280/xif0q/edible-oil/k/p/c/-original-imahadwr8ketn3du.jpeg?q=70",
                "https://rukminim2.flixcart.com/image/280/280/kkec4280/ghee/1/x/b/1-ghee-12x1-ltr-pet-jar-mason-jar-amul-original-imafzqv6gggbhygv.jpeg?q=70",
                "https://rukminim2.flixcart.com/image/280/280/xif0q/flour/j/n/v/-original-imagm7w8jfn29hp2.jpeg?q=70",
                "https://rukminim2.flixcart.com/image/280/280/xif0q/spice-masala/t/h/j/100-ajwain-by-flipkart-grocery-pouch-1-classic-whole-original-imagthp5qgmzjfqf.jpeg?q=70",
                "https://rukminim2.flixcart.com/image/280/280/xif0q/rice/4/o/n/-original-imah9258fw4s7xuq.jpeg?q=70"
        };

        String[] productNameArray = {
                "Classic Toor/Arhar Dal (Tuver Dal) (Split) by Flipkart Grocery",
                "FORTUNE Soya health refined Soyabean Oil Pouch (Soyabean nu Tel)",
                "Amul Pure Ghee Ghee Plastic Bottle",
                "AASHIRVAAD Shudh Chakki Atta (Akha Ghauno Lot)",
                "Classic Ajwain by Flipkart Grocery",
                "Tata Sampann High in Fibre (Pouva) (Thick) Poha"
        };

        String[] productDescriptionArray = {
                "Flipkart endeavours to ensure that the sellers provide accurate information on the platform. It is pertinent to note that, actual product packaging and materials may contain more and different information, which may include nutritional information/allergen declaration/special instruction for intended use/warning/directions, health & nutritional claims, etc. We recommend that consumers always read the label carefully before using or consuming any products. Please do not solely rely on the information provided on this website. For additional information, please get in touch with the manufacturer.",
                "Fortune soya health is certified as Indias No. 1 cooking oil brand. It is fortified with Vitamin A & Vitamin D which helps bosting immunity. Fortune Soya Health Oil contains PUFA, which helps in reducing cholesterol levels. Your everyday meals prepared with Fortune soya health oil not only tastes better, but also make your bones stronger and heart healthier. Indias No.1 Oil - Healthy Refined oil for your Family. Fortified with the goodness of Vitamin A and Vitamin D. Rich in Omega 3-helps keep Heart healthy. Contains PUFA-which helps in reducing cholesterol levels. Helps maintain Strong Bones. Adds extra Flavour to your favourite food.",
                "Flipkart endeavours to ensure that the sellers provide accurate information on the platform. It is pertinent to note that, actual product packaging and materials may contain more and different information, which may include nutritional information/allergen declaration/special instruction for intended use/warning/directions, health & nutritional claims, etc. We recommend that consumers always read the label carefully before using or consuming any products. Please do not solely rely on the information provided on this website. For additional information, please get in touch with the manufacturer.",
                "Launched on 27th May 2002, Aashirvaad Shudh Chakki Atta has now become No. 1 in branded packaged atta across the country. Aashirvaad prides itself in making 100% pure whole wheat atta with all its natural dietary fibres intact. To ensure high-quality atta reaches your home, Aashirvaad Shudh Chakki Atta is made using a 4-step advantage process: sourcing, cleaning, grinding, and nutrition lockage. With over 6,500 sourcing centres, Aashirvaad handpicks only the finest grains to guarantee premium quality. Each batch of wheat undergoes more than 40 rigorous tests to ensure that only the best grains are selected. The grains then undergo a thorough 3-step cleaning process before being ground using the traditional chakki-grinding method. This ensures the atta is 100% Sampoorna Atta and 0% Maida. Aashirvaad ensures locking-in the highest nutritional value in the whole wheat flour. It is high in fibre and a source of protein which are essential for your familys daily intake.",
                "Flipkart endeavours to ensure that the sellers provide accurate information on the platform. It is pertinent to note that, actual product packaging and materials may contain more and different information, which may include nutritional information/allergen declaration/special instruction for intended use/warning/directions, health & nutritional claims, etc. We recommend that consumers always read the label carefully before using or consuming any products. Please do not solely rely on the information provided on this website. For additional information, please get in touch with the manufacturer.",
                "Poha, also called Flattened/ Beaten rice, is a much loved and eaten snack in India. With a dash of peanuts and curry leaves, it makes for a breakfast enjoyed by everyone. Tata Sampann Thick Poha can be used to make a healthy snack as it uses minimal oil while roasting and is high in fibre which is an essential part of a healthy and balanced diet. The thick poha packed with its wholesome goodness intact is also a natural source of protein which is an important building block for the body. Tata Sampann Thick Poha can be used to prepare great-tasting and nutritious breakfast for a delightful day. Poha can be enjoyed in breakfast, with evening tea, for a light dinner or to satisfy midnight cravings. Just open a pack and make your favourite recipe."
        };

        String[] productNewPriceArray = {
                "128",
                "154",
                "598",
                "463",
                "23",
                "88"
        };

        String[] productOldPriceArray = {
                "290",
                "175",
                "669",
                "542",
                "60",
                "120"
        };

        String[] productDiscountArray = {
                "55",
                "12",
                "10",
                "14",
                "61",
                "26"
        };

        String[] productUnitArray = {
                "1 kg",
                "870 g",
                "1 L Bottle",
                "10 kg",
                "100 g",
                "1 kg"
        };

        for(int i=0;i<productNameArray.length;i++){
            String selectQuery = "SELECT * FROM PRODUCT WHERE SUBCATEGORYID='"+subCategoryIdArray[i]+"' AND NAME='"+productNameArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()>0){
            }
            else{
                String insertQuery = "INSERT INTO PRODUCT VALUES (NULL,'"+subCategoryIdArray[i]+"','"+productNameArray[i]+"','"+productImageArray[i]+"','"+productDescriptionArray[i]+"','"+productOldPriceArray[i]+"','"+productNewPriceArray[i]+"','"+productDiscountArray[i]+"','"+productUnitArray[i]+"')";
                db.execSQL(insertQuery);
            }
        }

    }

    private void doSubCategory() {
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

        for(int i=0;i<nameArray.length;i++){
            String selectQuery = "SELECT * FROM SUBCATEGORY WHERE NAME='"+nameArray[i]+"' AND CATEGORYID='"+categoryIdArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()>0){

            }
            else{
                String insertQuery = "INSERT INTO SUBCATEGORY VALUES (NULL,'"+categoryIdArray[i]+"','"+nameArray[i]+"','"+imageArray[i]+"')";
                db.execSQL(insertQuery);
            }
        }

    }

    private void doCategory() {
        String[] nameArray = {"Kilos","Mobiles","Fashion","Electronics","Home & Furniture","Appliances","Flight Bookings","Beauty, Toys & More","Two Wheelers"};
        String[] imageArray = {
                "https://rukminim2.flixcart.com/flap/80/80/image/29327f40e9c4d26b.png?q=100",
                "https://rukminim2.flixcart.com/flap/80/80/image/22fddf3c7da4c4f4.png?q=100",
                "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/0d75b34f7d8fbcb3.png?q=100",
                "https://rukminim2.flixcart.com/flap/80/80/image/69c6589653afdb9a.png?q=100",
                "https://rukminim2.flixcart.com/flap/80/80/image/ab7e2b022a4587dd.jpg?q=100",
                "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/0139228b2f7eb413.jpg?q=100",
                "https://rukminim2.flixcart.com/flap/80/80/image/71050627a56b4693.png?q=100",
                "https://rukminim2.flixcart.com/flap/80/80/image/dff3f7adcf3a90c6.png?q=100",
                "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/05d708653beff580.png?q=100"
        };

        for (int i=0; i<nameArray.length;i++) {
            String selectQuery = "SELECT * FROM CATEGORY WHERE NAME='"+nameArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()>0){

            }
            else {
                String insertQuery = "INSERT INTO CATEGORY VALUES(NULL,'" + nameArray[i] + "','" + imageArray[i] + "')";
                db.execSQL(insertQuery);
            }
        }
    }

    private void doSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString(ConstantSp.USERID, "").equals("")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}