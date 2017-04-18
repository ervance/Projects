import java.util.*;
import java.io.*;

// Use Token class as is. Do not make any changes to it
public class Token
{
   String kind;
   String  value ;
   

   public Token( String t, String val)
   {
      kind = t;
      value = val;
     
   }
   
   public Token( String t)
   {
      kind = t;
      value = "";
     
   }

   public String getKind()
   {
      return kind;
      
   }
   
   public String getValue()
   {
     return value;
   }
  
   
    
   public String toString()    //prints one token
   {
             
        String s = "" + kind;
        if( kind == "id") 
           s = s + "(" + "\"" + value + "\"" + ")";
        else if (kind == "int")
            s = s + "(" + value + ")";
       return s;
    }
}

//Complete the Lexer class. You can add more private methods and fields. //Do not change the signatures of the public classes

class Lexer
{
  
   private String progText;  //holds the input string
   private int nextCharPtr;  
   private char nextChar;
   private ArrayList<Token> tokenList;  //holds list of tokens found
   private static Map<String, String> KEYDICTIONARY = 
        new HashMap<String, String>() {{
          //Key Dictionary for kind
          put("begin", "begin");
          put("end", "begin");
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
      //************************NOTE: does the ++ actually do what I intended************************  
      nextCharPtr++;
      //System.out.println("Next char: " + nextChar);
      //System.out.println("Length of progText" + progText.length());
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
      //System.out.println(c == ' ');
      return c == ' ';
   }//end isWhitespace

   private boolean isLetter(char c)
   {//returns true if c is an upper or lowercase letter
      //System.out.println("is letter: " + c);
      return (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')));
   }//end isLetter

   private boolean isDigit(char c)
   {//returns true if c is a digit
      return ((c >= '0') && (c <= '9'));
   }//end isDigit

   private int nextState(char c)
   {//return the value of the next state
      int state;

      if(isLetter(c)){
        //next char is a letter
        //System.out.println("In state 1");  
        state = 1;
      }
      else if(isDigit(c))
        //next char is a digit 
        state = 2;
      else
        //next char is an operator|punctuation
        state = 3;

      return state;

   }//end nextState

   public ArrayList<Token> scan()
   {
      //This is where the finite state machine algorithm goes
      //See next page
      System.out.println("Start Scanner");
      int state = 0;
      char c = nextChar;
      System.out.println(c);
      System.out.println(nextChar);
      String token_value, token_kind;
      Token token;
      ArrayList<Token> tokenList = new ArrayList<>();
     
      while( nextChar != '$')
      {
        switch(state)
        {
            case 0:  //state: whitespace  
            { 
              //check next character
              //c = getNextChar();
              //System.out.print(c);
              int count = 1;
              while(isWhitespace(c)){
                //System.out.println(nextChar);
                c = getNextChar();
                count++;
              }
              //System.out.print(c);

              state = nextState(c);
              //System.out.println(tokenList);
              break;
            }//end state: whitespace
            
            case 1:   
            {
              //state: letter|digit
              token_kind = "id";
              //start accumulating the tokens
              //clear token value
              token_value = "";
              token_value = token_value + c;

              c = getNextChar();
              //System.out.println("past get next in state 1");
              //System.out.print(c);
              while(isLetter(c) || isDigit(c))
              {
                token_value = token_value + c;
                c = getNextChar();
                //System.out.print(c);
              }//end while
              
              //add token to list
              if (KEYDICTIONARY.containsKey(token_value)){
                //check for keyword begin, end, read, write
                //System.out.println(KEYDICTIONARY.get(token_value));
                tokenList.add(new Token(KEYDICTIONARY.get(token_value), ""));
              }
              else
                //add the id
                tokenList.add(new Token(token_kind, token_value));

              //move to next state
              state = nextState(c);
              //System.out.println(tokenList);
              break;
            }//end state: letter|digit

            case 2:   
            {
              //state: digit
              token_kind = "int";
              token_value = "";
              token_value = token_value + c;

              c = getNextChar();
              //System.out.print(c);
              while(isDigit(c))
              {
                token_value = token_value + c;
                c = getNextChar();
                //System.out.print(c);
              }//end while

              //add token to list
              tokenList.add(new Token(token_kind, token_value));

              //move to next state
              state = nextState(c);
              //System.out.println(tokenList);
              break;
            }//end state: digit

            case 3:  
            {
              //state operators|punctuation
              token_kind = "";
              token_value = "";
              //token_kind = token_kind + c;
              c = getNextChar();
              //System.out.print(c);
              while(!isDigit(c) && !isLetter(c) && !isWhitespace(c) && !(c == '$' ))
              {
                token_kind = token_kind + c;
                c = getNextChar();
                //System.out.print("I am suck in case 3: " + c);
              }//end while

              if(!(KEYDICTIONARY.containsKey(token_kind)))
                //throw an error - Uknown Symbol
                System.out.println("ERROR: This is just a holder:_" + token_kind+".");
              else
                //add the operator|punctuation to the list
                tokenList.add(new Token(KEYDICTIONARY.get(token_kind), token_value));

              //move to next state
              state = nextState(c);
              //System.out.println(tokenList);
              break;
            }//end state operators|punctuation
        } //end switch statement
        //System.out.println("The next char is "+ nextChar);
      }//end while  
      System.out.println("Scan completed");
      return tokenList; 
   }
      
   public static void main( String[] args)
   {
      
     public static void main( String[] args)
    {
        //Typical Test Case
        //Test Case 1 – " begin read a12b4c; book := 62 ; write book end  $"
        //Test Case 2 - ″ a :=b+c  5009*6 := c ; read 80 end $″
        //Test Case 3 – ″ a := b & c  5 * 6 = c end $″   bad one
        //Test Case 4 - ″ a := b + (c + 1); apple end $”
        //Test Case 5 - ″ a :  = b*( a + s)  $″   bad one
        //Test Case 6 - ″ a := b*( a * ) + s)  ″   bad one

        String [] testCases = {" begin read a12b4c; book := 62 ; write book end  $" ,
                "a :=b+c  5009*6 := c ; read 80 end  $",
                " a := b & c  5 * 6 = c end $",
                " a :  = b*( a + s)  $",
                " a := b + (c + 1); apple end $",
                " a := b*( a * ) + s)  ",
                " a := b*c $ *(c)",
                " $ begin x := end $",
                " end; begin as328ax8; 32 $",
                " begin read zas8291; bob281 := 62 ; tony := 809271 write book end  $",
                ":*-+($",
                ";+-()&this/-",
                "*+:-=/"
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
      
   }//end main

}//end class Lexer


