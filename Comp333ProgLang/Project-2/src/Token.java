/**
 * Created by Eric on 2/19/2016.
 */
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
