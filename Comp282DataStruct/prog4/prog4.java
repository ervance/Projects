/*
Name: Eric Vance
Class: Comp 282
Assignment 4
Date handed in: 11/16/2015
Description: The ArraySorts class is designed to implement QuickSort,  
HeapSort, and AlmostQuicksort.   Quick sort was implemented four 
different ways, random pivot with book partition, random pivot with
two pointer partition, lf pivot with book partition, and lf pivot with
two pointer partition. The three pointer partition was not completed
due to time constraints and bugs that were not squashed in time.  
HeapSort is implemented with a build heap from top down and bottom 
up method.  The AlmostQuicksort was a quick sort that did not take
advantage of a speed up using InsertionSort once a specific 
range was sorted with QuickSort.
*/

// You need java.math.* for Math.random() which you will want to use
// in some of your quicksorts and for your tests
import java.math.*;

class ArraySorts {

	public static void InsertionSort(int[] a, int n) {
		int unsorted, value, tempValue;

		//for the length of the unsorted array
		for (unsorted = 1; unsorted < n; unsorted++){

			value = unsorted;
			tempValue = a[value];
			//shift value left while its greater than 0
			//and smaller than value to the left
			while ( value > 0 && tempValue < a[value-1]){
				a[value] = a[value-1];
				value--;
			}//while

			a[value] = tempValue;


		}//for

	}//InsertionSort

	
	public static void QuickSort1(int [] a, int n, int cutoff){

		QuickSort1(a, 0, n-1, cutoff);
		InsertionSort(a, n);

	}//QuickSort1Driver

	private static void QuickSort1(int[] a, int lf, int rt, int cutoff) {

		int pivot, pi;

		while( (rt - lf + 1) >= cutoff ){
			//pick piviot
			pivot = (int)(Math.random() * (rt - lf + 1)) + lf;
			pi = partitionBook(a, lf, rt, pivot);

			//fake out recursion to reduce calls
			if (pi - lf > rt - pi){
				QuickSort1(a, pi + 1, rt, cutoff);
				rt = pi - 1;
			}//if
			else {
				QuickSort1(a, lf, pi-1, cutoff);
				lf = pi + 1;
			}//else


		}//while

	}//QuickSort1Recursive
	public static void QuickSort2(int [] a, int n, int cutoff){

		QuickSort2(a, 0, n-1, cutoff);
		InsertionSort(a, n);

	}//QuickSort2Driver

	private static void QuickSort2(int[] a, int lf, int rt, int cutoff) {

		int pivot, pi;

		while( (rt - lf + 1) >= cutoff ){
			//pick piviot
			pivot = (int)(Math.random() * (rt - lf + 1)) + lf;
			pi = partitionTwoPoint(a, lf, rt, pivot);

			//fake out recursion to reduce calls
			if ((pi - lf) < (rt - pi)){
				QuickSort2(a, pi+1, rt, cutoff);
				rt = pi;
			}//if
			else {
				QuickSort2(a, lf, pi, cutoff);
				lf = pi+1;
			}//else

		}//while

	}//QuickSort2Recursive

	public static void QuickSort3(int[] a, int n, int cutoff){

		QuickSort3(a, 0, n-1, cutoff);
		InsertionSort(a, n);

	}//QuickSort3Driver

	private static void QuickSort3(int[] a, int lf, int rt, int cutoff) {

		int pivot, pi;

		while( (rt - lf + 1) >= cutoff ){

			//pick piviot
			pivot = lf;
			pi = partitionBook(a, lf, rt, pivot);

			//fake out recursion to reduce calls
			if (pi - lf > rt - pi){
				QuickSort3(a, pi + 1, rt, cutoff);
				rt = pi - 1;
			}//if
			else {
				QuickSort3(a, lf, pi-1, cutoff);
				lf = pi + 1;
			}//else


		}//while

	}//QuickSort3Recursive

	public static void QuickSort4(int [] a, int n, int cutoff){

		QuickSort4(a, 0, n-1, cutoff);
		InsertionSort(a, n);

	}//QuickSort4Driver

