package com.example.queueme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    private Queue line = new Queue();//get queue from cloud
    private person me;
    private Handler mHandler;

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
    private int max_list = 10;
    private void add_more(){
        //my stuff
        for(int i = 0; i < max_list; i++){
            listppl.add(new_guy());
        }
    }
    private void add_to_queue(){
        //Random rand = new Random();
        //int rand_int1 = rand.nextInt(line.size());
        for(int i = 0; i < 5; i++){
            line.enqueue(listppl.get(i));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        add_more();
        //android stuff
        add_to_queue();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mHandler = new Handler();





        Toolbar uToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(uToolbar);
        final Button buttonEnter = findViewById(R.id.enter);
        final Button buttonLeave = findViewById(R.id.button7);
        final Button buttonReady = findViewById(R.id.button8);
        final Button buttonRefresh = findViewById(R.id.claimButton);
        final EditText nameField = (EditText) findViewById(R.id.name);

        buttonReady.setEnabled(false);


        //access with nameField.getText();
        //creating person
        //done creating person
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
        final Toast badEnq = Toast.makeText(getApplicationContext(), "You are already on the Queue", Toast.LENGTH_SHORT);
        final Toast badDeq = Toast.makeText(getApplicationContext(), "The queue is Empty!", Toast.LENGTH_SHORT);
        final Toast badSpot = Toast.makeText(getApplicationContext(), "You are not on the queue!", Toast.LENGTH_SHORT);

        final TextView textView = (TextView) findViewById(R.id.spot);

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
                if(me == null) {
                    me = new person(-1, nameField.getText().toString() + gibName(5));
                }
                if(!(Join())){
                    badEnq.show();
                }
                mHandler.postDelayed(m_Runnable,5000);
                update();
                update_text(textView);

            }
        });

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            //dropping from the queue
            public void onClick(View v) {
                if(!(Leave())){
                    badRemove.show();
                }
                update();
                me.setPosition(-1);
                update_text(textView);

            }
        });

        buttonReady.setOnClickListener(new View.OnClickListener() {
            //leaving the queue
            public void onClick(View v) {
                if(!(End())){
                    badDeq.show();
                }
                update();
                update_text(textView);

            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                update_text(textView);
            }
        });
        //final EditText nameField = (EditText) findViewById(R.id.name);

        //access with nameField.getText();
    }
    private void update_text(TextView tview){
        int pos = update();
        if(pos >= 0) tview.setText("You are currently position "+ (pos + 1) +" in line.");
        else tview.setText("You are currently not in line.");

    }
    private boolean Join(){
        int i = 0;
        //while(listppl.get(i).getPosition() != -1) i++;
        //if(i == listppl.size() - 1){
        //    add_more();
        //}
        return line.enqueue(me);
    }
    private boolean Leave() {
        if(line.isEmpty()) return false;
        //Random rand = new Random();
        //int rand_int1 = rand.nextInt(line.size());
        System.out.println("Removing person: " + me.getName());
        return line.remove(me);
    }
    private boolean End(){
        return line.dequeue();//removes first person from queue
    }
    private int update(){
        List<String> listnames = new ArrayList<String>();
        for(int i = 0; i < listppl.size(); i++){
            listppl.get(i).setPosition(line.spot(listppl.get(i)));//get updated spot data from gcloud
            listnames.add(listppl.get(i).getName());
        }
        //spot can also have a toast
        System.out.println("People:" + listnames);
        System.out.println("My Name:" + me.getName());
        System.out.println("Queue Size:" + line.size());
        System.out.println("Queue:" + line.print());
        line = line;//get updated line data from gcloud
        return line.spot(me);
    }


    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            final Button buttonReady = findViewById(R.id.button8);
            final TextView textView = (TextView) findViewById(R.id.spot);


            if(update() != 0)
                buttonReady.setEnabled(false);
            else
                buttonReady.setEnabled(true);

            update();
            update_text(textView);
            mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable


    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }
}