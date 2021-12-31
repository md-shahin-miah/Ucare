package com.project.ucare.screens.main.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ucare.R;
import com.project.ucare.common.Utils;
import com.project.ucare.db.ProfileHandler;
import com.project.ucare.screens.main.createprofile.CreateProfileActivity;
import com.project.ucare.models.Profile;
import com.project.ucare.screens.schedule.ScheduleActivity;
import com.xihad.androidutils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements ProfileAdapter.ProfileListener {


    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;

    ProfileAdapter adapter;
    ProgressBar progressBar;
    TextView noData, iconText, labelName, labelDate;

    LinearLayout rootView;

    ConstraintLayout profileRoot;

    ProfileHandler handler;


    private List<Profile> profileList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        AndroidUtils.Companion.init(getActivity());
        handler = new ProfileHandler(getActivity());


        floatingActionButton = root.findViewById(R.id.fab);
        progressBar = root.findViewById(R.id.progressBar);
        recyclerView = root.findViewById(R.id.profileList);
        noData = root.findViewById(R.id.noData);
        iconText = root.findViewById(R.id.iconText);
        labelName = root.findViewById(R.id.labelName);
        labelDate = root.findViewById(R.id.labelDate);
        rootView = root.findViewById(R.id.rootView);
        profileRoot = root.findViewById(R.id.profileRoot);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Set Adapter
        adapter = new ProfileAdapter(getActivity());
        adapter.setProfileListener(HomeFragment.this);
        recyclerView.setAdapter(adapter);


        //   getData();
        //  getProfile();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CreateProfileActivity.class);
                intent.putExtra("profile", "null");
                startActivity(intent);

                Log.d("qqq", "floatingActionButton:  call");

            }
        });

        Log.d("TAG", "onCreateView: vvarfd " + Utils.isDateValid("07/18/21"));


        return root;
    }

    private void getLocalProfile() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        setProfile(handler.getProfileByID(userId));
    }

//    private void getProfile() {
//        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//        FirebaseDatabase.getInstance().getReference().child("User").child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.hasChildren()) {
//                    Profile profile = snapshot.getValue(Profile.class);
//                  //  setProfile(profile);
//                    assert profile != null;
//                    handler.addProfile(profile);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void setProfile(Profile profile) {

        if (profile != null) {
            rootView.setVisibility(View.VISIBLE);
            labelName.setText(profile.getName());
            labelDate.setText("Birth Date: " + profile.getBirth_date() + " || " + "Gender: " + profile.getGender());
            iconText.setText(AndroidUtils.Companion.splitString(profile.getName(), 1));

            profileRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                    intent.putExtra("profile", profile);
                    startActivity(intent);
                    AndroidUtils.sharePrefSettings.setStringValue("pro", "");
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getLocalProfile();
        getLocalData();

    }

    private void getLocalData() {

        profileList = handler.getProfileList(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        progressBar.setVisibility(View.GONE);
        if (profileList.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
        adapter.setList(profileList);

    }


//    private void getData() {
//
//        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        FirebaseDatabase.getInstance().getReference().child("Profile").child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                progressBar.setVisibility(View.GONE);
//
//              //  profileList.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Profile profile = ds.getValue(Profile.class);
//                 //   profileList.add(profile);
//                    assert profile != null;
//                    handler.addProfile(profile);
//                }
//                if (profileList.isEmpty()) {
//                 //   noData.setVisibility(View.VISIBLE);
//                } else {
//                 //   noData.setVisibility(View.GONE);
//                }
//              //  adapter.setList(profileList);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }

    @Override
    public void onProfileClick(Profile profile) {

        AndroidUtils.sharePrefSettings.setStringValue("pro", profile.getId());

        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
        intent.putExtra("profile", profile);
        startActivity(intent);

    }

    @Override
    public void onProfileLongClick(Profile profile, View view) {

        PopupMenu popup = new PopupMenu(getActivity(), view, Gravity.RIGHT);
        popup.inflate(R.menu.menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.edit) {
                    Intent intent = new Intent(getActivity(), CreateProfileActivity.class);
                    intent.putExtra("profile", profile);
                    startActivity(intent);

                    return true;
                } else if (itemId == R.id.delete) {
                    askOptionToDelete(profile);
                    return true;
                }
                return false;
            }
        });
        popup.show();

    }

    private void askOptionToDelete(Profile profile) {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())

                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_24)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        handler.deleteProfile(profile.getId());
                        deleteProfile(profile);
                        getLocalData();

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

    private void deleteProfile(Profile profile) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Profile")
                .child(userId).child(profile.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Deleted Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Deleted Unsuccessful, Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
