import java.util.LinkedList;

/**
 * @author BorisMirage
 * Time: 2018/07/26 20:49
 * Created with IntelliJ IDEA
 */

public class Maze {

    public static final boolean FREE = false;
    public static final boolean WALL = true;

    private int row;
    private int column;
    private MazeCoord entry;
    private MazeCoord exit;
    private int[][] data;

    private LinkedList<MazeCoord> path = new LinkedList<>();
    private int exitRow;
    private int exitCol;
    private int[][] visitTimes;      // Record coord visited times


    /**
     * Necessary data that to construct a maze.
     *
     * @param mazeData 2D int array that store the info of maze (wall, space, distance).
     * @param startLoc MazeCoord start location
     * @param exitLoc  MazeCoord exit location
     */
    public Maze(int[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) {

        row = mazeData.length;
        column = mazeData[0].length;
        entry = startLoc;
        exit = exitLoc;
        data = mazeData;
//        int entryRow = entry.getRow();
//        int entryCol = entry.getCol();
        exitRow = exit.getRow();
        exitCol = exit.getCol();
        visitTimes = new int[numRows()][numCols()];
    }

    /**
     * Get number of rows in maze.
     *
     * @return number of rows in maze
     */
    public int numRows() {
        return row;
    }


    /**
     * Get number of columns in maze.
     *
     * @return number of columns in maze.
     */
    public int numCols() {
        return column;
    }

    /**
     * Check if input location has wall.
     *
     * @param loc input location
     * @return true if input location has wall, otherwise return false.
     */
    public boolean hasWallAt(MazeCoord loc) {

        /* FREE = false; WALL = true; */
        return data[loc.getRow()][loc.getCol()] == -1;
    }

    /**
     * Get entry MazeCoord.
     *
     * @return entry MazeCoord
     */
    public MazeCoord getEntryLoc() {

        return entry;
    }

    /**
     * Get exit MazeCoord.
     *
     * @return exit MazeCoord
     */
    public MazeCoord getExitLoc() {
        return exit;
    }

    /**
     * Get represented value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @return represented value store in 2D int array mazeData
     */
    private int getData(MazeCoord coord) {
        return data[coord.getRow()][coord.getCol()];
    }

    /**
     * Get value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @param value value to be set
     */
    private void setData(MazeCoord coord, int value) {
        data[coord.getRow()][coord.getCol()] = value;
    }

    /**
     * Add one to represented coord in visitTimes.
     *
     * @param coord input coord
     */
    private void addVisit(MazeCoord coord) {
        visitTimes[coord.getRow()][coord.getCol()] += 1;
    }

    /**
     * Get path from entry to exit.
     *
     * @return path from entry to exit
     */
    public LinkedList<MazeCoord> getPath() {

        return path;
//        LinkedList<MazeCoord> cacheList = new LinkedList<>();
//        LinkedList<MazeCoord> resultList;
//
//        /* Reverse to correct position when this path is reversed */
//        if (path.size() != 0) {
//            if (path.getFirst().getRow() != entryRow && path.getFirst().getCol() != entryCol) {
//                for (int i = path.size() - 1; i >= 0; i--) {
//                    cacheList.add(path.get(i));
//                }
//                path = cacheList;
//            }
//
//            /* Add exit coord to path last */
//            path.add(exit);
//
//            /* Make this path immutable */
//            resultList = path;
//            return resultList;
//        } else {
//            resultList = path;
//            return resultList;
//        }
    }

    public boolean search() {

        /* One-element maze */
        if (entry == exit) {
            path.add(exit);
            return true;
        }
        if (hasWallAt(entry) || hasWallAt(exit)) {
            return false;
        }
        setData(entry, 1);
        findPath(entry);
        if (data[exitRow][exitCol] != Integer.MAX_VALUE) {
            tracePath(exit);
            return true;
        } else {
            return false;
        }
    }

    private void tracePath(MazeCoord pos) {
        path.add(pos);
        if (pos.getRow() != entry.getRow() || pos.getCol() != entry.getCol()) {
            tracePath(findMinNext(pos));
        }
    }

    private MazeCoord findMinNext(MazeCoord cur) {
        int min = getData(cur);
        MazeCoord next = cur;
        for (int i = 1; i < 5; i++) {
            if (isAvailable(move(cur, i), visitTimes) > -1 && getData(move(cur, i)) < min) {
                min = getData(move(cur, i));
                next = move(cur, i);
            }
        }
        return next;
    }

    private void greedy(MazeCoord coord) {
        int coordData = getData(coord);
        if (isAvailable(move(coord, 1), visitTimes) > -1 && coordData + 1 < getData(move(coord, 1))) {
            setData(move(coord, 1), coordData + 1);
        }
        if (isAvailable(move(coord, 2), visitTimes) > -1 && coordData + 1 < getData(move(coord, 2))) {
            setData(move(coord, 2), coordData + 1);
        }
        if (isAvailable(move(coord, 3), visitTimes) > -1 && coordData + 1 < getData(move(coord, 3))) {
            setData(move(coord, 3), coordData + 1);
        }
        if (isAvailable(move(coord, 4), visitTimes) > -1 && coordData + 1 < getData(move(coord, 4))) {
            setData(move(coord, 4), coordData + 1);
        }
    }

    private void findPath(MazeCoord current) {
        greedy(current);
        MazeCoord next;
        if (isAvailable(move(current, 1), visitTimes) > 0) {
            next = move(current, 1);
            addVisit(next);
            setData(next, Math.min(getData(current) + 1, getData(next)));
            findPath(next);
        }
        if (isAvailable(move(current, 2), visitTimes) > 0) {
            next = move(current, 2);
            addVisit(next);
            setData(next, Math.min(getData(current) + 1, getData(next)));
            findPath(next);
        }
        if (isAvailable(move(current, 3), visitTimes) > 0) {
            next = move(current, 3);
            addVisit(next);
            setData(next, Math.min(getData(current) + 1, getData(next)));
            findPath(next);
        }
        if (isAvailable(move(current, 4), visitTimes) > 0) {
            next = move(current, 4);
            addVisit(next);
            setData(next, Math.min(getData(current) + 1, getData(next)));
            findPath(next);
        }
    }

    /**
     * Check if input position can be moved to.
     *
     * @param c           input coord
     * @param visitRecord 2D int array record visit times
     * @return -1 if out of bound or has wall, 0 if visited more than 4 times, or 1 if available.
     */
    private int isAvailable(MazeCoord c, int[][] visitRecord) {
        if (c.getRow() > numRows() - 1 || c.getRow() < 0 || c.getCol() > numCols() - 1 || c.getCol() < 0 || hasWallAt(c)) {
            return -1;
        } else if (visitRecord[c.getRow()][c.getCol()] > 5) {
            return 0;
        } else {
            return 1;
        }
    }

    private MazeCoord move(MazeCoord coord, int orientation) {

        if (orientation == 1) {

            /* Upward */
            int newRow = coord.getRow() - 1;
            int newColumn = coord.getCol();
            return new MazeCoord(newRow, newColumn);
        } else if (orientation == 2) {

            /* Downward */
            int newRow = coord.getRow() + 1;
            int newColumn = coord.getCol();
            return new MazeCoord(newRow, newColumn);
        } else if (orientation == 3) {

            /* Left */
            int newRow = coord.getRow();
            int newColumn = coord.getCol() - 1;
            return new MazeCoord(newRow, newColumn);
        } else if (orientation == 4) {

            /* Right */
            int newRow = coord.getRow();
            int newColumn = coord.getCol() + 1;
            return new MazeCoord(newRow, newColumn);
        } else {
            System.out.println("Orientation Error!");
            return coord;
        }
    }
}