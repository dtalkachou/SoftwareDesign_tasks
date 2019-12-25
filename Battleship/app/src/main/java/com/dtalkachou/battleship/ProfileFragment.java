package com.dtalkachou.battleship;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ProfileFragment extends Fragment {
    private static String ARG_PROFILE_PICTURE_URI = "mProfilePictureUri";
    private static String ARG_FIRST_NAME = "mFirstName";

    private Uri mProfilePictureUri;
    private String mFirstName;

    private OnSignOutListener mListener;

    public static ProfileFragment newInstance(Uri profilePictureUri, String firstName) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROFILE_PICTURE_URI, profilePictureUri);
        args.putString(ARG_FIRST_NAME, firstName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mProfilePictureUri = getArguments().getParcelable(ARG_PROFILE_PICTURE_URI);
            mFirstName = getArguments().getString(ARG_FIRST_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView profileFirstName = view.findViewById(R.id.profile_first_name);
        ImageView profilePictureImageView = view.findViewById(R.id.profile_picture);
        Button signOutButton = view.findViewById(R.id.sign_out_button);

        profileFirstName.setText(mFirstName);

        Glide.with(this).load(mProfilePictureUri).
                placeholder(R.drawable.com_facebook_profile_picture_blank_square).
                into(profilePictureImageView);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSignOut();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach (@NonNull Context context){
        super.onAttach(context);
        if (context instanceof OnSignOutListener) {
            mListener = (OnSignOutListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignOutListener");
        }
    }

    public interface OnSignOutListener {
        void onSignOut();
    }
}
