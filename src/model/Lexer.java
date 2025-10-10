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
        String symbol = (index + 1 < code_len && !Character.isWhitespace(code.charAt(index + 1))
            && !Character.isLetterOrDigit(code.charAt(index + 1)))?
                //+2 'cause substring takes end - 1.
                code.substring(index, index + 2) : String.valueOf(currentChar);
        var symbol_len = symbol.length();

        if (TOKENS_MAP.containsKey(symbol)) tokens.add(new Token(symbol, TOKENS_MAP.get(symbol)));
        else if (symbol_len == 2){
            String char1 = String.valueOf(symbol.charAt(0)), char2 = String.valueOf(symbol.charAt(1));
            //For symbols like +=, *=, -=, /=
            tokens.addAll(List.of(
                    new Token(char1, TOKENS_MAP.getOrDefault(char1, TokenType.NOT_FOUND)),
                    new Token(char2, TOKENS_MAP.getOrDefault(char2, TokenType.NOT_FOUND))
            ));
        }
        else tokens.add(new Token(symbol, TokenType.NOT_FOUND));

        index += symbol_len;
    }

    public void reset(){
        tokens.clear();
        index = 0;
    }

}
