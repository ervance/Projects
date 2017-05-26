#!/bin/bash
j=0
for j in 1 2
	do
	if [[ $j == 1 ]]; then
		#statements
		TEST="ONLY"
	elif [[ $j == 2 ]]; then
		#statements
		TEST="MIX"
	# elif [[ $j == 3 ]]; then
	# 	#statements
	# 	TEST="NT"
	# elif [[ $j == 4 ]]; then
	# 	#statements
	# 	TEST="MIX"

	fi
	printf "Starting $TEST tests...\n"
# 	# Run through fingerprints
	i=0
	for i in 1 2 3 4 5
	do
		
		FILENAME="speedtest$TEST$i.pcap"
		printf "Downloading Remote Capture $FILENAME...\n"
		CAPUTREDIRECTORIES="~/Desktop/speedtest/$TEST/"
		FILETODOWNLOAD="$CAPUTREDIRECTORIES$FILENAME"
		scp evance@192.168.1.112:$FILETODOWNLOAD ~/
		printf "FromDump($FILENAME, STOP true) -> st :: SpeedTest

		st[0] -> ToDump(shaped.pcap)
		st[1] -> ToDump(unshaped.pcap)" > ~/click-test-conf/speedtest.conf

		printf "Running Click Router...\n"
	 	~/click-inst/bin/click ~/click-test-conf/speedtest.conf 
	 	printf "Reporting Results\n"
		
		SHAPED="shaped.pcap"
		UNSHAPED="unshaped.pcap"
		SIZESHAPED=$(du -sb $SHAPED | awk '{ print $1 }')
		SIZEUNSHAPED=$(du -sb $UNSHAPED | awk '{ print $1 }')

		tshark -r $FILENAME -q -z conv,tcp -n > rawstatistics.txt
		tshark -r $SHAPED -q -z conv,tcp -n > shapedtcpstatistics.txt
		tshark -r $UNSHAPED -q -z conv,tcp -n > unshapedtcpstatistics.txt
		RESULTS="speedtestresults$TEST$i.txt"
		# cat rawstatistics.txt >> "raw$i.txt"
		# cat shapedtcpstatistics.txt >> "raw$i.txt"
		# cat unshapedtcpstatistics.txt >> "raw$i.txt"
		python pcaphost.py rawstatistics.txt shapedtcpstatistics.txt unshapedtcpstatistics.txt $TEST $FILENAME $SIZESHAPED > $RESULTS
		printf "Cleaning up pcap files...\n"
		scp $RESULTS evance@192.168.1.112:$CAPUTREDIRECTORIES
		rm shaped.pcap unshaped.pcap $FILENAME shapedtcpstatistics.txt unshapedtcpstatistics.txt rawstatistics.txt $RESULTS

	done
	printf "Finished $TEST...\n"
done
printf "Sending Results...\n"
printf "Done.\n"