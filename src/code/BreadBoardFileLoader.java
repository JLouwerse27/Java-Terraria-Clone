package src.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class BreadBoardFileLoader {

    private final byte[] fileContent;

    public BreadBoardFileLoader(Path path) {
        try {
            fileContent = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(BreadBoard board) throws IOException {

        short [] dim = dimensions();

        short width = dim[0];
        short height = dim[1];
        short zHeight = dim[2];

        System.out.println("BreadBoardFileLoader.load(): width = " + width + ", height = " + height + ", zHeight = " + zHeight);

        System.out.println(Arrays.toString(dimensions()));

        byte[][][] tiles = new byte[zHeight][height][width];
        byte[][][] dir1Raw = new byte[zHeight][height][width];
        byte[][][] dir2Raw = new byte[zHeight][height][width];

        byte tile = -128;
        for (int thang = 0; thang < 3; thang++) {
            if(thang == 0) {
                for(int i = 0; i < zHeight; i++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            tile = fileContent[6 + thang * zHeight * height * width + i * height * width + j * width + k];
                            tiles[i][j][k] = tile;
                        }
                    }
                }
            }else if(thang == 1) {
                for(int i = 0; i < zHeight; i++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            tile = fileContent[6 + thang * zHeight * height * width + i * height * width + j * width + k];
                            dir1Raw[i][j][k] = tile;
                        }
                    }
                }
            }else if(thang == 2) {
                for(int i = 0; i < zHeight; i++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            tile = fileContent[6 + thang * zHeight * height * width + i * height * width + j * width + k];
                            dir2Raw[i][j][k] = tile;
                        }
                    }
                }
            }
        }


//        tile = fileContent[6];
//        System.out.println("Tile should be " + tile);
//        tiles[0][0][0] = tile;
//        System.out.println("Tiles is: " + Arrays.deepToString(tiles));
//        tile = fileContent[7];
//        dir1Raw[0][0][0] = tile;
//        tile = fileContent[8];
//        dir2Raw[0][0][0] = tile;


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


        board.setBreadBoardStateByte(tiles, dir1, dir2);
//        for (int i = 6; i < zHeight + 6; i++) {
//            for (int j = 6; j < height + 6; j++) {
//                for (int k = 6; k < width + 6; k++) {
//                    tiles[z][j][k] = tile;
//                }
//            }
//        }

//        for (String line : lines) {
//            line = line.trim();
//            if (line.isEmpty()) continue;
//
//            if (line.startsWith("LAYER")) {
//                z++;
//                y = 0;
//                continue;
//            }
//            if (line.equals("TILES") || line.equals("DIR1") || line.equals("DIR2")) {
//                section = line;
//                y = 0;
//                continue;
//            }
//
//            String[] parts = line.split(",", -1);
//            for (int x = 0; x < width; x++) {
//                switch (section) {
//                    case "TILES":
//                        tiles[z][y][x] = parts[x];
//                        break;
//                    case "DIR1":
//                        dir1Raw[z][y][x] = parts[x];
//                        break;
//                    case "DIR2":
//                        dir2Raw[z][y][x] = parts[x];
//                        break;
//                }
//            }
//            y++;
//        }
//
//        // Convert string directions to enum
//        Direction[][][] dir1 = new Direction[zHeight][height][width];
//        Direction[][][] dir2 = new Direction[zHeight][height][width];
//        for (int zz = 0; zz < zHeight; zz++) {
//            for (int yy = 0; yy < height; yy++) {
//                for (int xx = 0; xx < width; xx++) {
//                    dir1[zz][yy][xx] = Direction.fromSymbol(dir1Raw[zz][yy][xx]);
//                    dir2[zz][yy][xx] = Direction.fromSymbol(dir2Raw[zz][yy][xx]);
//                }
//            }
//        }
//
//        board.setBreadBoardState(tiles, dir1, dir2);
//        System.out.println("BreadBoardFileLoader.load(): called setBreadBoardState()");
    }

    public short[] dimensions() throws IOException {
        short[] dims = {-1,-1,-1};

        dims[0] = (short) ((fileContent[0]) * 128 + (fileContent[1]));//x value is seperated into two bytes
        dims[1] = (short) (fileContent[2] * 128 + fileContent[3]);//y value is seperated into two bytes
        dims[2] = (short) (fileContent[4] * 128 + fileContent[5]);//z value is seperated into two bytes

        short width = dims[0];
        short height = dims[1];
        short zHeight = dims[2];

        return new short[]{
                width,
                height,
                zHeight
        };
    }
}