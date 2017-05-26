/*
Name: Eric Vance
Class: Comp 282
Assignment 3
Date handed in: 10/19/2015
Description: The SplayStrings class is a BST data structure 
that utilizes the Splay Tree methodology. It contains a search
method that will return the value if it is found or null if it
does not. This method will splay either the desired value's node 
or the last node visited to the root. An insert method is provided
that will insert a desired value into the tree if that value does 
not exist. Once inserted, or if it that value is already in the tree, 
it will splay the value's node to the root.  Methods to rotate are
used to splay nodes in the tree.  A helper method rootCaseCheck is 
used to handle the special case where one more zig is required to
complete the splay to the root.
*/




class StringNode {// The same as in the previous assignment

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
	
}//StringNode

// So that a String can change
class WrapString {
// Yes, I am allowing (and encouraging) direct access to the String
	public String string;
	
	public WrapString(String str) {
		this.string = str;
	}

}//WrapString class


class SplayStrings {

	private StringNode root;

	public SplayStrings(){
		root = null;
	}//SplayStrings constructor

	public static String myName() {

		return "Eric Vance";
	
	}
	// for output purposes -- override Object version
	public String toString() {
		return toString(root, 0);
	}
	private static String toString(StringNode l, int x) {
		String s = "";
		if (l == null)
			; // nothing to output
		else {
			if (l.getLeft() != null) // don't output empty subtrees
				s = '(' + toString(l.getLeft(), x + 1) + ')';
			s = s + l.getString() + "-" + x;
			if (l.getRight() != null) // don't output empty subtrees
				s = s + '(' + toString(l.getRight(), x + 1) + ')';
		}
		return s;
	}

	// if s is not in the tree splay the last node visited
	// final zig, if needed, is done here
	//return NULL if not found in the tree
	public StringNode search(String s) {
		StringNode node;
		WrapString str = new WrapString(s);

		root = search(str, root);

		//check for last zig
		root = rootCaseCheck(root, str.string);
		
		//check if the root is the node that was
		//originally searched for
		if (s.compareTo(root.getString()) != 0){
			//if not, return null
			node = null;
		}//if 
		else {
			//return search value
			node = root;
		}//else

		return node;

	}//search driver

	// recursive search method
	// if str not in the tree str returns with value of last node visited
	private StringNode search(WrapString str, StringNode t) {

		if( t == null ){
			;//do nothing, did not find str
		}//if
		else {
			int compare_val;
			compare_val = str.string.compareTo(t.getString());

			if ( compare_val < 0 ) {
				//search left
				t.setLeft(search(str,t.getLeft()));
				//check if str exists in tree
				if ( t.getLeft() == null ) {
					//does not exist
					//change wrapper to splay last node visited
					str.string = t.getString();
				}//if
				else { 
					//check for rotation
					int compare_rotate;
					compare_rotate = 
					str.string.compareTo(t.getLeft().getString());
					
					if ( compare_rotate != 0 ){
						//rotate and check type
						if (t.getLeft().getRight() != null && 
						str.string.compareTo(
						t.getLeft().getRight().getString()) == 0){
							//zig zag
							t.setLeft(rotateLeft(t.getLeft()));
							t = rotateRight(t);
						}//if
						else {
							//zig zig
							t = rotateRight(t);
							t = rotateRight(t);
						}//else
					}//if
				}//else
			}//if
			else if ( compare_val > 0 ) {
				//search right
				t.setRight(search(str, t.getRight()));
				//check if str exists in tree
				if ( t.getRight() == null ) {
					//does not exist
					//change wrapper to splay last node visited
					str.string = t.getString();
				}//if
				else {
					//check for rotation
					int compare_rotate;
					compare_rotate = 
					str.string.compareTo(t.getRight().getString());

					if ( compare_rotate != 0 ) {
						//rotate and check type
						if ( t.getRight().getLeft() != null && 
						str.string.compareTo(
						t.getRight().getLeft().getString()) == 0){
							//zig zag
							t.setRight(rotateRight(t.getRight()));
							t = rotateLeft(t);
						}//if
						else {
							//zig zig
							t = rotateLeft(t);
							t = rotateLeft(t);
						}//else

					}//if
				}//else 			
			}//else if
			else {
				;//found the node, do nothing and return the node
				//start splaying to top
			}
		}//else
		return t;
	}//search recursive

	public void insert(String s) {
		
		root = insert(root, s);
		//check for 1 last zig
		root = rootCaseCheck(root, s);

	}//insert driver

	private static StringNode insert(StringNode t, String s){

		if (t == null){
			//insert value in the tree
			t = new StringNode(s);
		}
		else{
			int ins_compare, rotate_compare;
			ins_compare = s.compareTo(t.getString());

			if ( ins_compare < 0 ) {
				//left sub tree
				t.setLeft(insert(t.getLeft(), s));
				
				rotate_compare = s.compareTo(t.getLeft().getString());
				//check for rotate
				if ( rotate_compare != 0 ){
					//rotation occurs
					//check for splay type
					if (t.getLeft().getRight() != null && 
					s.compareTo(t.getLeft().getRight().getString()) 
					== 0){
						//zig zag
						t.setLeft(rotateLeft(t.getLeft()));
						t = rotateRight(t);
					}//if
					else {
						//zig zig
						t = rotateRight(t);
						t = rotateRight(t);

					}//else if
				}//if
			}//if
			else if ( ins_compare > 0 ) {
				//right subtree
				t.setRight(insert(t.getRight(), s));
				
				rotate_compare = s.compareTo(t.getRight().getString());
				//check for rotate
				if ( rotate_compare != 0 ){
					//rotation occurs
					//check type
					if ( t.getRight().getLeft() != null && 
					s.compareTo(t.getRight().getLeft().getString()) 
					== 0 ) {
						//zig zag
						t.setRight(rotateRight(t.getRight()));
						t = rotateLeft(t);
					}//if
					else {
						//zig zig
						t = rotateLeft(t);
						t = rotateLeft(t);
					}//else

				}//if
			}//else if
			else {
				;//do nothing, the node is in the tree
			}//
		}//else

		return t;

	}//insert recursive

	private static StringNode rotateLeft(StringNode t) {
		
		StringNode right_child;
		//make pointer changes
		right_child = t.getRight();
		t.setRight(right_child.getLeft());
		right_child.setLeft(t);
		t = right_child;

		return t;
		

	}//rotate left

	private static StringNode rotateRight(StringNode t) {

		StringNode left_child;
		//make pointer changes 
		left_child = t.getLeft();
		t.setLeft(left_child.getRight());
		left_child.setRight(t);
		t = left_child;

		return t;
	}//rotate right

	private static StringNode rootCaseCheck(StringNode root, String s){
		//check for the special case where node stopped splaying
		//one node before the root

		//check for null tree case
		if(root != null){
			int compare_val;
			compare_val = s.compareTo(root.getString());

			//if value is not equal to the root
			//one final zig is needed
			if ( compare_val != 0 ) {
				//check zig direction
				if ( root.getLeft() != null && 
				s.compareTo(root.getLeft().getString()) == 0 ){
					//right
					root = rotateRight(root);
				}//if
				else {
					//left
					root = rotateLeft(root);
				}//else

			}//if
		}//if
		return root;
	}//rootCaseCheck

}//SplayStrings