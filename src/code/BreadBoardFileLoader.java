import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BreadBoardFileLoader {

    public static void load(BreadBoard board, Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);

//        String[] dims = lines.get(0).split(",");
//        int width = Integer.parseInt(dims[0].trim());
//        int height = Integer.parseInt(dims[1].trim());
        lines.remove(0);

        List<String[]> tileLines = new ArrayList<>();
        List<String[]> dir1Lines = new ArrayList<>();
        List<String[]> dir2Lines = new ArrayList<>();

        List<String[]> current = tileLines;
        for (String line : lines) {
            line = line.trim();
            if (line.equals("DIR1")) {
                current = dir1Lines;
            } else if (line.equals("DIR2")) {
                current = dir2Lines;
            } else if (!line.isEmpty() && !line.equals("TILES") && !line.equals("WIDTH") && !line.equals("HEIGHT")) {
                current.add(line.replace("\"", "").split(",", -1));
            }
        }

        int h = tileLines.size();
        int w = tileLines.get(0).length;

        Direction[][] dir1 = new Direction[h][w];
        Direction[][] dir2 = new Direction[h][w];
        String[][] tiles = new String[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tiles[i][j] = tileLines.get(i)[j];
                dir1[i][j] = Direction.fromSymbol(dir1Lines.get(i)[j]);
                dir2[i][j] = Direction.fromSymbol(dir2Lines.get(i)[j]);
            }
        }

        board.setBreadBoardState(tiles, dir1, dir2);
    }

    public static int[] dimensions(Path path) throws IOException {
        String firstLine = Files.lines(path).findFirst().orElseThrow();
        String[] parts = firstLine.split(",");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());
        return new int[]{width, height};
    }
}