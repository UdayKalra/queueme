package com.example.queueme;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Array;
import java.util.ArrayList;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
    String[] stores = {"one", "two", "three"};
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

        buttonReady.setEnabled(false);
        //done creating person
        Spinner spinner = (Spinner) findViewById(R.id.storeDropDown);
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
        final TextView textView = (TextView) findViewById(R.id.spot);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { }
            public void onNothingSelected(AdapterView<?> parent) { } // Another interface callback
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            //entering the queue
            public void onClick(View v) {
                Random r = new Random();
                if(me == null) {
                    me = new person(-1, nameField.getText().toString() + "::" + gibName(5), stores[r.nextInt(3)]);
                }
                if(!(Join())){
                    badEnq.show();
                }
                mHandler.postDelayed(m_Runnable,5000);
                addToFirebase(line.getArrList());
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
                update_text(textView);
            }
        });

        buttonReady.setOnClickListener(new View.OnClickListener() {
            //leaving the queue
            public void onClick(View v) {
                if(!(End())){
                    badDeq.show();
                }
                Leave();
                if(line.getArrList() != null){
                    addToFirebase(line.getArrList());
                }
                update();
                update_text(textView);
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
                update_text(textView);
            }
        });
    }
    private void update_text(TextView tview){
        int pos = update();
        if(pos >= 0) tview.setText("You are currently position "+ (pos + 1) +" in line.");
        else tview.setText("You are currently not in line.");
    }
    private boolean Join(){
        return line.enqueue(me);
    }
    private boolean Leave() {
        if(line.isEmpty() || me == null) return false;
        if(me != null){
            removeFromFirebase(line.indexfind(me));
        }
        return true;
    }
    private boolean End(){
        return line.dequeue();//removes first person from queue
    }

    private int update(){
        if(me != null) {
            // System.out.println("My Name:" + me.getName());
            // System.out.println("Queue Size:" + line.size());
            // System.out.println("Queue:" + line.print());
            me.setPosition(line.spot(me));
            return line.spot(me);
        }
        return -1;
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            final Button buttonReady = findViewById(R.id.button8);
            final TextView textView = (TextView) findViewById(R.id.spot);
            if(update() != 0)
                buttonReady.setEnabled(false);
            else
                buttonReady.setEnabled(true);
            update_text(textView);
            mHandler.postDelayed(m_Runnable, 5000);
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