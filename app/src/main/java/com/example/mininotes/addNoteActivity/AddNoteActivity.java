package com.example.mininotes.addNoteActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mininotes.DataBase.NotesDataBase;
import com.example.mininotes.MainActivity;
import com.example.mininotes.Model.Model;
import com.example.mininotes.R;
import com.example.mininotes.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText add_note, add_title;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(getApplicationContext());
        if (sharedPref.loadState() == true) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //TOOLBAR  SECTION
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//  INIT  SECTION
        add_title = findViewById(R.id.add_title);
        add_note = findViewById(R.id.add_note);
        floatingActionButton = findViewById(R.id.add_fab);

        floatingActionButton.setOnClickListener(this);


    }

    private void AddNotes() {
        Model model = new Model();
        NotesDataBase notesDataBase = new NotesDataBase(getApplicationContext());
        //Time insert
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yy hh:mm a");
        String time=sdf.format(calendar.getTime());

        if (!add_note.getText().toString().isEmpty() || !add_title.getText().toString().isEmpty()) {
            model.setTitle(add_title.getText().toString());
            model.setNote(add_note.getText().toString());
            model.setTime(time);

            long result = notesDataBase.AddData(model);
            if (result > 0) {
                //Toast.makeText(getApplicationContext(), "Stored", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to Store", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Empty Data can not be Stored!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddNotes();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finishActivity(1);
    }

    @Override
    public void onClick(View v) {
        AddNotes();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finishActivity(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save_addnew_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_addnew) {
            AddNotes();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (item.getItemId() == android.R.id.home)// for default Back Button
        {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}
