/*
Name: Eric Vance
Class: Comp 282
Assignment 5
Date handed in: 12/7/2015
Description: Comprised of five difference classes. The 
Edge_Node class is used to keep track of the edges that 
attach vertices.  The Vertex_Node class holds the name of 
vertex, the head of the list of neighbors to the vertex, 
the distance to the starting node of the BFS or DFS search, 
and the parent of the vertex based from the search.  
The Graph class contains methods provided by the professor
and the search methods BFS and DFS.  The Graph class will 
read input files and create a graph that consists of a 
linked list of Vertex_Nodes and each Vertex_Node will have
a linked list of Edge_Nodes.  A BFS and a DFS can be
performed on the graph printing the output of the chosen 
search method. The Queue, node, and queueException class  
provide a queue for the BFS to use during implementation.
*/



import java.io.*;// for BufferedReader
import java.util.*;// for StringTokenizer

class Edge_Node {
	private Vertex_Node target;
	private Edge_Node next;

	public Edge_Node(Vertex_Node t, Edge_Node e) {
		target = t;
		next = e;
	}

	public Vertex_Node getTarget(){
		return target;
	}

	public Edge_Node getNext(){
		return next;
	}

	//no setters needed
}

class Vertex_Node {
	private String name;
	private Edge_Node edge_head;
	private int distance;
	private Vertex_Node parent;
	private Vertex_Node next;


	public Vertex_Node (String s, Vertex_Node v) {
		name = s;
		next = v;
		distance = -1;
	}

	public String getName() {
		return name;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int d) {
		distance = d;
	}

	public Edge_Node getNbrList() {
		return edge_head;
	}

	public void setNbrList(Edge_Node e) {
		edge_head = e;
	}
	public Vertex_Node getNext() {
		return next;
	}

	public Vertex_Node getParent() {
		return parent;
	}
	public void setParent(Vertex_Node n) {
		parent = n;
	}
	//helper to output the vertex
	public void printVertex(){
		String p;
		if (parent == null)
			p = "null";
		else
			p = parent.getName();
		System.out.println(name + ", " + distance + ", " + p);
	}


}

class Graph { 

	private Vertex_Node head; 
	private int size; 

	public Graph() { 
		head = null; 
		size = 0; 
	}

	// reset all distance values to -1 
	private void clearDist() { 
		Vertex_Node pt = head;
		while (pt != null){
			pt.setDistance(-1);
			pt = pt.getNext();
		}//end while
	}

	public Vertex_Node findVertex(String s) { 
		Vertex_Node pt = head; 
		while (pt != null && s.compareTo(pt.getName()) != 0) 
			pt = pt.getNext(); 
		return pt; 
	}
	public Vertex_Node input(String fileName) throws IOException { 
		String inputLine, sourceName, targetName; 
		Vertex_Node source = null, target; 
		Edge_Node e; 
		StringTokenizer input; 
		BufferedReader inFile = new BufferedReader(new FileReader(fileName));
		inputLine = inFile.readLine();
		while (inputLine != null) { 
			input = new StringTokenizer(inputLine); 
			sourceName = input.nextToken(); 
			source = findVertex(sourceName); 
			if (source == null) { 
				head = new Vertex_Node(sourceName, head); 
				source = head;
				size++;
			}
			if (input.hasMoreTokens()) { 
				targetName = input.nextToken(); 
				target = findVertex(targetName); 
				if (target == null) { 
					head = new Vertex_Node(targetName, head); 
					target = head; 
					size++; 
				}

				// put edge in one direction -- after checking for repeat 
				e = source.getNbrList(); 
				while (e != null) { 
					if (e.getTarget() == target) {
					System.out.print("Multiple edges from " + source.getName() + " to "); 
					System.out.println(target.getName() + "."); 
					break; 
					} 
					e = e.getNext(); 
				} 
				
				source.setNbrList(new Edge_Node(target, source.getNbrList()));

				 // put edge in the other direction 
				e = target.getNbrList(); 
				while (e != null) { 
					if (e.getTarget() == source) {
					System.out.print("Multiple edges from " + target.getName() + " to ");
					System.out.println(source.getName() + "."); 
					break; 
					} 

				e = e.getNext(); 
				} 

				target.setNbrList(new Edge_Node(source, target.getNbrList()));

			} 
			inputLine = inFile.readLine();
		} 
		inFile.close(); 
		return source;
	}
 

	// You might find this helpful when debugging so that you 
	// can see what the graph actually looks like 
	public void output() { 
		Vertex_Node v = head;
		System.out.println("Im the head: " + v.getName()); 
		Edge_Node e; 
		while (v != null) { 
			System.out.print(v.getName() + ":  "); 
			e = v.getNbrList(); 
			while (e != null) { 
				System.out.print("Neighbors: ");
				System.out.print(e.getTarget().getName() + "  "); 
				e = e.getNext(); 
			} 
			System.out.println(); v = v.getNext(); 
		} 
	}


