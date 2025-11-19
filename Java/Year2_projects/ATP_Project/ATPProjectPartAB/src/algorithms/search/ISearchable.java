package algorithms.search;

import java.util.List;

public interface ISearchable {
    AState getStartingState();
    AState getGoalState();
    List<AState> getAllPossibleStates(AState state);
}


