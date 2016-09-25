package com.internship.socialfeed.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.internship.socialfeed.Configuration.Configuration;
import com.internship.socialfeed.LogOutActivity;
import com.internship.socialfeed.R;
import com.internship.socialfeed.components.User;
import com.internship.socialfeed.data.LoginProvider;
import com.internship.socialfeed.data.SessionManager;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
        initListView();
    }
    /**
    * Initializes toolbar and set toolbar's title to "Settings" with color=white
    * */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Settings");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    /**
     * Initializes listView
     * addLoader two, notification and help icons to the list
     *
     * */
    private void initListView() {
        final ListView settingsList=(ListView)findViewById(R.id.lvSettings);
        ArrayList<SettingsItem> settingsItems = new ArrayList<>();
        SettingsItem settingsItem1 = new SettingsItem(getResources().getStringArray(R.array.settings)[0],R.drawable.ic_notifications_black_24dp);
        SettingsItem settingsItem2 = new SettingsItem(getResources().getStringArray(R.array.settings)[1],R.drawable.ic_settings_power_black_24dp);
        SettingsItem settingsItem3 = new SettingsItem(getResources().getStringArray(R.array.settings)[2],R.drawable.ic_help_black_24dp);
        settingsItems.add(settingsItem1);
        settingsItems.add(settingsItem2);
        settingsItems.add(settingsItem3);
        ArrayAdapter adapter=new CustomSettingsAdapter(this,settingsItems);
        settingsList.setAdapter(adapter);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are You Sure You Want To Log Out?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        triggerLogOut();

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1) {
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
    }

    private void triggerLogOut() {
        //clear session
        LoginProvider loginProvider = new LoginProvider(getBaseContext(), new LoginProvider.OnLoginProviderListener() {
            @Override
            public void onLoginSuccess(User user) {

            }

            @Override
            public void onLoginFailed(String error) {

            }

            @Override
            public void onLoginCanceled() {
            }

            @Override
            public void onLogOutSuccess() {
                setResult(Configuration.AUTHENTICATION_REQUEST);
                finish();
            }
        });
    loginProvider.logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Configuration.TAG,"SettingsActivity");
        if (resultCode == Configuration.AUTHENTICATION_REQUEST) {
            setResult(Configuration.AUTHENTICATION_REQUEST);
            finish();
        }
    }
}
