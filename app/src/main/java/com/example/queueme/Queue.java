package com.example.queueme;

import java.util.*;

// Class for queue
class Queue
{
    private ArrayList<person> arr;  // array to store queue elements

    // Constructor to initialize queue
    Queue() {
        arr = new ArrayList<person>();
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
        return true;
    }
    public String print(){
        String ret = "";
        for(int i = 0; i < size(); i++){
            ret += arr.get(i).getName() + ", ";
        }
        return ret;
    }
    // Utility function to add an item to the queue
    public boolean enqueue(person item) {//can modify to have any size
        arr.add(item);
        return true;
    }
    private void shift(int posn){
        arr.remove(posn);
    }
    public boolean remove(person rem) {
        for(int i = 0; i < size(); i++){
            if(arr.get(i).getName().equals(rem.getName())){
                //found person in queue
                shift(arr.get(i).getPosition());//shifts everyone behind
                return true;
            }
        }
        return false;
    }
    public boolean remove(int indx) {
        //add safety check
        shift(arr.get(indx).getPosition());//shifts everyone behind
        return true;
    }
    public int spot(person m){
        for(int i = 0; i < size(); i++){
            if(arr.get(i).getName().equals(m.getName())){
                return i;
            }
        }
        return -1;//not found
    }

    // Utility function to return the size of the queue
    public int size() {
        return arr.size();
    }

    // Utility function to check if the queue is empty or not
    public Boolean isEmpty() {
        return (size() == 0);
    }
}