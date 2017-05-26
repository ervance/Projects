#!/bin/bash
printf "Beginning Fast.com pcap captures...\n"
sleep 3
mkdir -p ~/Desktop/fast/ONLY
printf "Starting the Fast.com Only...\n"
name="fastONLY"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	printf "Start test...\n"
	# open -a Safari https://www.fast.com
	sleep 20
	printf "Making sure to wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	kill -INT $pid
	sleep 7

done

mv fastONLY*.pcap ~/Desktop/fast/ONLY


mkdir -p ~/Desktop/fast/MIXNOFLX
printf "Starting the Fast.com test mixed with Traffic...\n"
name="fastMIXNOFLX"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	printf "Opening google.com...\n"
	open -a Safari http://www.google.com
	open -a Safari "https://www.google.com/search?client=safari&rls=en&q=how+to+open+a+page+and+click+a+link+in+shell+script&ie=UTF-8&oe=UTF-8#q=how+many+lives+to+cats+have?"
	sleep 1
	printf "Opening reddit.com...\n"
	open -a Safari https://www.reddit.com
	sleep 1
	printf "Start test...\n"
	printf "Starting Fast.com...\n"
	sleep 5
	# open -a Safari https://www.fast.com
	open -a Safari https://www.reddit.com/r/aww/
	sleep 2
	printf "Opening github.com...\n"
	open -a Safari https://www.github.com
	sleep 1
	printf "Opening bing.com...\n"
	open -a Safari https://www.bing.com
	sleep 1
	sleep 16
	printf "Wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	sleep 4
	kill -INT $pid
	sleep 7
done

mv fastMIXNOFLX*.pcap ~/Desktop/fast/MIXNOFLX


mkdir -p ~/Desktop/fast/MIXFLX
printf "Starting the Fast.com test mixed with Traffic...\n"
name="fastMIXFLX"
for i in 1 2 3 4 5
do
	file_name="$name$i.pcap"
	printf "Starting test $i...\n"
	printf "Saving to $file_name...\n"
	(tcpdump -i en0 -s 65535 -w $file_name) &
	sleep 7
	printf "Start test...\n"
	sleep 5
	# printf "Starting Fast.com...\n"
	# open -a Safari https://www.fast.com
	printf "Opening google.com...\n"
	open -a Safari http://www.google.com
	open -a Safari "https://www.google.com/search?client=safari&rls=en&q=how+to+open+a+page+and+click+a+link+in+shell+script&ie=UTF-8&oe=UTF-8#q=how+many+lives+to+cats+have?"
	sleep 1
	printf "Opening netflix.com...\n"
	open -a Safari https://www.netflix.com
	sleep 1
	printf "Opening reddit.com...\n"
	open -a Safari https://www.reddit.com
	open -a Safari https://www.reddit.com/r/aww/
	sleep 1
	printf "Opening github.com...\n"
	open -a Safari https://www.github.com
	sleep 16
	printf "Wait for tcpdump to finish...\n"
	pid=$(ps -e | pgrep tcpdump)
	sleep 4
	kill -INT $pid
	sleep 7
done

mv fastMIXFLX*.pcap ~/Desktop/fast/MIXFLX