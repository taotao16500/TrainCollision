/*

The EmptyRail class.  This is a place on the Track which does not have an actual
piece of track.

*/

import java.awt.*;

class EmptyRail extends Rail {

    // Return true if d is a valid direction for me.
    public boolean exitOK(Direction d) { return false; }
        
    // Register that Rail r is in Direction d.
    public void register(Rail r, Direction d) {}

    // Register that there is no Rail in Direction d.
    public void unRegister(Direction d) {}

    // Given that d is the Direction from which a Car entered,
    // report where the Car will exit.
    public Direction exit(Direction d) { return null; }

    // Given that d is the Direction from which a Car entered,
    // report which Rail is next.
    public Rail nextRail(Direction d) { return null; }

}

