package model;

import static resources.Configuration.MAGENTA;
import static resources.Configuration.RESET;
import static resources.Configuration.GREEN;

public record Token(String lexeme, TokenType type) {

    @Override
    public String toString() {
        return (MAGENTA + "%s " + RESET + "\"" + GREEN + "%s"+ RESET + "\"").formatted(type.name(), lexeme);
    }
}
