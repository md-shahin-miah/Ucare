package com.project.ucare.screens.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ucare.R;
import com.project.ucare.db.ProfileHandler;
import com.project.ucare.models.Profile;
import com.xihad.androidutils.AndroidUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity";
    EditText name, email, password, etBirthDate;
    Button signUpButton;

    ProgressBar progressBar;

    TextView textViewLogin;

    FirebaseAuth firebaseAuth;
    String gender = "";
    RadioGroup rgGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        AndroidUtils.Companion.init(SignUpActivity.this);
        AndroidUtils.Companion.setStatusBarColor(R.color.white);

        name = findViewById(R.id.et_su_name);
        email = findViewById(R.id.et_su_email);
        password = findViewById(R.id.et_su_password);
        signUpButton = findViewById(R.id.bt_su_signUp);
        progressBar = findViewById(R.id.Pb_SignUp);
        rgGender = findViewById(R.id.Su_rgGender);
        etBirthDate = findViewById(R.id.Su_et_BirthDate);
        textViewLogin = findViewById(R.id.Su_tv_register);


        firebaseAuth = FirebaseAuth.getInstance();
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etBirthDate.setText(sdf.format(myCalendar.getTime()));


            }

        };

        etBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        textViewLogin.setOnClickListener(v1 -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);

        });


        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.Su_rMale:
                        gender = "Male";
                        break;
                    case R.id.Su_rFemale:
                        gender = "Female";
                        break;

                    case R.id.Su_rOthers:
                        gender = "Others";
                        break;

                }

            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nam = name.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String date = etBirthDate.getText().toString().trim();

                if (nam.isEmpty()) {
                    name.setError("This field can not be blank");
                    return;

                }
                if (date.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Select a Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError("Please Provide valid email");
                    return;

                }

                if (mail.isEmpty()) {
                    email.setError("This field can not be blank");
                    return;

                }
                if (pass.isEmpty()) {
                    password.setError("This field can not be blank");
                    return;

                }
                if (pass.length() < 6) {
                    password.setError("must have more than 6 character");
                    return;

                }
                if (gender.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Select a Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.INVISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Log.d("useId", "onComplete: " + userId);
                            saveData(userId, nam, date, gender);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            signUpButton.setVisibility(View.VISIBLE);
                            Toast.makeText(SignUpActivity.this, task.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, "Failed To register ,try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


    }

    private void saveData(String id, String name, String date, String gender) {

        Profile profile = new Profile(id, "", name, date, gender, System.currentTimeMillis());

        FirebaseDatabase.getInstance().getReference().child("User").child(id).setValue(profile);

        ProfileHandler handler = new ProfileHandler(this);
        long result = handler.addProfile(profile);
        if (result > 0) {
            Toast.makeText(SignUpActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            signUpButton.setVisibility(View.VISIBLE);
            Toast.makeText(SignUpActivity.this, "Data Updated Failed", Toast.LENGTH_SHORT).show();
        }

    }

}