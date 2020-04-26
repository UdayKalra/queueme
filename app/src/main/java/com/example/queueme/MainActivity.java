package com.example.queueme;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.provider.Settings.Secure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {

    private Queue line = new Queue();//get queue from cloud
    private person me;
    private Handler mHandler;
    DatabaseReference databaseLine;
    List<person> lineData;

    private String gibName(int len){
        Random rand = new Random();
        String name = "";
        for(int i = 0; i < len; i++){
            int rand_int1 = rand.nextInt(93)+33;
            name = name + (char)(rand_int1);
        }
        return name;
    }
    final private int max_time = 20;
    public int counter = max_time;
    private boolean ready = false;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mHandler = new Handler();
        databaseLine = FirebaseDatabase.getInstance().getReference("line");
        Toolbar uToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(uToolbar);
        final Button buttonEnter = findViewById(R.id.enter);
        final Button buttonLeave = findViewById(R.id.button7);
        final Button buttonReady = findViewById(R.id.button8);
        final Button buttonRefresh = findViewById(R.id.claimButton);
        final EditText nameField = (EditText) findViewById(R.id.name);
        //starts timer
        new CountDownTimer(max_time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(counter < 0) counter = max_time;
                counter--;
                if(counter <= 0){
                    End();
                    Leave();
                    if(line.getArrList() != null){
                        addToFirebase(line.getArrList());
                    }
                    update();
                    update_text();
                }
            }
            @Override
            public void onFinish() { start(); }
        }.start();
        //end timer
        buttonReady.setEnabled(false);
        //done creating person
        final Spinner spinner = (Spinner) findViewById(R.id.storeDropDown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.store_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final Toast badRemove = Toast.makeText(getApplicationContext(), "You have already left the queue!", Toast.LENGTH_SHORT);
        final Toast badEnq = Toast.makeText(getApplicationContext(), "You are already on the queue", Toast.LENGTH_SHORT);
        final Toast badDeq = Toast.makeText(getApplicationContext(), "The queue is Empty!", Toast.LENGTH_SHORT);
        textView = (TextView) findViewById(R.id.spot);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(me == null){
                    me = new person(-1, nameField.getText().toString() + "::" + gibName(50), spinner.getSelectedItem().toString());
                }
                else {
                    String m = me.getName();
                    Integer p = me.getPosition();
                    me = new person(p, m, spinner.getSelectedItem().toString());
                }
            }
            public void onNothingSelected(AdapterView<?> parent) { } // Another interface callback
        });
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            //entering the queue
            public void onClick(View v) {
                Random r = new Random();
                if(me == null) {
                    me = new person(-1, nameField.getText().toString() + "::" + gibName(50), spinner.getSelectedItem().toString());
                }
                if(!(Join())){
                    badEnq.show();
                }
                mHandler.postDelayed(m_Runnable,2000);
                addToFirebase(line.getArrList());
                update_text();
            }
        });
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            //dropping from the queue
            public void onClick(View v) {
                if(!(Leave())){
                    badRemove.show();
                }
                update();
                update_text();
            }
        });

        buttonReady.setOnClickListener(new View.OnClickListener() {
            //leaving the queue
            public void onClick(View v) {
                if(!End()){
                    badDeq.show();
                }
                Leave();
                if(line.getArrList() != null){
                    addToFirebase(line.getArrList());
                }
                update();
                update_text();
            }
        });
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lineRef = rootRef.child("line");
        final ArrayList<person> lineP = new ArrayList<person>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lineP.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String n = ds.child("name").getValue(String.class);
                    Integer i = ds.child("position").getValue(Integer.class);
                    String s = ds.child("store").getValue(String.class);
                    person p = new person(i, n, s);
                    lineP.add(p);
                }
                line.setArr(lineP);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        lineRef.addValueEventListener(valueEventListener);

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(line.getArrList() != null){
                    addToFirebase(line.getArrList());
                }
                update_text();
            }
        });
    }

    private void update_text(){
        int pos = update();
        if(pos >= 0) {
            textView.setText("You are currently position "+ (pos + 1) +" in line");
        }
        else textView.setText("You are currently not in line");
    }
    private boolean Join(){
        return line.enqueue(me);
    }
    private boolean Leave() {
        if(line.isEmpty() || me == null) return false;
        if(me != null){
            if(line.spot(me) == 0) ready = false;
            removeFromFirebase(line.indexfind(me));
        }
        return true;
    }
    private boolean End() {
        if (line.dequeue()){
            ready = false;
            return true;//removes first person from queue
        }
        return false;
    }

    private int update(){
        if(me != null) {
            // System.out.println("My Name:" + me.getName());
            // System.out.println("Queue Size:" + line.size());
            // System.out.println("Queue:" + line.print());
            // update_text();

            me.setPosition(line.spot(me));
            int pos = line.spot(me);
            if(pos == 0){
                if(!ready) counter = max_time;
                Toast n = Toast.makeText(getApplicationContext(), "You are in front! accept in <" + counter + "s", Toast.LENGTH_SHORT);
                ready = true;
                if(counter % 5 == 0) n.show();
            }
            return pos;
        }
        return -1;
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            final Button buttonReady = findViewById(R.id.button8);
            if(update() != 0)
                buttonReady.setEnabled(false);
            else
                buttonReady.setEnabled(true);
            update_text();
            int refresh = 1000 * (line.spot(me) + 1);
            if(refresh < 1000) refresh = 1000;
            mHandler.postDelayed(m_Runnable, refresh);
        }
    };//runnable

    public void addToFirebase(ArrayList<person> p){
        databaseLine.setValue(p);
    }
    public void removeFromFirebase(int position){
        line.remove(me);
        DatabaseReference personinLine = FirebaseDatabase.getInstance().getReference("line").child(position + "");
        personinLine.removeValue();
        me = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }
}