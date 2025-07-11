package src.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Creates a .txt file which can later be loaded using BreadBoardFileLoader
 */
public class FileCreator {

    private String s100by1t = "__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__,__";
    private String s100by1d = "dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN";

    final static int X_BYTES = 2;
    final static int Y_BYTES = 2;
    final static int Z_BYTES = 2;
    final static int SUBLAYERS = 3;//tiles, dir1, dir2

    private static String fileName;

    public FileCreator(final int w, final int h, final int d, final String fileName) {
        this.fileName = fileName;
        if(createFile() == 0) {
            writeToFileBytes(w, h, d);
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
                return 0;
            } else {
                System.out.println("File already exists.");
                return 1;
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

    public static void saveToFile(final String s, final String fileName){
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(s);
            //System.out.println("Saved breadboard to " + fileName + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Byte version of writeToFile
     * @param w
     * @param h
     * @param d
     */
    public void writeToFileBytes(final int w, final int h, final int d) {
        System.out.println("Writing to file with bytes.");
        int length = X_BYTES + Y_BYTES + Z_BYTES + SUBLAYERS * w * h * d;
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


        System.out.println("done calculating what to output");

        try {
            saveToFileBytes(b, fileName);
            System.out.println("File wrote to.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void writeToFile(final int w, final int h, final int d) {
        System.out.println("Writing to file.");
        String s = "";

        s += w + "," + h + "," + d + "\n";
        for (int i = 0; i < d; i++) {
            s += "LAYER " + i + "\n";
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < h; k++) {
                    if(j == 0) {//actual gates "layer"
                        if(k == 0) s+= "TILES\n";
                        for (int l = 0; l < w; l++) {
                            s += TileString.BreadBoardEmpty.getSymbol();
                            s += ",";
                        }
                    }else {//direction "layer"
                        if(j == 1){
                            if(k == 0) s+= "DIR1\n"; //NO SPACE BETWEEN DIR AND 1 FOR NOW
                        }else {
                            if(k == 0) s+= "DIR2\n"; //NO SPACE BETWEEN DIR AND 2 FOR NOW
                        }
                        for (int l = 0; l < w; l++) {
                            s += Direction.NONE.getSymbol();
                            s += ",";
                        }
                    }
                    s = s.substring(0, s.length() - 1);
                    s += "\n";
                }
            }
            //s+="\n";
        }
        s = s.substring(0, s.length() - 1);//remove very last newline character

        System.out.println("done calculating what to output");

        try (FileWriter writer = new FileWriter("src/saves/testoutput.txt")) {
            writer.write(s);
            System.out.println("File wrote to.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Same as writeToFile but for multiples of 10 (don't use for multiples of 100)
     * @param w
     * @param h
     * @param d
     */
    public void writeToFile10(final int w, final int h, final int d) {
        System.out.println("Writing to file with a multiple of 10.");
        String s = "";

        s += w + "," + h + "," + d + "\n";
        for (int i = 0; i < d; i++) {
            s += "LAYER " + i + "\n";
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < h; k++) {
                    if(j == 0) {//actual gates "layer"
                        if(k == 0) s+= "TILES\n";
                        for (int l = 0; l < w; l++) {
                            s += TileString.BreadBoardEmpty.getSymbol();
                            s += ",";
                        }
                    }else {//direction "layer"
                        if(j == 1){
                            if(k == 0) s+= "DIR1\n"; //NO SPACE BETWEEN DIR AND 1 FOR NOW
                        }else {
                            if(k == 0) s+= "DIR2\n"; //NO SPACE BETWEEN DIR AND 2 FOR NOW
                        }
                        for (int l = 0; l < w; l++) {
                            s += Direction.NONE.getSymbol();
                            s += ",";
                        }
                    }
                    s = s.substring(0, s.length() - 1);
                    s += "\n";
                }
            }
            //s+="\n";
        }
        s = s.substring(0, s.length() - 1);//remove very last newline character

        System.out.println("done calculating what to output");

        try (FileWriter writer = new FileWriter("src/saves/testoutput.txt")) {
            writer.write(s);
            System.out.println("File wrote to.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Same as writeToFile but for multiples of 100
     * @param w
     * @param h
     * @param d
     */
    public void writeToFile100(int w, int h, final int d) {
        System.out.println("Writing to file with a multiple of 100.");
        String s = "";

        s += w + "," + h + "," + d + "\n";
        w/=100;//needs to be done
        //h/=100;
        for (int i = 0; i < d; i++) {
            s += "LAYER " + i + "\n";
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < h; k++) {
                    if(j == 0) {//actual gates "layer"
                        if(k == 0) s+= "TILES\n";
                        for (int l = 0; l < w; l++) {
                            s += s100by1t;
                            s += ",";
                        }
                    }else {//direction "layer"
                        if(j == 1){
                            if(k == 0) s+= "DIR1\n"; //NO SPACE BETWEEN DIR AND 1 FOR NOW
                        }else {
                            if(k == 0) s+= "DIR2\n"; //NO SPACE BETWEEN DIR AND 2 FOR NOW
                        }
                        for (int l = 0; l < w; l++) {
                            s += s100by1d;
                            s += ",";
                        }
                    }
                    s = s.substring(0, s.length() - 1);
                    s += "\n";
                }
            }
            //s+="\n";
        }
        s = s.substring(0, s.length() - 1);//remove very last newline character

        System.out.println("done calculating what to output");

        try (FileWriter writer = new FileWriter("src/saves/testoutput.txt")) {
            writer.write(s);
            System.out.println("File wrote to.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
