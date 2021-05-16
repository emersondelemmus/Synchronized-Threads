

import java.io.*;
import java.util.*;

/*
* @Author: Emerson de Lemmus
* A Multithreaded program demonstrating synchronization	
*/

class Line{
    
    public static int value = 0;

    public int get(){
        return this.value;
    }

    public void increment(Line line){
        Line.value = Line.value + 1;
    }

    public void decrement(Line line){
        if(Line.value < 0)
            Line.value = Line.value - 1;
    }

    public void pprint(Line line){
        System.out.println("Line value: " + Line.value);
    }
}

class Ride{

    public static int value = 0;
    //remember that max seats is 4

  /*  public int get(){
        return this.value;
    }
    */
    public void increment(Ride ride){
        Ride.value = Ride.value + 1;
        
    }

    public void emptyRide(Ride ride){
        Ride.value = 0;
        //might need to loop decrement one by one until 0
    }
    
    public int get(){
        return this.value;
    }
}
  
class Employee implements Runnable{

    Ride ride;
    Line line;
    int max = 4;

    
    Employee(Ride ride, Line line){
        //Line needs to be static so there is only one shared instance
        this.ride = ride;
        this.line = line;
    }

    public void checkLine()
    {
        boolean test = true;

        while(test){

            try {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                System.out.println("Failed");
            }

            int lineNumber = this.line.get();
            //int seatNum = this.ride.get();
            System.out.println("Seatnum: " + this.ride.get());

            if (lineNumber > 0)
            {
                
                if (this.ride.get() < 4)
                {
                    System.out.println("\nSeat Value: \t" + this.ride.get());
                    //put them on ride
                    this.ride.increment(this.ride);
                    System.out.println("\n\t\tLine: " + this.line.get());
                    this.line.decrement(this.line);
                    System.out.println("\n\t\t\tDecrement Line: " + this.line.get());

                }
                else if (this.ride.get() == 4) 
                {
                    //ppl in line and ride full means start ride
                    //startRide();
                    test = false;
                }
            } 
            else if (this.ride.get() >= 2)
            {
                //no one in line but enough ppl on ride then start ride
                //startRide();
                test = false;

            }

        }

    }

    public void startRide(){

        try {
            Thread.sleep(5000);
            this.ride.emptyRide(this.ride);
            //after throw everyone off ride
        }
        catch (InterruptedException e)
        {
            System.out.println("Rides Broke");
        }
    }

    public void run(){
        System.out.println("Starting work!");

        while(true){
            checkLine();
            startRide();
        }
    }
 
}


class Guest implements Runnable {

    Random rand = new Random();
    Line line;
    String name;

    Guest(Line line, String name){
        //Line needs to be static so there is only one shared instance
        this.line = line;
        this.name = name;
    }

    public void waitToGetInLine()
    {
        int waitTime = rand.nextInt((10000 - 1000) + 1000) + 1000;

        //System.out.println("Waiting...");

        try {
        Thread.sleep(waitTime);
        }
        catch (Exception e) {
            System.out.println("Interrupt"); 
        }
        getInLine();
    }
    
    public void getInLine(){
        //add a guest to line
        System.out.println(this.name + " line value: " + this.line.get()); 

        this.line.increment(this.line);

        //System.out.println(this.name + " line value: " + this.line.get()); 

    }

    public void run(){
        System.out.println("Guest start getting in line!");

        while(true){
            waitToGetInLine();
        }
    }

}


public class Emerson_Synchronized
{
    public static Line line = new Line();
    public static Ride ride = new Ride();


    public static void main(String[] args) 
    {


        Thread one = new Thread(new Guest(line, "Guests"));
        Thread two = new Thread(new Employee(ride, line));

        //Thread two = new Thread(new Guest(line, "Ponalreff"));
        
        one.start();
        two.start();
        
        
        
        try 
        {
            one.join();
            two.join();
        }
        catch(Exception e) 
        {
            System.out.println("interrupted");
        }
         
    }
}
