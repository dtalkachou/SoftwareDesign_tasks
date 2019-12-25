package com.dtalkachou.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements
        SignInFragment.OnSignInListener,
        ProfileFragment.OnSignOutListener,
        CreateRoomFragment.OnCreateRoomListener,
        RoomListFragment.OnFragmentInteractionListener,
        CreatedRoomDataFragment.OnRemoveRoomListener {

    private ProfileFragment mProfileFragment;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.create_room_fragment_frame, new CreateRoomFragment());
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
        int mSignInFragmentVisibility = currentUser != null ? View.GONE : View.VISIBLE;
        int mProfileFrameVisibility = currentUser != null ? View.VISIBLE : View.GONE;
        boolean mRoomFragmentsEnabled = currentUser != null;

        findViewById(R.id.sign_in_fragment).setVisibility(mSignInFragmentVisibility);
        findViewById(R.id.profile_frame).setVisibility(mProfileFrameVisibility);

        setEnabledViewGroup((ViewGroup) findViewById(R.id.room_list_fragment),
                mRoomFragmentsEnabled);
        setEnabledViewGroup((ViewGroup) findViewById(R.id.create_room_fragment_frame),
                mRoomFragmentsEnabled);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentUser != null) {
            Uri profilePictureUri = Uri.parse(currentUser.getPhotoUrl() + "?height=125");
            mProfileFragment = ProfileFragment.newInstance(profilePictureUri,
                    currentUser.getDisplayName());
            fragmentTransaction.replace(R.id.profile_frame, mProfileFragment);
        }
        else if (mProfileFragment != null) {
            fragmentTransaction.remove(mProfileFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSignIn(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
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

        // fixme: if currentUser have created room, then remove it and update UI
        updateProfileUI();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onCreateRoom(boolean protect) {
        final String room_id = Integer.toString(1001);
        final String password = protect ? Integer.toString(new Random().nextInt(10000)) : null;

        Map<String, Object> docData = new HashMap<>();
        docData.put("room_id", room_id);
        docData.put("owner_id", mAuth.getCurrentUser().getUid());
        docData.put("password", password);

        mDatabase.getReference("rooms").child(room_id).setValue(docData).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                                beginTransaction();
                        fragmentTransaction.replace(R.id.create_room_fragment_frame,
                                CreatedRoomDataFragment.newInstance(room_id, password));
                        fragmentTransaction.commit();
                    }
                });
    }

    @Override
    public void onRemoveRoom(String roomId) {
        mDatabase.getReference("rooms").child(roomId).removeValue().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                                beginTransaction();
                        fragmentTransaction.replace(R.id.create_room_fragment_frame,
                                new CreateRoomFragment());
                        fragmentTransaction.commit();
                    }
                });
    }
}
