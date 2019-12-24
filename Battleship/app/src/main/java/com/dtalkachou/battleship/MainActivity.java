package com.dtalkachou.battleship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity implements
        SignInFragment.OnSignInListener,
        ProfileFragment.OnSignOutListener,
        RoomListFragment.OnFragmentInteractionListener,
        CreateRoomFragment.OnFragmentInteractionListener{

    private ProfileFragment mProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AccessToken.getCurrentAccessToken() != null) {
            try {
                onSignIn();
            }
            catch (NullPointerException e) {
                LoginManager.getInstance().logOut();
                setEnabledRoomFragments(false);
            }
        }
        else {
            setEnabledRoomFragments(false);
        }
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

    @Override
    public void onSignIn() {
        Profile profile = Profile.getCurrentProfile();
        mProfileFragment = ProfileFragment.newInstance(profile.getProfilePictureUri(125, 125),
                profile.getFirstName());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.replace(R.id.profile_frame, mProfileFragment);
        fragmentTransaction.commit();

        findViewById(R.id.sign_in_fragment).setVisibility(View.GONE);
        findViewById(R.id.profile_frame).setVisibility(View.VISIBLE);

        setEnabledRoomFragments(true);
    }

    @Override
    public void onSignOut() {
        setEnabledRoomFragments(false);

        findViewById(R.id.profile_frame).setVisibility(View.GONE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.remove(mProfileFragment);
        fragmentTransaction.commit();

        findViewById(R.id.sign_in_fragment).setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
