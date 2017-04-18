/**
 * Eric Vance
 * Project 1
 * Comp 496
 * Fall 2016
 * 10/25/2016.
 */
import java.util.ArrayList;

public class Project1Driver {

    public static void main(String [] arg){


        //if the input file is not input.txt, change to the correct name
        ArrayList<Interval> lucIntervals = ReadTextFile.readIntervalTextFile("input.txt");
        ArrayList<Interval> eftIntervals = new ArrayList<>(lucIntervals);
        ArrayList<Interval> eftNewAlg = new ArrayList<>(lucIntervals);

        ChannelAllocation lucChannelAllocation = new ChannelAllocation();
        ChannelAllocation eftChannelAllocation = new ChannelAllocation();
        //run Lowest Unoccupied Channel and Earliest Finish Time variants
        lucChannelAllocation.LUCChannelAllocation(lucIntervals);
        eftChannelAllocation.EFTChannelAllocation(eftIntervals);

        //print output
        System.out.println(lucChannelAllocation);
        System.out.println(eftChannelAllocation);

        //This is the iterative normal implementation of EFT, throwing away overlap and continuing
        //to do this until there are no intervals left to throw away
        //if you wish to run this code just uncomment and run
        //ChannelAllocation etfNewAlgAllocation = new ChannelAllocation();
        //etfNewAlgAllocation.iterETF(eftNewAlg);
        //System.out.println(etfNewAlgAllocation);
    }


}
