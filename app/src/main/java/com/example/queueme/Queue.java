package com.example.queueme;

import java.util.*;

// Class for queue
class Queue
{
    private person arr[];         // array to store queue elements
    private person front;         // front points to front element in the queue
    private int capacity;      // maximum capacity of the queue
    private int count;         // current size of the queue

    // Constructor to initialize queue
    Queue(int size) {
        arr = new person[size];
        capacity = size;
        front = new person(0, "joe");//get from gcloud
        count = 0;
    }

    // Utility function to remove front element from the queue
    public boolean dequeue() {
        // check for queue underflow
        if (isEmpty()) {
            System.out.println("not enough to deq");
            //System.exit(1);
            return false;
        }
        shift(0);//next guy
        front = arr[0];
        return true;
    }
    public String print(){
        String ret = "";
        for(int i = 0; i < size(); i++){
            ret += arr[i].name + ", ";
        }
        return ret;
    }
    // Utility function to add an item to the queue
    public boolean enqueue(person item) {//can modify to have any size
        // check for queue overflow
        if (isFull()) {
            System.out.println("cannot fit any more");
            return false;
            //System.exit(1);
        }
        arr[count] = item;
        count++;
        return true;
    }
    private void shift(int posn){
        for(int i = posn; i < size() - 1; i++){
            arr[i] = arr[i+1];
            arr[i].position = i;//update spots
        }
        count--;
    }
    public boolean remove(person rem) {
        for(int i = 0; i < size(); i++){
            if(arr[i].name.equals(rem.name)){
                //found person in queue
                shift(arr[i].position);//shifts everyone behind
                return true;
            }
        }
        return false;
    }
    public boolean remove(int indx) {
        //add safety check
        shift(arr[indx].position);//shifts everyone behind
        return true;
    }
    public int spot(person m){
        for(int i = 0; i < size(); i++){
            if(arr[i].name.equals(m.name)){
                return i;
            }
        }
        return -1;//not found
    }
    // Utility function to return front element in the queue
    public person peek()
    {
        if (isEmpty()) {
            System.out.println("nobody to peek");
            //System.exit(1);
        }
        return front;
    }

    // Utility function to return the size of the queue
    public int size() {
        return count;
    }

    // Utility function to check if the queue is empty or not
    public Boolean isEmpty() {
        return (size() == 0);
    }

    // Utility function to check if the queue is empty or not
    public Boolean isFull() {
        return (size() == capacity);
    }
}