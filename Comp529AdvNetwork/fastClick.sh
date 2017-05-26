#!/bin/bash
TYPE="Fast.com"
# rm fastresults.txt
# touch fastresults.txt
j=0
for j in 1 2 3
	do
	if [[ $j == 1 ]]; then
		#statements
		TEST="ONLY"
	elif [[ $j == 2 ]]; then
		#statements
		TEST="MIXNOFLX"
	elif [[ $j == 3 ]]; then
		#statements
		TEST="MIXFLX"

	fi
	printf "Starting $TEST tests...\n"
 	# Run through fingerprints
	i=0
	CAPUTREDIRECTORIES="~/Desktop/fast/$TEST/"
	# STATFILE="statistics$TEST.csv"
	# printf "$TEST,raw,shaped,unshaped" > $STATFILE
	for i in 1 2 3 4 5
	do
		
		FILENAME="fast$TEST$i.pcap"
		printf "Downloading Remote Capture $FILENAME...\n"
		FILETODOWNLOAD="$CAPUTREDIRECTORIES$FILENAME"
		scp evance@192.168.1.112:$FILETODOWNLOAD ~/
		printf "FromDump($FILENAME, STOP true) -> f :: FastElement

f[0] -> ToDump(shaped.pcap)
f[1] -> ToDump(unshaped.pcap)" > ~/click-test-conf/fast.conf

		printf "Running Click Router...\n"
	 	~/click-inst/bin/click ~/click-test-conf/fast.conf 
	 	printf "Reporting Results\n"
		
		SHAPED="shaped.pcap"
		UNSHAPED="unshaped.pcap"
		SIZESHAPED=$(du -sb $SHAPED | awk '{ print $1 }')
		SIZEUNSHAPED=$(du -sb $UNSHAPED | awk '{ print $1 }')

		tshark -r $FILENAME -q -z conv,tcp -n > rawstatistics.txt
		tshark -r $SHAPED -q -z conv,tcp -n > shapedtcpstatistics.txt
		tshark -r $UNSHAPED -q -z conv,tcp -n > unshapedtcpstatistics.txt
		RESULTS="fastresults$TEST$i.txt"
		# cat rawstatistics.txt >> "raw$i.txt"
		# cat shapedtcpstatistics.txt >> "raw$i.txt"
		# cat unshapedtcpstatistics.txt >> "raw$i.txt"
		python pcaphost.py rawstatistics.txt shapedtcpstatistics.txt unshapedtcpstatistics.txt $TEST $FILENAME $SIZESHAPED > $RESULTS
		printf "Cleaning up pcap files...\n"
		scp $RESULTS evance@192.168.1.112:$CAPUTREDIRECTORIES
		rm shaped.pcap unshaped.pcap $FILENAME shapedtcpstatistics.txt unshapedtcpstatistics.txt rawstatistics.txt $RESULTS

	done
	printf "Finished $TEST...Sending results and cleaning up...\n"
	# scp $STATFILE evance@192.168.1.112:$CAPUTREDIRECTORIES

	# rm $STATFILE
done
printf "Done.\n"