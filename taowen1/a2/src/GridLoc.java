// The (x,y) location on the Track.
public class GridLoc {
    public int row;
    public int col;
    
    public GridLoc(int r, int c) {
        row = r;
        col = c;
    }

    public String toString() { return (row + " " + col); };
}

