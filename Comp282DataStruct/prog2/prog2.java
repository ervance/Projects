
/*
Name: Eric Vance
Class: Comp 282
Assignment 2
Date handed in: 9/24/2015
Description: The BSTStrings class is designed to build and 
manipulate a String binary search tree. It is able to copy a created tree,
search a tree, insert into the tree, delete from the tree, check the height,
check the number of leafs it contains, the height of the closest leaf,
how many single child nodes are in the tree, find the second smallest value in
the tree, and to be able to rotate any node right or left.
*/

class StringNode {

	private String word;
	
	private StringNode left, right;
	
	public StringNode(String w) {
		word = w;
		left = null;
		right = null;
	}//Constructor

	public StringNode getLeft() {
		return left;
	}//getLeft
	
	public void setLeft(StringNode pt) {
		left = pt;
	}//setLeft

	public StringNode getRight() {
		return right;
	}//getRight

	public void setRight(StringNode pt) {
		right = pt;
	}//setRight

	public String getString() {
		return word;
	}//getString
	// notice that there is no setString
}//StringNode


class BSTStrings {
// member variable pointing to the root of the BST
	private StringNode root;

	// default constructor
	public BSTStrings(){
		root = null;
	}

	// copy constructor
	public BSTStrings(BSTStrings t) {

		root = copyTree(t.root); // “root = t” does not make a copy
	}//constructor

	private static StringNode copyTree(StringNode l){
		StringNode new_t;

		if ( l == null ){
			//base case set the new node = null
			new_t = null;
		}//if
		else {
			//create a new node from the contents of the old node
			new_t = new StringNode(l.getString());
			//set the new nodes to the old nodes left
			new_t.setLeft(copyTree(l.getLeft()));
			//set the new nodes old node to the right
			new_t.setRight(copyTree(l.getRight()));
		}//else

		return new_t;

	}//copyTree

	// for output purposes -- override Object version
	@Override
	public String toString() {
		return toString(root, 0);
	}//toString

	private static String toString(StringNode l, int x) {
		String s = "";
		if (l == null){
			; // nothing to output
		} else {
			if (l.getLeft() != null){ // don't output empty subtrees
				s = '(' + toString(l.getLeft(), x+1) + ')';
				
			}//if
			s = s + l.getString() + "-" + x;
			if (l.getRight() != null){ // don't output empty subtrees
				s = s + '(' + toString(l.getRight(), x+1) + ')';
			}//if
		}//if else

		return s;
	}//toString

	public static String myName(){
		return "Eric Vance";
	}

	public StringNode search(String s){
		return search(root, s);
	}//search driver

	private static StringNode search(StringNode t, String s){

		if(t == null){
			;//do nothing if we dont find s
		}//if
		else if (t.getString().compareTo(s) > 0){
			//search left subtree if s is smaller than t
			t = search(t.getLeft(), s);
		}//else if
		else if (t.getString().compareTo(s) < 0){
			//search the right subtree if s is larger than t
			t = search(t.getRight(), s);
		}//else if
		
		return t;

	}//search
	
	// Insert s into this tree. If s is already in the tree, do nothing.
	public void insert(String s){

		root = insert(root, s);

	}//insert driver

	private static StringNode insert(StringNode t, String s){

		if (t == null){
			//add the node to the tree
			t = new StringNode(s);
		}//if
		else if (t.getString().compareTo(s) == 0){
			;//don't do anything if the node trying to insert exists already
		}//else
		else {
			//compare the strings
			if (t.getString().compareTo(s) > 0){
				//if s is smaller move to left subtree
				t.setLeft(insert(t.getLeft(), s));
			}//if
			else {
				//if s is larger move to right subtree
				t.setRight(insert(t.getRight(), s));
			}//else
			
			
		}//else
		return t;

	}//insert

	// Deletes val from the BST – does nothing if val is not in the tree.
	// Deletion must be done so that the replacement node is the smallest node
	// from the right subtree.
	public void delete(String val){

		root = delete(root, val);

	}//delete driver
	
