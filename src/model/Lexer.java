package model;

import java.util.ArrayList;
import java.util.List;
import static model.TokenMapper.TOKENS_MAP;

public class Lexer {
    public static List<Token> lexer(String code){
        List<Token> tokens = new ArrayList<>();
        int idx = 0, code_length = code.length();

        //Start to process the code
        while(idx < code_length){
            var current_char = code.charAt(idx);

            //We don't need whitespaces
            while (Character.isWhitespace(current_char)){
                if (++idx >= code_length) return tokens;
                current_char = code.charAt(idx);
            }

            //To start verifying keywords or identifiers
            if (Character.isLetter(current_char)){
                //Better for manipulating strings
                StringBuilder id = new StringBuilder();
                do {
                    id.append(code.charAt(idx++));
                } while(idx < code_length && Character.isLetterOrDigit(code.charAt(idx))
                        || code.charAt(idx) == '_' || code.charAt(idx) == '$'); //C also admits '$' as an ID

                var idStr = id.toString();
                //Ask if id is a keyword but id is an identifier
                TokenType type = TOKENS_MAP.getOrDefault(idStr, TokenType.ID);
                tokens.add(new Token(idStr, type));
                continue;
            }

            //To start verifying numbers
            if (Character.isDigit(current_char)){
                StringBuilder num = new StringBuilder();
                do {
                    num.append(code.charAt(idx++));
                } while(idx < code_length && Character.isDigit(code.charAt(idx)));

                tokens.add(new Token(num.toString(), TokenType.INT_NUM));
                continue;
            }

            //To start verifying pre-processing polices
            if (current_char == '#'){
                StringBuilder police = new StringBuilder();
                do {
                    police.append(code.charAt(idx++));
                } while( idx < code_length && Character.isLetter(code.charAt(idx)));

                var policeStr = police.toString();
                //A pre-processing police can't be an ID. So, if the police's not found, we return TokenType NOTFOUND
                TokenType type = TOKENS_MAP.getOrDefault(policeStr, TokenType.NOT_FOUND);
                tokens.add(new Token(policeStr, type));
                continue;
            }

            //To start verifying libraries
            if (current_char == '<'){
                tokens.add(new Token(String.valueOf(current_char), TokenType.LT));
                StringBuilder lib = new StringBuilder();
                do {
                    lib.append(code.charAt(++idx));
                } while (Character.isLetter(code.charAt(idx + 1)) || code.charAt(idx + 1) != '>');

                var libStr = lib.toString();
                tokens.add(new Token(libStr, TokenType.LIBRARY));
                idx++;
                continue;
            }

            //To start verifying symbols & escape chars
            String symbol = (idx + 1 < code_length && !Character.isWhitespace(code.charAt(idx+1)))?
                    //+2 'cause substring takes end - 1.
                    code.substring(idx, idx + 2) : String.valueOf(current_char);

            if (TOKENS_MAP.containsKey(symbol)) tokens.add(new Token(symbol, TOKENS_MAP.get(symbol)));
            else if (symbol.length() == 2){
                String char1 = String.valueOf(symbol.charAt(0)), char2 = String.valueOf(symbol.charAt(1));
                //For symbols like +=, *=, -=, /=
                tokens.addAll(List.of(
                        new Token(char1, TOKENS_MAP.getOrDefault(char1, TokenType.NOT_FOUND)),
                        new Token(char2, TOKENS_MAP.getOrDefault(char2, TokenType.NOT_FOUND))
                ));
            }
            else tokens.add(new Token(symbol, TokenType.NOT_FOUND));

            idx += symbol.length();
        }
        return tokens;
    }

}
