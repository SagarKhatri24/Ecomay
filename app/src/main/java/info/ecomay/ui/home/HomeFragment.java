package info.ecomay.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import info.ecomay.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    int[] idArray = {1,2,3,4,5,6,7,8,9};
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

    ArrayList<CategoryList> arrayList;

    int[] productIdArray = {1,2,3,4,5,6};
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
            "Fortune soya health is certified as India's No. 1 cooking oil brand. It is fortified with Vitamin A & Vitamin D which helps bosting immunity. Fortune Soya Health Oil contains PUFA, which helps in reducing cholesterol levels. Your everyday meals prepared with Fortune soya health oil not only tastes better, but also make your bones stronger and heart healthier. India's No.1 Oil - Healthy Refined oil for your Family. Fortified with the goodness of Vitamin A and Vitamin D. Rich in Omega 3-helps keep Heart healthy. Contains PUFA-which helps in reducing cholesterol levels. Helps maintain Strong Bones. Adds extra Flavour to your favourite food.",
            "Flipkart endeavours to ensure that the sellers provide accurate information on the platform. It is pertinent to note that, actual product packaging and materials may contain more and different information, which may include nutritional information/allergen declaration/special instruction for intended use/warning/directions, health & nutritional claims, etc. We recommend that consumers always read the label carefully before using or consuming any products. Please do not solely rely on the information provided on this website. For additional information, please get in touch with the manufacturer.",
            "Launched on 27th May 2002, Aashirvaad Shudh Chakki Atta has now become No. 1 in branded packaged atta across the country. Aashirvaad prides itself in making 100% pure whole wheat atta with all its natural dietary fibres intact. To ensure high-quality atta reaches your home, Aashirvaad Shudh Chakki Atta is made using a 4-step advantage process: sourcing, cleaning, grinding, and nutrition lockage. With over 6,500 sourcing centres, Aashirvaad handpicks only the finest grains to guarantee premium quality. Each batch of wheat undergoes more than 40 rigorous tests to ensure that only the best grains are selected. The grains then undergo a thorough 3-step cleaning process before being ground using the traditional 'chakki-grinding' method. This ensures the atta is 100% Sampoorna Atta and 0% Maida. Aashirvaad ensures locking-in the highest nutritional value in the whole wheat flour. It is high in fibre and a source of protein which are essential for your family's daily intake.",
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

    ArrayList<ProductList> productArrayList;
    SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = getActivity().openOrCreateDatabase("Ecomay.db", Context.MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(100), EMAIL VARCHAR(100), CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY (CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categoryTableQuery);

        String subCategoryTableQuery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY (SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subCategoryTableQuery);

        String productTableQuery = "CREATE TABLE IF NOT EXISTS PRODUCT (PRODUCTID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,OLDPRICE VARCHAR(10),NEWPRICE VARCHAR(10),DISCOUNT VARCHAR(10),UNIT VARCHAR(10))";
        db.execSQL(productTableQuery);

        categoryData();

        productData();

        return root;
    }

    private void productData() {

        binding.homeProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.homeProductRecycler.setNestedScrollingEnabled(false);

        productArrayList = new ArrayList<>();
        /*for(int i=0;i<productIdArray.length;i++){
            ProductList list = new ProductList();
            list.setProductId(productIdArray[i]);
            list.setSubCategoryId(subCategoryIdArray[i]);
            list.setName(productNameArray[i]);
            list.setDescription(productDescriptionArray[i]);
            list.setOldPrice(productOldPriceArray[i]);
            list.setNewPrice(productNewPriceArray[i]);
            list.setDiscount(productDiscountArray[i]);
            list.setUnit(productUnitArray[i]);
            list.setImage(productImageArray[i]);
            productArrayList.add(list);
        }*/
        String selectQuery = "SELECT * FROM PRODUCT";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                ProductList list = new ProductList();
                list.setProductId(Integer.parseInt(cursor.getString(0)));
                list.setSubCategoryId(Integer.parseInt(cursor.getString(1)));
                list.setName(cursor.getString(2));
                list.setDescription(cursor.getString(4));
                list.setOldPrice(cursor.getString(5));
                list.setNewPrice(cursor.getString(6));
                list.setDiscount(cursor.getString(7));
                list.setUnit(cursor.getString(8));
                list.setImage(cursor.getString(3));
                productArrayList.add(list);
            }
        }
        ProductAdapter adapter = new ProductAdapter(getActivity(),productArrayList);
        binding.homeProductRecycler.setAdapter(adapter);
    }

    private void categoryData() {
        //binding.homeCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        //binding.homeCategory.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        binding.homeCategory.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        arrayList = new ArrayList<>();
        /*for (int i=0;i<nameArray.length;i++){
            CategoryList list = new CategoryList();
            list.setCategoryId(idArray[i]);
            list.setName(nameArray[i]);
            list.setImage(imageArray[i]);
            arrayList.add(list);
        }*/
        String selectQuery = "SELECT * FROM CATEGORY";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                CategoryList list = new CategoryList();
                list.setCategoryId(Integer.parseInt(cursor.getString(0)));
                list.setName(cursor.getString(1));
                list.setImage(cursor.getString(2));
                arrayList.add(list);
            }
        }
        CategoryAdapter adapter = new CategoryAdapter(getActivity(),arrayList);
        binding.homeCategory.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}