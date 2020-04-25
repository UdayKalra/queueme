package com.example.queueme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    private Queue line = new Queue(100);//get queue from cloud
    private int spot = -1;//get from cloud

    public String gibName(int len){
        Random rand = new Random();
        String name = "";
        for(int i = 0; i < len; i++){
            int rand_int1 = rand.nextInt(26)+65;
            name = name + (char)(rand_int1);
        }
        return name;
    }
    List<person> listppl = new ArrayList<person>();
    private person new_guy(){
        return new person(-1, gibName(5));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //my stuff
        for(int i = 0; i < 10; i++){
            listppl.add(new_guy());
        }
        //android stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar uToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(uToolbar);
        final Button buttonEnter = findViewById(R.id.enter);
        final Button buttonLeave = findViewById(R.id.button7);
        final Button buttonReady = findViewById(R.id.button8);
        final Button buttonRefresh = findViewById(R.id.claimButton);
        Spinner spinner = (Spinner) findViewById(R.id.storeDropDown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.store_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final Toast badRemove = Toast.makeText(getApplicationContext(), "No one to remove!", Toast.LENGTH_SHORT);
        final Toast badPeek = Toast.makeText(getApplicationContext(), "No one to peek!", Toast.LENGTH_SHORT);
        final Toast badEnq = Toast.makeText(getApplicationContext(), "Reached Capacity", Toast.LENGTH_SHORT);
        final Toast badDeq = Toast.makeText(getApplicationContext(), "Not enough to Dequeue!", Toast.LENGTH_SHORT);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
            int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            //entering the queue
            public void onClick(View v) {
                if(!(Join())){
                    badEnq.show();
                }
                update();
            }
        });

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            //dropping from the queue
            public void onClick(View v) {
                if(!(Leave())){
                    badRemove.show();
                }
                update();
            }
        });

        buttonReady.setOnClickListener(new View.OnClickListener() {
            //leaving the queue
            public void onClick(View v) {
                if(!(End())){
                    badDeq.show();
                }
                update();
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                update();
            }
        });
    }

    private boolean Join(){
        int i = 0;
        while(listppl.get(i).position != -1) i++;
        return line.enqueue(listppl.get(i));
    }
    private boolean Leave() {
        if(line.isEmpty()) return true;
        Random rand = new Random();
        int rand_int1 = rand.nextInt(line.size());
        System.out.println("Removing person: " + rand_int1);
        String name = "" + (char)(rand_int1);
        return line.remove(rand_int1);
    }
    private boolean End(){
        return line.dequeue();//removes first person from queue
    }
    private void update(){
        List<String> listnames = new ArrayList<String>();
        for(int i = 0; i < listppl.size(); i++){
            listppl.get(i).position = line.spot(listppl.get(i));//get updated spot data from gcloud
            listnames.add(listppl.get(i).getName());
        }
        //spot can also have a toast
        System.out.println("People:" + listnames);
        System.out.println("Queue Size:" + line.size());
        System.out.println("Queue:" + line.print());
        line = line;//get updated line data from gcloud
    }
}
