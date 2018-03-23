/*

The Car class.  A Car object is a car in a train.  It has
weight and color, and can draw() and move().

*/

import java.awt.*;
import java.io.*;

abstract class Car {
    
    // My color.
    protected Color color;
    
    // My weight, in stone.
    protected int weight;
    
    // The Car that immediately follows me.
    protected Car nextCar;
    
    // The Rail that I am currently occupying.
    public Rail currentRail;
    
    // The direction in which I entered the current Rail.
    protected Direction dir;
    
    // Set me moving in direction d.
    public void setDirection(Direction d) {
        dir = d;
    }
    
    // Place this Car on Rail r.
    public void setRail(Rail r) {
        currentRail = r;
    }
    

    // Move forward one TrackPiece; t is the current TrackPiece.  Tell
    // all of the cars I am pulling to move as well.
    public void move() {
    
        Direction nD = currentRail.exit(dir);

        Direction nextDir = nD.opposite();
        Rail nextRail = currentRail.nextRail(dir);
        dir = nextDir;
        
        if (nextRail.enter(this)) {
            currentRail.leave();
            currentRail = nextRail;
            
            // We have to call this here rather than within currentRail.enter()
            // because otherwise the wrong Rail is used...
            currentRail.repaint();

            if (nextCar != null) {
                nextCar.move();
            }
        }
    }

    // Return true if the current Rail is a SwitchRail and I am going
    //straight through it.
    public boolean SwitchStraight() {
        
        if (currentRail instanceof SwitchRail) {
            return true;
        }
        return false;
    }

    // Return true if the current Rail is a SwitchRail and I am going
    // around a corner.
    public boolean SwitchCorner() {
        
        if (currentRail instanceof SwitchRail) {
            return false;
        }
        return false;

    }

    // Redraw myself.
    public void draw(Graphics g) {

        GridLoc myLoc = currentRail.location;
        Rectangle b = currentRail.bounds();
        
        // the polygon to draw on the screen.
        Polygon p;
        
        double width = b.width;
        double height = b.height;
        
        int sqrtOfHypotenuse = (int) Math.sqrt((width*width/4.0) + (height*height/4.0));

        int[] xPoints = new int[5];
        int[] yPoints = new int[5];
        int nPoints = 5;
        
        if (currentRail instanceof StraightRail
            || currentRail instanceof CrossRail
            || SwitchStraight()) {
            if (dir.equals("north") || dir.equals("south")) {
                makeStraightPolygon(xPoints, yPoints);
            } else {
                makeStraightPolygon(yPoints, xPoints);
            }
        } else if (currentRail instanceof CornerRail || SwitchCorner()) {
            if (currentRail instanceof NERail
                || currentRail instanceof NSERail
                || currentRail instanceof EWNRail) {
                makeCornerPolygon(xPoints, yPoints, -sqrtOfHypotenuse,
                    sqrtOfHypotenuse, (int)width, (int)(width/2), (int)(height/2), 0);
            } else if (currentRail instanceof NWRail
                || currentRail instanceof NSWRail
                || currentRail instanceof WENRail) {
                makeCornerPolygon(xPoints, yPoints, sqrtOfHypotenuse,
                    sqrtOfHypotenuse, (int)(width/2), 0, 0, (int)(height/2));
            } else if (currentRail instanceof SERail
                || currentRail instanceof SNERail
                || currentRail instanceof EWSRail) {
                makeCornerPolygon(xPoints, yPoints, -sqrtOfHypotenuse,
                    -sqrtOfHypotenuse, (int)(width/2), (int)width, (int)height, (int)(height/2));
            } else if (currentRail instanceof SWRail
                || currentRail instanceof SNWRail
                || currentRail instanceof WESRail) {
                makeCornerPolygon(xPoints, yPoints, sqrtOfHypotenuse,
                    -sqrtOfHypotenuse, 0, (int)(width/2), (int)(height/2), (int)height);
            }
        }

        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, 5);
        
    }
    
    // The points, in order, are the back right of the car, the front right of the car,
    // the front left of the car, and the back left of the car.
    private void makeCornerPolygon(int[] xPoints, int[] yPoints,
        int xSideOffset, int ySideOffset, int x0Mod, int x1Mod, int y0Mod, int y1Mod) {

        GridLoc myLoc = currentRail.location;

        xPoints[0] = x0Mod;
        xPoints[1] = x1Mod;
        xPoints[2] = xPoints[1] + xSideOffset/2;
        xPoints[3] = xPoints[0] + xSideOffset/2;
        xPoints[4] = xPoints[0];

        yPoints[0] = y0Mod;
        yPoints[1] = y1Mod;
        yPoints[2] = yPoints[1] + ySideOffset/2;
        yPoints[3] = yPoints[0] + ySideOffset/2;
        yPoints[4] = yPoints[0];
    }
    
    
    private void makeStraightPolygon(int[] aPoints, int[] bPoints) {
        Rectangle b = currentRail.bounds();
        int width = b.width;
        int height = b.height;
        
        aPoints[0] = width/4;
        aPoints[1] = 3*width/4;
        aPoints[2] = 3*width/4;
        aPoints[3] = width/4;
        aPoints[4] = aPoints[0];

        bPoints[0] = height/8;
        bPoints[1] = height/8;
        bPoints[2] = 7*height/8;
        bPoints[3] = 7*height/8;
        bPoints[4] = bPoints[0];
    }

    public String toString() { return "Car"; };

}

