package com.project.ucare.screens.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.ucare.R;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import androidx.annotation.NonNull;

import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;

    private  FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        editText = findViewById(R.id.resetemailId);
        button = findViewById(R.id.resetbuttonId);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String em = editText.getText().toString().trim();
                if (em.isEmpty()){
                    editText.setError("Email Required");
                    return;
                }
                else
                {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    firebaseAuth.sendPasswordResetEmail(em).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Please check your Email. if you want to reset your password..", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }
}