package algorithms.maze3D;

import algorithms.search.AState;

public class Maze3DState extends AState {
    private final Position3D pos;

    public Maze3DState(Position3D p,int cost){ super(cost); this.pos=p; }

    /**
     * a getter function for the 3D position
     *
     * @return the Position3D object representing the position of this Maze3DState
     */
    public Position3D getPosition(){ return pos; }

    /**
     * Compares this Maze3DState with the specified object for equality.
     *
     * @param object the object to be compared with this Maze3DState for equality
     * @return true if the specified object is equal to this Maze3DState; false otherwise
     */
    @Override public boolean equals(Object object){
        if (object instanceof Maze3DState) {
            Maze3DState s = (Maze3DState) object;
            return pos.equals(s.pos);
        }
        return false;
    }


    @Override public int hashCode(){ return pos.hashCode(); }

    @Override public String toString(){ return pos.toString(); }
}
