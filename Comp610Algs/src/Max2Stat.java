import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class Max2Stat {

    private int [] solution;
    private int numberOfVariables, maxTrues;
    private ArrayList<BooleanClause> clauses;
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

    public Max2Stat(ArrayList<BooleanClause> clauses, int numberOfVariables){
        solution = null;
        this.clauses = clauses;
        this.numberOfVariables = numberOfVariables;
        maxTrues = 0;
        runningTime =0;
        algorithm = "";
    }

    public void bruteForce(){
        //check every option and choose the highest value
        /******Start running time******/
        long startTime = System.nanoTime();
        algorithm = "Brute Force";

        int variable1, variable2, totalTrues;
        boolean variable1Neg, variable2Neg, mapBool1, mapBool2;
        int [] variableBoolMap = setInitialBoolMap(numberOfVariables); //initialized all to true

        //Going to try all options ex: 1 = T 2 = T, 1 = T 2 = F, 1 = F 2 = T, 1 = F 2 = F (N^2 = 4)
        for(int i =0; i < Math.pow(numberOfVariables, 2); i++) {
            totalTrues = 0;
            for (BooleanClause clause : clauses) {
                variable1 = clause.getVariable1() - 1; //minus one to offset for the array
                variable2 = clause.getVariable2() - 1;
                mapBool1 = getBool(variableBoolMap, variable1);//get boolean value of its current map
                mapBool2 = getBool(variableBoolMap, variable2);
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
            if(totalTrues > maxTrues){
                maxTrues = totalTrues;
                solution = new int[numberOfVariables];
                System.arraycopy(variableBoolMap, 0, solution, 0,variableBoolMap.length);
            }
            //try next set of boolean values for each variable
            variableBoolMap = incrementBoolMap(variableBoolMap);
        }

        //to time the brute force method for complexity
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime)/1000000.0;

    }

    public void maxVariableValue() {
        /*Looks at the maximum number of true propositions from variable
        * Run through the list and keep track of how many times x1 is negated/non-negated.
        * Do this for each variable, assign the truth value to the variable.
        *
        * Could be a problem if you the max was paired up ie
        * (x1 v x2) ... (-x1 vs -x2) could be a bunch of non negated and they are paired.  If there is on with both
        * negated you could get a better result if you negate either x1 or x2 so you can make that one clause true.
        *
        */
        /******Start running time******/
        long startTime = System.nanoTime();
        algorithm = "Max Variable";
//        List<BooleanClause> clauses = new ArrayList<>(clauses);//so that I dont erase, if we went to run more algs on the clauses
        int arraySize = numberOfVariables * 2;//double the size of variables, starts at 0 for var 1
        int [] maxVariableValue;
        ArrayList<BooleanClause> copyClauses = new ArrayList<>(clauses);
        ArrayList<Integer> ignoreList = new ArrayList<>();



//        int [] copyForDebugingMaxVariableValue = new int [maxVariableValue.length];
//        System.arraycopy(maxVariableValue, 0, copyForDebugingMaxVariableValue, 0,maxVariableValue.length);
//
//        int variableNumber = 1;
//        String printString = "";
//        for(int i = 0 ; i < (copyForDebugingMaxVariableValue.length)/2; i++) {
//            printString = printString + "Variable number: " + variableNumber + "\n";
//            printString = printString + "Number of Trues: " + copyForDebugingMaxVariableValue[i] + "\n";
//            printString = printString + "Number of Falses: " + copyForDebugingMaxVariableValue[(i + numberOfVariables)];
//            printString = printString + "\n\n";
//            variableNumber++;
//        }
//        System.out.print(printString);
//        maxTrues = clauses.size();
        solution = new int[numberOfVariables];
        //initalizeSolution(maxVariableValue);//sets the truth values based on the highest number to make trues
        while(!clauses.isEmpty()){
            System.out.println("Clauses left: " + clauses.size());
            //maxVariableValue[0] = non negated number for x1 maxVariableValue[11] = negated number of x1
            //non negations are maxVariableValue[variableValue - 1]
            //negations are maxVariableValue[variableValue + numberOfVariables]
            //second half of array is false
            maxVariableValue = calculateNumLiteralTruths(clauses, arraySize, ignoreList);
            int indexOfLit = getLargestIntIndex(maxVariableValue); //get the index of the largest number of T or bar
            int greatestLiteral = literalNum(indexOfLit);//returns which variable it is, ie X1 or X2
            int variableValue = getLiteralTruth(indexOfLit, greatestLiteral);//this represents the value of the number, - if negated
            //ignore both negated and truth
            ignoreList.add(variableValue);
            ignoreList.add(variableValue * -1);
            //I think I need to change this initalizeSolution to change only the maximum T or bar to corresponding truth value using greatest literal or indexOfLit
            //Ie If it was X3 with the most T, and the solution looked like [0,0,0] It would change it to [0,0,1] and then I would need to change X3 count to -1 so ignore it again
            //I remove all the clauses that are made try by X3 being true
            //recaluclate how many T or bar there are after that but only for remaining Literals, because now -1 is in place so dont count it, make sure to put the -1 in the right function
//            initalizeSolution(maxVariableValue);//recalculate the solution to find max number of  T or bar


//            maxTrues+= maxVariableValue[indexOfLit];
            //changes the used literal trues and falses to -1 so that we will not change its truth value anymore
            maxVariableValue = updateUsedLiterals(maxVariableValue, indexOfLit);
            int clauseSize = clauses.size();

            for(int i = 0; i < clauses.size(); i++){
                if(clauses.get(i) != null) {
//                    int var1 = clauses.get(i).getVariable1Val();
//                    int var2 = clauses.get(i).getVariable2Val();
                    int var1 = clauses.get(i).getVariable1Val();
                    int var2 = clauses.get(i).getVariable2Val();
                    int trueVal1 = solution[Math.abs(var1) - 1 ];
                    int trueVal2 = solution[Math.abs(var2) - 1 ];
//                    boolean var1Neg = var1 < 0 && solution[Math.abs(var1) - 1 ] == 0; //reason we check if it euqals zero at its current solution and if the current literal is negated  and current solution is 0 (meaning it is false) we want to return true
//                    boolean var2Neg = var2 < 0 && solution[Math.abs(var2) - 1] == 0;
//                    boolean oneTrue = (trueVal1 == 1 && var1 > 0) || (trueVal2 == 1 && var2 > 0);
//                    boolean bothTrue = trueVal1 == 1 && trueVal2 == 1;

//                    int variable1 = clauses.get(i).getVariable1() - 1; //minus one to offset for the array
//                    int variable2 = clauses.get(i).getVariable2() - 1;
//                    boolean mapBool1 = getBool(solution, variable1);//get boolean value of its current map
//                    boolean mapBool2 = getBool(solution, variable2);
//                    boolean variable1Neg = clauses.get(i).variable1Negated();
//                    boolean variable2Neg = clauses.get(i).variable2Negated();
//                    if(variable1Neg)
//                        mapBool1 = !mapBool1;
//                    if(variable2Neg)
//                        mapBool2 = !mapBool2;
//                    if(mapBool1 || mapBool2)
//                        maxTrues++;
//                    boolean bothFalse = trueVal1 == 0 && trueVal2 == 0;
                    boolean literal1 = getBoolValue(var1, trueVal1);
                    boolean literal2 = getBoolValue(var2, trueVal2);
                    /**
                     *
                     * It is all about the -1 I think i found the current solution but need to check somehow if it is -1 or not and make that a pass
                     *
                     *
                     * when solution is initalized to -1 need to usre that to check if there was a double count.  If it was -1 there was no double count
                     *
                     *
                     * NEED TO FIX WHERE IT CHECKS IF THE TRUE VALUE UWAS INITIATED BEFORE IT DEALS WITH THE DOUBLE COUNT AND SUBRATCT 1
                     *
                     *
                     * if one is true
                     *
                     *
                     * Need to add to the total trues here
                     *
                     * Maybe do the check for -1 here to skip something if need be, like keep it unitl they are not -1
                     */
                    //or current solution truth value used for those two clause both equal false
//                    if (var1Neg || var2Neg || oneTrue || bothTrue)
//                        maxTrues++;
//                    if (bothTrue || (var1Neg && var2Neg) || (bothFalse && !var1Neg && !var2Neg))
//                        maxTrues--;

                    if(var1 == variableValue || var2 == variableValue) {//if either literal contains the literal we set to true remove
                        clauses.remove(i);
                        maxTrues++;
                        i--;
                    }
                    else if (ignoreList.contains(var1) && ignoreList.contains(var2))
                        clauses.remove(i);

                        //clauses.add(i, null);

                }
            }

        }
        clauses = copyClauses;
        bruteMod(maxTrues);
        //maxTrues (THIS IS GOING TO RUIN RUN TIME
//        for(int i = 0; i < clauses.size(); i++){
//            int var1 = clauses.get(i).getVariable1Val();
//            int var2 = clauses.get(i).getVariable2Val();
//            boolean var1Neg = var1 < 0 && solution[Math.abs(var1) - 1] == 0; //reason we check if it euqals zero at its current solution and if the current literal is negated  and current solution is 0 (meaning it is false) we want to return true
//            boolean var2Neg = var2 < 0 && solution[Math.abs(var2) - 1] == 0;
//            if (!(var1Neg || var2Neg))//there is a double count need to subtract it
//                maxTrues--;
//
//        }

        //to time the brute force method for complexity
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime)/1000000.0;


    }

    private void bruteMod(int target){
        int maxFalses = clauses.size() - target;
        int variable1, variable2, totalTrues, totalFalses;
        boolean variable1Neg, variable2Neg, mapBool1, mapBool2;
        int [] variableBoolMap = setInitialBoolMap(numberOfVariables); //initialized all to true

        totalFalses = 0;
        //Going to try all options ex: 1 = T 2 = T, 1 = T 2 = F, 1 = F 2 = T, 1 = F 2 = F (N^2 = 4)
        for(int i =0; i < Math.pow(numberOfVariables, 2); i++) {
            totalTrues = 0;
            totalFalses = 0;
            for (BooleanClause clause : clauses) {
                variable1 = clause.getVariable1() - 1; //minus one to offset for the array
                variable2 = clause.getVariable2() - 1;
                mapBool1 = getBool(variableBoolMap, variable1);//get boolean value of its current map
                mapBool2 = getBool(variableBoolMap, variable2);
                variable1Neg = clause.variable1Negated();
                variable2Neg = clause.variable2Negated();
                //anding with the negations in case that clause has a negation in it
                if(variable1Neg)
                    mapBool1 = !mapBool1;
                if(variable2Neg)
                    mapBool2 = !mapBool2;
                if(mapBool1 || mapBool2)
                    totalTrues++;
                else
                    totalFalses++;

                if(totalFalses > maxFalses){
                    System.out.println("Stop. Total False: " + totalFalses + " Max False: " + maxFalses);
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

    private int [] updateUsedLiterals(int [] literalArray, int indexOfLit){

        //top is if the index is on false side
        if(indexOfLit > numberOfVariables - 1) {
            literalArray[indexOfLit] = -1;//this is to zero it out so we dont choose it again
            literalArray[indexOfLit - numberOfVariables] = -1;
        }
        else {//index is on true side
            literalArray[indexOfLit] = -1;
            literalArray[indexOfLit + numberOfVariables] = -1;
        }

        return literalArray;
    }

    private int [] calculateNumLiteralTruths(ArrayList<BooleanClause> clauses, int arraySize, ArrayList<Integer> ignoreList){

        int [] literalTruths = new int [arraySize];

        for (BooleanClause clause : clauses) {
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

    private void initalizeSolution(int [] literaValues){

        for(int i = 0; i < solution.length; i++){
            if(literaValues[i] > literaValues[i + numberOfVariables]) {
                solution[i] = 1;//means it is true
            }
            else if(literaValues[i] == -1){
                //do nothing because a truth has been assigned to it
            }
            else {
                solution[i] = 0;//the literal has a bar and should be false
            }
        }
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

    private int[] setInitialBoolMap(int n){
        //creates a map try try all different bool value combinations
        //uses a binary representation to simulate the different combinations
        //example nuberOfVariables = 4 booleanValueMap = [0,0,0,0]
        int [] bitValues = new int [n];
        for(int i = 0; i < bitValues.length; i++)
            bitValues[i] = 0;//set it to 0
        return bitValues;
    }

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

    private boolean getBoolValue(int literal, int truthAs){
        boolean boolVal;

        if(literal < 0)
            boolVal = truthAs != 1;
        else
            boolVal = truthAs == 1;

        return boolVal;
    }

    public int countSolutionTrues(ArrayList<BooleanClause> clauses, int [] solution){
        int variable1, variable2, totalTrues;
        boolean variable1Neg, variable2Neg, mapBool1, mapBool2;
        totalTrues = 0;
        for (BooleanClause clause : clauses) {
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

    public ArrayList<BooleanClause> getClauses(){
        return clauses;
    }
    @Override
    public String toString(){

        String clausesString = "";
        String solutionString = "";

        for (int bool:solution) {
            if(bool == 1)
                solutionString+= "T";
            else
                solutionString+= "F";
        }

//        for (BooleanClause clause: clauses) {
//            clausesString+= clause + "\n";
//        }

        return "Algorithm used: " +algorithm +
                "\nRunning time (milliseconds): " + runningTime +
                "\nNumber of variables: " + numberOfVariables +
                "\nMax Number of Trues: "+maxTrues +
                "\nSolution: " + solutionString;
                //"\nClauses:\n" + clausesString;

    }

}

//input reader
class ReadTextFile {

    //read and return an array list of boolean clauses from an input file
    public static Max2Stat readClauseTextFile(String fileName){

        BufferedReader bufferReader = null;
        String booleanClause;
        String [] booleanSplit;
        ArrayList<BooleanClause> clauses = new ArrayList<>();
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
                        clauses.add(new BooleanClause(firstClause, secondClause));
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

//Holds pair of booleans in the clause statement
class BooleanClause {

    private int variable1Val;//negated or not
    private int variable2Val;

    private int variable1;//variable number
    private int variable2;

    private boolean variable1Bool, variable2Bool;
    //added for greedy take max alg
    private boolean greedyCheck = false;

    public BooleanClause(int variable1Bool, int variable2Bool){
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
