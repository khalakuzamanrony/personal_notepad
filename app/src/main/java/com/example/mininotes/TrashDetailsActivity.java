package com.example.mininotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mininotes.DataBase.NotesDataBase;
import com.example.mininotes.DataBase.TrashNotesDataBase;
import com.example.mininotes.Model.Model;
import com.example.mininotes.Model.TrashModel;
import com.example.mininotes.R;

import org.w3c.dom.Text;

public class TrashDetailsActivity extends AppCompatActivity {
    private TextView trash_note, trash_title;
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
        setContentView(R.layout.activity_trash_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trash Box");


        trash_title = findViewById(R.id.trash_title);
        trash_note = findViewById(R.id.trash_notes);

        Bundle bundle = getIntent().getExtras();
        TrashModel trashModel = (TrashModel) bundle.getSerializable("All");
        trash_title.setText(trashModel.getTitle());
        trash_note.setText(trashModel.getNote());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.trash_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore:
                Restore();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.delete:
                DeletePermanent();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void DeletePermanent() {
        AlertDialog.Builder adb = new AlertDialog.Builder(TrashDetailsActivity.this,R.style.ADBStyle);
       adb.setTitle("Delete ");
        adb.setMessage("This Action will delete your note Permanently !");
        adb.setIcon(R.drawable.trash);
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

    public void DeleteData() {
        TrashModel trashModel2 = new TrashModel();
        TrashNotesDataBase trashNotesDataBase = new TrashNotesDataBase(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        TrashModel trashModelx = (TrashModel) bundle.getSerializable("All");

        trashModel2.setIndex(trashModelx.getIndex());

        Boolean res = trashNotesDataBase.DeleteTrashData(trashModel2);
        if (res == true) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
            finish();
        }
    }

    private void Restore() {
        Model model = new Model();
        TrashNotesDataBase trashNotesDataBase = new TrashNotesDataBase(getApplicationContext());
        NotesDataBase notesDataBase = new NotesDataBase(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        TrashModel trashModel = (TrashModel) bundle.getSerializable("All");

        model.setIndex(trashModel.getIndex());
        model.setTitle(trashModel.getTitle());
        model.setTime(trashModel.getTime());
        model.setNote(trashModel.getNote());
        long res = notesDataBase.AddData(model);

        if (res > 0) {
            DeleteData();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            Log.i("Restore error", "Failed");
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
