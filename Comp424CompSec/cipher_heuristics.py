''' Do heuristics to check for possible key lengths'''

import collections
import string
'''
	Finds the difference between the top 5 common and the letter you choose.
'''
def dist_from_top_5_letter(e_string):
	top5_common_diff = {'e' : 0, 't' : 0, 'a' : 0, 'o' : 0, 's' : 0}
	temp_common = top5_common_diff
	#counter will count the frequency of each letter in e_string
	ordered_freq = collections.Counter(e_string)
	print(ordered_freq)

	#check the users char choice against the top 5
	freq_char = raw_input('What character do you want to check? (enter 0 to skip): ')
	alphabet = string.ascii_lowercase

	while freq_char.lower() in alphabet:
		freq_char = freq_char.upper()
		for com_key in temp_common:
			#distance that the chosen char is from each char in the top
			#5 most frequent
			temp_common[com_key] = (ord(com_key) - ord(freq_char)) % 26
		print(freq_char + "\'s difference from top 5 letters is: \n")
		print(temp_common)
		print("\n" + "*" * 50 + "\n")
		temp_common = top5_common_diff

		print(ordered_freq)
		freq_char = raw_input('\nWhat character do you want to check?(input 0 to stop): ')


'''
	Shows frequency of the distance that shows up the most to provide a better idea of what the key could be
'''
def length_frequency(distance_dict):

	key_len_dict = { '1' : 0, '2' : 0, '3' : 0, '4' : 0, '5' : 0, '6' : 0, '7' : 0, '8' : 0, '9' : 0, '10' : 0}
	#Count how many times a distance appears for a digraph in order to see a distance pattern that could be
	#a key length
	for key in distance_dict:
		for key_len in key_len_dict:
			if distance_dict[key] == int(key_len):
				key_len_dict[key_len] += 1

	return key_len_dict


def digraph_key_lengths(e_string):

	common_digraphs_list = ['th', 'er', 'on', 'an', 're', 'he', 'in', 'ed', 'nd', 'ha', 'at', 'en', 'es', 'of', 'or',
							'nt', 'ea', 'ti', 'to', 'it', 'st', 'io', 'le', 'is', 'ou', 'ar', 'as', 'de', 'rt', 've']

	common_digraphs_lengths = {}

	#diagraphs heuristics
	for ch in range(len(e_string)):
		distance = 1
		occurance = 0
		i = ch
		#iterate through trying to find digraph matches
		#do until end of list or a distance is greater than 10
		#no need to go past 10 because key length is between 1 and 10
		while i < len(e_string) and distance <= 10:
			diagraph_s = e_string[ch] + e_string[i]
			if diagraph_s in common_digraphs_list:
				key = diagraph_s + ' - ' + str(occurance)
				common_digraphs_lengths[key] = distance
				occurance +=1
			else:
				distance += 1
			i += 1

	return common_digraphs_lengths



