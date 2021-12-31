package com.project.ucare.screens.main.createprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ucare.R;
import com.project.ucare.db.ProfileHandler;
import com.project.ucare.models.Profile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CreateProfileActivity extends AppCompatActivity {


    EditText etName, etBirthDate;
    RadioGroup rgGender;
    Button save;
    //   ProgressBar progressBar;

    String gender = "";
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setTitle("Create a profile");


        etName = findViewById(R.id.etName);
        etBirthDate = findViewById(R.id.etBirthDate);
        rgGender = findViewById(R.id.rgGender);
        save = findViewById(R.id.save);
        //  progressBar = findViewById(R.id.progressBar);


        try {
            profile = (Profile) getIntent().getSerializableExtra("profile");
        } catch (Exception e) {
            e.printStackTrace();
        }


        setEditData(profile);


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
                new DatePickerDialog(CreateProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int checkedId = radioGroup.getCheckedRadioButtonId();

                switch (checkedId) {
                    case R.id.rMale:
                        gender = "Male";
                        break;
                    case R.id.rFemale:
                        gender = "Female";
                        break;

                    case R.id.rOthers:
                        gender = "Others";
                        break;

                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String date = etBirthDate.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Name is Required");
                } else if (date.isEmpty()) {
                    Toast.makeText(CreateProfileActivity.this, "Select a Date", Toast.LENGTH_SHORT).show();
                } else if (gender.isEmpty()) {
                    Toast.makeText(CreateProfileActivity.this, "Select a Gender", Toast.LENGTH_SHORT).show();
                } else {
                    save.setVisibility(View.VISIBLE);
                    //   progressBar.setVisibility(View.VISIBLE);
                    saveData(name, date, gender);
                }


            }
        });

    }

    private void setEditData(Profile profile) {

        if (profile != null) {

            etName.setText(profile.getName());
            etBirthDate.setText(profile.getBirth_date());

            switch (profile.getGender()) {
                case "Male":
                    RadioButton radioButton = findViewById(R.id.rMale);
                    radioButton.setChecked(true);
                    gender = "Male";
                    break;
                case "Female":
                    RadioButton radioButton2 = findViewById(R.id.rFemale);
                    radioButton2.setChecked(true);
                    gender = "Female";
                    break;

                case "Others":
                    RadioButton radioButton3 = findViewById(R.id.rOthers);
                    radioButton3.setChecked(true);
                    gender = "Others";
                    break;
            }
        }
    }

    private void saveData(String name, String date, String gender) {

        String id = getId();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Profile profile = new Profile(id, userId, name, date, gender, System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference().child("Profile").child(userId).child(id).setValue(profile);

        ProfileHandler handler = new ProfileHandler(this);
        long result = handler.addProfile(profile);
        if (result > 0) {
            Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private String getId() {
        String id = String.valueOf(System.currentTimeMillis());
        if (profile != null) {
            id = profile.getId();
        }
        return id;

    }


}
