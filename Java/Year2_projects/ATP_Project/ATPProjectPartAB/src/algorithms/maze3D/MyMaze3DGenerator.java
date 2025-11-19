package algorithms.maze3D;

import java.util.*;

/**
 * Uses a similar approach to the 2D version but with 6 possible directions
 * instead of 4. The algorithm:
 * 1. Fills the entire 3D grid with walls
 * 2. Picks a random starting point on the surface
 * 3. Uses DFS to carve paths through the maze
 * 4. Selects a goal point on the surface
 */
public class MyMaze3DGenerator extends AMaze3DGenerator {
    /**
     * Creates a new 3D maze using randomized DFS.
     * 
     * @param depth number of layers in the z-direction
     * @param rows number of rows in each layer
     * @param cols number of columns in each layer
     * @return the generated 3D maze
     */
    @Override
    public Maze3D generate(int depth,int rows,int cols){
        Maze3D maze = new Maze3D(depth,rows,cols);
        for(int z=0;z<depth;z++)
            for(int r=0;r<rows;r++)
                for(int c=0;c<cols;c++)
                    maze.set(z,r,c,1);

        Random rnd=new Random();
        int face=rnd.nextInt(6);
        int sd= (face==4)? depth-1 : (face==5?0:rnd.nextInt(depth));
        int sr= (face==0)?0 :(face==1?rows-1:rnd.nextInt(rows));
        int sc= (face==2)?0 :(face==3?cols-1:rnd.nextInt(cols));
        maze.setStartPosition(sd,sr,sc);
        maze.set(sd,sr,sc,0);

        boolean[][][] visited = new boolean[depth][rows][cols];
        visited[sd][sr][sc]=true;
        int[][] DIRS = {{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
        Deque<int[]> st=new ArrayDeque<>();
        st.push(new int[]{sd,sr,sc});

        while(!st.isEmpty()){
            int[] cur=st.peek();
            List<int[]> nbr=new ArrayList<>();
            for(int[] d:DIRS){
                int nd=cur[0]+d[0]*2, nr=cur[1]+d[1]*2, nc=cur[2]+d[2]*2;
                if(nd>=0&&nd<depth&&nr>=0&&nr<rows&&nc>=0&&nc<cols&&!visited[nd][nr][nc])
                    nbr.add(new int[]{nd,nr,nc});
            }
            if(nbr.isEmpty()){ st.pop(); continue; }
            int[] nxt=nbr.get(rnd.nextInt(nbr.size()));
            int midD=(cur[0]+nxt[0])/2, midR=(cur[1]+nxt[1])/2, midC=(cur[2]+nxt[2])/2;
            maze.set(midD,midR,midC,0); maze.set(nxt[0],nxt[1],nxt[2],0);
            visited[nxt[0]][nxt[1]][nxt[2]]=true;
            st.push(nxt);
        }

        List<int[]> cand=new ArrayList<>();
        for(int z=0;z<depth;z++)
            for(int r=0;r<rows;r++)
                for(int c=0;c<cols;c++)
                    if(maze.get(z,r,c)==0 && isSurface(z,r,c,depth,rows,cols) &&
                            !(z==sd&&r==sr&&c==sc))
                        cand.add(new int[]{z,r,c});
        int[] g = cand.isEmpty()? new int[]{depth-1,rows-1,cols-1}
                : cand.get(rnd.nextInt(cand.size()));
        maze.setGoalPosition(g[0],g[1],g[2]);
        maze.set(g[0],g[1],g[2],0);
        return maze;
    }

    /**
     * Determines whether the given position in a 3D space is on the surface.
     * A position is considered to be on the surface if it is at the boundary
     * of the 3D dimensions (depth, rows, columns).
     *
     * @param d the current depth index of the position
     * @param r the current row index of the position
     * @param c the current column index of the position
     * @param D the total number of depth layers in the space
     * @param R the total number of rows in the space
     * @param C the total number of columns in the space
     * @return true if the position (d, r, c) is on the surface, false otherwise
     */
    private boolean isSurface(int d,int r,int c,int D,int R,int C){
        return d==0||d==D-1||r==0||r==R-1||c==0||c==C-1;
    }
}
