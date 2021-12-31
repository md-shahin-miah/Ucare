package com.project.ucare.screens.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ucare.R;
import com.project.ucare.db.ProfileHandler;
import com.project.ucare.models.Profile;
import com.project.ucare.screens.main.MainActivity;
import com.xihad.androidutils.AndroidUtils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView resisterText, forget_password;
    EditText email, password;
    Button loginButton, googleButton;
    ProgressBar Pb_login, Pb_G_login;
    // jhgj

    FirebaseAuth firebaseAuth;

    GoogleSignInClient mGoogleSignInClient;

    ProfileHandler profileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        AndroidUtils.Companion.init(LoginActivity.this);
        AndroidUtils.Companion.setStatusBarColor(R.color.white);
        profileHandler = new ProfileHandler(this);


        resisterText = findViewById(R.id.tv_register);
        forget_password = findViewById(R.id.forget_password);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        loginButton = findViewById(R.id.bt_login);
        googleButton = findViewById(R.id.bt_google_login);

        Pb_login = findViewById(R.id.Pb_login);
        Pb_G_login = findViewById(R.id.pb_g_login);

        firebaseAuth = FirebaseAuth.getInstance();

        resisterText.setOnClickListener(v1 -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);

        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pb_G_login.setVisibility(View.VISIBLE);
                googleButton.setVisibility(View.GONE);
                sinIn();


            }
        });

        forget_password.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);

        });


        loginButton.setOnClickListener(v -> {

            AndroidUtils.Companion.hideSoftKeyBoard();

            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

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

            Pb_login.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);

            logIn(mail, pass);


        });


    }

    private static int RC_SIGN_IN = 101;

    private void sinIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                    firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Pb_G_login.setVisibility(View.GONE);
                            googleButton.setVisibility(View.VISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //  updateUI(user);

                            saveData(user.getUid(), user.getDisplayName(), "", "");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Pb_G_login.setVisibility(View.GONE);
                            googleButton.setVisibility(View.VISIBLE);
                            // If sign in fails, display a message to the user.
                            ///  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }
                });
    }


    private void logIn(String mail, String pass) {
        firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SharedPreferences.Editor prefs = LoginActivity.this.getSharedPreferences("login", MODE_PRIVATE).edit();
                    prefs.putString("mail", mail);
                    prefs.putString("pass", pass);
                    prefs.apply();

                    getProfile();
                    Pb_login.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Pb_login.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, task.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void getProfile() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (profile != null)
                        profileHandler.addProfile(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void saveData(String id, String name, String date, String gender) {

        Profile profile = new Profile(id, "", name, date, gender, System.currentTimeMillis());

        FirebaseDatabase.getInstance().getReference().child("User").child(id).setValue(profile);

        ProfileHandler handler = new ProfileHandler(this);
        long result = handler.addProfile(profile);


    }

}