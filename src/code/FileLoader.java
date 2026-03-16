package src.code;

import src.code.Enums.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static src.code.FileCreator.*;

public class FileLoader {

    private final byte[] fileContent;
    //public static short NUMBER_OF_TELEPORT_WIRES = 0;
    //FUTURE id (i.e. current number of pairs + 1)
    private static short id = 0;

    public FileLoader(Path path) {
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
        short depth = dim[2];

        System.out.println("FileLoader.load(): width = " + width + ", height = " + height + ", depth = " + depth);

        System.out.println("FileLoader.load(): dimensions array: "+Arrays.toString(dimensions()));

        byte[][][] tiles = new byte[depth][height][width];
        byte[][][] dir1Raw = new byte[depth][height][width];
        byte[][][] dir2Raw = new byte[depth][height][width];
        byte[][] teleports = new byte[TOTAL_NUMBER_OF_TELEPORT_WIRES][INFO_PER_TELEPORT];

        byte tile = -128;
        for (int sublayer = 0; sublayer < FileCreator.SUBLAYERS; sublayer++) {
            if(sublayer == 0) {
                for(int i = 0; i < depth; i++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            tile = fileContent[6 + sublayer * depth * height * width + i * height * width + j * width + k];
                            tiles[i][j][k] = tile;
                        }
                    }
                }
            }else if(sublayer == 1) {
                for(int i = 0; i < depth; i++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            tile = fileContent[6 + sublayer * depth * height * width + i * height * width + j * width + k];
                            dir1Raw[i][j][k] = tile;
                        }
                    }
                }
            }else if(sublayer == 2) {
                for(int i = 0; i < depth; i++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            tile = fileContent[6 + sublayer * depth * height * width + i * height * width + j * width + k];
                            dir2Raw[i][j][k] = tile;
                        }
                    }
                }
            }
        }

        byte datum = -128;
        for (int tw = 0; tw < TOTAL_NUMBER_OF_TELEPORT_WIRES; tw++) {
            for (int datumNo = 0; datumNo < INFO_PER_TELEPORT; datumNo++) {
                datum = fileContent[6+ FileCreator.SUBLAYERS * depth * height * width + tw * INFO_PER_TELEPORT + datumNo];
                teleports[tw][datumNo] = datum;
                //WARNING: TELEPORT BLOCKS MUST BE PERFECTLY PLACED IN ORDER FOR THIS TO WORK
                //MUST GO TP1, TP0, TP1, TP0, ETC.
                //check if tw is odd, and the id place is not equal to 0.
                if(tw % 2 == 1 && datumNo == 7 && datum != 0){
                    id++;
                }
            }
        }


        Direction[][][] dir1 = new Direction[depth][height][width];
        Direction[][][] dir2 = new Direction[depth][height][width];
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    dir1[z][y][x] = Direction.fromSymbol(dir1Raw[z][y][x]);
                    dir2[z][y][x] = Direction.fromSymbol(dir2Raw[z][y][x]);
                }
            }
        }


        board.setBreadBoardStateByteInitial(tiles, dir1, dir2, teleports, id);
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