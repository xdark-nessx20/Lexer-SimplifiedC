package model;

import java.util.ArrayList;
import java.util.List;

import static model.TokenMapper.TOKENS_MAP;

public class Lexer {
    private final List<Token> tokens;
    private int index;
    private char currentChar;

    public Lexer() {
        tokens = new ArrayList<>();
        index = 0;
    }

    public List<Token> processCode(String code) {
        var code_length = code.length();

        //Start to process the code
        while (index < code_length) {
            currentChar = code.charAt(index);

            //We don't need whitespaces
            while (Character.isWhitespace(currentChar)) {
                if (++index >= code_length) return tokens;
                currentChar = code.charAt(index);
            }

            //To start verifying keywords or identifiers
            if (Character.isLetter(currentChar) || currentChar == '_' || currentChar == '$') {
                processKeywordsAndIdentifiers(code, code_length);
                continue;
            }

            //To start verifying int and decimal numbers
            if (Character.isDigit(currentChar) || (currentChar == '.' && index + 1 < code_length
                    && Character.isDigit(code.charAt(index + 1)))) {
                processNumbers(code, code_length);
                continue;
            }

            //To start verifying pre-processing polices
            if (currentChar == '#') {
                processPolices(code, code_length);
                continue;
            }

            //To start verifying text
            if (currentChar == '"') {
                processText(code, code_length);
                continue;
            }

            //To start verifying symbols
            processSymbols(code, code_length);
        }
        return tokens;
    }

    private void processText(String code, int code_len) {
        //Add the DQMark
        tokens.add(new Token("\"", TOKENS_MAP.get("\"")));

        if (!nextChar(code, code_len)) return;

        var text = new StringBuilder();
        while (index < code_len && currentChar != '"') {
            //For extracting pre-processing polices in a text
            if (currentChar == '\\') {
                if (!text.isEmpty()){
                    tokens.add(new Token(text.toString(), TokenType.TEXT));
                    text.setLength(0);
                }

                processEscChars(code, code_len);
                continue;
            }

            //For extracting string formats in a text
            if (currentChar == '%') {
                if (!text.isEmpty()){
                    tokens.add(new Token(text.toString(), TokenType.TEXT));
                    text.setLength(0);
                }

                var format = String.valueOf(currentChar);
                if (nextChar(code, code_len) && currentChar != '"' && currentChar != '\\') {
                    format += currentChar;
                    TokenType type = TOKENS_MAP.getOrDefault(format, TokenType.NOT_FOUND);
                    tokens.add(new Token(format, type));
                    nextChar(code, code_len);
                } else tokens.add(new Token(format, TokenType.TEXT));
                continue;
            }

            text.append(currentChar);
            nextChar(code, code_len);
        }

        //Add remaining text
        if (!text.isEmpty()) tokens.add(new Token(text.toString(), TokenType.TEXT));

        //Add the closing " if it exists
        if (currentChar == '"') {
            tokens.add(new Token("\"", TokenType.DQ_MARK));
            index++;
        }
    }

    private void processKeywordsAndIdentifiers(String code, int code_len) {
        //Better for manipulating strings
        var id = new StringBuilder();
        do {
            id.append(currentChar);
        } while (nextChar(code, code_len) && (Character.isLetterOrDigit(currentChar) || currentChar == '_'
                || currentChar == '$')); //C also admits '$' as an ID

        var idStr = id.toString();
        //Ask if id is a keyword but id is an identifier
        TokenType type = TOKENS_MAP.getOrDefault(idStr, TokenType.ID);
        tokens.add(new Token(idStr, type));
    }

    private void processNumbers(String code, int code_len) {
        var number = new StringBuilder();
        boolean hasDecimalPoint = false, hasExpo = false;

        //Verify if num starts with decimal point
        if (currentChar == '.') {
            hasDecimalPoint = true;
            number.append(currentChar);
            if (!nextChar(code, code_len)) return;
        }

        //Process integer and decimal part
        do {
            number.append(currentChar);
            if (!nextChar(code, code_len)) break;

            //Verify for decimal point
            if (currentChar == '.' && !hasDecimalPoint) {
                //Verify if the next char is digit or exp
                if (index + 1 < code_len) {
                    var nextCh = code.charAt(index + 1);
                    if (Character.isDigit(nextCh) || nextCh == 'E' || nextCh == 'e'
                            || nextCh == ';' || Character.isWhitespace(nextCh)) {
                        hasDecimalPoint = true;
                        number.append(currentChar);
                        if (!nextChar(code, code_len)) break;
                    }
                    else break; //Invalid decimal
                }
                else break;
            }
        } while (Character.isDigit(currentChar));

        //Verify for exp
        if (index < code_len && (currentChar == 'E' || currentChar == 'e')) {
            hasExpo = true;
            number.append(currentChar);
            if (!nextChar(code, code_len)){
                tokens.add(new Token(number.toString(), TokenType.DECIMAL_NUM));
                return;
            }

            // Optional sign after exp
            if (currentChar == '+' || currentChar == '-') {
                number.append(currentChar);
                if (!nextChar(code, code_len)) {
                    tokens.add(new Token(number.toString(), TokenType.DECIMAL_NUM));
                    return;
                }
            }

            // Must have at least one digit after exp
            if (!Character.isDigit(currentChar)) {
                // Invalid exponent format
                tokens.add(new Token(number.toString(), TokenType.NOT_FOUND));
                return;
            }

            // Process exponent digits
            while (Character.isDigit(currentChar)) {
                number.append(currentChar);
                if (!nextChar(code, code_len)) break;
            }
        }

        var type = (hasDecimalPoint || hasExpo) ? TokenType.DECIMAL_NUM : TokenType.INT_NUM;
        tokens.add(new Token(number.toString(), type));
    }

    private void processPolices(String code, int code_len) {
        var police = new StringBuilder();
        do {
            police.append(currentChar);
        } while (nextChar(code, code_len) && Character.isLetter(currentChar));

        var policeStr = police.toString();
        //A pre-processing police can't be an ID. So, if the police's not found, we return TokenType NOTFOUND,
        // unless the professor says that it has to throw an error message
        TokenType type = TOKENS_MAP.getOrDefault(policeStr, TokenType.NOT_FOUND);
        tokens.add(new Token(policeStr, type));
    }

    private void processEscChars(String code, int code_len) {
        var firstChar = String.valueOf(currentChar);
        //It's necessary to verify if the code hasn't finished
        if (!nextChar(code, code_len)) {
            tokens.add(new Token(firstChar, TokenType.NOT_FOUND));
            return;
        }

        String esc = firstChar + currentChar;
        tokens.add(new Token(esc, TOKENS_MAP.getOrDefault(esc, TokenType.NOT_FOUND)));
        nextChar(code, code_len);
    }

    private void processSymbols(String code, int code_len) {
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

    public void reset() {
        tokens.clear();
        index = 0;
    }

    private boolean nextChar(String code, int code_len) {
        if (++index >= code_len) return false;
        currentChar = code.charAt(index);
        return true;
    }

}
