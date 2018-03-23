/*

The Train class.  Trains have an Engine, followed by one or more Cars,
followed by a Caboose.  There is no limit to the length of a Train.  The
train has a speed, which is related to the size of the engine, the weight
of the whole train, and the amount of power flowing through the tracks.

A train has a delay, which is directly related to the speed -- the faster
the train is moving, the shorter the delay.  Each turn, a Train will move
one track piece in the current direction.

*/

import java.awt.*;
import java.io.*;

class Train extends Thread {

    private int totalWeight;    // The sum of the weights of my cars.
    private int delay;          // The amount of time between each of my turns.
    private boolean forward;    // Whether I am moving forward.
    
    private Engine engine;      // The Car pulling the train.
    private Car caboose;    // The Car at the end of the train.
    protected Track theTrack;   // The Track on which I am running.
    protected int numCars=0;   // The number of cars in me.
        
    public Train (String threadName) {
        super(threadName);
    }
    
    // Add Car T to the end of me.
    public void addToTrain(Car T) {
        if (engine != null) {
            caboose.nextCar = T;
        } else {
            engine = (Engine) T;
        }
        
        caboose = T;
        numCars++;
    }
    
    // Set my delay between moves to d.
    public void setSpeed(int d) {
        delay = d;
    }
    

    // Add me to Track T at location loc moving in direction dir.
    public void addToTrack(Track T, Direction dir, GridLoc loc) {
        theTrack = T;
        theTrack.addTrain(this);
        
        Car currCar = engine;
        while (currCar != null) {
            currCar.setDirection(dir);
            theTrack.addCar(loc, currCar);

            // Now figure out the dir for the next Car,
            // and the next loc.
            
            if (dir.equals("north")) {
                loc.row--;
            } else if (dir.equals("south")) {
                loc.row++;
            } else if (dir.equals("east")) {
                loc.col++;
            } else if (dir.equals("west")) {
                loc.col--;
            }

            Direction nD = currCar.currentRail.exit(dir);
            Rail nextRail = currCar.currentRail.nextRail(nD);
            
            // Now I know the Rail on which the next currCar will
            // be.  Find out how it got on to it.
            dir = nextRail.exit(dir.opposite());
            
            currCar = currCar.nextCar;
        }
    }
    
    // Halve my delay.
    public void accelerateALot() {
        delay /= 2;
    }

    // Double my delay.
    public void decelerateALot() {
        delay *= 2;
    }

    // Speed up by a factor of 20ms.
    public void accelerate() {
        delay -= 20;
    }

    // Slow down by a factor of 20ms.
    public void decelerate() {
        delay += 20;
    }

    public void run() {
        while (true) {
            engine.move();
            
            // Sleep for 1 second.
            try {sleep(delay);} catch (InterruptedException e) {}
        }
    }
    
}

