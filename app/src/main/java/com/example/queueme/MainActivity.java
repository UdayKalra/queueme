package com.example.queueme;

import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private Queue line = new Queue(10);//get queue from cloud
    private int spot = 0;//get from cloud
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
