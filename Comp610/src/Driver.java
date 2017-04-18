import java.util.ArrayList;

public class Driver {

    public static void main(String [] args){
//        Max2Stat max2Stat = ReadTextFile.readClauseTextFile("test1.txt");
        Max2Stat max2Stat = ReadTextFile.readClauseTextFile("test1.txt");
        max2Stat.bruteForce();
//        max2Stat1.maxVariableValue();
        System.out.print(max2Stat);
//        System.out.println(max2Stat1);
//        ArrayList<BooleanClause> clauses = new ArrayList<>(max2Stat1.getClauses());
        //int [] solution1 = {1,0,0,0,0,0,0,1,0,1,0,0,1,1,0,1,1,1,0,0,0,0,0,0,1,1,1,1,0};
        //System.out.println(max2Stat1.countSolutionTrues(clauses, solution1));
//        int [] solution2 = max2Stat1.createSolution("TFFFFFFTFTFFTTFTTTFFFFFFTTTTF");
//        int [] solution2 = max2Stat1.createSolution("TTTTTFTFTTFTTFFTFTFFFFFTTTTTT");
//        System.out.println("To confirm: " + max2Stat1.countSolutionTrues(clauses, solution2));

    }

}
