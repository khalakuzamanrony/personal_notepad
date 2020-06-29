package com.example.mininotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mininotes.Adapter.RecyclerViewAdapter;
import com.example.mininotes.DataBase.NotesDataBase;
import com.example.mininotes.DataBase.ProfileDataBase;
import com.example.mininotes.DataBase.TrashNotesDataBase;
import com.example.mininotes.Model.Model;
import com.example.mininotes.Model.TrashModel;
import com.example.mininotes.addNoteActivity.AddNoteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    //SharedPref
    private SharedPref sharedPref;
    //RecyclerView
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Model> arrayList = new ArrayList<Model>();
    boolean isStag;
    //Search
    private EditText search;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(getApplicationContext());
        if (sharedPref.loadState() == true) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TOOLBAR  SECTION
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("All Notes are Here");


        // INIT  SECTION
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerview);
        linearLayout = findViewById(R.id.messageLayout);

        //Search here !
        search = findViewById(R.id.search);
        /*search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    recyclerViewAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    Log.d("SerachError", String.valueOf(e));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


        //Ading INFO To Header
        AddInfotoHead();

//RecycerView Call
        recyclerViewCall();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finishActivity(1);

            }
        });


//Navigation Drawer
        navigationView.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //Navigation Icon Color
        navigationView.setItemIconTintList(null);
        //Navigation Width Controll
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        navigationView.setLayoutParams(params);
        //Navigation Cheaked Item
        navigationView.setCheckedItem(R.id.nav_notes);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_notes:

                        Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.nav_about:
                        // Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();

                        Intent intent3 = new Intent(getApplicationContext(), AboutActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case R.id.share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String sub = String.valueOf(R.string.app_name);
                        String body = BuildConfig.APPLICATION_ID;
                        intent.putExtra(Intent.EXTRA_SUBJECT, sub);
                        intent.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(intent, "Share.."));
                        break;
                    case R.id.settings:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        Intent intent2 = new Intent(getApplicationContext(), Settings.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.view:
                        ArrayList<Model> arrayList = new ArrayList<>();
                        NotesDataBase notesDataBase = new NotesDataBase(getApplicationContext());
                        arrayList = notesDataBase.LoadAllData();
                        isStag = !isStag;
                        if (isStag) {
                            //Toast.makeText(getApplicationContext(),"Stag Mode",Toast.LENGTH_SHORT).show();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(arrayList, getApplicationContext(), sharedPref.loadState());
                            recyclerView.setAdapter(recyclerViewAdapter);
                            saveStateofView(isStag);
                        } else {
                            //Toast.makeText(getApplicationContext(),"Liner Mode",Toast.LENGTH_SHORT).show();
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(staggeredGridLayoutManager);
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(arrayList, getApplicationContext(), sharedPref.loadState());
                            recyclerView.setAdapter(recyclerViewAdapter);
                            saveStateofView(isStag);
                        }

                        break;
                    case R.id.help:
                        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this, R.style.ADBStyle);
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.help_view, null);
                        adb.setView(view);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        adb.setIcon(R.drawable.help);
                        adb.setTitle(Html.fromHtml("<font color ='#0C9184'>Help </font> "));
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        Dialog dialog = adb.create();
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.edit_text_bg);
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(1000, 1200);
                        break;
                    case R.id.trash:
                        startActivity(new Intent(getApplicationContext(), TrashActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finishActivity(1);
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


        });


    }

    private void saveStateofView(boolean isStag) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPref", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("viewState", isStag);
        editor.commit();
    }

    private boolean loadStateofView() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPref", getApplicationContext().MODE_PRIVATE);
        boolean isStag = sharedPreferences.getBoolean("viewState", false);
        return isStag;
    }

    public void AddInfotoHead() {
        final Settings settings = new Settings();

        View view = navigationView.getHeaderView(0);
        TextView username, useremail;
        ImageView edit;
        edit = view.findViewById(R.id.editprofile);
        edit.setVisibility(View.GONE);
        username = view.findViewById(R.id.username);
        useremail = view.findViewById(R.id.useremail);
        ProfileDataBase profileDataBase = new ProfileDataBase(getApplicationContext());
        Cursor cursor = profileDataBase.LoadAllData();
        while (cursor.moveToNext()) {
            username.setText(cursor.getString(1));
            useremail.setText(cursor.getString(2));
        }
    }

    private void recyclerViewCall() {
        final NotesDataBase notesDataBase = new NotesDataBase(getApplicationContext());
        arrayList = notesDataBase.LoadAllData();
        //Sorting ITEMS
       /* Collections.sort(arrayList, new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {

                return o2.getTime().compareTo(o1.getTime());
            }
        });*/

        if (arrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            // recyclerView.setLayoutManager(staggeredGridLayoutManager);
            //GridLayoutManager gridLayoutManager =new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerViewAdapter = new RecyclerViewAdapter(arrayList, getApplicationContext(), sharedPref.loadState());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.scrollToPosition(-1);


            //ItemTouchHelper
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN | ItemTouchHelper.START|ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    Collections.swap(arrayList,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                    recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    TrashModel trashModel = new TrashModel();
                    int p = viewHolder.getLayoutPosition();
                    TrashNotesDataBase trashNotesDataBase = new TrashNotesDataBase(getApplicationContext());
                    //Model model = recyclerViewAdapter.getPosition(viewHolder.getLayoutPosition());
                    Model model = arrayList.get(p);

                    NotesDataBase notesDataBase1 = new NotesDataBase(getApplicationContext());
                    notesDataBase1.DeleteData(model);

                    trashModel.setIndex(model.getIndex());
                    trashModel.setNote(model.getNote());
                    trashModel.setTitle(model.getTitle());
                    trashModel.setTime(model.getTime());
                    trashNotesDataBase.AddTrashData(trashModel);

                    arrayList.remove(p);
                    recyclerViewAdapter.notifyItemRemoved(p);
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.ADBStyle);
            adb.setTitle(Html.fromHtml("<font color ='#0C9184'>Exit </font> "));
            adb.setMessage(Html.fromHtml("<font color ='#0C9184'>Are You Sure To Exit ?</font> "));
            adb.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            Dialog dialog = adb.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 500);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

     /*   if (item.getItemId()==R.id.app_bar_search){

        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    recyclerViewAdapter.getFilter().filter(newText);
                } catch (Exception e) {

                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
}
