package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Solution implements Serializable{
    private static final long serialVersionUID = 1L;

    private final ArrayList<AState> solutionPath;

    public Solution(List<AState> path) {
        // Copy into an ArrayList so getSolutionPath() returns exactly that
        this.solutionPath = new ArrayList<>(path);
    }

    public ArrayList<AState> getSolutionPath() {
        return solutionPath;
    }
}