	private static void QuickSort4(int[] a, int lf, int rt, int cutoff) {

		int pivot, pi;

		while( (rt - lf + 1) >= cutoff ){

			//pick piviot
			pivot = lf;
			pi = partitionTwoPoint(a, lf, rt, pivot);
			
			//fake out recursion to reduce calls
			if ((pi - lf) < (rt - pi)){
				QuickSort2(a, pi+1, rt, cutoff);
				rt = pi;
			}//if
			else {
				QuickSort2(a, lf, pi, cutoff);
				lf = pi+1;
			}//else

		}//while

	}//QuickSort4Recursive

	public static void AlmostQS1(int [] a, int n, int cutoff){

		QuickSort1(a, 0, n-1, cutoff);
		//InsertionSort(a, a.length-1);

	}//AlmostQuickSort1Driver

	public static void AlmostQS2(int [] a, int n, int cutoff){

		QuickSort2(a, 0, n-1, cutoff);
		//InsertionSort(a, a.length-1);

	}//AlmostQuickSort2Driver

	
	public static void HeapSortTD(int [] a, int n){

		buildTD(a, n);
		HeapSort(a, n-1);

	}//HeapBuildTD with HeapSort

	
	private static void buildTD(int[] a, int n) {
		//Top down build
		int parentValue, parentIndex, rtchild, lfchild, 
			lfchildindex, rtchildindex;
		parentIndex = 0;

		//while there are items in the array to
		//check
		while(parentIndex <= n-1){

			//calculate indices
			lfchildindex = 2*parentIndex + 1;
			rtchildindex = 2*parentIndex + 2;
			
			//if not a leaf
			if(lfchildindex <= n-1){
				lfchild = a[lfchildindex];
				parentValue = a[parentIndex];
				//check left child
				if ( parentValue < lfchild ){
					//trickle up if left child larger
					trickleUp(a, lfchildindex);
				}//if
			}//if
			//if not a leaf
			if(rtchildindex <= n-1){
				parentValue = a[parentIndex];
				rtchild = a[rtchildindex];
				//check right child
				if (parentValue < rtchild){
					//trickle up if right child larger
					trickleUp(a, rtchildindex);
				}//if
			}//if
			
			parentIndex++;
		}//while

	}//HeapSortTD

	
	public static void HeapSortBU(int[] a, int n){
		
		buildBu(a, n-1);
		HeapSort(a, n-1);

	}//Driver

	
	private static void HeapSort(int[] a, int n){
		
		//sort the heap, swap and trickle
		while(n >= 0){
			swap(a, 0, n);
			trickleDown(a,0, n-1);
			n--;		
		}//while
		
	}//HeapsortBU


	private static void buildBu(int[] a, int n){
		int parentValue, parentIndex, rtchildValue, lfchildValue, 
			lfchildindex, rtchildindex, size;
		//Bottom Up build
		size = n;
		while ( n >= 0){
			
			parentIndex = n;
			parentValue = a[n];
			//calculate indices
			lfchildindex = 2*n + 1;
			rtchildindex = 2*n + 2;

			//if a right child exists
			if (rtchildindex <= size){
				//if a right exists so does a left
				lfchildValue = a[lfchildindex];
				rtchildValue = a[rtchildindex];

				//check is trickling is needed
				if( rtchildValue > parentValue || lfchildValue > parentValue){
					trickleDown(a,parentIndex,size);
				}//if
			}//if
			else {
				//right child doesnt exist
				//check if a left child exists
				if (lfchildindex <= size){

					lfchildValue = a[lfchildindex];
					//if the left child is larger
					//than the parent trickle
					if(lfchildValue > parentValue){
						trickleDown(a,parentIndex,size);
					}//if
				}//if
			}//else
			//decrease the size
			n--;
		}//while
	}//build Heap

	public static boolean implemented3partition() {

		//ran out of time to comlete this
		//gave it the ole college try
		//whish I could have done it
		//thank you for taking time to write it

    	return false;

	}//implemented3partition

	public static void QuickSort_3Partition(int a[], int n, int cutoff) {

		//ran out of time to comlete this
		//gave it the ole college try
		//whish I could have done it
		//thank you for taking time to write it

		QuickSort_3Partition(a, 0, n-1, cutoff);
		//InsertionSort(a, n);

	}//QuickSort_3Partition

