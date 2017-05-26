#!/bin/env python
import sys,os,socket,re
from tabulate import tabulate

def get_statistics(file, type, args):
    test_type = str(args[4])
    test_file = str(args[5])
    file_size = str(args[6])


    # strip first 5 and last line
    tshark_split = file.split('\n')[5:-1]

    '''
    IP of TCP Stream Dst ----- Frames ---- Bytes total
    '''
    host_list = []

    stat_split = [line.split() for line in tshark_split if len(line.split()) > 10]
    stats = [(item[2].split(':')[0], item[7], item[8], item[9], item[10]) for item in stat_split]
    connection = 0
    total_frames = 0
    if stat_split:
        for ip in stats:
            try:
                connection += 1
                lookup = socket.gethostbyaddr(ip[0])
                host_name = lookup[0]
                ip_addr = lookup[2][0]
                frames = ip[1]
                total_frames += int(frames)
                b = ip[2]
                rel_start = ip[3][:-6]
                duration = ip[4][:-2]
                host_ip = (connection, host_name, ip_addr, frames, b, rel_start, duration)
                host_list.append(host_ip)
            except socket.error:
                host_name = 'N\A'
                ip_addr = ip[0]
                frames = ip[1]
                total_frames += int(frames)
                b = ip[2]
                rel_start = ip[3][:-6]
                duration = ip[4][:-2]
                host_ip = (connection, host_name, ip_addr, frames, b, rel_start, duration)
                host_list.append(host_ip)
                total_frames += int(frames)
                connection -= 1

        rows = []
        for h_ip in host_list:
            rows.append(list(h_ip))

        output_str = tabulate(rows, headers=[' No.', 'Host', "IP", "Total Frames", "Total Bytes", "Relative Start",
                                             "Duration"])

        output_header = tabulate([['Click Element:', test_type],
                                  ['Test PCAP', test_file],
                                  ['Click PCAP', type],
                                  ['Size Click PCAP', file_size],
                                  ['Details:', str(len(host_list)) + 'TCP Connections'],
                                  ['Total Packets:', str(total_frames)]
                                  ])
        output_header += "\n\n**Note: If TCP Dst IP DNS did not resolve it was not added in this table***\n"
        output = output_header + output_str + "\n\n"

        # update_csv(test_type, host_list)
    else:
        output_str = 'No Shaped Files'
        output_header = tabulate([['Click Element:', test_type],
                                  ['Test PCAP', test_file],
                                  ['Click PCAP', type],
                                  ['Size Click PCAP', file_size],
                                  ['Details:', str(len(host_list)) + 'TCP Connections'],
                                  ['Total Packets:', str(total_frames)]
                                  ])
        output_header += "\n\n**Note: If TCP Dst IP DNS did not resolve it was not added in this table***\n"
        output = output_header + output_str + "\n\n"
    # print type + " " + str(total_frames)
    return output

# ############## ############## ############## ############## ############## ############## #############

def update_csv(test, host_list):
    file_name = 'statistics' + test + '.csv'

    f = open(file_name, 'r+')
    lines = f.readlines()
    f.close()
    f = open(file_name, 'w')
    if lines:

        for line in lines:
            for connection in host_list:
                if line.startswith(connection[1]):
                    f.write(str(line).strip('\n') + ","+str(connection[3])+'\n')
    else:
        for connection in host_list:
            f.write(str(connection[1]) + ","+str(connection[3])+'\n')

    f.close()



args = sys.argv
pcap_raw = os.path.abspath(str(args[1]))
pcap_stat_shaped = os.path.abspath(str(args[2]))
pcap_stat_unshaped = os.path.abspath(str(args[3]))

'''
Usage:
python pcaphost.py <name of tshark.dump> <test the data was from> <shaped or unshaped> <size of the file>
'''


with open(pcap_raw, 'r') as f:
    pcap_raw = f.read()

output = get_statistics(pcap_raw, 'Raw', args)

with open(pcap_stat_shaped, 'r') as f:
    pcap_stats_shaped = f.read()

output += get_statistics(pcap_stats_shaped, "Shaped", args)


with open(pcap_stat_unshaped, 'r') as f:
    pcap_stats_unshaped = f.read()

output += get_statistics(pcap_stats_unshaped, "Unshaped", args)


print output
