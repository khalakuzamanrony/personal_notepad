package com.example.mininotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mininotes.DataBase.NotesDataBase;
import com.example.mininotes.DataBase.TrashNotesDataBase;
import com.example.mininotes.Model.Model;
import com.example.mininotes.Model.TrashModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Update extends AppCompatActivity {
    private EditText update_title, update_note;
    private FloatingActionButton up_fab;
    private ActionBar actionBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPref sharedPref=new SharedPref(getApplicationContext());
        if (sharedPref.loadState() == true) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //TOOLBAR  SECTION
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Note");
        getSupportActionBar().setSubtitle("Your can edit your notes here!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*actionBar = getSupportActionBar();
        actionBar.setTitle("Notes");*/

// INIT  SECTION
        up_fab = findViewById(R.id.update_fab);
        update_note = findViewById(R.id.update_note);
        update_title = findViewById(R.id.update_title);

        Bundle bundle = getIntent().getExtras();
        Model model = (Model) bundle.getSerializable("All");
        update_title.setText(model.getTitle());
        update_note.setText(model.getNote());


        up_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });
    }

    //Update Data
    public void UpdateData() {
        Model modelx = new Model();
        String gTitle = update_title.getText().toString();
        String gNote = update_note.getText().toString();

        Bundle bundle = getIntent().getExtras();
        Model model2 = (Model) bundle.getSerializable("All");
        String x = model2.getIndex();


        NotesDataBase notesDataBase = new NotesDataBase(getApplicationContext());
        modelx.setNote(gNote);
        modelx.setTitle(gTitle);
        modelx.setIndex(x);

        if (!gTitle.equals(update_title) || !gNote.equals(update_note)) {
            Boolean result = notesDataBase.UpdateData(modelx);
            if (result == true) {
                //Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 0);

            } else {
                Toast.makeText(getApplicationContext(), "Failed to Updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No new Words Found ! Nothing to update", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 0);

        }
    }


    //Delete Data
    private void DeleteData() {
        NotesDataBase notesDataBase = new NotesDataBase(getApplicationContext());
        TrashNotesDataBase trashNotesDataBase = new TrashNotesDataBase(getApplicationContext());
        TrashModel trashModel = new TrashModel();


        Bundle bundle = getIntent().getExtras();
        Model model = (Model) bundle.getSerializable("All");
        String x = model.getIndex();
        Model modely = new Model();
        modely.setIndex(x);


        trashModel.setTitle(model.getTitle());
        trashModel.setNote(model.getNote());
        trashModel.setTime(model.getTime());
        trashModel.setIndex(model.getIndex());

        Boolean delete = notesDataBase.DeleteData(modely);
        if (delete == true) {
            Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
            trashNotesDataBase.AddTrashData(trashModel);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivityForResult(intent, 0);


        } else {
            Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UpdateData();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save) {
            UpdateData();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            AlertDialog.Builder adb = new AlertDialog.Builder(this,R.style.ADBStyle);
            adb.setTitle("Delete");
            adb.setIcon(R.drawable.trash);
            adb.setMessage("Are you Sure to Delete?");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteData();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            Dialog dialog=adb.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
            dialog.show();
            Window window=dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,400);


        }
        return super.onOptionsItemSelected(item);
    }


}
