import javax.swing.*;
import java.io.*;
import java.util.LinkedList;

/**
 * Main function of Maze
 * The test maze file path should be regarded as program argument.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:52
 * Created with IntelliJ IDEA
 */

public class StartMaze {

    private static final char WALL_CHAR = '1';
    private static final char FREE_CHAR = '0';

    /**
     * Main class that required maze file path as argument.
     *
     * @param args maze file path
     */
    public static void main(String[] args) {

        String fileDirectory = "./src/TestMaze";
        File testFile = new File(fileDirectory);
        LinkedList<String> filePath = new LinkedList<>();
        String[] nameList = testFile.list();
//        System.out.println(Arrays.toString(nameList));
        if (nameList != null) {
            for (String fileName : nameList) {
                if (!fileName.equals(".DS_Store")) {
                    String temp = fileDirectory + "/" + fileName;
                    filePath.add(temp);
                }
            }
        }

        /* Generate maze */
        try {
            while (filePath.size() != 0) {
                JFrame frame = readMazeFile(filePath.pop());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: File not found: " + fileDirectory);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
//        System.out.println(filePath);

    }

    /**
     * Read maze file and convert it to Maze data.
     * This method will read data in maze file and create a new MazeFrame object for display and Maze path searching.
     *
     * @param fileName maze file path
     * @return new MazeFrame for later usage
     * @throws IOException file does not find via given path
     */
    private static MazeFrame readMazeFile(String fileName) throws IOException {

        /* Create a File object and using Reader to read it. */
        File readMazeFile = new File(fileName);

        /* Create a input reader to read data and save to buffer. */
        InputStreamReader newReader = new InputStreamReader(new FileInputStream(readMazeFile));
        BufferedReader newBufferRead = new BufferedReader(newReader);
//        System.out.println(fileName);
        /* Read first line about new maze, convert to integer. */
        String[] para = newBufferRead.readLine().split(" ");

        int[][] readMazeData = new int[Integer.parseInt(para[0])][Integer.parseInt(para[1])];

        /* Read following lines. */
        for (int i = 0; i < Integer.parseInt(para[0]); i++) {
            String line = newBufferRead.readLine();
            for (int j = 0; j < line.length(); j++) {
                char current = line.charAt(j);

                /* Free = false, Wall = true.
                 *  Free = 0, Wall = 1. */
                if (current == WALL_CHAR) {
                    readMazeData[i][j] = -1;
                } else if (current == FREE_CHAR) {
                    readMazeData[i][j] = 0;
                }
            }
        }

        /* Read last two lines to obtain elements: entry and exit. */
        String[] startLine = newBufferRead.readLine().split(" ");
        String[] endLine = newBufferRead.readLine().split(" ");

        MazeCoord newMazeStart = new MazeCoord(Integer.parseInt(startLine[0]), Integer.parseInt(startLine[1]));
        MazeCoord newMazeExit = new MazeCoord(Integer.parseInt(endLine[0]), Integer.parseInt(endLine[1]));

        return new MazeFrame(readMazeData, newMazeStart, newMazeExit);
    }

}