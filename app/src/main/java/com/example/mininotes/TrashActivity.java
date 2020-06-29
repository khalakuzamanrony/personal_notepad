package com.example.mininotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mininotes.Adapter.RecyclerViewAdapter;
import com.example.mininotes.Adapter.TrashRecyclerViewAdapter;
import com.example.mininotes.DataBase.NotesDataBase;
import com.example.mininotes.DataBase.TrashNotesDataBase;
import com.example.mininotes.Model.Model;
import com.example.mininotes.Model.TrashModel;

import java.util.ArrayList;

public class TrashActivity extends AppCompatActivity implements View.OnLongClickListener {
    ArrayList<TrashModel> selected_list = new ArrayList<>();
    RecyclerView.Adapter adapter;
    public TextView counterText;
    public Boolean in_AC = false;
    int Count = 0;

    ArrayList<TrashModel> arrayList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView trashRecyclerView;
    private LinearLayout trashMsgLayout, linearLayout;

    private TrashRecyclerViewAdapter trashRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPref sharedPref=new SharedPref(getApplicationContext());
        if (sharedPref.loadState() == true) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trash");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //INIT
        trashRecyclerView = findViewById(R.id.trash_recyclerview);
        trashMsgLayout = findViewById(R.id.trash_messageLayout);
        linearLayout = findViewById(R.id.linearLayout);
        counterText = findViewById(R.id.countertext);
        counterText.setVisibility(View.GONE);

        callRV();
    }

    private void callRV() {

        TrashNotesDataBase trashNotesDataBase = new TrashNotesDataBase(getApplicationContext());
        arrayList = trashNotesDataBase.LoadAllTrashData();
        if (arrayList.isEmpty()) {
            trashRecyclerView.setVisibility(View.GONE);
        } else {
            trashMsgLayout.setVisibility(View.GONE);
            trashRecyclerView.setHasFixedSize(true);

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            trashRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            adapter = new TrashRecyclerViewAdapter(arrayList, TrashActivity.this);
            trashRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.contextual_menu);
        counterText.setVisibility(View.VISIBLE);
        in_AC = true;
        adapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


}
