package com.dtalkachou.battleship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private SignInFragment mSignInFragment;
    private RoomListFragment mRoomListFragment;
    private CreatedRoomDataFragment mCreatedRoomDataFragment;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mSignInFragment = new SignInFragment();
        mRoomListFragment = new RoomListFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.create_room_fragment_frame, new CreateRoomFragment());
        fragmentTransaction.add(R.id.room_list_fragment_frame, mRoomListFragment);
        fragmentTransaction.commit();

        updateUI(mAuth.getCurrentUser());
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

    private void updateUI(FirebaseUser currentUser) {
        setEnabledViewGroup((ViewGroup) findViewById(R.id.room_list_fragment_frame),
                currentUser != null);
        // todo: fix bug with CreateRoomFragmentEnabled
        setEnabledViewGroup((ViewGroup) findViewById(R.id.create_room_fragment_frame),
                currentUser != null);

        mRoomListFragment.setCurrentUserId(mAuth.getUid());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        if (currentUser != null) {
            Uri profilePictureUri = Uri.parse(currentUser.getPhotoUrl()
                    + "?height=125");
            mProfileFragment = ProfileFragment.newInstance(profilePictureUri,
                    currentUser.getDisplayName());

            fragmentTransaction.remove(mSignInFragment);
            fragmentTransaction.add(R.id.profile_frame, mProfileFragment);

            findViewById(R.id.profile_frame).setVisibility(View.VISIBLE);
        }
        else {
            if (mProfileFragment != null) {
                fragmentTransaction.remove(mProfileFragment);
            }
                fragmentTransaction.add(R.id.parent_frame, mSignInFragment);

                findViewById(R.id.profile_frame).setVisibility(View.GONE);
        }
        fragmentTransaction.commit();
    }

    private void updateCreateRoomUI() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.create_room_fragment_frame) instanceof
                CreatedRoomDataFragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.create_room_fragment_frame, new CreateRoomFragment());
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
                        updateUI(mAuth.getCurrentUser());
                    }
                });
    }

    @Override
    public void onSignOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        updateCreateRoomUI();
        updateUI(null);
    }

    @Override
    public void onCreateRoom(boolean protect) {
        final DatabaseReference roomRef = mDatabase.
                getReference(ROOMS_DATABASE_REF).push();

        final String roomId = roomRef.getKey();
        NumberFormat nf = new DecimalFormat("0000");
        final String password = protect ?
                nf.format(new Random().nextInt(10000)) : null;
        Room room = new Room(roomId, mAuth.getUid(), password);

        roomRef.setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mCreatedRoomDataFragment = CreatedRoomDataFragment.newInstance(roomId, password);

                roomRef.child(Room.OPPONENT_ID).addValueEventListener(mCreatedRoomDataFragment);

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
                        updateCreateRoomUI();
                    }
                });
    }
}
