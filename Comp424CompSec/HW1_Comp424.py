#!/usr/bin/python
import itertools
import infer_spaces
from math import log
import cipher_heuristics
import pycipher

'''
	Main program ran with Python 2.7
	author: Eric Vance
	external libraries: pycipher, itertools
		***NOTE*** must install pycipher using pip if needed
		pip install pycipher
	external code: infer_spaces method in infer_spaces.py
	Additional python files included in same directory
	to run correctly: cipher_heuristics.py, infer_spaces.py, linux_dict.txt

'''

'''
	Creates a string table to reference the possible columnar transpose keys
	based on the analysis
'''
def create_ct_key_freq_str(possible_key_freq):
	key_freq_string = ''
	line_counter = 0
	for key in sorted(possible_key_freq, key=possible_key_freq.get, reverse=True):
		if line_counter % 5 == 0:
			key_freq_string += "\n | Key Length: " + str(key) + " Frequency: " + str(possible_key_freq[key]) + "  |  "
		else:
			key_freq_string += " | Key Length: " + str(key) + " Frequency: " + str(possible_key_freq[key]) + "  |  "
		line_counter += 1

	return key_freq_string



'''
	Takes a word list and determines if it should be looked at.
	Criteria:
		List does not contain more than 6 single letter words.
'''
def valid_list(word_list):
	counter = 0
	is_valid_word_list = True

	'''
	check the list for 1 char words.
	1 char word means a dictionary word was not found.
	add to the counter anytime a 1 char word is found.
	a solution most likely will not have more than 10 single
	char words.
	'''
	for word in word_list:
		if counter < 10:
			if len(word) == 1:
				counter += 1
		else:
			is_valid_word_list = False

	return is_valid_word_list


'''
Runs analysis on the encrypted string
returns the encrypted string after you choose a shift
'''
def simple_shift_analysis(e_string):
	#anaylise against top 5 letters
	cipher_heuristics.dist_from_top_5_letter(e_string)
	#take choice of shift key
	caesar_key = int(raw_input('What distance would you like to try for Caesar?: '))
	#decyper the shift based on key selected
	e_string = pycipher.Caesar(caesar_key).decipher(e_string).lower()
	#display to user
	print '\nCaesar Shifted Encrypted String: \n' + e_string

	return e_string


def columnar_transpose_analysis(e_string):
	digraph_lengths = cipher_heuristics.digraph_key_lengths(e_string)
	possible_key_freq = cipher_heuristics.length_frequency(digraph_lengths)
	print '\n' + '*' * 50 + '\n'
	print("Possible Transpose Key Lengths: ")
	print(digraph_lengths)

	#create columnar transpose key table to display
	key_freq_str = create_ct_key_freq_str(possible_key_freq)
	print key_freq_str

	# open dictionary file for infer_spaces
	# Build a cost dictionary, assuming Zipf's law and cost = -math.log(probability).
	with open('linux_dict.txt') as f:
		words = f.read().split()
	word_cost = dict((k, log((i + 1) * log(len(words)))) for i, k in enumerate(words))
	max_word = max(len(x) for x in words)
	# ------------------------------------------------------------------------------

	#set up loop to try different keys during for decryption
	key_len = -1
	#do not accept any keys out of the range 10
	while key_len > 10 or key_len < 0:
		key_len = int(raw_input("\nWhat length of key would you like to try?(exit with 0): "))
	#exit the columnar transpose decryption on 0
	while key_len != 0:

		#Create a string from 1 to selected key_len ie key_len = 7, '1234567'
		print('\nComputing key permutations...')
		key_string = ''
		for i in range(1, key_len + 1):
			key_string += str(i)

		#create a permutation list of all the possible permutations of the key
		#to try in decryption
		perm_list = list("".join(string) for string in itertools.permutations(key_string))
		perm_list = list(set(perm_list))


		possible_decryptions = [] #keeps track of successful/possible decryptions to display to user

		#attempt to find solutions with the selected key length
		with open('solutions.txt', 'w') as f:
			print("Decrypting plaintext...")
			#try each permutation of the key
			for permutation in perm_list:
				#decrypt using current key using picipher library
				plaintext = pycipher.ColTrans(permutation).decipher(e_string).lower()
				#use infer_spaces to try and find words in a non delimeter string
				word_list = infer_spaces.find_words(plaintext, word_cost, max_word, words)
				#check to see if the word_list returned was a vaild/possible solution
				#if it is write the word and key to the solutions.txt and possible_decryptions
				#otherwise ignore the solution
				if valid_list(word_list.split(" ")):
					f.write('Key is: ' + permutation + ' Plaintext: ' + word_list + '\n')
					possible_decryptions.append(word_list)

		#display possible decryptions
		print("\nPossible Decryptions: ")
		list_number = 1 #formatting/presentation purposes
		for string in possible_decryptions:
			print str(list_number) + '. ' + string
			list_number += 1
		print '\n' + '*' * 50 + '\n'

		#ask the user again in case no solutions were found, and if solutions were found, can exit on entering 0
		print "Possible Transpose Key Lengths:\n" + key_freq_str
		key_len = int(raw_input("\nWhat length of key would you like to try?(exit with 0): "))
		while key_len > 10 or key_len < 0:
			key_len = int(raw_input("\nWhat length of key would you like to try?(exit with 0): "))


def main():

	start_over = 'y'
	while start_over == 'y':
		print '*' * 50
		print 'COMP 424 Fall 2016 Homework 1\nAuthor: Eric Vance\nClass Tuesday 19:00'
		print '*' * 50 + '\n'

		#encrypted string
		e_string = "DRPWPWXHDRDKDUBKIHQVQRIKPGWOVOESWPKPVOBBDVVVDXSURWRLUEBKOLVHIHBKHLHBLNDQRFLOQ".lower()

		print 'String to decrypt ' + e_string + '\n'

		e_string = simple_shift_analysis(e_string)
		####################################
		#Begin columnar transpose analysis
		####################################
		columnar_transpose_analysis(e_string)
		start_over = ''
		#in case the simple shift was incorrect and need to start over
		while start_over != 'y' and start_over != 'n':
			start_over = raw_input("\nWould you like to start over and try a new simple shift key? y = yes n = no:  ")

	print '\nThank you for decrypting.  Have a great day!'

if __name__ == "__main__":
	main()