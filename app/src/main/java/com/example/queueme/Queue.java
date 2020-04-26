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
    private void update_spots(){
        for(int i = 0; i < arr.size(); i++){
            arr.get(i).setPosition(i);
        }
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
        update_spots();
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
        //check if alr added
        for(int i = 0; i< arr.size(); i++){
            if(arr.get(i).getName().equals(item.getName()))
                return false;
        }
        arr.add(item);
        update_spots();
        return true;
    }
    private void shift(int posn){
        arr.remove(posn);
        update_spots();
    }
    public boolean remove(person rem) {
        int i = spot(rem);
        if(i == -1) return false;
        shift(i);
        update_spots();
        return true;
    }
    public boolean remove(int indx) {
        //add safety check
        shift(arr.get(indx).getPosition());//shifts everyone behind
        update_spots();
        return true;
    }
    public int spot(person m){
        for(int i = 0; i< arr.size(); i++){
            if(arr.get(i).getName().equals(m.getName()))
                return i;
        }
        return -1;
    }

    // Utility function to return the size of the queue
    public int size() {
        return arr.size();
    }

    // Utility function to check if the queue is empty or not
    public Boolean isEmpty() {
        return (size() == 0);
    }
    public ArrayList<person> getArrList(){
        return arr;
    }
    public void setArr(ArrayList<person> p){
        arr = p;
    }
}