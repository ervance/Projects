#!/bin/bash
printf "Beginning tool tests and pcap captures...\n"
sleep 3
printf "Starting the iperf3 test using TCP...\n"
mkdir -p ~/Desktop/iperf3tests/TCP
name="iperf3testTCP"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	iperf3 -c 192.168.1.115
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	kill -INT $pid
	sleep 7
done

mv iperf3testTCP*.pcap ~/Desktop/iperf3tests/TCP

mkdir -p ~/Desktop/iperf3tests/UDP
printf "Starting the iperf3 test using UDP...\n"
name="iperf3testUDP"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	iperf3 -u -c 192.168.1.115
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	kill -INT $pid
	sleep 7
done

mv iperf3testUDP*.pcap ~/Desktop/iperf3tests/UDP

mkdir -p ~/Desktop/iperf3tests/NT
printf "Starting the iperf3 test using Normal Traffic...\n"
name="iperf3testNT"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	printf "Opening google.com...\n"
	open -a Safari http://www.google.com
	sleep 8
	printf "Opening netflix.com...\n"
	open -a Safari https://www.netflix.com
	sleep 8
	printf "Opening reddit.com...\n"
	open -a Safari https://www.reddit.com
	sleep 8
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	kill -INT $pid
	sleep 7
done

mv iperf3testNT*.pcap ~/Desktop/iperf3tests/NT


mkdir -p ~/Desktop/iperf3tests/MIX
printf "Starting the iperf3 test using MIX Tests and Traffic...\n"
name="iperf3testMIX"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	printf "Opening google.com...\n"
	open -a Safari http://www.google.com
	sleep 4
	if [ $(( $i % 2 == 0 )) ]
		then
			printf "iperf3 test using TCP...\n"
			iperf3 -c 192.168.1.115
		else
			printf "iperf3 test using UDP...\n"
			iperf3 -u -c 192.168.1.115
	fi
	printf "Opening reddit.com...\n"
	open -a Safari https://www.reddit.com
	sleep 4
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	kill -INT $pid
	sleep 7
done

mv iperf3testMIX*.pcap ~/Desktop/iperf3tests/MIX
