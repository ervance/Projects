/*
Name: Eric Vance
Class: Comp 282
Assignment 1
Date handed in: 9/9/2015
Description: The WordSquare class constructs a list of words from a chosen word size 
and text file that contains a list of words.  The constructor will build a list of 
words form the text file that are all the size of the chosen size.  

It contains a buildSquare method that will build a square with either a provided 
word or a randomly chosen word if nothing was provided. As it builds the square 
with words it will check to make sure that every column and row create a word that 
exists from the generated word list.  It will attempt to build 1,000,000 times at 
that point it will exit the program and return that square was not found if it did 
not complete it within the 1,000,000 tries.

The range class is designed to aid the buildSquare method, and holds a range that a 
random word will be chosen from.  The range is determined by the prefix of the 
currently populated row or column.

*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; // for BufferedReader
import java.lang.Math;

class WordSquare { 
	private int word_size, word_ct; 
	private String word_list[]; 
	private char result[][];
	
	// sets word_size and word_ct and loads the word-size words into word_list 
	public WordSquare(int word_size, String fileName) throws IOException { 
	 // get the word size 
		this.word_size = word_size; 
		word_ct = 0; 

		String inputLine;
		BufferedReader inFile;
		// first count the number of words that are the give size in file provide 
		inFile = new BufferedReader(new FileReader(fileName));
		inputLine = inFile.readLine(); 
		
		while (inputLine != null) { 
			if (inputLine.length() == word_size) 
				word_ct++; 
			inputLine = inFile.readLine(); 
		} 

		inFile.close();
		inFile = new BufferedReader(new FileReader(fileName));
		inputLine = inFile.readLine();
		//Second put those words into an array that has the exact size needed
		//Create the array with the size needed
		word_list = new String [word_ct];
		int pos = 0;
		//This populates the array word_list with the correct sized words
		while (inputLine != null) {
			if(inputLine.length() == word_size){
				word_list[pos] = inputLine;
				pos++;
			}
			inputLine = inFile.readLine();
		}

	}

	// try to build a word square of size word_size 
	// uses the algorithm as described in the hand out 
	public boolean buildSquare(String givenWord) { 
		String w, randWord, row_pref, col_pref;
		int ct, row, col, round; 
		boolean OK = false;
		boolean out_of_range = false;
		boolean row_prefix_pop, col_prefix_pop; 
		Range wordRange;

		// initialize the square to contain *'s 
		result = new char[word_size][word_size]; 
		for (row = 0; row < word_size; row++) { 
			for (col = 0; col < word_size; col++) 
				result[row][col] = '*'; 
		}
		// try to build the required word square 
		for (ct = 0; ct < 1000000 && !OK; ct++) {
			
			out_of_range = false; //reset the out_of_range flag to try again
			//round is keeping track of the column and or row that the program should insert into
			//this will reset it to 0 if an incorrect square was built
			round = 0;
			while( round < word_size && !out_of_range && !OK){
			//flags that will be set if out of range or finished completing the square
				
				if (round == 0){
					row_pref = "";
					//reset the square with '*' because and attempt failed.
					for (row = 0; row < word_size; row++) { 
						for (col = 0; col < word_size; col++) 
							result[row][col] = '*'; 
					}//for
					//Populate with the given word in the first row
					if( givenWord.length() != 0){	
						for (row = round, col = 0; col < word_size; col++){
								result[row][col] = givenWord.charAt(col);
						}//for
							
					} else {
						//populate the first row with a random word if there was no word provided
						randWord = word_list[(int)(Math.random()*word_list.length)];
						for (row = round, col = 0; col < word_size; col++){
							result[row][col] = randWord.charAt(col);
						}
					}//if else
					
				}else {
					
					//if it is not the first round, begin finding the prefix
					row_prefix_pop = false;
					row_pref = "";
					for(row = round, col = 0; col < result[row].length && !row_prefix_pop; col++){
						if (result[row][col] != '*'){
							row_pref = row_pref + result[row][col];
						} else {
							//The flag is set to exit the loop if a '*' was hit, 
							//meaning the prefix was created
							row_prefix_pop = true;
						}//if else
					}//for
			
					//find the range of where to randomly choose a word from the 
					//row prefix that was found
					wordRange = findRange(row_pref);
					
					//check to see if there was no word found in the list with the
					//prefix, set the flag to exit the loop if true
					if (wordRange.getTopRange() == -1 && wordRange.getBotRange() == -1){
						out_of_range = true;
					} else {
						//generates a random word with the range and adds it to the corresponding row.  
						//Row is determined by the round.
						w = word_list[wordRange.getBotRange() + 
						  (int)(Math.random()*(wordRange.getTopRange() - wordRange.getBotRange()))];
						for (col = 0; col < result[row].length; col++){
							result[round][col] = w.charAt(col);
						}//for
					}//if else
				}//if else
					
				
				//Build the column if its not out of range
				if(!out_of_range){
					//this will help leave the for loop if the prefix becomes populated
					//before the length of the word is reached
					col_prefix_pop = false;
					col_pref = "";
					//Find the prefix for the current column
					for(row=0; row < result.length && !col_prefix_pop; row++){
						if (result[row][round] != '*'){
							col_pref = col_pref + result[row][round];
						} else {
							//The flag is set to exit the loop if a '*' was hit, meaning
							//the prefix was created
							col_prefix_pop = true;
						}//if else
					}//for
						
					//find the range of where to randomly choose a word from the
					//column prefix that was found
					wordRange = findRange(col_pref);
					//check to see if there was no word found in the list with that prefix, 
					//set the flag to exit the loop if an out of range was found
					if (wordRange.getTopRange() == -1 && wordRange.getBotRange() == -1){
						out_of_range = true;
					} else if (wordRange.getTopRange() == wordRange.getBotRange()){
						//The last thing to check in a square is the last column, if the last 
						//column's top and bot range are equal, the square is complete
						OK = true;
					} else {
						//generates a random word for the column with the range if the square is
						//not completed
						w = word_list[wordRange.getBotRange() + 
						  (int)(Math.random()*(wordRange.getTopRange() - wordRange.getBotRange()))];
						
						//add the random word to the column corresponding to the round
						for (row = 0; row < result.length; row++){
							result[row][round] = w.charAt(row);
						}//for
					}//if else
					
				}//if
				//move onto the next row and column by increasing the round
				round++;
			}//while
		}
		//returns if the square was found
		return OK;
	}//buildSquare

	// return the range of indices in word_list that have prefix prefix 
	// return a range of (-1, -1) if there are none 
	// You must use binary search to find the general location of the range 
	public Range findRange(String prefix) { 
		Range r = new Range();
		int bot_range, top_range, middle;
		boolean  valid;
		
		//call the binary search to return an int where we'll start to determine our range
		//it will return -1 if no word was found
		middle = bSearch(prefix);
		bot_range = middle;
		top_range = middle;
		
		//find the bottom range
		//if middle = -1, then no word exists in the list and we skip this
		//and if prefix size is = to the word size, we found a full word and can skip finding the range
		if(middle != -1 && prefix.length() != word_size){
			//a flag to check and make sure we dont go out of bounds when walking the range
			valid = true;
			//as long as the range is no already at the bottom, start walking it lower
			if(bot_range-1 > 0){
				while (word_list[bot_range-1].startsWith(prefix) && valid){
					if(bot_range-1 > 0)
						bot_range--;
					else
						//if the last index 0 is hit, this will be set to end the loop
						valid = false;
				}//while
			}//if

			
			//find the top range
			valid = true;
			//as long as the range is not at the top, start walking it up
			if(top_range + 1 < word_list.length){
				while (word_list[top_range].startsWith(prefix) && valid){
					if(top_range + 1 < word_list.length)
						top_range++;
					else
						//set if we pass the top index of the list, ends the loop
						valid = false;
				}//while
			}//if
		}//if
		
		//set the ranges and return the range object
		r.setTopRange(top_range);
		r.setBotRange(bot_range);
		return r;
	}//findRange

	private int bSearch(String prefix){
		int bot_range = 0;
		int top_range = word_list.length - 1;
		int middle = ((bot_range + top_range)/2 );
		boolean found;
		//start the binary search by setting the found flag to false
		
		found = false;
			
		//while a word starting with the prefix is not found, it will search
		while (!found){
				//reset the middle value to the new top and bottom
			middle = ((bot_range + top_range) / 2);
				
			if(word_list[middle].substring(0,prefix.length()).compareTo(prefix) == 0 ){
				//found approximately where the range begins, exit the loop by setting found to true
				found = true;

			} else if (word_list[middle].substring(0,prefix.length()).compareTo(prefix) < 0 ) {
				//if the prefix is alphabetically after the "middle" word, eliminate the bottom half
				bot_range = middle + 1;
					
			} else if (word_list[middle].substring(0,prefix.length()).compareTo(prefix) > 0) {
				///if the prefix is alphabetically before the "middle" word, eliminate the top half
					
				top_range = middle - 1;
					
			}//if else if
				
			if (bot_range >= top_range && !found) {
				//there was no prefix return a -1 and set the found flag
				middle = -1;
				found = true;
			}//if
				
		}//while
		//contains the value of where to start the range, or -1 if it was not found.
		return middle;
	}//bSearch

	public String toString() {
		//Generate the word square as string object
		int row, col;
		String wordSquare = "";
		//build the word square string
		for(row = 0; row < result.length; row++){
			for (col = 0; col < result[row].length; col++){
				wordSquare = wordSquare + result[row][col] + " ";
			}
			wordSquare = wordSquare + "\n";
		}
		return wordSquare;
	}//toString
	
	public static String myName() {
		//returns my name as a string
		return "Eric Vance";
	}//myName
	

}//WordSquare


class Range {
	//class holding two int variables that will contain the range of the words to randomly choose from.
	private int top_range;
	private int bot_range;
	
	public Range(){
		top_range = 0;
		bot_range = 0;
	}//Range
	
	//simple getters and setters
	public int getTopRange(){
		return top_range;
	}//getTopRange
	
	public int getBotRange(){
		return bot_range;
	}//getBotRange
	
	public void setTopRange(int value){
		top_range = value;
	}//setTopRange
	
	public void setBotRange(int value){
		bot_range = value;
	}//setBotRange

}//Range
