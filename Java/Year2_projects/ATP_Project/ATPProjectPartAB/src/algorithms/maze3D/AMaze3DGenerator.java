package algorithms.maze3D;

public abstract class AMaze3DGenerator implements IMaze3DGenerator {
    @Override
    public long measureAlgorithmTimeMillis(int depth,int rows,int columns){
        long startTime=System.currentTimeMillis();
        generate(depth,rows,columns);
        return System.currentTimeMillis()-startTime;
    }
}
