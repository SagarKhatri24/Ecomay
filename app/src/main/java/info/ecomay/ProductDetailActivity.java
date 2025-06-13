package info.ecomay;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name,newPrice,oldPrice,discount,unit,addItem,description;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        imageView = findViewById(R.id.product_detail_image);
        name = findViewById(R.id.product_detail_name);
        newPrice = findViewById(R.id.product_detail_new_price);
        oldPrice = findViewById(R.id.product_detail_old_price);
        discount = findViewById(R.id.product_detail_discount);
        unit = findViewById(R.id.product_detail_unit);
        addItem = findViewById(R.id.product_detail_add_item);
        description = findViewById(R.id.product_detail_description);

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME,""));
        newPrice.setText(ConstantSp.PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_NEWPRICE,""));
        oldPrice.setText(ConstantSp.PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_OLDPRICE,""));
        oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        discount.setText(sp.getString(ConstantSp.PRODUCT_DISCOUNT,"")+"% off");
        unit.setText(sp.getString(ConstantSp.PRODUCT_UNIT,""));

        description.setText(sp.getString(ConstantSp.PRODUCT_DESCRIPTION,""));

        Glide.with(ProductDetailActivity.this).load(sp.getString(ConstantSp.PRODUCT_IMAGE,"")).placeholder(R.mipmap.ic_launcher).into(imageView);

    }
}