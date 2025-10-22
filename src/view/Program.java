package view;

import model.Lexer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.util.stream.Stream;

import static resources.Configuration.*;

public class Program {
    public static void main(String[] args) {
        var files = getTestFiles();
        var codes = getCodeFromFiles(files);
        var lexer = new Lexer();

        codes.forEach(code -> {
            System.out.println(BLUE + "Test #%d".formatted(codes.indexOf(code)) + RESET);
            lexer.processCode(code).forEach(token -> {
                System.out.println("Token: %s".formatted(token.toString()));
            });
            lexer.reset();
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
            System.err.println("Error: %s".formatted(ex.getMessage()));
            return List.of();
        }
    }

    private static List<String> getCodeFromFiles(List<Path> files) {
        List<String> codes = new ArrayList<>();
        //For reading distinct codes in the same file
        var currentCode = new StringBuilder();
        files.forEach(file ->{
            try(Stream<String> lines = Files.lines(file)){
                lines.forEach(line -> {
                    line = line.trim();
                    //We assume that the codes always start with either main func or a pre-processing police
                    if(line.contains("main()") || line.startsWith("#")){
                        if(!currentCode.isEmpty() && currentCode.toString().contains("main()")){
                            codes.add(currentCode.toString());
                            //Cleaning the StringBuilder to reuse it
                            currentCode.setLength(0);
                        }
                        currentCode.append(line);

                    }
                    else currentCode.append(line);
                });
                codes.add(currentCode.toString());
            } catch (IOException ex){
                System.err.println("Error: %s".formatted(ex.getMessage()));
            }
        });
        return codes;
    }
}
