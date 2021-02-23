package com.project.dotamarketplace;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.dotamarketplace.Activity.HomeActivity;
import com.project.dotamarketplace.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    TextView userNametxt, passwordtxt, showResult;
    Button btnLogin, btnToRegister;
    DataHelper dbHelper;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        init(view);
        return view;
    }

    private void init(View v) {
        dbHelper = new DataHelper(this.getActivity());
        userNametxt = v.findViewById(R.id.editTextUserName);
        passwordtxt = v.findViewById(R.id.editTextPasswordLogin);
        btnLogin = v.findViewById(R.id.btnLogin);
        btnToRegister = v.findViewById(R.id.btnToRegister);

        showResult = v.findViewById(R.id.showResult);
        user = new User();

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment = new RegisterFragment();
//                getChildFragmentManager().beginTransaction()
//                        .replace(R.id.fr, fragment)
//                        .addToBackStack(null)
//                        .commit();

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameStr = userNametxt.getText().toString();
                String passwordStr = passwordtxt.getText().toString();
                user = dbHelper.login(userNameStr, passwordStr);

                if(userNameStr.isEmpty()){
                    showResult.setText("Username must be filled");
                    showResult.setVisibility(View.VISIBLE);
                }else if(passwordStr.isEmpty()){
                    showResult.setText("Password must be filled");
                    showResult.setVisibility(View.VISIBLE);
                }else if(user == null){
                    showResult.setText("Username Password not match");
                    showResult.setVisibility(View.VISIBLE);
                }else{
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("user", user);
                    Log.d("ngesend", user.getName());
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }
}