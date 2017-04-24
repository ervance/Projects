import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


class Max2Stat {

    private int [] solution;
    private int numberOfVariables, maxTrues;
    private ArrayList<Clause> clauses;
    private double runningTime;
    private String algorithm;

    public Max2Stat(){
        solution = null;
        clauses = new ArrayList<>();
        numberOfVariables = 0;
        maxTrues = 0;
        runningTime =0;
        algorithm = "";
    }

    public Max2Stat(ArrayList<Clause> clauses, int numberOfVariables){
        solution = null;
        this.clauses = clauses;
        this.numberOfVariables = numberOfVariables;
        maxTrues = 0;
        runningTime =0;
        algorithm = "";
    }

    /*
    * Brute force approach
    * Start with a bit string representing True and False assignments
    * Start at all false ie. 0000
    * Check total number of clauses satisfied
    * If it is larger than the previous assignment take it
    * */
    public void bruteForce(){
        //check every option and choose the highest value
        /******Start running time******/
        long startTime = System.nanoTime();
        algorithm = "Brute Force";

        int totalTrues;
        int [] solutionMap = new int [numberOfVariables];
        //Try all options ex: 1 = T 2 = T, 1 = T 2 = F, 1 = F 2 = T, 1 = F 2 = F (N^2 = 4)
        for(int i =0; i < Math.pow(2, numberOfVariables); i++) {
            totalTrues = countSolutionTrues(clauses, solutionMap);
            if(totalTrues > maxTrues){
                maxTrues = totalTrues;
                solution = new int[numberOfVariables];
                System.arraycopy(solutionMap, 0, solution, 0,solutionMap.length);
            }
            //try next set of boolean values for each variable
            solutionMap = incrementBoolMap(solutionMap);
        }

        //time the brute force method for complexity
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime)/1000000.0;
    }

    /*
    * Greedy Approach
    * Looks at the maximum number of true propositions from variable
    * Run through the list and keep track of how many times x1 is negated/non-negated.
    * Do this for each variable, make the variable with the highest count of negated/non-negated to true.
    * ie, x1 = 8 -x1 = 9 x2 = 3 -x2 = 8, Set -x1 = False so that it will make any clauses it is in evaluate True.
    */
    public void maxVariableValue() {
        /******Start running time******/
        long startTime = System.nanoTime();
        algorithm = "Max Variable";
        int arraySize = numberOfVariables * 2;//double the size of variables, starts at 0 for var 1
        int [] numberOfLiteralOccur;
        ArrayList<Clause> copyClauses = new ArrayList<>(clauses);
        ArrayList<Integer> ignoreList = new ArrayList<>();
        solution = new int[numberOfVariables];

        while(!copyClauses.isEmpty()){
            //while there are clauses left, calculate the maximum occurrence of a literal
            //count how many clauses it satisfies
            //remove all of the clauses it satisfied or any clause that can no longer be satisfied ie (F v F)
            //recalculate to find maximum occurrence of a literal
            //repeat until no more clauses left

            //numberOfLiteralOccur[0] = number of occurrences of x1 in all clauses that are left
            //numberOfLiteralOccur[11] = number of occurrences of -x1 (x1 bar) in all clauses that are left
            //numberOfLiteralOccur[currLiteral - 1] is to offset the index ie x1 = index 0, x2 = index 1
            //numberOfLiteralOccur[currLiteral + numberOfVariables] is the negated of literal. ie -x1, -x2

            //calculate the total occurrences of negated and non-negated literals that are left
            numberOfLiteralOccur = calculateNumLiteralTruths(copyClauses, arraySize, ignoreList);
            //get the index of the most occurring literal
            int indexOfLit = getLargestIntIndex(numberOfLiteralOccur);
            //returns which variable it is, ie X1 or X2
            int greatestLiteral = literalNum(indexOfLit);
            //this represents the value of the number, - if negated
            //ie. if it is x1 then currLiteral = 1 if -x3 then currLiteral = -3
            int currLiteral = getLiteralTruth(indexOfLit, greatestLiteral);
            //ignore both negated and truth
            ignoreList.add(currLiteral);
            ignoreList.add(currLiteral * -1);

            for(int i = 0; i < copyClauses.size(); i++){
                if(copyClauses.get(i) != null) {

                    int var1 = copyClauses.get(i).getVariable1Val();
                    int var2 = copyClauses.get(i).getVariable2Val();

                    //if clause contains the literal count as true and remove
                    if(var1 == currLiteral || var2 == currLiteral) {
                        copyClauses.remove(i);
                        maxTrues++;
                        i--;
                    }
                    //both of the variables in the clause are False so remove
                    else if (ignoreList.contains(var1) && ignoreList.contains(var2))
                        copyClauses.remove(i);
                }
            }
        }
        //run modified brute force for a more accurate answer
        bruteMod(maxTrues);

        //finish timing the algorithm
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime)/1000000.0;
    }

    /*
    * used in conjunction with the greedy max variable
    * to brute force a more accurate answer
    * */
    private void bruteMod(int target){
        //this is used in conjunction with the maxVariableValue
        //run this after the greedy method has found an answer
        int maxFalses = clauses.size() - target;
        int variable1, variable2, totalTrues, totalFalses;
        boolean variable1Neg, variable2Neg, mapBool1, mapBool2;
        int [] variableBoolMap = new int[numberOfVariables];

        //Going to try all options ex: 1 = T 2 = T, 1 = T 2 = F, 1 = F 2 = T, 1 = F 2 = F (N^2 = 4)
        for(int i =0; i < Math.pow(2, numberOfVariables); i++) {
            totalTrues = 0;
            totalFalses = 0;
            for (Clause clause : clauses) {
                variable1 = clause.getVariable1() - 1; //minus one to offset for the array
                variable2 = clause.getVariable2() - 1;
                mapBool1 = getBool(variableBoolMap, variable1);//get boolean value of its current map
                mapBool2 = getBool(variableBoolMap, variable2);
                variable1Neg = clause.variable1Negated();
                variable2Neg = clause.variable2Negated();
                //if variable is negated in clause
                if(variable1Neg)
                    mapBool1 = !mapBool1;
                if(variable2Neg)
                    mapBool2 = !mapBool2;

                //check if it makes clause true
                if(mapBool1 || mapBool2)
                    totalTrues++;
                else
                    totalFalses++;
                //Stop current brute force solution check if the solution is already worse than the greedy has produced
                if(totalFalses > maxFalses){
                    break;
                }
            }

            if(totalTrues > maxTrues){
                maxTrues = totalTrues;
                solution = new int[numberOfVariables];
                System.arraycopy(variableBoolMap, 0, solution, 0,variableBoolMap.length);
            }
            //try next set of boolean values for each variable
            variableBoolMap = incrementBoolMap(variableBoolMap);

        }

    }

    /*
    *  Solution:        Will be represented as a string of bits
    *  Closeness:       A neighbor is close if they differ by 1 bit. ie.  0000 1000 0100 0010 0001 are all neighbors.
    *  Movement:        Calculate all neighbors.  A move will be made if there is a solution that is at least as good
    *                   as the current one. (in other terms if the neighbor increases or is equal to the total clauses
    *                   satisfied move to that neighbor.
    *  Stop Condition:  If no moves were made, ie all neighbors satisfy less clauses.  The algorithm will terminate
    *
    */

    public void localSearch(){
        long startTime = System.nanoTime();
        algorithm = "Local Search";
        int tempCount;//holds the temp satisfied clause count to compare
        //initialize solution to start at all false
        int [] mostSatisfiedClauseSolution = new int [numberOfVariables];
        solution = new int [numberOfVariables];
        //each neighbor is one bit difference
        //flip the bit and take the neighbor that has the highest value
        //start with all false
        int satisfiedClauses = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
        //stopping condition will be when no other neighbor increases the total clauses satisfied
        boolean stoppingConditionMet = false;
        while(!stoppingConditionMet) {
            stoppingConditionMet = true;//reset so if no neighbors are found to be larger it will stop
            for (int i = 0; i < solution.length; i++) {
                //flip bits to test neighbors that differ by 1
                //skip any that are 1 because that was part of a previous neighbor
                if(mostSatisfiedClauseSolution[i] == 0){
                    mostSatisfiedClauseSolution[i] = 1;
                    tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                    if(tempCount >= satisfiedClauses) {
                        //neighbor had a larger truth satisfied
                        satisfiedClauses = tempCount;//current largest amount of clauses satisfied
                        //solution is the current truth assignment
                        System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0,mostSatisfiedClauseSolution.length);
                        stoppingConditionMet = false;//stopping condition has not been met
                        mostSatisfiedClauseSolution[i] = 0;//flip bit back to try other neighbors
                    }
                    else{
                        //solution is not larger
                        mostSatisfiedClauseSolution[i] = 0;
                    }
                }
            }
            //move to the neighbor that had the largest amount of clauses satisfied
            System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0,solution.length);
        }
        //check if any trues would yield more if any were false
        System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0,solution.length);
        for(int i = 0; i < solution.length; i++){
            //skip any of the truth assignments that are false already
            if(mostSatisfiedClauseSolution[i] == 1){
                mostSatisfiedClauseSolution[i] = 0;
                tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                //if flipping that bit to false yields more clauses take that solution
                if(tempCount >= satisfiedClauses){
                    satisfiedClauses = tempCount;
                    System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0,mostSatisfiedClauseSolution.length);
                    mostSatisfiedClauseSolution[i] = 1;
                }
                else
                    //do not take that that solution, previous is better
                    mostSatisfiedClauseSolution[i] = 1;
            }

        }
        maxTrues = satisfiedClauses;

        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime)/1000000.0;
    }

    private int getLiteralTruth(int indexOfLit, int greatestLiteral){
        int variableValue;

        if(indexOfLit < numberOfVariables) {
            solution[greatestLiteral - 1] = 1;//means it is true, set solution
            variableValue = greatestLiteral;
        }
        else {
            solution[greatestLiteral - 1] = 0;//the literal has a bar and should be false
            variableValue = greatestLiteral * -1;//making it negative
        }

        return variableValue;
    }

    private int [] calculateNumLiteralTruths(ArrayList<Clause> clauses, int arraySize, ArrayList<Integer> ignoreList){

        int [] literalTruths = new int [arraySize];

        for (Clause clause : clauses) {
            //count how many negations or non negations there are
            //whichever is the max, assign that truth value
            int clause1 = clause.getVariable1();
            int clause2 = clause.getVariable2();
            int variableValue1 = clause.getVariable1Val();
            int variableValue2 = clause.getVariable2Val();

            if(!ignoreList.contains(variableValue1)) {
                if (variableValue1 > 0)
                    literalTruths[clause1 - 1]++;
                else
                    literalTruths[(clause1 + numberOfVariables) - 1]++;
            }
            if(!ignoreList.contains(variableValue2)) {
                if (variableValue2 > 0)
                    literalTruths[clause2 - 1]++;
                else
                    literalTruths[(clause2 + numberOfVariables) - 1]++;
            }
        }
        return literalTruths;

    }

    private int literalNum(int index){//returns the variable number, ie X1 or X2
        int literalNum;

        if(index > numberOfVariables - 1)//this means its second half so it would be false
            literalNum = (index % numberOfVariables) + 1;
        else
            literalNum = index + 1;

        return literalNum;
    }

    private int getLargestIntIndex(int [] literals){
        int intIndex, largest;
        largest = intIndex = 0;

        for (int i = 0; i < literals.length; i++){
            if (literals[i] > largest) {
                largest = literals[i];
                intIndex = i;
            }
        }
        return intIndex;
    }

    private boolean getBool(int [] boolMap, int variable){
        return boolMap[variable] == 1;
    }

    /*
    *  Utilized for the brute force method
    *  increments the current truth assignments to
    *  check every possibility
    */
    private int [] incrementBoolMap(int [] boolMap){
        //does binary addition to try every combination of True and False
        int carry, currentValue, counter;
        carry = 1;
        counter = 0;
        do{
            currentValue = boolMap[counter];
            if(currentValue + carry == 2) {
                boolMap[counter] = 0;
                carry = 1;
            }
            else {
                boolMap[counter] = 1;
                carry = 0;
            }
            counter++;
        }while(counter < boolMap.length && carry != 0);

        return boolMap;
    }

    //returns the total number of clauses satisfied by the current True False assignment
    public int countSolutionTrues(ArrayList<Clause> clauses, int [] solution){
        int variable1, variable2, totalTrues;
        boolean variable1Neg, variable2Neg, mapBool1, mapBool2;
        totalTrues = 0;
        for (Clause clause : clauses) {
            variable1 = clause.getVariable1() - 1; //minus one to offset for the array
            variable2 = clause.getVariable2() - 1;
            mapBool1 = getBool(solution, variable1);//get boolean value of its current map
            mapBool2 = getBool(solution, variable2);
            variable1Neg = clause.variable1Negated();
            variable2Neg = clause.variable2Negated();
            //anding with the negations in case that clause has a negation in it
            if(variable1Neg)
                mapBool1 = !mapBool1;
            if(variable2Neg)
                mapBool2 = !mapBool2;
            if(mapBool1 || mapBool2)
                totalTrues++;
        }

        return totalTrues;
    }

    //to verify total clause assignment for strings of T and F
    public int [] createSolution(String tfString){
        int [] solution = new int [tfString.length()];
        for (int i = 0; i < tfString.length(); i++ ){
            char ch = tfString.charAt(i);
            if( ch == 'T'){
                solution[i] = 1;
            }
            else
                solution[i] = 0;
        }
        return solution;
    }

    @Override
    public String toString(){

        String solutionString = "";

        for (int bool:solution) {
            if(bool == 1)
                solutionString+= "T";
            else
                solutionString+= "F";
        }

        return "Algorithm used: " +algorithm +
                "\nRunning time (milliseconds): " + runningTime +
                "\nNumber of variables: " + numberOfVariables +
                "\nMax Number of Trues: "+maxTrues +
                "\nSolution: " + solutionString;
    }

}

