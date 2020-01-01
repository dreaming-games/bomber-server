package bomber.field;

import bomber.general.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FieldParser {
    private GameField fromBMapFormat(InputStream in, String fileLogName) {
        // Stuff we wanna get about the map
        ArrayList<Point> spawnPoints = new ArrayList<>();
        ArrayList<byte[]> byteLines = new ArrayList<>();
        int byteLineLength = -1;

        // Read the stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String nextLine;
            for (int y = 0; (nextLine = reader.readLine()) != null; y++) {
                if (byteLineLength != -1 && byteLineLength != nextLine.length()) {
                    throw new RuntimeException("Lines had different sizes in .bmap " +
                            "input file '" + fileLogName + "' line " + y + " -> " + (y + 1));
                }
                byteLineLength = nextLine.length();
                byte[] line = new byte[byteLineLength];
                for (int x = 0; x < byteLineLength; x++) {
                    // Parse this character
                    char c = nextLine.charAt(x);

                    if (c >= '0' && c <= '9') {
                        // Player? Add spawn point and set place empty
                        spawnPoints.add(new Point(x, y));
                        line[x] = MapObject.EMPTY;
                        continue;
                    }
                    if (!MapObject.isMapObject((byte) c)) {
                        throw new RuntimeException("Unrecognized symbol " +
                                "in .bmap input: '" + c + "'");
                    }
                    line[x] = (byte) c;
                }
                byteLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the actual field
        return new GameField(byteLines, spawnPoints);
    }

    public GameField fromBMapFile(String file) {
        return fromBMapFormat(getClass().getClassLoader()
                .getResourceAsStream(file), file);
    }
}
