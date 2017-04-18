/**
 * Eric Vance
 * Project 1
 * Comp 496
 * Fall 2016
 * 10/25/2016.
 */

import java.util.ArrayList;
import java.util.Collections;


class ChannelAllocation {

    private int numChannels;
    private ArrayList<Channel> channelsArray = new ArrayList<Channel>();
    private String printString; //for displaying the algorithm being used


    public ChannelAllocation(){
        numChannels = 1;
        channelsArray.add(new Channel());
        printString = "";
    }

    public int getNumChannels(){
        return numChannels;
    }

    public void LUCChannelAllocation(ArrayList<Interval> intervals){
        printString = "LUC ";//for print format
        //sorts the intervals by earliest start
        Collections.sort(intervals, Interval.EARLIEST_START_SORT);
        //Channel Allocate
        allocate(intervals);

    }

    public void EFTChannelAllocation(ArrayList<Interval> intervals){
        printString = "EFT "; //for print format
        //sorts the intervals by earliest start
        //runs the same algorithm as LUC allocation
        Collections.sort(intervals, Interval.EARLIEST_FINISH_SORT);
        //Channel Allocate
        allocate(intervals);
    }

    public void newAlgEft(ArrayList<Interval> intervals){
        printString = "Normal EFT ";
        Collections.sort(intervals, Interval.EARLIEST_FINISH_SORT);
        eftallocate(intervals);
    }

    //Channel Allocation Algorithm
    private void allocate(ArrayList<Interval> sortedIntervals){
        int firstInterval = 0;
        Interval interval;
        //while there are intervals left
        while(sortedIntervals.size() != 0){
            //get the first interval and check for an open slot in an already existing channel
            interval = sortedIntervals.get(firstInterval);
            if(!occupyOpen(interval))
                //the lowest channel is not available, create a new channel and
                //immediately occupy it.
                occupyNew(interval);
            //throw away the interval that has been assigned a channel slot
            sortedIntervals.remove(firstInterval);
        }
    }

    //returns true if there was an open channel to assign the interval
    //otherwise it returns false that the interval was not assigned
    private boolean occupyOpen(Interval interval){
        boolean channelAssigned = false;
        int channelNumber = 0;
        Channel currentChannel;

        //if the interval has not be assigned to a channel and there are channels left to check
        while(!channelAssigned && channelNumber < numChannels) {
            //check if the channel is free and add if it is free
            currentChannel = channelsArray.get(channelNumber);
            if (currentChannel.isFree(interval)) {
                currentChannel.addInterval(interval);
                currentChannel.setFree(false);
                channelAssigned = true; //interval was assigned
            }
            else {
                //Need to check for other channels
                channelNumber++;

            }
        }
        //true an open channel was occupied
        //false if all channels were taken
        return channelAssigned;
    }

    //create a new channel and add the interval to the channel
    private void occupyNew(Interval interval){
        channelsArray.add(new Channel(interval));
        numChannels++;
    }

    private void eftallocate(ArrayList<Interval> sortedIntervals){
        ArrayList<Interval> overlap, newChannelArray;
        Interval currentInterval;
        int channelNumber =0;

        //while there exists intervals to assign
        while(!sortedIntervals.isEmpty()) {
            overlap = new ArrayList<>();
            newChannelArray = new ArrayList<>();

            //while there are intervals left to assign, assign the earliest finish time
            //throw away the overlap and assign the next earliest finish time
            while(!sortedIntervals.isEmpty()) {
                currentInterval = sortedIntervals.get(0);
                sortedIntervals.remove(0);
                newChannelArray.add(currentInterval);

                //throw away the overlap
                while (!sortedIntervals.isEmpty() && sortedIntervals.get(0).getStartTime() < currentInterval.getFinishTime()) {
                    overlap.add(sortedIntervals.get(0));
                    sortedIntervals.remove(0);
                }
            }

            //Add all the earliest finish time intervals that do not over lap to a new channel
            //increase the channel number
            channelsArray.add(channelNumber, new Channel(newChannelArray));
            channelNumber++;
            //and repeat with the intervals that were thrown away
            sortedIntervals = new ArrayList<>(overlap);

        }

    }


    public String toString(){
        printString += "Uses " + numChannels + " channels\n";
        for(int i=0; i < channelsArray.size(); i++)
            printString += i+1 + ". " + channelsArray.get(i) + "\n";

        return printString;
    }
}
