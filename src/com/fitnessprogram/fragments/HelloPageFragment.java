package com.fitnessprogram.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.fitnessprogram.main.HelloPageActionInterface;
import com.fitnessprogram.main.R;

public class HelloPageFragment extends Fragment {

    private static final String HELLO_PAGE_LAYOUT = "HELLO_PAGE_LAYOUT";

    private HelloPageActionInterface mListener;

    public HelloPageFragment(int layoutId) {
        Bundle args = new Bundle();
        args.putInt(HELLO_PAGE_LAYOUT, layoutId);
        setArguments(args);
    }

    @Override
    public void onAttach(Activity activity) {
        if(activity instanceof HelloPageActionInterface)
            mListener = (HelloPageActionInterface) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getArguments().getInt(HELLO_PAGE_LAYOUT), container, false);
        Button createUserButton = (Button) root.findViewById(R.id.create_new_user);
        Button loginUserButton = (Button) root.findViewById(R.id.login_user);
        createUserButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                mListener.onCreateNewUser();
            }
        });
        loginUserButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                mListener.onAuthorizeUser();
            }
        });
        return root;
    }

}
