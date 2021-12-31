package com.project.ucare.screens.main.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ucare.R;
import com.project.ucare.screens.auth.LoginActivity;
import com.project.ucare.screens.auth.SignUpActivity;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    TextView feedBack, deleteAccount, logOut;

    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        listRingtones();
        feedBack = root.findViewById(R.id.feedBack);
        deleteAccount = root.findViewById(R.id.deleteAccount);
        logOut = root.findViewById(R.id.logOut);


        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                composeEmail();
            }
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())

                        .setTitle("Delete Account")
                        .setMessage("Do you want to Delete Account")
                        .setIcon(R.drawable.ic_baseline_delete_24)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code

                                deleteAccount();

                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                myQuittingDialogBox.show();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        if (firebaseAuth.getCurrentUser() == null) {
//                            //Do anything here which needs to be done after signout is complete
//                            Intent intent = new Intent(getContext(), LoginActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();
//                        }
//                    }
//                };
//
////Init and attach
//                firebaseAuth = FirebaseAuth.getInstance();
//                firebaseAuth.addAuthStateListener(authStateListener);
//
////Call signOut()
//                firebaseAuth.signOut();
//
//
                SharedPreferences.Editor editor = getContext().getSharedPreferences("myContact", MODE_PRIVATE).edit();
                editor.putString("name", "1");
                editor.putString("number", "0");
                editor.apply();


                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                mGoogleSignInClient.signOut();


                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();


            }
        });


        return root;
    }

    private void sentEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "ucare.project101@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Ucare");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

        startActivity(Intent.createChooser(intent, "Send Feedback"));
    }

    public void composeEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "ucare.project101@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Ucare");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
//        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
        startActivity(Intent.createChooser(emailIntent, "Send Feedback"));

    }

    public void listRingtones() {
        RingtoneManager manager = new RingtoneManager(getActivity());
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            // Do something with the title and the URI of ringtone

            Log.d("TAG", "listRingtones: " + title + "        " + uri);


        }
    }

    public void pickRingtone(View view) {
        // TODO Auto-generated method.   stub

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");

        // for existing ringtone
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(
                getContext(), RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, uri);

        startActivityForResult(intent, 5);

        Log.d(TAG, "pickRingtone: " + uri);

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void deleteAccount() {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("login", MODE_PRIVATE);
        String mail = prefs.getString("mail", "0");
        String pass = prefs.getString("pass", "0");
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mail, pass);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


//                                            SharedPreferences prefs = getContext().getSharedPreferences("myContact", MODE_PRIVATE);

                                          //"No name defined" is the default value.


                                            Intent intent = new Intent(getContext(), LoginActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                            Log.d(TAG, "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }

}