	private static void QuickSort_3Partition(int a[], int lf, int rt, int cutoff){

		//ran out of time to comlete this
		//gave it the ole college try
		//whish I could have done it
		//thank you for taking time to write it


		int pivot1,pivot2, pi1, pi2;
		int [] pivots;

		while( rt - lf >= cutoff-1){
			//pick piviot
			pivot1 = (int)(Math.random() * (rt - lf)) + lf;
         	pivot2 = (int)(Math.random() * (rt - lf)) + lf;

			pivots = partitionThreePoint(a, lf, rt, pivot1, pivot2);

			pi1 = pivots[0];
			pi2 = pivots[1];

			if ((pi1 - lf < rt - pi2 ) && ( pi1 - lf < pi1 - pi2)){
				QuickSort_3Partition(a, lf, pi1, cutoff);
				lf = pi1+1;
			}//if
			else if ((pi2 - pi1) < (pi1 - lf) && pi2 - pi1 < (rt - pi2)){
				QuickSort_3Partition(a, pi1+1, pi2-1, cutoff);
				rt = pi2 + 1;
				lf = pi1 - 1;
			}//else if
			else {
				QuickSort_3Partition(a, pi2, rt, cutoff);
				rt = pi2 - 1;

			}//else

		}//while
	}//QuickSort_3Partition

	private static int [] partitionThreePoint(int a[], int lf, int rt, int pi1, int pi2){
		//ran out of time to comlete this
		//gave it the ole college try
		//whish I could have done it
		//thank you for taking time to write it

		int pi1Val, pi2Val, ls, ll, bu, temp;
		int [] pivots = new int [2];

		ls = lf;
		temp = 0;
		bu = lf + 1;
		ll = rt;
		pi1Val = a[pi1];
		pi2Val = a[pi2];

		if (pi1Val < pi2Val){
			swap(a, lf, pi1);
			swap(a, rt, pi2);
		}
		else {
			swap(a, rt, pi1);
			swap(a, lf, pi2);
			pi1Val = a[pi2];
			pi2Val = a[pi1];
			pi1 = temp;
			pi1 = pi2;
			pi2 = temp;
		}//end if swap

		while (bu < ll) {

			if (a[bu] < pi1Val){

				ls++;
				swap(a,ls,bu);
				bu++;
			}
			else if ( a[bu] >= pi1Val && a[bu] <= pi2Val){

				bu++;
			}
			else {
				ll--;
				swap(a,ll,bu);
			}

		}//end while

		swap(a, ls, pi1);
		swap(a, ll, pi2);

		pivots[0] = pi1;
		pivots[1] = pi2;
      
      return pivots;

	}//partitionThreePoint
	

