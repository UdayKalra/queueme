package com.example.queueme;

import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    private Queue line = new Queue(10);//get queue from cloud
    private int spot = 0;//get from cloud
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            public void onClick(View v) {
                if(!(Join())){
                    badEnq.show();
                }
            }
        });

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!(Leave())){
                    badRemove.show();
                }
            }
        });

        buttonReady.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!(End())){
                    badDeq.show();
                }
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                update();
            }
        });



    }

    private boolean Join(){
        int end = line.size();
        person me = new person();
        return line.enqueue(me);
    }
    private boolean Leave() {
        return line.remove(spot);
    }
    private boolean End(){
        System.out.print("You have reached the end of the Queue!");
        return line.dequeue();//removes first person from queue
    }
    private void update(){
        line = line;//get updated line data from gcloud
    }
}
