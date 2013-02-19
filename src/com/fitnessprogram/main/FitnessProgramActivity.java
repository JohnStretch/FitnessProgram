package com.fitnessprogram.main;

import com.fitnessprogram.dialog.UserDialog;

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
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FitnessProgramActivity extends Activity {

   private ViewGroup mRootView;
   private ListView mUserList;

   private final Uri USER_URI = Uri.parse("content://com.fitnessprogram.database.Users/userProf");
   private final String USER_NAME = "name";
   private final String USER_WEIGHT = "weight";

   private final static String DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG";

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_program);

        Cursor cursor = getContentResolver().query(USER_URI, null, null, null, null);
        startManagingCursor(cursor);

        String [] from = {USER_NAME, USER_WEIGHT};
        int [] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, cursor, from, to);
        

        mRootView = (ViewGroup) findViewById(R.id.content_layout);
        mUserList = (ListView) findViewById(R.id.user_list);
        ProgressBar progressBar = new ProgressBar(getApplicationContext());
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(false);
        mUserList.setEmptyView(progressBar);
        mUserList.setAdapter(sca);
        mRootView.addView(progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fitness_program, menu);
        return true;
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.create_new_person:
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
			if(prev != null) ft.remove(prev);
			ft.addToBackStack(null);
			DialogFragment addUserDialog = new UserDialog(R.layout.add_user_dialog);
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

	public void addNewUser(String name, int weight) {
		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, name);
		cv.put(USER_WEIGHT, weight);
		Uri newUri = getContentResolver().insert(USER_URI, cv);
	}
}
