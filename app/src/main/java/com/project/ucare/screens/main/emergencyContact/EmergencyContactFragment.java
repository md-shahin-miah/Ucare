package com.project.ucare.screens.main.emergencyContact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.ucare.R;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class EmergencyContactFragment extends Fragment {


    ImageView my_contact_iv, my_contact_iv_edit, national_ec_iv, national_hn_iv, national_wc_iv;
    TextView my_contact_name, my_contact_number, add_myContact_tv;
    EditText et_name_my_ec, et_number_my_cn;
    Button saveButton;

    LinearLayout edit_myc_lin, my_contact_lin;

    String name;
    String number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_emergency_contact, container, false);


        my_contact_iv = root.findViewById(R.id.my_contact_iv);
        national_ec_iv = root.findViewById(R.id.national_ec_iv);
        national_hn_iv = root.findViewById(R.id.national_hn_iv);
        national_wc_iv = root.findViewById(R.id.national_wc_iv);

        my_contact_name = root.findViewById(R.id.my_contact_name);
        my_contact_number = root.findViewById(R.id.my_contact_number);

        et_name_my_ec = root.findViewById(R.id.et_name_my_ec);
        et_number_my_cn = root.findViewById(R.id.et_number_my_cn);
        add_myContact_tv = root.findViewById(R.id.add_myContact_tv);

        edit_myc_lin = root.findViewById(R.id.edit_myc_lin);
        my_contact_lin = root.findViewById(R.id.my_contact_lin);


        saveButton = root.findViewById(R.id.save_button);


        my_contact_iv_edit = root.findViewById(R.id.my_contact_iv_edit);


        getSharePref();
        setFetches();

        add_myContact_tv.setOnClickListener(v -> {
            edit_myc_lin.setVisibility(View.VISIBLE);
        });


        saveButton.setOnClickListener(v -> {

            String nameMc = et_name_my_ec.getText().toString();
            String numberMc = et_number_my_cn.getText().toString();

            SharedPreferences.Editor editor = getContext().getSharedPreferences("myContact", MODE_PRIVATE).edit();
            editor.putString("name", nameMc);
            editor.putString("number", numberMc);
            editor.apply();

            getSharePref();


            my_contact_name.setText(name);
            my_contact_number.setText(number);

            edit_myc_lin.setVisibility(View.GONE);
            my_contact_lin.setVisibility(View.VISIBLE);
            add_myContact_tv.setVisibility(View.GONE);


        });


        my_contact_iv_edit.setOnClickListener(v -> {
            edit_myc_lin.setVisibility(View.VISIBLE);
            getSharePref();

            et_name_my_ec.setText(name);
            et_number_my_cn.setText(number);


        });


        my_contact_iv.setOnClickListener(v -> {
            callIntent(number);
        });

        national_ec_iv.setOnClickListener(v -> {
            callIntent("999");
        });
        national_hn_iv.setOnClickListener(v -> {
            callIntent("333");
        });
        national_wc_iv.setOnClickListener(v -> {
            callIntent("109");
        });


        return root;
    }

    private void getSharePref() {
        SharedPreferences prefs = getContext().getSharedPreferences("myContact", MODE_PRIVATE);
        name = prefs.getString("name", "1");//"No name defined" is the default value.
        number = prefs.getString("number", "0"); //0 is the default
    }


    private void callIntent(String numberph) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numberph));
        startActivity(intent);
    }

    @Override
    public void onResume() {

        getSharePref();
        setFetches();
        super.onResume();
    }


    public void setFetches() {
        my_contact_name.setText(name);
        my_contact_number.setText(number);

        Log.d(TAG, "onResume:-------------------------------- ");
        if (name.equals("1") && number.equals("0")) {

            edit_myc_lin.setVisibility(View.GONE);
            my_contact_lin.setVisibility(View.GONE);
            add_myContact_tv.setVisibility(View.VISIBLE);
        } else {
            edit_myc_lin.setVisibility(View.GONE);
            my_contact_lin.setVisibility(View.VISIBLE);
            add_myContact_tv.setVisibility(View.GONE);
        }


    }


}