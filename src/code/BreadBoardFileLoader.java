package src.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
public class BreadBoardFileLoader {

    public static void load(BreadBoard board, Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        int[] dims = dimensions(path);
        int width = dims[0];
        int height = dims[1];
        int zHeight = dims[2];

        String[][][] tiles = new String[zHeight][height][width];
        String[][][] dir1Raw = new String[zHeight][height][width];
        String[][][] dir2Raw = new String[zHeight][height][width];

        int z = -1;
        String section = "";
        int y = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("LAYER")) {
                z++;
                y = 0;
                continue;
            }
            if (line.equals("TILES") || line.equals("DIR1") || line.equals("DIR2")) {
                section = line;
                y = 0;
                continue;
            }

            String[] parts = line.split(",", -1);
            for (int x = 0; x < width; x++) {
                switch (section) {
                    case "TILES":
                        tiles[z][y][x] = parts[x];
                        break;
                    case "DIR1":
                        dir1Raw[z][y][x] = parts[x];
                        break;
                    case "DIR2":
                        dir2Raw[z][y][x] = parts[x];
                        break;
                }
            }
            y++;
        }

        // Convert string directions to enum
        Direction[][][] dir1 = new Direction[zHeight][height][width];
        Direction[][][] dir2 = new Direction[zHeight][height][width];
        for (int zz = 0; zz < zHeight; zz++) {
            for (int yy = 0; yy < height; yy++) {
                for (int xx = 0; xx < width; xx++) {
                    dir1[zz][yy][xx] = Direction.fromSymbol(dir1Raw[zz][yy][xx]);
                    dir2[zz][yy][xx] = Direction.fromSymbol(dir2Raw[zz][yy][xx]);
                }
            }
        }

        board.setBreadBoardState(tiles, dir1, dir2);
        System.out.println("BreadBoardFileLoader.load(): called setBreadBoardState()");
    }

    public static int[] dimensions(Path path) throws IOException {
        String firstLine = Files.lines(path).findFirst().orElseThrow();
        String[] parts = firstLine.split(",");
        return new int[]{
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim())
        };
    }
}