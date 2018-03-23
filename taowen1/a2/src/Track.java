/*

The Track class.  A Track object is made up of Rails, and
has zero or more trains in it.

*/

import java.awt.*;
import java.io.*;

class Track extends Frame {    
    // The Panel on which the Track appears.
    TrackPanel trackPanel;

    // ------------------------------------------------------------------
    // The following items are the Rails and Trains on the Track.

    // private Rail first;    // The first Rail in the graph.
    
    // The maximum number of trains I can hold.
    private int MAX_TRAINS = 10;
    private Train[] trainList = new Train[MAX_TRAINS];    // The Trains running on me.
    private int numTrains = 0;
    
    private Rail[][] rails;    // The grid of rails.
    
    // Whether my Trains are running.  If true, no more Rails can be placed.
    private boolean running = false;

    // ------------------------------------------------------------------
    // The following buttons define the interface for running
    // and saving me.
    private Button runStopButton, quitButton;

    // The following buttons are used to control the track.
    private Button accelButton, decelButton, accelLotsButton, decelLotsButton;
    
    // Add the buttons for creating Rails.  
    protected void buildTrack() {
        trackPanel = new TrackPanel();
        add("Center", trackPanel);
        
        runStopButton = new Button("Run");
        quitButton = new Button("Quit");
        accelButton = new Button("Accelerate");
        decelButton = new Button("Decelerate");
        accelLotsButton = new Button("Accelerate A Lot");
        decelLotsButton = new Button("Decelerate A Lot");

        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(0, 1));
        p2.add(runStopButton);
        p2.add(accelLotsButton);
        p2.add(decelLotsButton);
        p2.add(accelButton);
        p2.add(decelButton);
        p2.add(quitButton);
        add("East", p2);