	private static StringNode delete(StringNode t, String val){

		StringNode parent, pt;

		if ( t == null ){
			;//nothing to delete
		}//if 
		else if (t.getString().compareTo(val) > 0 ){
			//if val is smaller move to the left subtree
			t.setLeft(delete(t.getLeft(), val));
		}//else if
		else if (t.getString().compareTo(val) < 0 ){
			//if val is larger move to the right subtree
			t.setRight(delete(t.getRight(), val));
		}//else if
		else {
			//it is not smaller or larger so it is equal
			//handle the delete cases
			if (t.getRight() == null && t.getLeft() == null){
				//case that target node is a leaf
				t = null;
			}//if
			else if (t.getLeft() == null){
				//case that target node has no left subtree
				t = t.getRight();
			}//else if
			else if (t.getRight() == null){
				//case that target node has no right subtree
				t = t.getLeft();
			}//else if
			else{
				//case the target node has two children
				//create a parent to keep track as we move
				//create a pt to keep track of the node
				parent = null;
				pt = t.getRight();
				while(pt.getLeft() != null){
					//enters as long as the left child of
					//the pt is not null, so that we find the
					//smallest node of the right subtree
					parent = pt;
					pt = pt.getLeft();
				}//while
				if (parent != null){
					//the right subtree had at least one smaller
					//node moved down the left side of the
					//right subtree to find the smallest of that
					//larger tree to make the node replacement
					parent.setLeft(pt.getRight());
					pt.setRight(t.getRight());
					pt.setLeft(t.getLeft());
					t = pt;
				}//if
				else {
					//this is the case where the right node of
					//target is the smallest in the right subtree,
					//so parent was never set to a node and equal null
					pt.setLeft(t.getLeft());
					t = pt;
					
				}//else
			}//else		
		}//else
				
		return t;

	}//delete

	// return the height of the tree
	public int height(){

		return height(root);

	}//height driver

	private static int height(StringNode t){
		int h, hl, hr;

		if ( t == null){
			//base case, leaf or empty tree
			h = 0;
		} else {
			//accumulate the height of the right subtree
			hr = height(t.getRight());
			//accumulate the height of the left subtree
			hl = height(t.getLeft());
			//chose the larger tree height and add one 
			//as it move back up the recursive calls
			if ( hl > hr ){
				h = hl + 1;
			} else {
				h = hr + 1;
			}//if else
		}//if else

		return h;

	}//height
	
	public int leafCt(){
		
		return leafCt(root, 0);
	}//leafCt driver

	private static int leafCt(StringNode t, int ct){

		if (t == null) {
			;//do nothing keep ct the same
		}//if
		else if ( t.getLeft() == null && t.getRight() == null ){
			//is a leaf need to add 1 to the count
			ct = ct + 1;
		}//if
		else{ 
			//set ct equal to the count and recursively
			//call the left subtree.  It returns a count and then
			//use that ct and call the right subtree recursively
			//Further accumulating the leaf count and storing it in ct
			ct = leafCt(t.getLeft(), ct);
			ct = leafCt(t.getRight(), ct);

		}//else

		return ct;
	}//leafCt


	// Returns the distance from the root to the closest leaf.
	public int closeness(){
		
		return closeness(root, 0);

	}//closeness driver

	private static int closeness(StringNode t, int h){
		//Height of left sub tree, and right subtree variables
		int hl, hr;

		if ( t == null ){
			//a null node has a height of 0
			//takes care of the null tree
			h = 0;
		}//if 
		else if ( t.getRight() == null && t.getLeft() == null ) {
			//a node with no children has a height of 1
			//this takes care of a tree with one node
			//or a leaf
			h = 1;

		}//else if
		else {
			//similar to height, we want to accumulate the heights
			hr = closeness(t.getRight(), h);
			hl = closeness(t.getLeft(), h);
			
			//hl <= hr found a leaf that is closer to the root on the left
			//add 1 to increase h as we move back towards the root
			if ( hl <= hr ){
				if (hl == 0){
					//left subtree was null and 0 was returned,
					//discard this value and use hr
					h = hr + 1;
				}//if
				else {
					h = hl + 1;
				}//else
			}//if 
			else {
				//if hr was greater, add 1 and store it in h
				if (hr == 0 ){
					//right subtree was null
					//and a 0 was returned, discard this value
					//and use hl
					h = hl + 1; 
				}//if
				else {
					h = hr + 1;
				}//else
			}//if else

		}//if else

		return h;

	}//closeness

	
	// Returns the number of nodes with exactly one non-null child
	public int jointChildCt(){

		return jointChildCt(root, 0);

	}//joinChildCt driver

	private static int jointChildCt(StringNode t, int ct){
		//base case, an empty tree
		if ( t == null ){
			ct = 0;
		}//if
		//if one child is null, increase the count 
		//xor will make sure only one child is null
		else if ( t.getLeft() == null ^ t.getRight() == null ){
			//one child is null, check which one
			//Recursively called the child that is not null and add
			//the results from that to the count accumulator
			
			if ( t.getLeft() != null ){
				ct = 1 + jointChildCt(t.getLeft(), ct);
			}//if
			else {
				ct = 1 + jointChildCt(t.getRight(), ct);
			}//if else
		}//else if

		//go down both trees looking for a single child tree
		//and accumulating the count of both trees	 
		else {
			ct = jointChildCt(t.getLeft(), ct);
			ct = ct + jointChildCt(t.getRight(), ct);
		}//else

		return ct;
			
	}//jointChildCt