	public static String myName() {

		return "Eric Vance";

	}//myName


	
	private static int partitionBook( int[] a, int lf, int rt, int pivot ){
		int pivotVal, temp, ls, bu;

		//set pointers and swap piviot to first position
		ls = lf; 
		bu = lf+1;
		pivotVal = a[pivot];
		swap(a, lf, pivot);

		while (bu <=  rt){
			if ( a[bu] < pivotVal){
				//if less than piviot value
				//swap with the next value after the last smallest
				ls++;
				swap(a, ls, bu);
				bu++;
			} //if
			else {
				//value is greater than the piviot
				//it stays where it is and advance the uknown pointer
				bu++;
			}//else
		}//while

		//one last swap to put the piviot value as the largest of the
		//smaller values
		swap(a, lf, ls);

		return ls;

	}//PartitionBook



	
	private static int partitionTwoPoint(int[] a, int lf, int rt, int pivot ){
		int pivotVal, start, end;
        start = lf;
        end = rt;
		pivotVal = a[pivot];
		while ( lf < rt ){
			//while the left is smaller than the right check to partition
				
	        while ( a[lf] < pivotVal && lf <= end){
				//as long as the value is less than the piviot
				//move to next value once it is a value greater than
				//the value is bad, stop to swap
				lf++;
			}//while
	        while ( a[rt] > pivotVal && rt >= start){
				//if the value is greater than check next value
				//to the left, when it is a value less than
				//the value is bad, stop to swap
				rt--;
			}//while
					
			//swap only if the left pointer is still less
			//than the right pointer handle the special case
			//they equal after
	               
			if ( lf < rt){	
				//swap and increment if needed
				swap(a, lf, rt);
				if(rt != lf){
					//only increment lf 
					//not equal to not pass
					//over the value before
					//special case check
					lf++;
				}//if
				rt--;
				
            }//if

		}//end while

        //special case if they are equal

        //check to see if value at lf it belongs 
        //in the >= side	
       	if(rt == lf && a[lf] > pivotVal){
         	rt--;
        }//if

		return rt;


	}//PartitionTwoPoint

	
	private static void swap(int[] a, int swapPosition, int itemToSwapIndex){
		//swap
		int temp;
		temp = a[swapPosition];
		a[swapPosition] = a[itemToSwapIndex];
		a[itemToSwapIndex] = temp;

	}//swap

	
	private static void trickleUp(int[] a, int startIndex){
		int parentIndex, tempValue;
		boolean shifted = false;

		//calculate parent index
		parentIndex = (startIndex - 1) / 2;
		//hold start value for last swap after shifts
		tempValue = a[startIndex];

		//while it has not be trickled up
		while (!shifted){
			if(tempValue > a[parentIndex] && parentIndex > 0){
				//shift
				a[startIndex] = a[parentIndex];
				startIndex = parentIndex;
				//new parent index
				parentIndex = (parentIndex - 1) / 2;
			}//else
			else if(parentIndex == 0 && tempValue > a[parentIndex]){
				//largest case
				a[startIndex] = a[parentIndex];
				a[parentIndex] = tempValue;
				shifted = true;
			}//else
			else {
				//stop shifting
				a[startIndex] = tempValue;
				shifted = true;
			}//else
		}//while

	}//trickleUp

	
	private static void trickleDown(int[] a, int startIndex, int limitIndex){
		int parentIndex, childValue, trickleValue, 
		lfchildindex, rtchildValue, rtchildindex, lfchildValue;
		boolean shifted = false;

		//can maybe get rid of this and just use startindex for parent
		parentIndex = startIndex;
		trickleValue = a[parentIndex];
		lfchildindex = 2*parentIndex + 1;
		rtchildindex = 2*parentIndex + 2;

		//while it hasnt been tricked down
		while(!shifted){

			//if there is a right child, then
			//a left child exists too
			if(rtchildindex <= limitIndex){
				lfchildValue = a[lfchildindex];
				rtchildValue = a[rtchildindex];

				if(lfchildValue > trickleValue || rtchildValue > trickleValue){
					//determine bigger child
					if(lfchildValue > rtchildValue){
						//left child is larger
						a[parentIndex] = lfchildValue;
						parentIndex = lfchildindex;
						//calculate indices
						lfchildindex = 2*parentIndex + 1;
						rtchildindex = 2*parentIndex + 2;	
					}//if
					else {
						//right child is larger
						a[parentIndex] = rtchildValue;
						parentIndex = rtchildindex;
						//calculate indices
						lfchildindex = 2*parentIndex + 1;
						rtchildindex = 2*parentIndex + 2;
					}
				}//if
				else {
					//both children smaller 
					//parent in correct place
					a[parentIndex] = trickleValue;
					//it has been shifted
					shifted = true;

				}//else
			}//if
			//if no right child check if there is a left child
			else if (lfchildindex <= limitIndex) {
				
				lfchildValue = a[lfchildindex];
				if (lfchildValue > trickleValue){
					//child is larger, shift it
					a[parentIndex] = lfchildValue;
					parentIndex = lfchildindex;
					//calculate indices
					lfchildindex = 2*parentIndex + 1;
					rtchildindex = 2*parentIndex + 2;
				}//if
				else {
					//in correct place
					a[parentIndex] = trickleValue;
					//it has been shifted
					shifted = true;
				}//else
			}//else if
			else {
				//no children
				a[parentIndex] = trickleValue;
				//no need to shift so its shifted
				shifted = true;
			}//else
		}//while

	}//trickleDown

}//end ArraySorts	