	public void output_bfs(Vertex_Node s) { 
		Queue q;
		Vertex_Node source, output, ptr;
		Edge_Node e;
		boolean vertices_remain;
		source = s;
		//create queue to hold vertices
		q = new Queue();

		do{
			//queue starting vertex of graph
			q.enqueue(source);
			//increase distance of starting
			//vertex by 1 to equal 0
			source.setDistance(source.getDistance()+1);

			//while queue is not empty
			while(!q.isEmpty()){
				//get neighbor list
				e = source.getNbrList();
				//put non visted vertices on
				//the queue	
				enqueueEdges(q,e,source);
				//output/print front of the queue
				output = q.dequeue();
				output.printVertex();
				//repeat with next item in the queue
				//if not empty
				if(!q.isEmpty())
					source = q.peek();		
			}//end while

			//no vertices remain from current bfs
			vertices_remain = false;
			//check if unconnected vertices exist
			ptr = head;
			while (ptr != null && !vertices_remain){
				if (ptr.getDistance() == -1){
					//found a unnconected vertex
					//vertices remain to be searched
					vertices_remain = true;
					source = ptr;
				}//end if
				ptr = ptr.getNext();
			}//end while
		}//do BFS while unconnected vertices remain
		while(vertices_remain);
		//reset all distances once finished
		clearDist();
	}//end output_bfs

	public void output_dfs(Vertex_Node s) { 
		Vertex_Node ptr;
		output_dfs(s, s.getDistance());

		//check for unconnected vertices
		ptr = head;
		while(ptr != null){
			//if an unconnected vertex remains
			//dfs on that vertex
			if(ptr.getDistance() == -1)
				output_dfs(ptr, ptr.getDistance());
			ptr = ptr.getNext();
		}//end while
		//reset all distances before searching
		clearDist();
	}//end output_dfs driver

	private static void output_dfs(Vertex_Node s, int distance){

		Edge_Node e_ptr;
		//mark node distance/visited
		s.setDistance(distance+1);
		e_ptr = s.getNbrList();
		//output/print current vertex
		s.printVertex();

		while(e_ptr != null){
			//check for unvisted neighbors
			if(e_ptr.getTarget().getDistance() == -1){
				//set parent and DFS unvisited vertex
				e_ptr.getTarget().setParent(s);
				output_dfs(e_ptr.getTarget(), s.getDistance());
			}
			e_ptr = e_ptr.getNext();
		}//end while
	}//end recrusive output_dfs


	 // Similar to what you did in the previous assignment 
	public static boolean implementedDFS() { 
		return true; 
	} 

	public static String myName() { 
		return "Eric Vance"; 
	}

	private void enqueueEdges(Queue q, Edge_Node e, Vertex_Node parent){
		int visited;

		//check neighbor list for unvisted vertices
		while(e != null){
			visited = e.getTarget().getDistance();
			if(visited == -1){
				//if verticie is unvisited
				//set parent and distance
				//add to queue
				e.getTarget().setDistance(parent.getDistance() + 1);
				e.getTarget().setParent(parent);
				q.enqueue(e.getTarget());
			}//end if
			e = e.getNext();
		}//end while
	}//end enqueueEdges

}//end Graph Class

class Queue {
	private Node head;
	private Node tail;
	private int length;
	
	public Queue () {
		head = null;
		tail = null;
		length = 0;
		
	}//end constructor
	
	public boolean isEmpty() {
		return head == null;
	}//end isEmpty
	
	public void enqueue (Vertex_Node v_node){
		Node newNode = new Node(v_node);
		//add node to queue
		if(this.isEmpty()){
			head = newNode;
		} 
		else {
			tail.next = newNode;
		}
		tail = newNode;
		length++;
	}//end enqueue
	
	public Vertex_Node dequeue() throws QueueException {
		//remove first node from queue
		if(!this.isEmpty()) {
			Node temp = head;
			head = head.next;
			length--;
			return temp.item;
			
		} else {
			throw new QueueException("Queue is empty.");
		}
	}//end dequeue
	
	public Vertex_Node peek() throws QueueException {
		//look at front of queue
		if(!this.isEmpty()) {
			return head.item;
		}
		else {
			throw new QueueException("Queue is empty.");
		}
	}//end peek
	
	public int getLength(){
		return length;
	}//end getLength
	
	
}//end Queue Class

class Node {
	Vertex_Node item;
	Node next;

	public Node(Vertex_Node v_node){
		item = v_node;
		Node next = null;
	}//end constructor
		
}//end Node Class

class QueueException extends java.lang.RuntimeException {
	public QueueException(String s){
		super(s);
	}
}//end QueueException