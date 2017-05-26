#!/bin/bash
printf "Beginning speedtest tests and pcap captures...\n"
sleep 3
mkdir -p ~/Desktop/speedtest/ONLY
printf "Starting the speedtest test using Normal Traffic...\n"
name="speedtestONLY"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	# open -a Safari http://beta.speedtest.net/run
	printf "Start speed test....\n"

	sleep 50
	printf "Ending test 3 seconds...\n"
	sleep 3
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	kill -INT $pid
	sleep 7
done

mv speedtest*.pcap ~/Desktop/speedtest/ONLY


mkdir -p ~/Desktop/speedtest/MIX
printf "Starting the speedtest test using Test mixed with Traffic...\n"
name="speedtestMIX"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	printf "Start speedtest...\n"
	sleep 10
	# open -a Safari http://beta.speedtest.net/run
	
	# (speedtest-cli > /dev/null) &
	# pidspeed=$!
	printf "Opening google.com...\n"
	open -a Safari http://www.google.com
	sleep 4
	printf "Opening reddit.com...\n"
	open -a Safari https://www.reddit.com
	sleep 4
	printf "Opening github.com...\n"
	open -a Safari https://www.github.com
	sleep 4
	printf "Opening netflix.com...\n"
	open -a Safari https://www.netflix.com
	sleep 4
	sleep 34
	printf "Ending 3 seconds...\n"
	sleep 3
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	# printf "Waiting if speedtest is running still...\n"
	# wait $pidspeed
	kill -INT $pid
	sleep 7
done

mv speedtestMIX*.pcap ~/Desktop/speedtest/MIX
