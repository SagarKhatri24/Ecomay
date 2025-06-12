package info.ecomay.ui.home;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        categoryData();

        productData();

        return root;
    }

    private void productData() {

        binding.homeProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        productArrayList = new ArrayList<>();
        for(int i=0;i<productIdArray.length;i++){
            ProductList list = new ProductList();
            list.setProductId(productIdArray[i]);
            list.setSubCategoryId(subCategoryIdArray[i]);
            list.setName(productNameArray[i]);
            list.setOldPrice(productOldPriceArray[i]);
            list.setNewPrice(productNewPriceArray[i]);
            list.setDiscount(productDiscountArray[i]);
            list.setUnit(productUnitArray[i]);
            list.setImage(productImageArray[i]);
            productArrayList.add(list);
        }
        ProductAdapter adapter = new ProductAdapter(getActivity(),productArrayList);
        binding.homeProductRecycler.setAdapter(adapter);
    }

    private void categoryData() {
        //binding.homeCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        //binding.homeCategory.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        binding.homeCategory.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        arrayList = new ArrayList<>();
        for (int i=0;i<nameArray.length;i++){
            CategoryList list = new CategoryList();
            list.setCategoryId(idArray[i]);
            list.setName(nameArray[i]);
            list.setImage(imageArray[i]);
            arrayList.add(list);
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