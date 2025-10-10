package model;

import java.util.ArrayList;
import java.util.List;
import static model.TokenMapper.TOKENS_MAP;

public class Lexer {
    private final List<Token> tokens;
    private int index;
    private char currentChar;

    public Lexer(){
        tokens = new ArrayList<>();
        index = 0;
    }

    public List<Token> processCode(String code){
        var code_length = code.length();

        //Start to process the code
        while(index < code_length){
            currentChar = code.charAt(index);

            //We don't need whitespaces
            while (Character.isWhitespace(currentChar)){
                if (++index >= code_length) return tokens;
                currentChar = code.charAt(index);
            }

            //To start verifying keywords or identifiers
            if (Character.isLetter(currentChar) || currentChar == '_' || currentChar == '$'){
                processKeywordsAndIdentifiers(code, code_length);
                continue;
            }

            //To start verifying numbers
            if (Character.isDigit(currentChar)){
                processNumbers(code, code_length);
                continue;
            }

            //To start verifying pre-processing polices
            if (currentChar == '#'){
                processPolices(code, code_length);
                continue;
            }

            //To start verifying escape chars
            if (currentChar == '\\'){
                processEscChars(code, code_length);
                continue;
            }

            //To start verifying symbols
            processSymbols(code, code_length);
        }
        return tokens;
    }

    private void processKeywordsAndIdentifiers(String code, int code_len){
        //Better for manipulating strings
        var id = new StringBuilder();
        do {
            id.append(currentChar);
            currentChar = code.charAt(++index);
        } while (index < code_len && Character.isLetterOrDigit(currentChar) || currentChar == '_'
                || currentChar == '$'); //C also admits '$' as an ID

        var idStr = id.toString();
        //Ask if id is a keyword but id is an identifier
        TokenType type = TOKENS_MAP.getOrDefault(idStr, TokenType.ID);
        tokens.add(new Token(idStr, type));
    }

    private void processNumbers(String code, int code_len){
        var number = new StringBuilder();
        do {
            number.append(currentChar);
            currentChar = code.charAt(++index);
        } while (index < code_len && Character.isDigit(currentChar));

        tokens.add(new Token(number.toString(), TokenType.INT_NUM));
    }

    private void processPolices(String code, int code_len){
        var police = new StringBuilder();
        do {
            police.append(currentChar);
            currentChar = code.charAt(++index);
        } while (index < code_len && Character.isLetter(currentChar));

        var policeStr = police.toString();
        //A pre-processing police can't be an ID. So, if the police's not found, we return TokenType NOTFOUND
        TokenType type = TOKENS_MAP.getOrDefault(policeStr, TokenType.NOT_FOUND);
        tokens.add(new Token(policeStr, type));
    }

    private void processEscChars(String code, int code_len){
        var esc = String.valueOf(currentChar) + code.charAt(++index);
        TokenType type = TOKENS_MAP.getOrDefault(esc, TokenType.NOT_FOUND);
        tokens.add(new Token(esc, type));
        index++;
    }

    private void processSymbols(String code, int code_len){
        // First, try to match a 2 char token and if its found, make a substring, else try a single token
        String twoCharSymbol = (index + 1 < code_len) ? code.substring(index, index + 2) : null;

        if (twoCharSymbol != null && TOKENS_MAP.containsKey(twoCharSymbol)) {
            // Found a valid 2-char token (==, !=, >=, <=, &&, ||, ++, --, >>, <<, ->, %d, %s) until infinite and beyond
            tokens.add(new Token(twoCharSymbol, TOKENS_MAP.get(twoCharSymbol)));
            index += 2;
        } else {
            // Single token
            String singleChar = String.valueOf(currentChar);
            TokenType type = TOKENS_MAP.getOrDefault(singleChar, TokenType.NOT_FOUND);
            tokens.add(new Token(singleChar, type));
            index++;
        }
    }

    public void reset(){
        tokens.clear();
        index = 0;
    }

}