	// Returns the node containing the second smallest item, or null if there
	// is no second smallest.
	public StringNode secondSmallest(){
		
		return secondSmallest(root);

	}//secondSmallest driver

	private static StringNode secondSmallest(StringNode t){

		//this is the case of an empty tree, return null
		if ( t == null ){
			t = null;
		}//if

		//if there is no left subtree and there is a right subtree
		//then the root must be the second smallest
		else if ( t.getLeft() == null && t.getRight() == null){
			t = null;
		}//else if
 		
 		//if left subtree is null, check to see if the right
 		//subtree has a smaller node
		else if(t.getLeft() == null ){
			if (t.getRight().getLeft() == null ) {
				//if it does not, then this is the second smallest node
				t = t.getRight();
			}//if
			else {
				//the right subtree has a smaller node
				t = secondSmallest(t.getRight());
			}//else
		}
		else {
			//left is not null so check if the left subtree has a
			//smaller node
			if( t.getLeft().getLeft() == null ){
				//null means no smaller node to the left
				//but check the right to see if thats the smallest
				if(t.getLeft().getRight() != null){
					//this will walk down the right subtree's
					//left subtree until it is null meaning it found
					//the smallest node in the right subtree
					//meaning the second smallest node in the tree
					t = t.getLeft().getRight();
					while(t.getLeft() != null){
						t = t.getLeft();
					}//while
				}//if
				
			}//if
			else {
				//if the left subtree has a smaller node
				//recursivly find it
				t = secondSmallest(t.getLeft());
			}//else
		}//else

	return t;

	}//secondSmallest

	// Rotate the node containing val to the left – do nothing if not
	// possible, e.g., val is not in the tree or there is no right child
	// of val.
	public void rotateLeft(String val){
		root = rotateLeft(root, val);
	}//rotateLeft driver

	private static StringNode rotateLeft(StringNode t, String val){

		if (t == null){
			;//do nothing, nothing to rotate
		}//if
		else if (t.getRight() == null && t.getLeft() == null){
			//do nothing nothing to rotate
		}//if
		else {
			//value equals current node
			if (t.getString().compareTo(val) == 0){
				
				if (t.getRight() == null){
					//cant rotate left with a null right
				}//if
				else {
					//rotate left
					StringNode tRc = t.getRight();
					t.setRight(tRc.getLeft());
					tRc.setLeft(t);
					t = tRc;
				}//else
			}//if
			else {
				//check to make sure not null and recursively
				//look for the node to rotate left
				if (t.getLeft() != null ){
					if (t.getString().compareTo(val) > 0){
						//go left
						t.setLeft(rotateLeft(t.getLeft(), val));
						
					}//if
				}//if
				if(t.getRight() != null) {
					if (t.getString().compareTo(val) < 0){
						//go right
						t.setRight(rotateLeft(t.getRight(), val));
						
					}//if
				}//if
			}//else
			
		}//else
		return t;
	}//rotateleft

	// Rotate the node containing val to the right – do nothing if not
	// possible.
	public void rotateRight(String val){
		root = rotateRight(this.root, val);
	}//rotateRight driver

	private static StringNode rotateRight(StringNode t, String val){

		if (t == null){
			;//do nothing, nothing to rotate
		}//if
		else if (t.getRight() == null && t.getLeft() == null){
			;//do nothing, nothing to rotate
		}//else if
		else {
			if (val.compareTo(t.getString()) == 0){
				if (t.getLeft() == null) {
					;//do nothing, can't rotate right with null left
				}//if
				else {
					//rotate right
					StringNode tLc = t.getLeft();
					t.setLeft(tLc.getRight());
					tLc.setRight(t);
					t = tLc;
				}//else
			}//if
			else {
				//check to make sure not null and recursively
				//look for the node to rotate right
				if (t.getLeft() != null){
					if (t.getString().compareTo(val) > 0){
						t.setLeft(rotateRight(t.getLeft(), val));
					}//if
				}//if
				if (t.getRight() != null){
					if (t.getString().compareTo(val) < 0){
						t.setRight(rotateRight(t.getRight(), val));
					}//if
				}//if
			}//else
		}//else
		return t;
	}//rotateright
}//BSTStrings