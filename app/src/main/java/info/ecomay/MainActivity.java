package info.ecomay;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button login,createAccount;
    EditText email,password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView passwordHide,passwordShow;

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("Ecomay.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(100), EMAIL VARCHAR(100), CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        login = findViewById(R.id.main_login);
        createAccount = findViewById(R.id.main_create_account);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        passwordHide = findViewById(R.id.main_hide);
        passwordShow = findViewById(R.id.main_show);

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else {
                    /*String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' AND PASSWORD='"+password.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login Unsuccessfully", Toast.LENGTH_LONG).show();
                        Snackbar.make(view, "Login Unsuccessfully", Snackbar.LENGTH_SHORT).show();
                    }*/

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        String selectLoginQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' AND PASSWORD='"+password.getText().toString()+"'";
                        Cursor cursorSelect = db.rawQuery(selectLoginQuery,null);
                        if(cursorSelect.getCount()>0) {
                            while (cursorSelect.moveToNext()){
                                String sId = cursorSelect.getString(0);
                                String sName = cursorSelect.getString(1);
                                String sEmail = cursorSelect.getString(2);
                                String sContact = cursorSelect.getString(3);
                                String sPassword = cursorSelect.getString(4);
                                String sGender = cursorSelect.getString(5);
                                String sCountry = cursorSelect.getString(6);

                                sp.edit().putString(ConstantSp.USERID,sId).commit();
                                sp.edit().putString(ConstantSp.NAME,sName).commit();
                                sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                                sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                                sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                                sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                                sp.edit().putString(ConstantSp.COUNTRY,sCountry).commit();

                                //Log.d("RESPONSE",sId+"\n"+sName+"\n"+sEmail+"\n"+sContact+"\n"+sPassword+"\n"+sGender+"\n"+sCountry);
                            }

                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                            Snackbar.make(view, "Invalid Password", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Email Id Not Registered", Toast.LENGTH_LONG).show();
                        Snackbar.make(view, "Email Id Not Registered", Snackbar.LENGTH_SHORT).show();
                    }


                    /*if(email.getText().toString().trim().equalsIgnoreCase("admin@gmail.com") && password.getText().toString().trim().equalsIgnoreCase("Admin@123")) {
                        System.out.println("Login Successfully");
                        Log.d("RESPONSE", "Login Successfully");
                        Log.e("RESPONSE", "Login Successfully");
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login Unsuccessfully", Toast.LENGTH_LONG).show();
                        Snackbar.make(view, "Login Unsuccessfully", Snackbar.LENGTH_SHORT).show();
                    }*/
                }
            }
        });

    }
}