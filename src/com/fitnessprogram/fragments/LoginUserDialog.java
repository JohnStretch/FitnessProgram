package com.fitnessprogram.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fitnessprogram.main.R;

public class LoginUserDialog extends DialogFragment {

    private static final String DIALOG_LAYOUT = "LAYOUT";

    public LoginUserDialog(int layoutId) {
        Bundle args = new Bundle();
        args.putInt(DIALOG_LAYOUT, layoutId);
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getArguments().getInt(DIALOG_LAYOUT), container, false);
        final EditText name = (EditText) root.findViewById(R.id.user_name_field);
        final EditText password = (EditText) root.findViewById(R.id.user_password_field);
        Button positive = (Button) root.findViewById(R.id.possitive_button);
        Button negative = (Button) root.findViewById(R.id.negative_button);
        positive.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
//                ((FitnessProgramActivity) getActivity()).addNewUser(name.getText().toString(), 200);
                // name.setText("");
                // password.setText("");
                dismiss();
            }
        });
        negative.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // name.setText("");
                // password.setText("");
                dismiss();
            }
        });
        return root;
    }

}
