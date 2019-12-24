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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements
        SignInFragment.OnSignInListener,
        ProfileFragment.OnSignOutListener,
        RoomListFragment.OnFragmentInteractionListener,
        CreateRoomFragment.OnFragmentInteractionListener{

    private ProfileFragment mProfileFragment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    private void setEnabledRoomFragments(boolean enabled) {
        setEnabledViewGroup((ViewGroup) findViewById(R.id.room_list_fragment), enabled);
        setEnabledViewGroup((ViewGroup) findViewById(R.id.create_room_fragment), enabled);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            mProfileFragment = ProfileFragment.newInstance(currentUser.getPhotoUrl(),
                    currentUser.getDisplayName());

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                    beginTransaction();
            fragmentTransaction.replace(R.id.profile_frame, mProfileFragment);
            fragmentTransaction.commit();

            findViewById(R.id.sign_in_fragment).setVisibility(View.GONE);
            findViewById(R.id.profile_frame).setVisibility(View.VISIBLE);

            setEnabledRoomFragments(true);
        }
        else {
            setEnabledRoomFragments(false);

            findViewById(R.id.profile_frame).setVisibility(View.GONE);

            if (mProfileFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                        beginTransaction();
                fragmentTransaction.remove(mProfileFragment);
                fragmentTransaction.commit();
            }

            findViewById(R.id.sign_in_fragment).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSignIn(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onSignOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        updateUI(null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
