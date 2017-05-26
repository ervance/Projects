//Author Eric Vance
//Comp 610
//Max2stat Project

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


class Max2Stat {

    private int[] solution;
    private int numberOfVariables, maxTrues, startingVariable;
    private ArrayList<Clause> clauses;
    private double runningTime;
    private String algorithm;
    private HashMap<Integer, ArrayList<Clause>> variableHashMap;

    public Max2Stat() {
        solution = null;
        clauses = new ArrayList<>();
        numberOfVariables = 0;
        maxTrues = 0;
        runningTime = 0;
        algorithm = "";
    }

    public Max2Stat(ArrayList<Clause> clauses, int numberOfVariables, HashMap<Integer, ArrayList<Clause>> variableHashMap, int startingVariable) {
        solution = null;
        this.clauses = clauses;
        this.numberOfVariables = numberOfVariables;
        this.variableHashMap = variableHashMap;
        this.startingVariable = startingVariable;
        maxTrues = 0;
        runningTime = 0;
        algorithm = "";
    }

    /*
    * Brute force approach
    * Start with a bit string representing True and False assignments
    * Start at all false ie. 0000
    * Check total number of clauses satisfied
    * If it is larger than the previous assignment take it
    * */
    public void bruteForce() {
        //check every option and choose the highest value
        /******Start running time******/
        long startTime = System.nanoTime();
        algorithm = "Brute Force";

        int totalTrues;
        int[] solutionMap = new int[numberOfVariables];
        //Try all options ex: 1 = T 2 = T, 1 = T 2 = F, 1 = F 2 = T, 1 = F 2 = F (N^2 = 4)
        for (int i = 0; i < Math.pow(2, numberOfVariables); i++) {
            totalTrues = countSolutionTrues(clauses, solutionMap);
            if (totalTrues > maxTrues) {
                maxTrues = totalTrues;
                solution = new int[numberOfVariables];
                System.arraycopy(solutionMap, 0, solution, 0, solutionMap.length);
            }
            //try next set of boolean values for each variable
            solutionMap = incrementBoolMap(solutionMap);
        }

        //time the brute force method for complexity
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime) / 1000000.0;
    }

    public void branchAnndBoundBruteForce() {
        BranchBoundObj truthAsandTotal = new BranchBoundObj("", 0);
        truthAsandTotal = branchBoundBruteForce(truthAsandTotal, variableHashMap, 1);
        maxTrues = truthAsandTotal.bound;
        solution = createSolution(truthAsandTotal.truthAs);
    }

    // Bruteforce branch and bound ***NOT WORKING***
    private BranchBoundObj branchBoundBruteForce(BranchBoundObj currTruthAndTotal, HashMap<Integer, ArrayList<Clause>> clauses, int currentVariable) {
        BranchBoundObj answer;

        if (clauses.containsKey(currentVariable)) {
            String truthAs = currTruthAndTotal.truthAs;
            int total = currTruthAndTotal.bound;
            String leftTruthAs, rightTruthAs;
            BranchBoundObj leftCurrTandT, rightCurrTandT;
            int leftTotal, rightTotal, nextVariable;
            HashMap<Integer, ArrayList<Clause>> currentClauses = new HashMap<>(clauses);
            ArrayList<Clause> tempListLeft = new ArrayList<>(clauses.get(currentVariable));
            ArrayList<Clause> tempListRight = new ArrayList<>(clauses.get(currentVariable));

            if (clauses.isEmpty()) {
                answer = new BranchBoundObj(truthAs, total);
            } else {

                leftTruthAs = truthAs + "T";
                //go left (True)
                leftTotal = total;
                for (int i = 0; i < tempListLeft.size() && !tempListLeft.isEmpty(); i++) {
                    //this means it satisfies the clause, so add to the truths and remove
                    int var1, var2;
                    boolean varcheck1, varcheck2;
                    var1 = tempListLeft.get(i).getVariable1();
                    var2 = tempListLeft.get(i).getVariable2();
                    //var 1 is true
                    varcheck1 = (var1 == currentVariable && !tempListLeft.get(i).variable1Negated());
                    //var 2 is true
                    varcheck2 = (var2 == currentVariable && !tempListLeft.get(i).variable2Negated());
                    if (varcheck1) {
                        leftTotal++;
                        tempListLeft.remove(i);
                        i--;
                    } else if (varcheck2) {
                        leftTotal++;
                        tempListLeft.remove(i);
                        i--;
                    } else {
                        //need to remove if both are false
//                        int leftTruthAsLength = leftTruthAs.length();
//                        //if var1 and var2 are smaller than the current truth assignment then means its been assigned already
//                        //check if both are false since neither of above passed
//                        if (var1 - 1 < leftTruthAsLength && var2 - 1 < leftTruthAsLength) {
//                            boolean var1IsFalse = (leftTruthAs.charAt(var1 - 1) == 'T' && tempListLeft.get(i).variable1Negated()) ||
//                                    (leftTruthAs.charAt(var1 - 1) == 'F' && !tempListLeft.get(i).variable1Negated());
//                            boolean var2IsFalse = (leftTruthAs.charAt(var2 - 1) == 'T' && tempListLeft.get(i).variable2Negated()) ||
//                                    (leftTruthAs.charAt(var2 - 1) == 'F' && !tempListLeft.get(i).variable2Negated());
//                            if (var1IsFalse && var2IsFalse)
//                                tempListLeft.remove(i);
//                            i--;
//                        }
                        if(var1 <= currentVariable && var2 <= currentVariable){
                            //remove beacuse other variable has been checked and doesnt belong in this list
                            tempListLeft.remove(i);
                            i--;
                        }

                    }

                }
                currentClauses.put(currentVariable, tempListLeft);
                nextVariable = ++currentVariable;
                leftCurrTandT = new BranchBoundObj(leftTruthAs, leftTotal);
                currTruthAndTotal = branchBoundBruteForce(leftCurrTandT, clauses, nextVariable);

                //check possibility of right
                rightTruthAs = truthAs + "F";
                rightTotal = total;
                for (int i = 0; i < tempListRight.size(); i++) {
                    int var1, var2;
                    boolean varcheck1, varcheck2;
                    var1 = tempListRight.get(i).getVariable1();
                    var2 = tempListRight.get(i).getVariable2();
                    varcheck1 = (tempListRight.get(i).getVariable1() == currentVariable && tempListRight.get(i).variable1Negated());
                    varcheck2 = (tempListRight.get(i).getVariable2() == currentVariable && tempListRight.get(i).variable2Negated());
                    //this means it satisfies the clause, so add to the truths and remove
                    if (varcheck1) {
                        rightTotal++;
                        tempListRight.remove(i);
                    } else if (varcheck2) {
                        rightTotal++;
                        tempListRight.remove(i);
                    } else {
                        //need to remove if both are false
//                        int rightTruthAsLength = rightTruthAs.length();
                        //if var1 and var2 are smaller than the current truth assignment then means its been assigned already
                        //check if both are false since neither of above passed
//                        if (var1 - 1 < rightTruthAsLength && var2 - 1 < rightTruthAsLength) {
//                            boolean var1IsFalse = (rightTruthAs.charAt(var1 - 1) == 'T' && tempListRight.get(i).variable1Negated()) ||
//                                    (rightTruthAs.charAt(var1 - 1) == 'F' && !tempListRight.get(i).variable1Negated());
//                            boolean var2IsFalse = (rightTruthAs.charAt(var2 - 1) == 'T' && tempListRight.get(i).variable2Negated()) ||
//                                    (rightTruthAs.charAt(var2 - 1) == 'F' && !tempListRight.get(i).variable2Negated());
//                            if (var1IsFalse && var2IsFalse)
//                                tempListRight.remove(i);
//                            i--;
//                        }
                        if(var1 <= currentVariable && var2 <= currentVariable) {
                            //remove beacuse other variable has been checked and doesnt belong in this list
                            tempListRight.remove(i);
                            i--;
                        }

                    }
                }
                if (currTruthAndTotal.bound <= rightTotal) {
                    //go right
                    currentClauses.put(currentVariable, tempListRight);
                    nextVariable = ++currentVariable;
                    rightCurrTandT = new BranchBoundObj(rightTruthAs, leftTotal);
                    answer = branchBoundBruteForce(rightCurrTandT, currentClauses, nextVariable);
                } else {
                    //skip the right side
                    answer = currTruthAndTotal;
                }

            }
        } else

        {
            //skip the variable because it doesnt exist as a clause
            //need to add a value to the truth assignment though
            currTruthAndTotal.truthAs = currTruthAndTotal.truthAs + 'T';
            int nextVariable = currentVariable++;
            answer = branchBoundBruteForce(currTruthAndTotal, clauses, nextVariable);
        }

        return answer;
    }


    /*
    * Greedy Approach
    * Looks at the maximum number of true propositions from variable
    * Run through the list and keep track of how many times x1 is negated/non-negated.
    * Do this for each variable, make the variable with the highest count of negated/non-negated to true.
    * ie, x1 = 8 -x1 = 9 x2 = 3 -x2 = 8, Set -x1 = False so that it will make any clauses it is in evaluate True.
    */
    public void maxVariableValue() {
        /*
        * *****Start running time*****
        * */
        long startTime = System.nanoTime();
        algorithm = "Max Variable";
        int arraySize = numberOfVariables * 2;//double the size of variables, starts at 0 for var 1
        int[] numberOfLiteralOccur;
        ArrayList<Clause> copyClauses = new ArrayList<>(clauses);
        ArrayList<Integer> ignoreList = new ArrayList<>();
        solution = new int[numberOfVariables];

        while (!copyClauses.isEmpty()) {
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

            for (int i = 0; i < copyClauses.size(); i++) {
                if (copyClauses.get(i) != null) {

                    int var1 = copyClauses.get(i).getVariable1Val();
                    int var2 = copyClauses.get(i).getVariable2Val();

                    //if clause contains the literal count as true and remove
                    if (var1 == currLiteral || var2 == currLiteral) {
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
        runningTime = (finishTime - startTime) / 1000000.0;
    }

    /*
    * used in conjunction with the greedy max variable
    * to brute force a more accurate answer
    * */
    private void bruteMod(int target) {
        //this is used in conjunction with the maxVariableValue
        //run this after the greedy method has found an answer
        int maxFalses = clauses.size() - target;
        int variable1, variable2, totalTrues, totalFalses;
        boolean variable1Neg, variable2Neg, mapBool1, mapBool2;
        int[] variableBoolMap = new int[numberOfVariables];

        //Going to try all options ex: 1 = T 2 = T, 1 = T 2 = F, 1 = F 2 = T, 1 = F 2 = F (N^2 = 4)
        for (int i = 0; i < Math.pow(2, numberOfVariables); i++) {
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
                if (variable1Neg)
                    mapBool1 = !mapBool1;
                if (variable2Neg)
                    mapBool2 = !mapBool2;

                //check if it makes clause true
                if (mapBool1 || mapBool2)
                    totalTrues++;
                else
                    totalFalses++;
                //Stop current brute force solution check if the solution is already worse than the greedy has produced
                if (totalFalses > maxFalses) {
                    break;
                }
            }

            if (totalTrues > maxTrues) {
                maxTrues = totalTrues;
                solution = new int[numberOfVariables];
                System.arraycopy(variableBoolMap, 0, solution, 0, variableBoolMap.length);
            }
            //try next set of boolean values for each variable
            variableBoolMap = incrementBoolMap(variableBoolMap);

        }

    }

    /*
       Local Search Constant Bit
    *  Solution:        Will be represented as a string of bits
    *  Closeness:       A neighbor is close if they differ by 1 bit. ie.  0000 1000 0100 0010 0001 are all neighbors.
    *                   Only trues will be flipped followed by false.
    *  Movement:        Calculate all neighbors.  A move will be made if there is a solution that is at least as good
    *                   as the current one. (in other terms if the neighbor increases or is equal to the total clauses
    *                   satisfied move to that neighbor.
    *  Stop Condition:  If no moves were made, ie all neighbors satisfy less clauses.  The algorithm will terminate
    *
    */

    public void localSearchConstantBit() {
        long startTime = System.nanoTime();
        algorithm = "Local Search Constant Bit";
        System.out.println("Starting " + algorithm);
        int tempCount;//holds the temp satisfied clause count to compare
        int newLineCount = 0;
        //initialize solution to start at all false
        int[] mostSatisfiedClauseSolution = new int[numberOfVariables];
        solution = new int[numberOfVariables];
        //each neighbor is one bit difference
        //flip the bit and take the neighbor that has the highest value
        //start with all false
        int satisfiedClauses = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
        //stopping condition will be when no other neighbor increases the total clauses satisfied

        //this loop tests false to true
        boolean stoppingConditionMet = false;
        boolean secondCheck = false;
        while (!stoppingConditionMet && !secondCheck) {
            stoppingConditionMet = true;//reset so if no neighbors are found to be larger it will stop
            for (int i = startingVariable -1 ; i < solution.length; i++) {
                //flip bits to test neighbors that differ by 1
                //skip any that are 1 because that was part of a previous neighbor
                if(variableHashMap.containsKey(i)) {
                    if (mostSatisfiedClauseSolution[i] == 0) {
                        mostSatisfiedClauseSolution[i] = 1;
                        tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                        if (tempCount > satisfiedClauses) {
                            //neighbor had a larger truth satisfied
                            satisfiedClauses = tempCount;//current largest amount of clauses satisfied
                            //solution is the current truth assignment
                            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                            stoppingConditionMet = false;//stopping condition has not been met
                            secondCheck = false;
                            mostSatisfiedClauseSolution[i] = 0;//flip bit back to try other neighbors
                            if (newLineCount % 5 == 0) {
                                System.out.println(algorithm + " moving to better solution...");
                                newLineCount++;
                            }
                        } else if (tempCount == satisfiedClauses) {
                            secondCheck = true;
                            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                            mostSatisfiedClauseSolution[i] = 0;//flip bit back to try other neighbors
                            if (newLineCount % 5 == 0) {
                                System.out.println(algorithm + " moving to better solution...");
                                newLineCount++;
                            }
                        } else {
                            //solution is not larger
                            mostSatisfiedClauseSolution[i] = 0;
                        }
                    }
                }
            }
            //move to the neighbor that had the largest amount of clauses satisfied
            System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
        }
        //run the same local search now flipping true to false
        System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
        stoppingConditionMet = false;
        secondCheck = false;
        while (!stoppingConditionMet && !secondCheck) {
            stoppingConditionMet = true;
            for (int i = startingVariable -1 ; i < solution.length; i++) {
                //skip any of the truth assignments that are false already
                if(variableHashMap.containsKey(i)) {
                    if (mostSatisfiedClauseSolution[i] == 1) {
                        mostSatisfiedClauseSolution[i] = 0;
                        tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                        //if flipping that bit to false yields more clauses take that solution
                        if (tempCount > satisfiedClauses) {
                            satisfiedClauses = tempCount;
                            stoppingConditionMet = false;
                            secondCheck = false;
                            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                            mostSatisfiedClauseSolution[i] = 1;
                        } else if (tempCount == satisfiedClauses) {
                            secondCheck = true;
                            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                            mostSatisfiedClauseSolution[i] = 1;//flip bit back to try other neighbors
                        } else
                            //do not take that that solution, previous is better
                            mostSatisfiedClauseSolution[i] = 1;

                    }
                }

            }
            //move to the neighbor that had the largest amount of clauses satisfied
            System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
        }

        maxTrues = satisfiedClauses;

        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime) / 1000000.0;
    }


    /*
    *  Local Search One Flip Bit
    *  Solution:        Will be represented as a string of bits
    *  Closeness:       A neighbor is close if they differ by 1 bit. ie.  0000 1000 0100 0010 0001 are all neighbors.
    *                   True or false will be flipped.
    *  Movement:        Calculate all neighbors.  A move will be made if there is a solution that is at least as good
    *                   as the current one. (in other terms if the neighbor increases or is equal to the total clauses
    *                   satisfied move to that neighbor.
    *  Stop Condition:  If no moves were made, ie all neighbors satisfy less clauses.  The algorithm will terminate
    *
    */

    public void localSearchFlip() {
        long startTime = System.nanoTime();
        algorithm = "Local Search Flip One Bit";
        System.out.println("Starting " + algorithm);
        int tempCount;//holds the temp satisfied clause count to compare
        int newLineCount = 0;
        //initialize solution to start at all false
        int[] mostSatisfiedClauseSolution = new int[numberOfVariables];
        solution = new int[numberOfVariables];
        //each neighbor is one bit difference
        //flip the bit and take the neighbor that has the highest value
        //start with all false
        int satisfiedClauses = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
        //stopping condition will be when no other neighbor increases the total clauses satisfied


        for (int j = 0; j < 1; j++) {
            boolean stoppingConditionMet = false;
            int chosenNeighbor = -1;
            while (!stoppingConditionMet) {
                stoppingConditionMet = true;//reset so if no neighbors are found to be larger it will stop
                for (int i = 0; i < solution.length; i++) {
                    //flip bits to test neighbors that differ by 1
                    //skip any that are 1 because that was part of a previous neighbor
                    if (i != chosenNeighbor) {
                        mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                        tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                        if (tempCount > satisfiedClauses) {
                            //neighbor had a larger truth satisfied
                            satisfiedClauses = tempCount;//current largest amount of clauses satisfied
                            //solution is the current truth assignment
                            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                            stoppingConditionMet = false;//stopping condition has not been met
                            mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);//flip bit back to try other neighbors
                            chosenNeighbor = i;
                            //Print status
                            if (newLineCount % 5 == 0) {
                                System.out.println(algorithm + " moving to better solution...");
                                newLineCount++;
                            }

                        } else if (tempCount == satisfiedClauses) {
                            mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);//flip bit back to try other neighbors
                            chosenNeighbor = i;
                            if (newLineCount % 5 == 0) {
                                System.out.println(algorithm + " moving to equal solution...");
                                newLineCount++;
                            }
//                            secondCheck = true;
                        }else {
                            //solution is not larger
                            mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                        }

                    }
                }
                //move to the neighbor that had the largest amount of clauses satisfied
                System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
            }
        }

        maxTrues = satisfiedClauses;
        System.out.println("Finished " + algorithm);
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime) / 1000000.0;
    }

    /*
    *  Local Search Flip Bit Random Start
    *  Solution:        Will be represented as a string of bits
    *  Closeness:       A neighbor is close if they differ by 1 bit. ie.  0000 1000 0100 0010 0001 are all neighbors.
    *                   True or false will be flipped.  Two rounds with a random start.
    *  Movement:        Calculate all neighbors.  A move will be made if there is a solution that is at least as good
    *                   as the current one. (in other terms if the neighbor increases or is equal to the total clauses
    *                   satisfied move to that neighbor.
    *  Stop Condition:  If no moves were made, ie all neighbors satisfy less clauses.  The algorithm will terminate
    *
    */

    public void localSearchFlipRandom() {
        long startTime = System.nanoTime();
        algorithm = "Local Search Flip Random Start";
        System.out.println("Starting " + algorithm);
        int tempCount;//holds the temp satisfied clause count to compare
        int newLineCount = 0;
        //initialize solution to start at all false
        solution = new int[numberOfVariables];

        //each neighbor is one bit difference
        //flip the bit and take the neighbor that has the highest value
        //start with all false
        //stopping condition will be when no other neighbor increases the total clauses satisfied
        int oldSatisifedClauses, satisfiedClauses;
        satisfiedClauses = 0;

        for (int j = 0; j < 2; j++) {
            boolean stoppingConditionMet = false;
            int chosenNeighbor = -1;
            int[] mostSatisfiedClauseSolution = randomizeSolution(numberOfVariables);
            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
            satisfiedClauses = oldSatisifedClauses = countSolutionTrues(clauses, mostSatisfiedClauseSolution);

            while (!stoppingConditionMet) {
                stoppingConditionMet = true;//reset so if no neighbors are found to be larger it will stop
                for (int i = startingVariable-1; i < solution.length; i++) {
                    //flip bits to test neighbors that differ by 1
                    //skip any that are 1 because that was part of a previous neighbor
                    if(variableHashMap.containsKey(i)) {
                        if (i != chosenNeighbor) {
                            mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                            tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                            if (tempCount > satisfiedClauses) {
                                //neighbor had a larger truth satisfied
                                satisfiedClauses = tempCount;//current largest amount of clauses satisfied
//                            System.out.println(satisfiedClauses);
                                //solution is the current truth assignment
                                System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                                stoppingConditionMet = false;//stopping condition has not been met
                                mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);//flip bit back to try other neighbors
                                chosenNeighbor = i;
                                if (newLineCount % 5 == 0) {
                                    System.out.println(algorithm + " moving to better solution...");
                                    newLineCount++;
                                }
                            } else if (tempCount == satisfiedClauses) {
                                System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                                mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                                if (newLineCount % 5 == 0) {
                                    System.out.println(algorithm + " moving to better solution...");
                                    newLineCount++;
                                }
                            } else {
                                //solution is not larger
                                mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                            }

                        }
                    }

                }
                //if the new solution found is larger or equal we will use it, otherwise keep the old random solution
                if (oldSatisifedClauses <= satisfiedClauses) {
                    //move to the neighbor that had the largest amount of clauses satisfied
                    System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
                    oldSatisifedClauses = satisfiedClauses;
                }
            }


        }

        maxTrues = satisfiedClauses;

        localSearchConstantBit();
        System.out.println("Finished " + algorithm);
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime) / 1000000.0;
    }

    /*
    *  Local Search Flip Bit Random Start Constant Bit
    *  Solution:        Will be represented as a string of bits
    *  Closeness:       A neighbor is close if they differ by 1 bit. ie.  0000 1000 0100 0010 0001 are all neighbors.
    *                   True or false will be flipped.  Two rounds with a random start.
    *  Movement:        Calculate all neighbors.  A move will be made if there is a solution that is at least as good
    *                   as the current one. (in other terms if the neighbor increases or is equal to the total clauses
    *                   satisfied move to that neighbor.
    *  Stop Condition:  If no moves were made, ie all neighbors satisfy less clauses.  The algorithm will terminate
    *
    */


    public void localSearchRandomStartConstantBit() {
        long startTime = System.nanoTime();
        algorithm = "Local Search Flip Random Random Start Constant Bit";
        System.out.println("Starting " + algorithm);
        int tempCount = 0;//holds the temp satisfied clause to compare
        //initialize solution to start at all false
        solution = new int[numberOfVariables];
        //each neighbor is one bit difference
        //flip the bit and take the neighbor that has the highest value
        //start with all false
        //stopping condition will be when no other neighbor increases the total clauses satisfied
        int oldSatisifedClauses, satisfiedClauses, chosenNeighbor, newLineCount;
        satisfiedClauses = 0;
        int[] mostSatisfiedClauseSolution = new int[numberOfVariables];
        boolean stoppingConditionMet;
        for (int j = 0; j < 1; j++) {
            stoppingConditionMet = false;
            newLineCount = 0;
            chosenNeighbor = -1;
            mostSatisfiedClauseSolution = randomizeSolution(numberOfVariables);
            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
            satisfiedClauses = oldSatisifedClauses = countSolutionTrues(clauses, mostSatisfiedClauseSolution);

            //second Check is designed to keep from a race condition of continuing to
            //find multiple assignments producing equal max trues
            while (!stoppingConditionMet) {
                //reset so if no neighbors are found to be larger it will stop
                stoppingConditionMet = true;
                for (int i = startingVariable-1; i < solution.length; i++) {
                    //skip testing if the variable does not exist
                    if(variableHashMap.containsKey(i)) {
                        //flip bits to test neighbors that differ by 1
                        //skip any that are 1 because that was part of a previous neighbor
                        if (i != chosenNeighbor) {
                            mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                            tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                            if (tempCount > satisfiedClauses) {
                                //neighbor had a larger truth satisfied
                                satisfiedClauses = tempCount;//current largest amount of clauses satisfied
                                //solution is the current truth assignment
                                System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                                //stopping condition has not been met
                                stoppingConditionMet = false;
                                //flip bit back to try other neighbors
                                mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                                chosenNeighbor = i;
                                if (newLineCount % 5 == 0) {
                                    System.out.println(algorithm + " moving to better solution...");
                                    newLineCount++;
                                }
                            } else if (tempCount == satisfiedClauses) {
                                System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                                //flip bit back to try other neighbors
                                mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                            } else {
                                //solution is not larger
                                mostSatisfiedClauseSolution = flipBitI(mostSatisfiedClauseSolution, i);
                            }

                        }
                    }

                }
                //if the new solution found is larger or equal we will use it, otherwise keep the old random solution
                if (oldSatisifedClauses <= satisfiedClauses) {
                    //move to the neighbor that had the largest amount of clauses satisfied
                    System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
                    oldSatisifedClauses = satisfiedClauses;
                }
            }


        }
        System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, solution.length);
        //run local search flipping false to true
        oneWayLocalSearch(mostSatisfiedClauseSolution, 0,1,satisfiedClauses);
        //run local search now flipping true to false
        System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
        oneWayLocalSearch(mostSatisfiedClauseSolution, 1,0,satisfiedClauses);
        maxTrues = satisfiedClauses;
        System.out.println("Finished " + algorithm);
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime) / 1000000.0;
    }

    /*
    *  Local Search Random Solution
    *  Solution:        Will be represented as a string of bits
    *  Closeness:       A neighbor is random.
    *  Movement:        Randomize each round. A round is the length of possible solutions.  Move will occur as soon as
    *                   a random solution increases the value.
    *  Stop Condition:  If no moves were made after trying len(solution), The algorithm will terminate
    *
    */

    public void localSearchRandom() {
        long startTime = System.nanoTime();
        algorithm = "Local Search Random Start";
        System.out.println("Starting "  + algorithm);
        int newLineCount = 0;
        int tempCount;//holds the temp satisfied clause count to compare
        //initialize solution to start at all false
        int[] mostSatisfiedClauseSolution = new int[numberOfVariables];
        solution = new int[numberOfVariables];
        //each neighbor is one bit difference
        //flip the bit and take the neighbor that has the highest value
        //start with all false
        int satisfiedClauses = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
        //stopping condition will be when no other neighbor increases the total clauses satisfied

        boolean stoppingConditionMet = false;

        while (!stoppingConditionMet) {
            stoppingConditionMet = true;//reset so if no neighbors are found to be larger it will stop

            for (int i = 0; i < mostSatisfiedClauseSolution.length; i++) {

                //try a random solution every iteration
                mostSatisfiedClauseSolution = randomizeSolution(mostSatisfiedClauseSolution.length);

                tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                if (tempCount >= satisfiedClauses) {
                    //neighbor had a larger truth satisfied
                    satisfiedClauses = tempCount;//current largest amount of clauses satisfied
                    //solution is the current truth assignment
                    System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                    stoppingConditionMet = false;//stopping condition has not been met
                    if (newLineCount % 5 == 0) {
                        System.out.println(algorithm + " moving to better solution...");
                        newLineCount++;
                    }
                }
            }
            //move to the neighbor that had the largest amount of clauses satisfied
            System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
        }

        maxTrues = satisfiedClauses;
        System.out.println("Finished " + algorithm);
        long finishTime = System.nanoTime();
        runningTime = (finishTime - startTime) / 1000000.0;
    }


    //Helper function to reduce redundant code.  Emulates the constant bit local search.  Only flipping a bit one way.  Ie if true is chosen,
    //it will only flip true to false and skip over false and vice versa
    private void oneWayLocalSearch(int [] mostSatisfiedClauseSolution, int bitToFlip, int flipped, int satisfiedClauses){
        boolean stoppingConditionMet = false;
        int tempCount;
        int newLineCount = 0;

        while (!stoppingConditionMet) {
            stoppingConditionMet = true;
            for (int i = startingVariable -1; i < solution.length; i++) {
                //skip variable if it does not exist
                if(variableHashMap.containsKey(i)) {
                    //skip any of the truth assignments that are false already
                    if (mostSatisfiedClauseSolution[i] == bitToFlip) {
                        mostSatisfiedClauseSolution[i] = flipped;
                        tempCount = countSolutionTrues(clauses, mostSatisfiedClauseSolution);
                        //if flipping that bit to false yields more clauses take that solution
                        if (tempCount >= satisfiedClauses) {
                            satisfiedClauses = tempCount;
                            stoppingConditionMet = false;
                            System.arraycopy(mostSatisfiedClauseSolution, 0, solution, 0, mostSatisfiedClauseSolution.length);
                            mostSatisfiedClauseSolution[i] = bitToFlip;
                            if (newLineCount % 5 == 0) {
                                System.out.println(algorithm + " moving to better solution...");
                                newLineCount++;
                            }
                        } else
                            //do not take that that solution, previous is better
                            mostSatisfiedClauseSolution[i] = bitToFlip;
                    }
                }

            }
            //move to the neighbor that had the largest amount of clauses satisfied
            System.arraycopy(solution, 0, mostSatisfiedClauseSolution, 0, solution.length);
        }
    }

    private int[] randomizeSolution(int length) {
        int[] randomSolution = new int[length];
        double randomBit;
        for (int i = 0; i < randomSolution.length; i++) {
            randomBit = Math.random();
            if (randomBit < 0.5)
                randomSolution[i] = 0;
            else
                randomSolution[i] = 1;
        }

        return randomSolution;
    }

    private int[] flipBitI(int[] mostSatisfiedClauseSolution, int i) {
        if (mostSatisfiedClauseSolution[i] == 0)
            mostSatisfiedClauseSolution[i] = 1;
        else
            mostSatisfiedClauseSolution[i] = 0;

        return mostSatisfiedClauseSolution;
    }

    private int getLiteralTruth(int indexOfLit, int greatestLiteral) {
        int variableValue;

        if (indexOfLit < numberOfVariables) {
            solution[greatestLiteral - 1] = 1;//means it is true, set solution
            variableValue = greatestLiteral;
        } else {
            solution[greatestLiteral - 1] = 0;//the literal has a bar and should be false
            variableValue = greatestLiteral * -1;//making it negative
        }

        return variableValue;
    }

    private int[] calculateNumLiteralTruths(ArrayList<Clause> clauses, int arraySize, ArrayList<Integer> ignoreList) {

        int[] literalTruths = new int[arraySize];

        for (Clause clause : clauses) {
            //count how many negations or non negations there are
            //whichever is the max, assign that truth value
            int clause1 = clause.getVariable1();
            int clause2 = clause.getVariable2();
            int variableValue1 = clause.getVariable1Val();
            int variableValue2 = clause.getVariable2Val();

            if (!ignoreList.contains(variableValue1)) {
                if (variableValue1 > 0)
                    literalTruths[clause1 - 1]++;
                else
                    literalTruths[(clause1 + numberOfVariables) - 1]++;
            }
            if (!ignoreList.contains(variableValue2)) {
                if (variableValue2 > 0)
                    literalTruths[clause2 - 1]++;
                else
                    literalTruths[(clause2 + numberOfVariables) - 1]++;
            }
        }
        return literalTruths;

    }

    public ArrayList<Clause> getClauses() {
        return clauses;
    }

    private int literalNum(int index) {//returns the variable number, ie X1 or X2
        int literalNum;

        if (index > numberOfVariables - 1)//this means its second half so it would be false
            literalNum = (index % numberOfVariables) + 1;
        else
            literalNum = index + 1;

        return literalNum;
    }

    private int getLargestIntIndex(int[] literals) {
        int intIndex, largest;
        largest = intIndex = 0;

        for (int i = 0; i < literals.length; i++) {
            if (literals[i] > largest) {
                largest = literals[i];
                intIndex = i;
            }
        }
        return intIndex;
    }

    private boolean getBool(int[] boolMap, int variable) {
        return boolMap[variable % numberOfVariables] == 1;
    }

    public int getMaxTrues(){
        return maxTrues;
    }

    /*
    *  Utilized in the brute force method
    *  increments the current truth assignments to
    *  check every possibility
    */
    private int[] incrementBoolMap(int[] boolMap) {
        //does binary addition to try every combination of True and False
        int carry, currentValue, counter;
        carry = 1;
        counter = 0;
        do {
            currentValue = boolMap[counter];
            if (currentValue + carry == 2) {
                boolMap[counter] = 0;
                carry = 1;
            } else {
                boolMap[counter] = 1;
                carry = 0;
            }
            counter++;
        } while (counter < boolMap.length && carry != 0);

        return boolMap;
    }

    //returns the total number of clauses satisfied by the current True False assignment
    public int countSolutionTrues(ArrayList<Clause> clauses, int[] solution) {
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
            if (variable1Neg)
                mapBool1 = !mapBool1;
            if (variable2Neg)
                mapBool2 = !mapBool2;
            if (mapBool1 || mapBool2)
                totalTrues++;
        }

        return totalTrues;
    }

    //Debugging tool to verify total clause assignment for strings of T and F
    public int[] createSolution(String tfString) {
        int[] solution = new int[tfString.length()];
        for (int i = 0; i < tfString.length(); i++) {
            char ch = tfString.charAt(i);
            if (ch == 'T') {
                solution[i] = 1;
            } else
                solution[i] = 0;
        }
        return solution;
    }

    @Override
    public String toString() {

        String solutionString = "";

        for (int bool : solution) {
            if (bool == 1)
                solutionString += "T";
            else
                solutionString += "F";
        }
        String output =
                "\nMax Number of Trues: " + maxTrues +
                "\nSolution: " + solutionString;
        return output;
    }

    class BranchBoundObj {

        String truthAs;
        int bound;

        public BranchBoundObj(String truthAs, int bound) {
            this.truthAs = truthAs;
            this.bound = bound;
        }

    }

}


