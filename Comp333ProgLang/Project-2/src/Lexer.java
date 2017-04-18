/*
Authors: Eric Vance, Lorn Miller, Keith Johnson
Project 2
Comp333
Tu-Th 11:00
Description: The lexer class is designed to find tokens in an input string
and return a token list that labels the tokens as to their type.
*/

import java.util.*;
import java.io.*;

class Lexer
{

    private String progText;  //holds the input string
    private int nextCharPtr;
    private char nextChar;
    private ArrayList<Token> tokenList;  //holds list of tokens found
    private static final Map<String, String> KEYDICTIONARY =
            new HashMap<String, String>() {{
                //Key Dictionary for kind
                put("begin", "begin");
                put("end", "end");
                put("write", "write");
                put("read", "read");
                put(":=", "assign");
                put("/", "divide");
                put("*", "times");
                put("+", "plus");
                put("-", "minus");
                put(";", "semicolon");
                put("(", "lparent");
                put(")", "rparent");
            }};


    public Lexer( String p)
    {
        progText = p;
        nextCharPtr = 0;
        nextChar = progText.charAt(nextCharPtr);
    }//end Lexer Constructor

    private char getNextChar()
    {
        nextCharPtr++;
        if (nextCharPtr < progText.length())
            nextChar = progText.charAt(nextCharPtr);
        return nextChar;
    }//end getNextChar

    private void pushBackChar()
    {
        //optional but might be useful
        nextCharPtr--;
        nextChar = progText.charAt(nextCharPtr);
    }//end pushBackChar

    private boolean isWhitespace(char c)
    {//returns true if the character is whitespace
        return c == ' ';
    }//end isWhitespace

