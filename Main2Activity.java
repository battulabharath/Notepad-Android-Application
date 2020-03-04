package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashSet;

import static android.R.layout.simple_list_item_1;


public class Main2Activity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    ListView listView;
    Menu menu;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);


        MenuItem searchItem = menu.findItem( R.id.app_bar_search );





        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        if(item.getItemId() == R.id.add_note) {
            Intent intent = new Intent(getApplicationContext(), EditnoteActivity.class);
            startActivity(intent);
            return true;
        }

        if(item.getItemId() == R.id.app_bar_search){
            MenuItem searchItem = menu.findItem( R.id.app_bar_search );

            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<String> templist = new ArrayList<>(  );

                    for(String temp : notes){
                        if(temp.toLowerCase().contains(newText.toLowerCase())){
                            templist.add( temp );
                        }
                    }
                    ArrayAdapter<String> adaptersearch = new ArrayAdapter<>( Main2Activity.this, simple_list_item_1, templist );
                    listView.setAdapter(adaptersearch);

                    return true;
                }
            } );

        }

        return false;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ListView listView = (ListView) findViewById(R.id.listview);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepad", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null){
            notes.add("Sample Note!");
        } else {
            notes = new ArrayList<>(set);
        }


        arrayAdapter = new ArrayAdapter(this, simple_list_item_1,notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),EditnoteActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(Main2Activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepad", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

    }
}
