package info.ecomay.ui.profile;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import info.ecomay.ConstantSp;
import info.ecomay.DashboardActivity;
import info.ecomay.MainActivity;
import info.ecomay.R;
import info.ecomay.SignupActivity;
import info.ecomay.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    EditText name, email, contact, password, confirmPassword;
    RadioGroup gender;
    RadioButton male,female;
    Spinner country;
    Button submit,editProfile,logout,delete;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String[] countryArray = {"Select Country", "India", "USA", "UK", "Canada"};

    ImageView passwordHide, passwordShow, confirmPasswordHide, confirmPasswordShow;

    SQLiteDatabase db;

    String sGender, sCountry;

    SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sp = getActivity().getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = getActivity().openOrCreateDatabase("Ecomay.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(100), EMAIL VARCHAR(100), CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        name = binding.profileName;
        email = binding.profileEmail;
        contact = binding.profileContact;
        password = binding.profilePassword;
        confirmPassword = binding.profileConfirmnPassword;
        gender = binding.profileGender;
        country = binding.profileCountry;
        submit = binding.profileSubmit;

        passwordHide = binding.profileHide;
        passwordShow = binding.profileShow;

        male = binding.profileMale;
        female = binding.profileFemale;

        editProfile = binding.profileEdit;
        logout = binding.profileLogout;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout!");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are You Sure Want to Logout!");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //sp.edit().remove(ConstantSp.USERID).commit();
                        sp.edit().clear().commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Rate Us", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        delete = binding.profileDelete;

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Account!");
                builder.setMessage("Are You Sure Want to Delete Your Account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String deleteQuery = "DELETE FROM USERS WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
                        db.execSQL(deleteQuery);
                        Toast.makeText(getActivity(), "Profile Deleted Successfully", Toast.LENGTH_SHORT).show();

                        sp.edit().clear().commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

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

        confirmPasswordHide = binding.profileConfirmHide;
        confirmPasswordShow = binding.profileConfirmShow;

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

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, countryArray);
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
                RadioButton rb = root.findViewById(i);
                sGender = rb.getText().toString();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
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
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().trim().length() < 6) {
                    password.setError("Min. 6 Char Password Required");
                } else if (confirmPassword.getText().toString().trim().equals("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (confirmPassword.getText().toString().trim().length() < 6) {
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                } else if (!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())) {
                    confirmPassword.setError("Password Does Not Match");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (country.getSelectedItemPosition() <= 0) {
                    Toast.makeText(getActivity(), "Please Select Country", Toast.LENGTH_SHORT).show();
                } else {
                    String updateQuery = "UPDATE USERS SET NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',PASSWORD='"+password.getText().toString()+"',GENDER='"+sGender+"',COUNTRY='"+sCountry+"' WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
                    db.execSQL(updateQuery);

                    Toast.makeText(getActivity(), "Profile Update Successfully", Toast.LENGTH_SHORT).show();

                    sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                    sp.edit().putString(ConstantSp.COUNTRY,sCountry).commit();

                    setData(false);
                    /*String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' OR CONTACT='"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        Toast.makeText(getActivity(), "Email Id/Contact No. Already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String insertQuery = "INSERT INTO USERS VALUES (NULL,'" + name.getText().toString() + "','" + email.getText().toString() + "','" + contact.getText().toString() + "','" + password.getText().toString() + "','" + sGender + "','" + sCountry + "')";
                        db.execSQL(insertQuery);

                        Toast.makeText(getActivity(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }*/
                }
            }
        });

        setData(false);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        return root;
    }

    private void setData(boolean b) {
        name.setEnabled(b);
        email.setEnabled(b);
        contact.setEnabled(b);
        password.setEnabled(b);
        confirmPassword.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);
        country.setEnabled(b);

        if(b){
            passwordHide.setVisibility(VISIBLE);
            passwordShow.setVisibility(VISIBLE);
            confirmPassword.setVisibility(VISIBLE);
            confirmPasswordHide.setVisibility(VISIBLE);
            confirmPasswordShow.setVisibility(VISIBLE);

            editProfile.setVisibility(GONE);
            submit.setVisibility(VISIBLE);
        }
        else{
            passwordHide.setVisibility(GONE);
            passwordShow.setVisibility(GONE);

            confirmPassword.setVisibility(GONE);
            confirmPasswordHide.setVisibility(GONE);
            confirmPasswordShow.setVisibility(GONE);

            editProfile.setVisibility(VISIBLE);
            submit.setVisibility(GONE);
        }

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        password.setText(sp.getString(ConstantSp.PASSWORD,""));
        confirmPassword.setText(sp.getString(ConstantSp.PASSWORD,""));

        if(sp.getString(ConstantSp.GENDER,"").equalsIgnoreCase("Male")){
            male.setChecked(true);
        }
        else if(sp.getString(ConstantSp.GENDER,"").equalsIgnoreCase("Female")){
            female.setChecked(true);
        }
        else{

        }

        int iCountryIndex = 0;
        for(int i=0;i<countryArray.length;i++){
            if(sp.getString(ConstantSp.COUNTRY,"").equals(countryArray[i])){
                iCountryIndex = i;
                break;
            }
        }
        country.setSelection(iCountryIndex);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}