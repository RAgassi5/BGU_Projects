package test;

import algorithms.maze3D.IMaze3DGenerator;
import algorithms.maze3D.MyMaze3DGenerator;
import algorithms.maze3D.Maze3D;
import algorithms.maze3D.Position3D;


public class RunMaze3DGenerator {

    public static void main(String[] args) {
        testMazeGenerator(new MyMaze3DGenerator());
    }


    private static void testMazeGenerator(IMaze3DGenerator generator) {
        System.out.printf("3D-maze generation time(ms): %d%n",
                generator.measureAlgorithmTimeMillis(100, 100, 100));


        Maze3D maze = generator.generate(5, 5, 5);
        maze.print();

        Position3D start = maze.getStartPosition();
        System.out.printf("Start Position: %s%n", start);
        System.out.printf("Goal  Position: %s%n", maze.getGoalPosition());
    }
}