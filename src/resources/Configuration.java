package resources;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {
    public static String PROJECT_PATH = System.getProperty("user.dir");
    public static Path TEST_FILES_PATH = Paths.get(PROJECT_PATH, "src", "resources", "test_files");
    public static String EXT_TXT = ".txt";

    public static String MAGENTA = "\u001B[1;35m";
    public static String GREEN = "\u001B[32m";
    public static String RESET = "\u001B[0m";
}