    private boolean isLetter(char c)
    {//returns true if c is an upper or lowercase letter
        return (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')));
    }//end isLetter

    private boolean isDigit(char c)
    {//returns true if c is a digit
        return ((c >= '0') && (c <= '9'));
    }//end isDigit

    private boolean endOfText(){
        //Returns true if the end of hte text has been reached
        return (nextCharPtr+1) > progText.length()-1;
    }

    private boolean isOperator(char c, String tokenKind, boolean assignmentCase){
        //Helper case check function to case 3
        return !isDigit(c) && !isLetter(c) && !isWhitespace(c) && !(c == '$')
                && !KEYDICTIONARY.containsKey(tokenKind) && assignmentCase && !endOfText();
    }

    private int nextState(char c)
    {//return the value of the next state
        int state;
        if (isWhitespace(c))
            //next char is whitespace
            state = 0;
        else if(isLetter(c))
            //next char is a letter
            state = 1;
        else if(isDigit(c))
            //next char is a digit
            state = 2;
        else
            //next char is an operator|punctuation
            state = 3;

        return state;

    }//end nextState


    public ArrayList<Token> scan() {
        //Finite state machine algorithm
        System.out.println("Start Scanner...");
        int state = 0;
        char c = nextChar;
        String tokenValue, tokenKind;
        boolean endOfText;
        //a flag to signal if the program ended correctly with a '$'
        boolean endsWith$ = true;
        ArrayList<Token> tokenList = new ArrayList<>();
        //If '$' is found it is end of the file, but if '$' is not the last char
        //keep reading the program.
        while ((nextChar != '$' || progText.indexOf(nextChar) <  progText.length()-1) && endsWith$) {
            switch (state) {
                case 0:  //state: whitespace
                {
                    //check next character
                    endOfText = endOfText();
                    while (isWhitespace(c) && !endOfText) {
                        endOfText = endOfText();
                        c = getNextChar();
                    }//end while
                    state = nextState(c);
                    break;
                }//end state: whitespace

                case 1: {
                    //state: letter|digit
                    tokenKind = "id";
                    //start accumulating the tokens
                    //clear token value
                    tokenValue = "";
                    tokenValue = tokenValue + c;
                    c = getNextChar();
                    endOfText = endOfText();
                    while ((isLetter(c) || isDigit(c)) && !endOfText) {
                        tokenValue = tokenValue + c;
                        //update to check if at end of text
                        endOfText = endOfText();
                        c = getNextChar();
                    }//end while

                    //add token to list
                    if (KEYDICTIONARY.containsKey(tokenValue))
                        //check for keyword begin, end, read, write
                        tokenList.add(new Token(KEYDICTIONARY.get(tokenValue), ""));
                    else
                        //add the id
                        tokenList.add(new Token(tokenKind, tokenValue));

                    //move to next state
                    state = nextState(c);
                    break;
                }//end state: letter|digit

                case 2: {
                    //state: digit
                    tokenKind = "int";
                    tokenValue = "";
                    tokenValue = tokenValue + c;

                    c = getNextChar();
                    endOfText = endOfText();
                    while (isDigit(c) && !endOfText) {
                        tokenValue = tokenValue + c;
                        endOfText = endOfText();
                        c = getNextChar();
                    }//end while

                    //add token to list
                    tokenList.add(new Token(tokenKind, tokenValue));

                    //move to next state
                    state = nextState(c);
                    break;
                }//end state: digit

                case 3: {
                    //state operators|punctuation
                    boolean assignmentCase = true;
                    tokenKind = "";
                    tokenValue = "";
                    tokenKind = tokenKind + c;
                    c = getNextChar();
                    //check for ':' only case
                    if(tokenKind.equals(":") && c != '=')
                        assignmentCase = false;
                    while (isOperator(c, tokenKind, assignmentCase)) {
                        tokenKind = tokenKind + c;
                        c = getNextChar();
                    }//end while

                    if ((KEYDICTIONARY.containsKey(tokenKind)))
                        //add the operator|punctuation to the list
                        tokenList.add(new Token(KEYDICTIONARY.get(tokenKind), tokenValue));
                    else if (!(KEYDICTIONARY.containsKey(tokenKind)) && !tokenKind.equals("") && !tokenKind.equals("$"))
                        //Throw an Error Uknown Symbol
                        System.out.println("ERROR Uknown Symbol at " + (nextCharPtr - 1) + ": \"" + tokenKind + "\"");

                    //move to next state
                    state = nextState(c);
                    break;
                }//end state operators|punctuation
            } //end switch statement
            //check to make sure it ended properly with  '$'
            if(nextCharPtr ==  progText.length())
            {
                System.out.println("ERROR: Program did not end with \'$\'");
                endsWith$ = false;
            }//end if
        }//end while
        //check $ is last
        if (nextChar == '$' && progText.indexOf(nextChar) <  progText.length()-1)
        {//Warn user that anything after $ will not considered.
            System.out.println("WARNING: All characters after first \'$\' will not be considered.");
        }//end if
        System.out.println("Scan completed.");
        return tokenList;
    }//end scan
    public static void main( String[] args)
    {
        //Typical Test Case
        //Test Case 1 – " begin read a12b4c; book := 62 ; write book end  $"
        //Test Case 2 - ″ a :=b+c  5009*6 := c ; read 80 end $″
        //Test Case 3 – ″ a := b & c  5 * 6 = c end $″   bad one
        //Test Case 4 - ″ a := b + (c + 1); apple end $”
        //Test Case 5 - ″ a :  = b*( a + s)  $″   bad one
        //Test Case 6 - ″ a := b*( a * ) + s)  ″   bad one

        String [] testCases = {
            " begin read a12b4c; book := 62 ; write book end  $" ,
                "a :=b+c  5009*6 := c ; read 80 end  $",
                " a := b & c  5 * 6 = c end $",
                " a :  = b*( a + s)  $",
                " a := b + (c + 1); apple end $",
                " a := b*( a * ) + s)  ",
                };
        String program;
        Lexer lex;
        for(int i = 0; i < testCases.length; i++){
            program = testCases[i];
            lex = new Lexer(program);
            System.out.println("*********** TEST "+ (i+1) + " ***********");
            System.out.println("Input to Lexer:  " + program);
            ArrayList<Token> tokenList = lex.scan();
            System.out.println("Output tokenlist: " + tokenList + "\n");
        }


    }//end main
}//end class Lexer
