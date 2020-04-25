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
        front = new person();//get from gcloud
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
        front = arr[1];
        count--;
        return true;
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
    public boolean remove(int indx) {
        //check if valid
        if(indx < 0 || indx > count) {
            System.out.println("nobody to remove");
            //System.exit(1);
            return false;
        }
        return true;
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