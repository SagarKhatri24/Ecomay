package info.ecomay;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

public class SignupActivity extends AppCompatActivity {

    EditText name,email,contact,password,confirmPassword;
    RadioGroup gender;
    Spinner country;
    CheckBox terms;
    Button signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String[] countryArray = { "Select Country", "India", "USA", "UK", "Canada" };

    ImageView passwordHide,passwordShow,confirmPasswordHide,confirmPasswordShow;

    SQLiteDatabase db;

    String sGender,sCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = openOrCreateDatabase("Ecomay.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(100), EMAIL VARCHAR(100), CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirmn_password);
        gender = findViewById(R.id.signup_gender);
        country = findViewById(R.id.signup_country);
        terms = findViewById(R.id.signup_terms);
        signup = findViewById(R.id.signup_button);

        passwordHide = findViewById(R.id.signup_hide);
        passwordShow = findViewById(R.id.signup_show);

        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setTransformationMethod(null);
                passwordShow.setVisibility(GONE);
                passwordHide.setVisibility(VISIBLE);
            }
        });

        passwordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setTransformationMethod(new PasswordTransformationMethod());
                passwordShow.setVisibility(VISIBLE);
                passwordHide.setVisibility(GONE);
            }
        });

        confirmPasswordHide = findViewById(R.id.signup_confirm_hide);
        confirmPasswordShow = findViewById(R.id.signup_confirm_show);

        confirmPasswordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassword.setTransformationMethod(null);
                confirmPasswordShow.setVisibility(GONE);
                confirmPasswordHide.setVisibility(VISIBLE);
            }
        });

        confirmPasswordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                confirmPasswordShow.setVisibility(VISIBLE);
                confirmPasswordHide.setVisibility(GONE);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1,countryArray);
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

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                sGender = rb.getText().toString();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equals("")){
                    name.setError("Name Required");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().trim().equals("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().trim().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else if(confirmPassword.getText().toString().trim().equals("")){
                    confirmPassword.setError("Confirm Password Required");
                }
                else if(confirmPassword.getText().toString().trim().length()<6){
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                }
                else if(!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SignupActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(country.getSelectedItemPosition()<=0){
                    Toast.makeText(SignupActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
                }
                else if(!terms.isChecked()){
                    Toast.makeText(SignupActivity.this, "Please Accept Terms & Condition", Toast.LENGTH_SHORT).show();
                }
                else{
                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' OR CONTACT='"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        Toast.makeText(SignupActivity.this, "Email Id/Contact No. Already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String insertQuery = "INSERT INTO USERS VALUES (NULL,'" + name.getText().toString() + "','" + email.getText().toString() + "','" + contact.getText().toString() + "','" + password.getText().toString() + "','" + sGender + "','" + sCountry + "')";
                        db.execSQL(insertQuery);

                        Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }
        });

    }
}