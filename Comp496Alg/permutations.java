//Logic for problem #5 homework 2

public class permutations {

    public static void nPermutations(int n){
        int size = n;
        int lock = 0;
        int [] permArray = new int [n];
        //initialize the permutation
        for (int i = 0; i < permArray.length; i++)
            permArray[i] = i + 1;
        nPermutations(lock, permArray, size - 1);
    }

    private static void nPermutations(int lock, int [] permArray, int size){

        if (lock == size) {
            //base case all ints are locked
            //print out the permutation
            for (int i = 0; i < permArray.length; i++)
                System.out.print(permArray[i]);
            System.out.println();
        }
        else {
            for (int i = lock; i < permArray.length; i++){
                //swap lock and next int
                int temp = permArray[lock];
                permArray[lock] = permArray[i];
                permArray[i] = temp;
                //recursively permute the next level by locking
                //the next position and swaping
                nPermutations(lock + 1, permArray, size);
                //swap back to original to recursivly permute next swap
                permArray[i] = permArray[lock];
                permArray[lock] = temp;
            }
        }
    }
}

