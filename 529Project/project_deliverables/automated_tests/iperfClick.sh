#!/bin/bash
for j in 1 2 3 4
	do
	if [[ $j == 1 ]]; then
		#statements
		TEST="TCP"
	elif [[ $j == 2 ]]; then
		#statements
		TEST="UDP"
	elif [[ $j == 3 ]]; then
		#statements
		TEST="NT"
	elif [[ $j == 4 ]]; then
		#statements
		TEST="MIX"

	fi
	printf "Starting $TEST tests...\n"
	# Run through fingerprints
	for i in 1 2 3 4 5
	do
		
		FILENAME="iperf3test$TEST$i.pcap"
		printf "Downloading Remote Capture $FILENAME...\n"
		CAPUTREDIRECTORIES="~/Desktop/iperf3tests/$TEST/"
		FILETODOWNLOAD="$CAPUTREDIRECTORIES$FILENAME"
		scp evance@192.168.1.112:$FILETODOWNLOAD ~/
		printf "FromDump(iperf3test$TEST$i.pcap, STOP true)
		  -> iperf :: Iperf

		iperf[0] -> ToDump(shaped.pcap)
		iperf[1] -> ToDump(unshaped.pcap)" > ~/click-test-conf/iperf.conf

		printf "Running Click Router...\n"
	 	~/click-inst/bin/click ~/click-test-conf/iperf.conf 
	 	printf "Reporting Results\n"
		
		SHAPED="shaped.pcap"
		UNSHAPED="unshaped.pcap"
		SIZESHAPED=$(du -sb $SHAPED | awk '{ print $1 }')
		SIZEUNSHAPED=$(du -sb $UNSHAPED | awk '{ print $1 }')
		
		SHAPED="shaped.pcap"
		UNSHAPED="unshaped.pcap"
		SIZESHAPED=$(du -sb $SHAPED | awk '{ print $1 }')
		SIZEUNSHAPED=$(du -sb $UNSHAPED | awk '{ print $1 }')

		if [[ $j == 2 ]]; then
			tshark -r $FILENAME -q -z conv,udp -n > rawstatistics.txt
			tshark -r $SHAPED -q -z conv,udp -n > shapedtcpstatistics.txt
			tshark -r $UNSHAPED -q -z conv,udp -n > unshapedtcpstatistics.txt
		else
			tshark -r $FILENAME -q -z conv,tcp -n > rawstatistics.txt
			tshark -r $SHAPED -q -z conv,tcp -n > shapedtcpstatistics.txt
			tshark -r $UNSHAPED -q -z conv,tcp -n > unshapedtcpstatistics.txt
		fi
		
		RESULTS="iperf3results$TEST$i.txt"
		python pcaphost.py rawstatistics.txt shapedtcpstatistics.txt unshapedtcpstatistics.txt $TEST $FILENAME $SIZESHAPED > $RESULTS
		printf "Cleaning up pcap files...\n"
		scp $RESULTS evance@192.168.1.112:$CAPUTREDIRECTORIES
		rm shaped.pcap unshaped.pcap $FILENAME shapedtcpstatistics.txt unshapedtcpstatistics.txt rawstatistics.txt $RESULTS

	done
	printf "Finished $TEST...\n"
done
printf "Sending Results...\n"
printf "Done.\n"
