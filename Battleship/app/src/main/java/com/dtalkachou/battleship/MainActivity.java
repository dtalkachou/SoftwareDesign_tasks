package com.dtalkachou.battleship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity implements
        SignInFragment.OnSignInListener,
        ProfileFragment.OnSignOutListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = AccessToken.getCurrentAccessToken() == null
                ? new SignInFragment() : getProfileFragmentInstance();
        initProfileFrame(fragment);
    }

    private void initProfileFrame(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.replace(R.id.profile_frame, fragment);
        fragmentTransaction.commit();
    }

    private ProfileFragment getProfileFragmentInstance() {
        Profile profile = Profile.getCurrentProfile();
        return ProfileFragment.newInstance(profile.getProfilePictureUri(250, 250),
                profile.getFirstName());
    }

    @Override
    public void onSignIn(int resultCode) {
        if (resultCode == RESULT_OK) {
            initProfileFrame(getProfileFragmentInstance());
        }
    }

    @Override
    public void onSignOut() {
        initProfileFrame(new SignInFragment());
    }
}
