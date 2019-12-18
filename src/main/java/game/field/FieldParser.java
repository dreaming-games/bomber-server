package game.field;

import game.general.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FieldParser {
    private byte parseChar(char c, ArrayList<Point> spawnPoints, Point loc) {
        switch (c) {
            case '#':
                // A wall
                return 1;
            case '.':
                // A (empty) crate
                return 10;
            case ' ':
                // Empty space
                return 0;
            default:
                if (c >= '0' && c <= '9') {
                    // Player? Add spawn point and set place empty
                    spawnPoints.add(loc);
                    return 0;
                }

                // Check for upgrade crates
                throw new RuntimeException("Unrecognized symbol " +
                        "in .bmap input: '" + c + "'");
        }
    }

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
                    line[x] = parseChar(nextLine.charAt(x),
                            spawnPoints, new Point(x, y));
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
        return fromBMapFormat(getClass().getClassLoader().getResourceAsStream(file), file);
    }
}