//input reader
class ReadTextFile {

    //read and return an array list of boolean clauses from an input file
    public static Max2Stat readClauseTextFile(String fileName) {

        BufferedReader bufferReader = null;
        String booleanClause;
        String[] booleanSplit;
        ArrayList<Clause> tempList1, tempList2;
        ArrayList<Clause> clauses = new ArrayList<>();
        HashMap<Integer, ArrayList<Clause>> variables = new HashMap<>();
        int firstClause, secondClause, numberOfVariables, largestVariable, firstClauseAbs, secondClauseAbs, smallestVariable;
        smallestVariable = 1000000000;//maximize smallest variable
        largestVariable = 0;
        try {
            bufferReader = new BufferedReader(new java.io.FileReader(fileName));
            while ((booleanClause = bufferReader.readLine()) != null) {
                if (!booleanClause.equals("")) {//added in case input.txt has empty lines
                    booleanSplit = booleanClause.split(" ");
                    if (booleanSplit.length == 2) {//added in case input.txt has less than 2 items after split
                        firstClause = Integer.parseInt(booleanSplit[0]);
                        secondClause = Integer.parseInt(booleanSplit[1]);
                        Clause clause = new Clause(firstClause, secondClause);
                        clauses.add(clause);
                        firstClauseAbs = Math.abs(firstClause);
                        secondClauseAbs = Math.abs(secondClause);

                        ArrayList<Clause> clauseList1 = variables.get(firstClauseAbs);
                        ArrayList<Clause> clauseList2 = variables.get(secondClauseAbs);
                        //creating hashmap for variable1
                        //this code was intended for branch and bound
                        if (clauseList1 == null) {
                            tempList1 = new ArrayList<>();
                            tempList1.add(clause);
                            variables.put(firstClauseAbs, tempList1);
                        } else {
                            tempList1 = variables.get(firstClauseAbs);
                            tempList1.add(clause);
                            variables.put(firstClauseAbs, tempList1);
                        }
                        //creating hashmap for variable2
                        if (clauseList2 == null) {
                            tempList2 = new ArrayList<>();
                            tempList2.add(clause);
                            variables.put(secondClauseAbs, tempList2);
                        } else {
                            tempList2 = variables.get(secondClauseAbs);
                            tempList2.add(clause);
                            variables.put(secondClauseAbs, tempList2);
                        }
                        //Need to fix the issue of how I am looking up the solution value
                        if (firstClauseAbs > largestVariable)
                            largestVariable = firstClauseAbs;
                        if (secondClauseAbs > largestVariable)
                            largestVariable = firstClauseAbs;
                        //get the smallest variable to skip if there is a big chunk missing
                        if (firstClauseAbs < smallestVariable)
                            smallestVariable = firstClauseAbs;
                        if (secondClauseAbs < smallestVariable)
                            smallestVariable = secondClauseAbs;
                    }
                }
            }
            bufferReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File " + fileName + " not found.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to read file name: " + fileName);
            System.exit(0);
        }
        clauses.trimToSize();//make sure length is amount of items
        numberOfVariables = largestVariable;
        return new Max2Stat(clauses, numberOfVariables, variables, smallestVariable);
    }

}

//Clause statement
class Clause {

    private int variable1Val;//negated or not
    private int variable2Val;

    private int variable1;//variable number
    private int variable2;

    private boolean variable1Bool, variable2Bool;

    Clause(int variable1Bool, int variable2Bool) {
        //set the boolean values
        this.variable1Bool = variable1Bool <= 0;
        this.variable2Bool = variable2Bool <= 0;
        variable1Val = variable1Bool;
        variable2Val = variable2Bool;
        variable1 = Math.abs(variable1Val);
        variable2 = Math.abs(variable2Val);

    }

    @Override
    public String toString() {
        return "( x" + variable1Val + " v x" + variable2Val + " )";
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
