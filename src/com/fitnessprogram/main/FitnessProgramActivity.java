package com.fitnessprogram.main;

import com.fitnessprogram.fragments.HelloPageFragment;
import com.fitnessprogram.fragments.LoginUserDialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FitnessProgramActivity extends Activity implements HelloPageActionInterface{

    private View mRootView;

    private final Uri USER_URI = Uri.parse("content://com.fitnessprogram.database.Users/userProf");
    private final String USER_NAME = "name";
    private final String USER_WEIGHT = "weight";

    private final static String DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG";
    private final static String HELLO_PAGE_FRAGMENT = "HELLO_PAGE_FRAGMENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_fragment_holder);
        mRootView = findViewById(R.id.common_fragment_holder);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(HELLO_PAGE_FRAGMENT);
        if(prev != null) ft.remove(prev);
        ft.addToBackStack(null);
        HelloPageFragment helloFragment = new HelloPageFragment(R.layout.hello_page_layout);
        ft.add(R.id.common_fragment_holder, helloFragment, HELLO_PAGE_FRAGMENT);
        ft.commit();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case R.id.create_new_person:
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
            if (prev != null)
                ft.remove(prev);
            ft.addToBackStack(null);
            DialogFragment addUserDialog = new LoginUserDialog(R.layout.add_user_dialog);
            addUserDialog.show(ft, DIALOG_FRAGMENT_TAG);
            break;
        case R.id.remove_person:
            getContentResolver().delete(USER_URI, "_id = 2", null);
            break;
        case R.id.show_all_users:
            ContentValues cv1 = new ContentValues();
            cv1.put(USER_NAME, "name 5");
            cv1.put(USER_WEIGHT, 505);
            Uri uri = ContentUris.withAppendedId(USER_URI, 2);
            int cnt = getContentResolver().update(uri, cv1, null, null);
            break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void addNewUser(String name, int weight) {
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, name);
        cv.put(USER_WEIGHT, weight);
        Uri newUri = getContentResolver().insert(USER_URI, cv);
    }

    public void onCreateNewUser() {
        //Stub for add new user Fragment
    }

    public void onAuthorizeUser() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
        if (prev != null)
            ft.remove(prev);
        ft.addToBackStack(HELLO_PAGE_FRAGMENT);
        DialogFragment addUserDialog = new LoginUserDialog(R.layout.add_user_dialog);
        addUserDialog.show(ft, DIALOG_FRAGMENT_TAG);
    }
}
