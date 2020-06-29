package com.example.mininotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mininotes.DataBase.ProfileDataBase;
import com.example.mininotes.Model.ProfileModel;

public class Settings extends AppCompatActivity {
    Boolean init_state = true;
    private Toolbar toolbar;
    private ImageView profile_edit;
    private TextView name, email;
    private Switch aSwitch;
    SharedPref sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref=new SharedPref(getApplicationContext());
        if (sharedPref.loadState()==true)
        {
            setTheme(R.style.AppTheme_Dark);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");


        //INIT
        profile_edit = findViewById(R.id.profile_edit);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        aSwitch = findViewById(R.id.tswitch);
        if (sharedPref.loadState()==true)
        {
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                   // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPref.setState(true);
                    restartApp();
                }
                else {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPref.setState(false);
                    restartApp();
                }
            }

            private void restartApp() {
                Intent intent=new Intent(getApplicationContext(),Settings.class);;
                startActivity(intent);
                finish();
            }
        });

        InsertData();
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(Settings.this);
                final View view = LayoutInflater.from(Settings.this).inflate(R.layout.profile_edit_layout, null, false);
                adb.setView(view);
                adb.setTitle(Html.fromHtml("<font color ='#0C9184'>Edit Profile </font> "));
                adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText1, editText2;
                        ProfileModel profileModel = new ProfileModel();
                        ProfileDataBase profileDataBase = new ProfileDataBase(getApplicationContext());

                        editText1 = view.findViewById(R.id.profile_name_edit);
                        editText2 = view.findViewById(R.id.profile_email_edit);
                        String x = editText1.getText().toString();
                        String y = editText2.getText().toString();

                        profileModel.setName(x);
                        profileModel.setEmail(y);

                        profileDataBase.AddProfileData(profileModel);
                        InsertData();

                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog=adb.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
                dialog.show();

            }
        });

    }



    private void InsertData() {
        ProfileDataBase profileDataBase = new ProfileDataBase(getApplicationContext());
        Cursor cursor = profileDataBase.LoadAllData();
        while (cursor.moveToNext()) {
            name.setText(cursor.getString(1));
            email.setText(cursor.getString(2));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
