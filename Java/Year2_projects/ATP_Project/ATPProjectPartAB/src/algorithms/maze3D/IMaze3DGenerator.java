package algorithms.maze3D;

public interface IMaze3DGenerator {
    Maze3D generate(int depth,int rows,int columns);
    long   measureAlgorithmTimeMillis(int depth,int rows,int columns);
}
