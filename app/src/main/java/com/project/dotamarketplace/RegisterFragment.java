package com.project.dotamarketplace;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dotamarketplace.Activity.HomeActivity;
import com.project.dotamarketplace.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    TextView fullName, userName, password, confirmPassword, phone, result;
    RadioGroup rbGender;
    RadioButton rbMale, rbFemale;
    Button register;
    CheckBox agreement;
    DataHelper dbHelper;


    @Override
    public void onStop() {
        super.onStop();
        reset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        reset();

        return view;
    }

    private void reset() {
        fullName.setText("");
        userName.setText("");
        password.setText("");
        confirmPassword.setText("");
        phone.setText("");
        rbMale.setChecked(false);
        rbFemale.setChecked(false);
        agreement.setChecked(false);
    }

    public void init(View v) {
        dbHelper = new DataHelper(this.getActivity());
        fullName = v.findViewById(R.id.editTextTextFullnName);
        userName = v.findViewById(R.id.editTextTextUserName);
        password = v.findViewById(R.id.editTextTextPassword);
        confirmPassword = v.findViewById(R.id.editTextTextConfirmPassword);
        phone = v.findViewById(R.id.editTextTextPhone);

        rbGender = v.findViewById(R.id.rbgrupGender);
        rbFemale = v.findViewById(R.id.rbFemale);
        rbMale = v.findViewById(R.id.rbMale);
        agreement = v.findViewById(R.id.agreementCheck);

        register = v.findViewById(R.id.btnRegister);

        result = v.findViewById(R.id.show);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gender = "";
                String fullNameStr = fullName.getText().toString();
                String userNameStr = userName.getText().toString();
                String passwordStr = password.getText().toString();
                String ConfStr = confirmPassword.getText().toString();
                String phoneStr = phone.getText().toString();

                if (rbFemale.isChecked()) gender = "Female";
                else if (rbMale.isChecked()) gender = "Male";
                if (validate(fullNameStr, userNameStr, passwordStr, ConfStr, phoneStr, gender)) {
                    int userid = dbHelper.getLastID() + 1;

                    User user = new User(userid, fullNameStr, userNameStr, passwordStr, phoneStr, gender,0);
                    dbHelper.insertUser(user);
                    Toast.makeText(getActivity(), "Register Succes, Continue to Login", Toast.LENGTH_LONG).show();

                    reset();
                }

            }

        });


    }

    private boolean validate(String fullNameStr, String userNameStr, String passwordStr, String confStr, String phoneStr, String gender) {
        String phoneSubstring = "";
        if (!phoneStr.isEmpty()){
            phoneSubstring = phoneStr.substring(1,phoneStr.length());
        }
        if (fullNameStr.isEmpty() || userNameStr.isEmpty() || passwordStr.isEmpty() || confStr.isEmpty() || phoneStr.isEmpty()){
            result.setText("Fill All");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if (!fullNameStr.contains(" ")){
            result.setText("name must 2 words");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if ((fullNameStr.length() < 5) || fullNameStr.length() > 25){
            result.setText("name must between 5 - 25");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if (!validatePassword(passwordStr)){
            return false;
        }else if (!confStr.equals(passwordStr)){
            result.setText("Confirmation password must be same with passwords");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if (!phoneStr.startsWith("+62")){
            result.setText("Phone must start with \"+62\"");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if (phoneStr.length()<12){
            result.setText("Phone cant more than 12");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if (!phoneSubstring.matches("^[0-9]+$")){
            result.setText("Phone must be numeric");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if (gender.isEmpty()){
            result.setText("Gender must be selected");
            result.setVisibility(View.VISIBLE);
            return false;
        }else if(!agreement.isChecked()){
            result.setText("Agreement must be selected");
            result.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }

    private boolean validatePassword(String passwordStr) {
        boolean results = false;
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;

        for (Character c: passwordStr.toCharArray()){
            if (Character.isUpperCase(c)) upper = true;
            else if(Character.isLowerCase(c)) lower = true;
            else if (Character.isDigit(c)) digit = true;
        }

        if (!upper){
            result.setText("Password must contain at least 1 uppercase");
            result.setVisibility(View.VISIBLE);
        }else if (!lower){
            result.setText("Password must contain at least 1 lowercase");
            result.setVisibility(View.VISIBLE);
        }else if (!digit){
            result.setText("Password must contain at least 1 digit");
            result.setVisibility(View.VISIBLE);
        }else if (passwordStr.length()>15){
            result.setText("Password must less than 15");
            result.setVisibility(View.VISIBLE);
        }else {
            results = true;
        }

        return results;
    }
}