package com.dtalkachou.battleship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.dtalkachou.models.Room;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements
        SignInFragment.OnSignInListener,
        ProfileFragment.OnSignOutListener,
        CreateRoomFragment.OnCreateRoomListener,
        CreatedRoomDataFragment.OnRemoveRoomListener {
    private static String ROOMS_DATABASE_REF = "rooms";

    private ProfileFragment mProfileFragment;
    private CreatedRoomDataFragment mCreatedRoomDataFragment;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.add(R.id.create_room_fragment_frame,
                new CreateRoomFragment());
        fragmentTransaction.add(R.id.room_list_fragment_frame,
                new RoomListFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateProfileUI();
    }

    private static void setEnabledViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                setEnabledViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    private void updateProfileUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        int signInFragmentVisibility = currentUser != null
                ? View.GONE : View.VISIBLE;
        int profileFrameVisibility = currentUser != null
                ? View.VISIBLE : View.GONE;
        boolean roomFragmentsEnabled = currentUser != null;

        findViewById(R.id.sign_in_fragment).
                setVisibility(signInFragmentVisibility);
        findViewById(R.id.profile_frame).
                setVisibility(profileFrameVisibility);

        setEnabledViewGroup((ViewGroup) findViewById(R.id.room_list_fragment_frame),
                roomFragmentsEnabled);
        setEnabledViewGroup((ViewGroup) findViewById(R.id.create_room_fragment_frame),
                roomFragmentsEnabled);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        if (currentUser != null) {
            Uri profilePictureUri = Uri.parse(currentUser.getPhotoUrl()
                    + "?height=125");
            mProfileFragment = ProfileFragment.newInstance(profilePictureUri,
                    currentUser.getDisplayName());
            fragmentTransaction.replace(R.id.profile_frame, mProfileFragment);
        }
        else if (mProfileFragment != null) {
            fragmentTransaction.remove(mProfileFragment);
        }
        fragmentTransaction.commit();
    }

    private void removeCreatedRoomDataUI() {
        if (mCreatedRoomDataFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                    beginTransaction();
            fragmentTransaction.replace(R.id.create_room_fragment_frame,
                    new CreateRoomFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof RoomListFragment) {
            mDatabase.getReference(ROOMS_DATABASE_REF).
                    addChildEventListener((RoomListFragment) fragment);
        }
    }

    @Override
    public void onSignIn(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.
                getCredential(token.getToken());
        mAuth.signInWithCredential(credential).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateProfileUI();
                    }
                });
    }

    @Override
    public void onSignOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        updateProfileUI();
        removeCreatedRoomDataUI();
    }

    @Override
    public void onCreateRoom(boolean protect) {
        DatabaseReference roomRef = mDatabase.
                getReference(ROOMS_DATABASE_REF).push();

        final String roomId = roomRef.getKey();
        NumberFormat nf = new DecimalFormat("0000");
        final String password = protect ?
                nf.format(new Random().nextInt(10000)) : null;
        Room room = new Room(roomId, mAuth.getCurrentUser().getUid(), password);

        roomRef.setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mCreatedRoomDataFragment = CreatedRoomDataFragment.
                        newInstance(roomId, password);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                        beginTransaction();
                fragmentTransaction.replace(R.id.create_room_fragment_frame,
                        mCreatedRoomDataFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onRemoveRoom(String roomId) {
        mDatabase.getReference(ROOMS_DATABASE_REF).child(roomId).removeValue().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        removeCreatedRoomDataUI();
                    }
                });
    }
}
