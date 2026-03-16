package src.code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Creates a .txt file which can later be loaded using FileLoader
 */
public class FileCreator {

    final static int X_BYTES = 2;
    final static int Y_BYTES = 2;
    final static int Z_BYTES = 2;
    final static int SUBLAYERS = 3;//tiles, dir1, dir2
    final public static int TOTAL_NUMBER_OF_TELEPORT_WIRES = 300;
    final public static int INFO_PER_TELEPORT = 8;//2 each for X,Y,Z and two for ID (teleportNumber is stored in Direction1)

    private final int FALSE = 0;
    private final int TRUE = 1;

    final public static byte TELEPORT_INFO_DUMMY_VALUE = 0;

    private final String fileName;

    public FileCreator(final int w, final int h, final int d, final String fileName) {
        this.fileName = fileName;
        if(createFile() == FALSE) {
            writeToFileInitially(w, h, d);
        }
//        if(w % 100 == 0 && h % 100 == 0){
//            writeToFile100(w, h, d);
//        }else if(w % 10 == 0 && h % 10 == 0){
//            writeToFile10(w, h, d);
//        }else {
//            writeToFile(w, h, d);
//        }
    }

    /**
     * Creates a file
     * @return if the file already exists, -1 if error
     */
    public int createFile() {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created.");
                return FALSE;
            } else {
                System.out.println("File already exists.");
                return TRUE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static void saveToFileBytes(final byte[] b, final String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.write(path, b);
    }

    /**
     * Populate the BRAND NEW file with zeros.
     * @param w
     * @param h
     * @param d
     */
    public void writeToFileInitially(final int w, final int h, final int d) {
        System.out.println("Writing to file with bytes.");
        int length = X_BYTES + Y_BYTES + Z_BYTES + SUBLAYERS * w * h * d + TOTAL_NUMBER_OF_TELEPORT_WIRES * INFO_PER_TELEPORT;
        byte [] b = new byte[length];

        b[0] = (byte) (w / 128);
        b[1] = (byte) (w % 128);
        b[2] = (byte) (h / 128);
        b[3] = (byte) (h % 128);
        b[4] = (byte) (d / 128);
        b[5] = (byte) (d % 128);

        for (int i = 0; i < FileCreator.SUBLAYERS; i++) {
            for (int j = 0; j < d; j++) {
                if(i == 0){
                    for (int k = 0; k < h; k++) {
                        for (int l = 0; l < w; l++) {
                            b[6+ i * d * h * w + j * h * w + k * w + l] = 0;
                        }
                    }
                }else {
                    for (int k = 0; k < h; k++) {
                        for (int l = 0; l < w; l++) {
                            b[6+ i * d * h * w + j * h * w + k * w + l] = 0;
                        }
                    }
                }
            }
        }

        for (int tw = 0; tw < TOTAL_NUMBER_OF_TELEPORT_WIRES; tw++) {
            for (int datum = 0; datum < INFO_PER_TELEPORT; datum++) {
                b[6+ (FileCreator.SUBLAYERS - 1) * d * h * w + (d-1) * h * w + (h-1) * w + (w-1)
                        + tw * INFO_PER_TELEPORT + datum] = TELEPORT_INFO_DUMMY_VALUE;
            }
        }

        System.out.println("done calculating what to output");

        try {
            saveToFileBytes(b, fileName);
            System.out.println("File wrote to.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