        pack();
    }

    // Read Rail-placing commands from the user.
    public boolean handleEvent (Event evt) {
        Object target = evt.target;
        
        if (evt.id == Event.ACTION_EVENT) {
            if (target instanceof Button) {
                if ("Run".equals(evt.arg)) {
                    running = true;
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].start();
                    }
                    ((Button)target).setLabel("Suspend");
                } else if ("Suspend".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].suspend();
                    }
                    running = false;
                    ((Button)target).setLabel("Resume");
                } else if ("Resume".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].resume();
                    }
                    running = false;
                    ((Button)target).setLabel("Suspend");
                } else if ("Accelerate".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].accelerate();
                    }
                } else if ("Decelerate".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].decelerate();
                    }
                } else if ("Accelerate A Lot".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].accelerateALot();
                    }
                } else if ("Decelerate A Lot".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].decelerateALot();
                    }
                } else if ("Quit".equals(evt.arg)) {
                    for (int i=0; i < numTrains; i++) {
                        trainList[i].stop();
                    }
                    running = false;
		    System.exit(0);
                }
                return true;
            }
        }
        
        // If we get this far, I couldn't handle the event
        return false;
    }
        

    // If test and r1 != null and r1.exitOK(r1), connect or unrester
    // r1 and r2 depending on whether r2's exits match r1's.
    protected void registerOrUnRegister(boolean test, Rail r1, Rail r2, Direction d) {
        if (test && r1 != null && r1.exitOK(d)) {
            if (r2.exitOK(d.opposite())){
                connectRails(r1, r2, d);
            } else {
                r1.unRegister(d);
            }
        }
        
    }

    // Connect the Rail at (row,col) to its neighbours.
    protected void connectRail(int row, int col) {
        Rail r = rails[row][col];
        
        Direction north = new Direction("north");
        Direction south = new Direction("south");
        Direction east = new Direction("east");
        Direction west = new Direction("west");
        
        if (r != null) {
            Rail rN = row > 0 ? rails[row-1][col] : null;
            Rail rS = row < rails.length-1 ? rails[row+1][col] : null;
            Rail rE = col < rails[0].length-1 ? rails[row][col+1] : null;
            Rail rW = col > 0 ? rails[row][col-1] : null;
            
            registerOrUnRegister(row > 0, rN, r, south);
            registerOrUnRegister(row < rails.length-1, rS, r, north);
            registerOrUnRegister(col > 0, rW, r, east);
            registerOrUnRegister(col < rails[0].length-1, rE, r, west);
        }
    }
    
    
    // Connect rails r1 and r2; r2 is in direction d from r1.  
    protected void connectRails(Rail r1, Rail r2, Direction d) {
        r1.register(r2, d);
        r2.register(r1, d.opposite());
    }
    
    
    // Add e to the rail at location loc.
    public void addCar(GridLoc loc, Car e) {
        rails[loc.row][loc.col].enter(e);
        e.setRail(rails[loc.row][loc.col]);
    }

    // paint
    // ------------------------------------------------------------------
    // Paint the display.
    
    public void paint(Graphics g) {
        update(g);
    }

    // update
    // ------------------------------------------------------------------
    // Update the display; tell all my Tracks to update themselves.
    
    public void update(Graphics g) {
    
        trackPanel.repaint();        
    }

    // Add T to myself.
    public void addTrain(Train T) {
        trainList[numTrains] = T;
        numTrains++;
    }

    // Set up a new, simple Track.
    public Track() {
        rails = new Rail[20][20];
        
        buildTrack();
        
        for (int row=0; row < rails.length; row++) {
            for (int col=0; col < rails[0].length; col++) {
                rails[row][col] = new EmptyRail();
            }
        }
            
        rails[0][0] = new SERail(new GridLoc(0, 0));

        rails[0][1] = new EWRail(new GridLoc(0, 1));
        connectRails(rails[0][0], rails[0][1], new Direction("east"));

        rails[0][2] = new SWRail(new GridLoc(0, 2));
        connectRails(rails[0][1], rails[0][2], new Direction("east"));

        rails[1][2] = new NSRail(new GridLoc(1, 2));
        connectRails(rails[0][2], rails[1][2], new Direction("south"));
        
        rails[2][2] = new CrossRail(new GridLoc(2, 2));
        connectRails(rails[1][2], rails[2][2], new Direction("south"));
        
        rails[2][3] = new EWRail(new GridLoc(2, 3));
        connectRails(rails[2][2], rails[2][3], new Direction("east"));
        
        rails[2][4] = new EWRail(new GridLoc(2, 4));
        connectRails(rails[2][3], rails[2][4], new Direction("east"));
        
        rails[2][5] = new CrossRail(new GridLoc(2, 5));
        connectRails(rails[2][4], rails[2][5], new Direction("east"));
        
        rails[2][6] = new SWRail(new GridLoc(2, 6));
        connectRails(rails[2][5], rails[2][6], new Direction("east"));
        
        rails[3][6] = new NWRail(new GridLoc(3, 6));
        connectRails(rails[2][6], rails[3][6], new Direction("south"));
        
        rails[3][5] = new NERail(new GridLoc(3, 5));
        connectRails(rails[3][6], rails[3][5], new Direction("west"));
        connectRails(rails[2][5], rails[3][5], new Direction("south"));
        
        rails[1][5] = new SERail(new GridLoc(1, 5));
        connectRails(rails[2][5], rails[1][5], new Direction("north"));
        
        rails[1][6] = new EWRail(new GridLoc(1, 6));
        connectRails(rails[1][5], rails[1][6], new Direction("east"));
        
        rails[1][7] = new SWRail(new GridLoc(1, 7));
        connectRails(rails[1][6], rails[1][7], new Direction("east"));
        
        rails[2][7] = new NSRail(new GridLoc(2, 7));
        connectRails(rails[1][7], rails[2][7], new Direction("south"));
        
        rails[3][7] = new NSRail(new GridLoc(3, 7));
        connectRails(rails[2][7], rails[3][7], new Direction("south"));
        
        rails[4][7] = new NWRail(new GridLoc(4, 7));
        connectRails(rails[3][7], rails[4][7], new Direction("south"));
        
        rails[4][6] = new EWRail(new GridLoc(4, 6));
        connectRails(rails[4][7], rails[4][6], new Direction("west"));
        
        rails[4][5] = new WESRail(new GridLoc(4, 5));
        connectRails(rails[4][6], rails[4][5], new Direction("west"));
        
        rails[4][4] = new NERail(new GridLoc(4, 4));
        connectRails(rails[4][5], rails[4][4], new Direction("west"));
        
        rails[3][4] = new SWRail(new GridLoc(3, 4));
        connectRails(rails[4][4], rails[3][4], new Direction("north"));
        
        rails[3][3] = new EWSRail(new GridLoc(3, 3));
        connectRails(rails[3][4], rails[3][3], new Direction("west"));

        rails[4][3] = new SNWRail(new GridLoc(4, 3));
        connectRails(rails[4][3], rails[3][3], new Direction("north"));
        
        rails[5][3] = new NERail(new GridLoc(5, 3));
        connectRails(rails[5][3], rails[4][3], new Direction("north"));
        
        rails[5][4] = new EWRail(new GridLoc(5, 4));
        connectRails(rails[5][4], rails[5][3], new Direction("west"));
        
        rails[5][5] = new NWRail(new GridLoc(5, 5));
        connectRails(rails[5][5], rails[5][4], new Direction("west"));
        connectRails(rails[5][5], rails[4][5], new Direction("north"));
        
        // These are just put there to see what they look like.
        rails[6][0] = new EWNRail(new GridLoc(6, 0));
        rails[6][1] = new EWSRail(new GridLoc(6, 1));
        rails[6][2] = new WENRail(new GridLoc(6, 2));
        rails[6][3] = new WESRail(new GridLoc(6, 3));
        rails[6][4] = new NSERail(new GridLoc(6, 4));
        rails[6][5] = new NSWRail(new GridLoc(6, 5));
        rails[6][6] = new SNERail(new GridLoc(6, 6));
        rails[6][7] = new SNWRail(new GridLoc(6, 7));
// ------------------------------------------------------------------------------
        
       
        
        rails[3][2] = new CrossRail(new GridLoc(3, 2));
        connectRails(rails[3][3], rails[3][2], new Direction("west"));
        connectRails(rails[2][2], rails[3][2], new Direction("south"));

        rails[4][2] = new WENRail(new GridLoc(4, 2));
        connectRails(rails[3][2], rails[4][2], new Direction("south"));
        connectRails(rails[4][3], rails[4][2], new Direction("west"));

        rails[4][1] = new NERail(new GridLoc(4, 1));
        connectRails(rails[4][2], rails[4][1], new Direction("west"));

        rails[3][1] = new SERail(new GridLoc(3, 1));
        connectRails(rails[3][2], rails[3][1], new Direction("west"));
        connectRails(rails[3][1], rails[4][1], new Direction("south"));
        
        rails[2][1] = new EWRail(new GridLoc(2, 1));
        connectRails(rails[2][2], rails[2][1], new Direction("west"));

        rails[2][0] = new NERail(new GridLoc(2, 0));
        connectRails(rails[2][1], rails[2][0], new Direction("west"));

        rails[1][0] = new NSRail(new GridLoc(1, 0));
        connectRails(rails[2][0], rails[1][0], new Direction("north"));
        connectRails(rails[1][0], rails[0][0], new Direction("north"));
              
        trackPanel.addToPanel(rails);
    }

}
