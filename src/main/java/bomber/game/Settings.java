package bomber.game;

import java.io.*;
import java.util.HashMap;

public class Settings {
    private final HashMap<String, String> valueMap;
    public Settings() {
        valueMap = new HashMap<>();
    }

    String get(String s) {
        return this.valueMap.get(s);
    }

    int getInt(String s) {
        // Todo: should not do this every time maybe...
        return Integer.parseInt(this.valueMap.get(s));
    }

    boolean isTrue(String setting) {
        String value = valueMap.get(setting);
        return value != null && value.equalsIgnoreCase("true");
    }

    private void addSettingsFromReader(BufferedReader reader) throws IOException {
        String line;
        while ( (line = reader.readLine()) != null) {
            String[] parts = line.split(" ?[:=] ?",2);
            if (line.startsWith("//") || line.startsWith("#")
                    || parts.length < 2) continue;
            this.valueMap.put(parts[0], parts[1]);
        }
    }

    public void addSettingsFromFile(String file) throws IOException {
        InputStream stream = Settings.class.getClassLoader()
                .getResourceAsStream(file);
        if (stream == null) {
            throw new FileNotFoundException(file);
        }
        BufferedReader r = new BufferedReader(
                new InputStreamReader(stream));
        addSettingsFromReader(r);
    }
}
