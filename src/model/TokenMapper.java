package model;

import java.util.HashMap;
import java.util.Map;

public class TokenMapper {
    public static Map<String, TokenType> TOKENS_MAP = new HashMap<>();
    static{
        TOKENS_MAP.put("auto", TokenType.AUTO); TOKENS_MAP.put("break", TokenType.BREAK);
        TOKENS_MAP.put("case", TokenType.CASE); TOKENS_MAP.put("char", TokenType.CHAR);
        TOKENS_MAP.put("const", TokenType.CONST); TOKENS_MAP.put("continue", TokenType.CONTINUE);
        TOKENS_MAP.put("default", TokenType.DEFAULT); TOKENS_MAP.put("do", TokenType.DO);
        TOKENS_MAP.put("double", TokenType.DOUBLE); TOKENS_MAP.put("else", TokenType.ELSE);
        TOKENS_MAP.put("enum", TokenType.ENUM); TOKENS_MAP.put("extern", TokenType.EXTERN);
        TOKENS_MAP.put("float", TokenType.FLOAT); TOKENS_MAP.put("for", TokenType.FOR);
        TOKENS_MAP.put("goto", TokenType.GOTO); TOKENS_MAP.put("if", TokenType.IF);
        TOKENS_MAP.put("int", TokenType.INT); TOKENS_MAP.put("long", TokenType.LONG);
        TOKENS_MAP.put("register", TokenType.REGISTER); TOKENS_MAP.put("return", TokenType.RETURN);
        TOKENS_MAP.put("short", TokenType.SHORT); TOKENS_MAP.put("signed", TokenType.SIGNED);
        TOKENS_MAP.put("sizeof", TokenType.SIZEOF); TOKENS_MAP.put("static", TokenType.STATIC);
        TOKENS_MAP.put("struct", TokenType.STRUCT); TOKENS_MAP.put("switch", TokenType.SWITCH);
        TOKENS_MAP.put("typedef", TokenType.TYPEDEF); TOKENS_MAP.put("union", TokenType.UNION);
        TOKENS_MAP.put("unsigned", TokenType.UNSIGNED); TOKENS_MAP.put("void", TokenType.VOID);
        TOKENS_MAP.put("volatile", TokenType.VOLATILE); TOKENS_MAP.put("while", TokenType.WHILE);
        TOKENS_MAP.put("scanf", TokenType.READ); TOKENS_MAP.put("printf", TokenType.WRITE);
        //
        TOKENS_MAP.put("#define", TokenType.DEFINE); TOKENS_MAP.put("#elif", TokenType.ELIF);
        TOKENS_MAP.put("#if", TokenType.PRE_IF); TOKENS_MAP.put("#else", TokenType.PRE_ELSE);
        TOKENS_MAP.put("#endif", TokenType.ENDIF); TOKENS_MAP.put("#error", TokenType.ERROR);
        TOKENS_MAP.put("#ifdef", TokenType.IFDEF); TOKENS_MAP.put("#ifndef", TokenType.IF_NDEF);
        TOKENS_MAP.put("#include", TokenType.INCLUDE); TOKENS_MAP.put("#message", TokenType.MESSAGE);
        TOKENS_MAP.put("#undef", TokenType.UNDEF); TOKENS_MAP.put("&&", TokenType.AND_AND);
        TOKENS_MAP.put("||", TokenType.OR_OR); TOKENS_MAP.put("!", TokenType.NOT_OP);
        TOKENS_MAP.put("&", TokenType.AND_OP); TOKENS_MAP.put("|", TokenType.OR_OP);
        TOKENS_MAP.put("^", TokenType.XOR_OP); TOKENS_MAP.put("~", TokenType.BIT_NOT_OP);
        TOKENS_MAP.put(">>", TokenType.SHR_OP); TOKENS_MAP.put("<<", TokenType.SHL_OP);
        TOKENS_MAP.put(">", TokenType.GT); TOKENS_MAP.put(">=", TokenType.GT_EQ);
        TOKENS_MAP.put("<", TokenType.LT); TOKENS_MAP.put("<=", TokenType.LT_EQ);
        TOKENS_MAP.put("==", TokenType.EQ); TOKENS_MAP.put("!=", TokenType.NOT_EQ);
        //
        TOKENS_MAP.put("[", TokenType.L_SQUARE); TOKENS_MAP.put("]", TokenType.R_SQUARE);
        TOKENS_MAP.put("(", TokenType.L_PAR); TOKENS_MAP.put(")", TokenType.R_PAR);
        TOKENS_MAP.put("{", TokenType.L_BRACE); TOKENS_MAP.put("}", TokenType.R_BRACE);
        TOKENS_MAP.put(",", TokenType.COMMA); TOKENS_MAP.put(";", TokenType.SEMI);
        //
        TOKENS_MAP.put(".", TokenType.POINT); TOKENS_MAP.put("->", TokenType.R_ARROW);
         TOKENS_MAP.put("'", TokenType.QMARK); TOKENS_MAP.put("=", TokenType.ASSIGN);
        TOKENS_MAP.put("%", TokenType.MOD); TOKENS_MAP.put("%f", TokenType.FRM_DEC);
        TOKENS_MAP.put("%d", TokenType.FRM_INT); TOKENS_MAP.put("%s", TokenType.FRM_STR);
        TOKENS_MAP.put("+", TokenType.PLUS); TOKENS_MAP.put("-", TokenType.MINUS);
        TOKENS_MAP.put("*", TokenType.MULTI); TOKENS_MAP.put("/", TokenType.DIV);
        TOKENS_MAP.put("++", TokenType.INCREASE); TOKENS_MAP.put("--", TokenType.DECREASE);
        //
        TOKENS_MAP.put("\\n", TokenType.NEW_LINE); TOKENS_MAP.put("\\0", TokenType.END_STR);
        TOKENS_MAP.put("\\t", TokenType.H_TAB); TOKENS_MAP.put("\\r", TokenType.START_STR);
        TOKENS_MAP.put("\\", TokenType.LIT_BACKSLASH); TOKENS_MAP.put("\\f", TokenType.NEW_PAGE);
        TOKENS_MAP.put("\"", TokenType.DQ_MARK); TOKENS_MAP.put("\\\"", TokenType.LIT_DQ_MARK);
        TOKENS_MAP.put("\\v", TokenType.V_TAB); TOKENS_MAP.put("\\a", TokenType.ALERT);
    }
}
