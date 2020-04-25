package com.example.queueme;

import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


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
                Join();
            }
        });

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Leave();
            }
        });

        buttonReady.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                End();
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                update();
            }
        });



    }

    private void Join(){
        int end = line.size();
        person me = new person();
        line.enqueue(me);
    }
    private void Leave() {
        line.remove(spot);
    }
    private void End(){
        System.out.print("You have reached the end of the Queue!");
        line.dequeue();//removes first person from queue
    }
    private void update(){
        line = line;//get updated line data from gcloud
    }
}
