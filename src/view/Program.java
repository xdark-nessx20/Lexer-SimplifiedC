package view;

import model.Lexer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.Files;

import static resources.Configuration.*;

public class Program {
    public static void main(String[] args) {
        var files = getTestFiles();
        List<String> codes = getCodeFromFiles(files);

        codes.forEach(code -> {
            Lexer.lexer(code).forEach(token -> {
                var tokenStr = "Token: %s".formatted(token.toString());
                System.out.println(tokenStr);
            });
            System.out.println();
        });
    }

    private static List<Path> getTestFiles() {
        //try-with-resources open and close the stream at finish no matter what
        try(var files = Files.list(TEST_FILES_PATH)) {
            //We just need files, no dirs, and the program only allows .txt files
            return files.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(EXT_TXT))
                    .toList();
        } catch (IOException ex){
            System.out.println("Error: %s".formatted(ex.getMessage()));
            return List.of();
        }
    }

    private static List<String> getCodeFromFiles(List<Path> files) {
        return files.stream()
                .map(file -> {
                    try{
                        return Files.readString(file);
                    } catch (IOException ex){
                        System.out.println("Error reading \"%s\" : %s".formatted(file.toString(), ex.getMessage()));
                        return "";
                    }
                }).toList();
    }
}
