package bomber.field;

public class MapObject {
    public static final byte EMPTY   = (byte) ' ';
    public static final byte WALL    = (byte) '#';
    public static final byte CRATE   = (byte) '.';

    private static final byte[] objects = { EMPTY, WALL, CRATE };
    static boolean isMapObject(byte b) {
        for (byte o : objects) {
            if (o == b) return true;
        }
        return false;
    }
}