//input reader
class ReadTextFile {

    //read and return an array list of boolean clauses from an input file
    public static Max2Stat readClauseTextFile(String fileName){

        BufferedReader bufferReader = null;
        String booleanClause;
        String [] booleanSplit;
        ArrayList<Clause> clauses = new ArrayList<>();
        int firstClause, secondClause, numberOfVariables;
        numberOfVariables = 0;
        try{
            bufferReader = new BufferedReader(new java.io.FileReader(fileName));
            while((booleanClause = bufferReader.readLine()) != null){
                if(!booleanClause.equals("")) {//added in case input.txt has empty lines
                    booleanSplit = booleanClause.split(" ");
                    if (booleanSplit.length == 2) {//added in case input.txt has less than 2 items after split
                        firstClause = Integer.parseInt(booleanSplit[0]);
                        secondClause = Integer.parseInt(booleanSplit[1]);
                        clauses.add(new Clause(firstClause, secondClause));
                        if(numberOfVariables < Math.abs(firstClause))
                            numberOfVariables = Math.abs(firstClause);
                        if (numberOfVariables < Math.abs(secondClause))
                            numberOfVariables = Math.abs(secondClause);
                    }
                }
            }
            bufferReader.close();
        }
        catch(FileNotFoundException e){
            System.err.println("File " + fileName + " not found.");
        }
        catch(IOException e){
            System.err.println("Unable to read file name: " + fileName );
        }
        clauses.trimToSize();//make sure length is amount of items

        return new Max2Stat(clauses, numberOfVariables);
    }

}

//Clause statement
class Clause {

    private int variable1Val;//negated or not
    private int variable2Val;

    private int variable1;//variable number
    private int variable2;

    private boolean variable1Bool, variable2Bool;

    Clause(int variable1Bool, int variable2Bool){
        //set the boolean values
        this.variable1Bool = variable1Bool <= 0;
        this.variable2Bool = variable2Bool <= 0;
        variable1Val = variable1Bool;
        variable2Val = variable2Bool;
        variable1 = Math.abs(variable1Val);
        variable2 = Math.abs(variable2Val);

    }

    @Override
    public String toString(){
        return "( x" + variable1Val +" v x" + variable2Val + " )";
    }


    public int getVariable1Val() {
        return variable1Val;
    }

    public int getVariable2Val() {
        return variable2Val;
    }

    public boolean variable1Negated() {
        return variable1Bool;
    }

    public boolean variable2Negated() {
        return variable2Bool;
    }

    public int getVariable1() {
        return variable1;
    }

    public int getVariable2() {
        return variable2;
    }

}
