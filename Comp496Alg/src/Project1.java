/**
 * Eric Vance
 * Project 1
 * Comp 496
 * Fall 2016
 * 10/25/2016.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class ReadTextFile {

    //read and return an arraylist of intervals from an inputfile.txt
    public static ArrayList<Interval> readIntervalTextFile(String fileName){
        BufferedReader bufferReader = null;
        String intervalString;
        String [] intervalSplit;
        ArrayList<Interval> intervals = new ArrayList<>();
        int start, finish;

        try{
            bufferReader = new BufferedReader(new java.io.FileReader(fileName));
            while((intervalString = bufferReader.readLine()) != null){
                if(!intervalString.equals("")) {//added in case input.txt has empty lines
                    intervalSplit = intervalString.split(" ");
                    if (intervalSplit.length == 2) {//added in case input.txt has less than 2 items after split
                        start = Integer.parseInt(intervalSplit[0]);
                        finish = Integer.parseInt(intervalSplit[1]);
                        intervals.add(new Interval(start, finish));
                    }
                }
            }
            bufferReader.close();
        }
        catch(FileNotFoundException e){
            System.err.println("File " + fileName + " not found.");
        }
        catch(IOException e){
            System.err.println("Unable to read file name: " + fileName );
        }
        return intervals;
    }

}

class Interval implements Comparable<Interval> {

    private int startTime;
    private int finishTime;

    public Interval(){
        startTime = -1;
        finishTime = -1;
    }

    public Interval(int start, int finish){
        startTime = start;
        finishTime = finish;
    }

    //custom comparator to sort intervals by earliest start for LUC
    public static final Comparator<Interval> EARLIEST_START_SORT = new Comparator<Interval>() {
        @Override
        public int compare(Interval interval1, Interval interval2) {

            return Integer.compare(interval1.getStartTime(), interval2.getStartTime());

        }
    };
    //custom comparator to sort intervals by earliest finish for EFT
    public static final Comparator<Interval> EARLIEST_FINISH_SORT = new Comparator<Interval>() {
        @Override
        public int compare(Interval interval1, Interval interval2) {

            return Integer.compare(interval1.getFinishTime(), interval2.getFinishTime());

        }
    };

    //provide specific compareTo for interval class
    //Use the Integer class compareTo
    @Override
    public int compareTo(Interval interval){

        Integer start1 = new Integer(this.startTime);
        Integer start2 = new Integer(interval.getStartTime());

        return start1.compareTo(start2);
    }

    public int getStartTime(){
        return startTime;
    }

    public int getFinishTime(){
        return finishTime;
    }
    //display purposes
    @Override
    public String toString(){
        return "(" + startTime + "," + finishTime + ")";
    }

}

class Channel {

    private ArrayList<Interval> channel; //list of intervals occupying the channel
    private boolean free; //if the channel is currently being used

    public Channel(){
        channel = new ArrayList<>();
        free = true;
    }

    public Channel(Interval interval){
        channel = new ArrayList<>();
        channel.add(interval);
        free = false;
    }
    //this constructor is for the eft alg
    public Channel(ArrayList<Interval> fullChannel){
        channel = fullChannel;
        free = false;
    }

    public void setFree(boolean isFree){
        free = isFree;
    }

    public void addInterval(Interval interval){
        channel.add(interval);
    }

    //check if the channel is free and return result
    public boolean isFree(Interval interval){
        Interval lastInterval;
        int intervalStart, lastIntervalFinish;
        boolean isFree;

        //if the channel is currently not free, check to see if the interval that is currently
        //occupying the channel has finished.
        if(!free) {
            lastInterval = channel.get(channel.size() - 1);//last interval occupying the channel
            lastIntervalFinish = lastInterval.getFinishTime();
            intervalStart = interval.getStartTime();

            //if last interval on the channel is finished
            //return that the channel is free, otherwise return false
            if(lastIntervalFinish <= intervalStart)
                isFree = true;
            else
                isFree = false;
        }
        else
            isFree = true;

        return isFree;
    }
    //display the channel
    public String toString(){
        String channelStr = "";

        for(int i = 0; i < channel.size(); i++)
           channelStr += channel.get(i) + " ";

        return channelStr;
    }

}

class ChannelAllocation {

    private int numChannels;
    private ArrayList<Channel> channelsArray = new ArrayList<Channel>();
    private String printString; //for displaying the algorithm being used


    public ChannelAllocation(){
        numChannels = 1;
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
        channelsArray.add(new Channel());
        allocate(intervals);

    }

    public void EFTChannelAllocation(ArrayList<Interval> intervals){
        printString = "IterEFT "; //for print format
        //sorts the intervals by earliest start
        //runs the same algorithm as LUC allocation
        Collections.sort(intervals, Interval.EARLIEST_FINISH_SORT);
        //Channel Allocate
        channelsArray.add(new Channel());
        allocate(intervals);
    }

    public void iterETF(ArrayList<Interval> intervals){
        printString = "Normal IterEFT ";
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
        //empty current channel

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
            if(!overlap.isEmpty()) {
                sortedIntervals = new ArrayList<>(overlap);
                numChannels++;
            }

        }

    }


    public String toString(){
        printString += "Uses " + numChannels + " channels\n";
        for(int i=0; i < channelsArray.size(); i++)
            printString += i+1 + ". " + channelsArray.get(i) + "\n";

        return printString;
    }
}
