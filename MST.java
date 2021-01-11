/* MST.java
   CSC 226 - Fall 2018
   Problem Set 2 - Template for Minimum Spanning Tree algorithm
   
   The assignment is to implement the mst() method below, using Kruskal's algorithm
   equipped with the Weighted Quick-Union version of Union-Find. The mst() method computes
   a minimum spanning tree of the provided graph and returns the total weight
   of the tree. To receive full marks, the implementation must run in time O(m log m)
   on a graph with n vertices and m edges.
   
   This template includes some testing code to help verify the implementation.
   Input graphs can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
       java MST
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. graphs.txt), run the program with
       java MST graphs.txt
   
   The input format for both methods is the same. Input consists
   of a series of graphs in the following format:
   
       <number of vertices>
       <adjacency matrix row 1>
       ...
       <adjacency matrix row n>
   	
   For example, a path on 3 vertices where one edge has weight 1 and the other
   edge has weight 2 would be represented by the following
   
   3
   0 1 0
   1 0 2
   0 2 0
   	
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   NOTE: For the purpose of marking, we consider the runtime (time complexity)
         of your implementation to be based only on the work done starting from
	 the mst() method. That is, do not not be concerned with the fact that
	 the current main method reads in a file that encodes graphs via an
	 adjacency matrix (which takes time O(n^2) for a graph of n vertices).
   
   (originally from B. Bird - 03/11/2012)
   (revised by N. Mehta - 10/9/2018)
*/

/*
 * Mohamed Almahmood
 * V00824909
 * Assignment 2
 * 23-10-2018
 * CSC226
 * */

import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.util.Iterator;
import edu.princeton.cs.algs4.*;


public class MST {
	

	//create a class array to hold the root of a vertex and its weight.
	private static class array{
		int root;
		int weight;
	}
	
	
	//create a method that will find the root of an element
	static int find_root(array sub[], int index) {
		//if the vertex is not the root then find the root
		 if (sub[index].root != index) {
	            sub[index].root = find_root(sub, sub[index].root); 
		 }
	        return sub[index].root; 
	}
	
	//This function will unite x and y if no cycle is formed using union by rank
	static void unite_if_no_cycle(array sub[], int v, int w) {
		int root_of_v = find_root(sub, v); //find the root of v 
        int root_of_w = find_root(sub, w); //find the root of w
        //attach the tree with the smaller weight to the tree with the larger weight
        //ie. make the bigger tree the parent of the smaller tree.
        
        //if the weight of Tv is greater than the weight of Tw
        if (sub[root_of_v].weight > sub[root_of_w].weight) {
            sub[root_of_w].root = root_of_v; //attach the root of w to v
        
        }else //if the weight of Tv is less than the weight of Tw
        	if (sub[root_of_v].weight < sub[root_of_w].weight) {
            sub[root_of_v].root = root_of_w;//attach the root of v to w
        //if both trees have equal weights then attach one to the other and increment weight.
        }else { 
            sub[root_of_w].root = root_of_v; 
            sub[root_of_v].weight++; 
        }
	}
	
	
	
    /* mst(adj)
    Given an adjacency matrix adj for an undirected, weighted graph, return the total weight
    of all edges in a minimum spanning tree.

    The number of vertices is adj.length
    For vertex i:
      adj[i].length is the number of edges
      adj[i][j] is an int[2] that stores the j'th edge for vertex i, where:
        the edge has endpoints i and adj[i][j][0]
        the edge weight is adj[i][j][1] and assumed to be a positive integer
 */
    static int mst(int[][][] adj) {
	int n = adj.length;
	
	
	
	/*Create an edge weighted graph*/
	EdgeWeightedGraph G = new EdgeWeightedGraph(n);
	for (int i = 0; i<n; i++) {
		for (int j = 0; j<adj[i].length; j++) {
			Edge e = new Edge(i, adj[i][j][0], adj[i][j][1]);
			G.addEdge(e);
		}
	}
	/*Create a priority queue*/
	MinPQ<Edge> pq = new MinPQ<Edge>();
	for (Edge e : G.edges()) {
		pq.insert(e);
	}
	/*Create an array of size equal to the number of vertices to keep track of subsets*/
	array sub[] = new array[n];
	for (int index = 0; index < n; ++index) {
		sub[index] = new array();
	}
	/*populate the subset with indexes that refer to themselves ie. each index is its own root 
	 *The rank of each root node is zero as no vertices have been inserted yet */
	for(int vertix = 0; vertix < n; ++vertix) {
		sub[vertix].root = vertix;
		sub[vertix].weight = 0;
	}
	
	
	
	
	int weight = 0;
	int i = 0;
	while (adj[i].length < adj.length-1 && !pq.isEmpty()) //while the number of edges is less than the number of vetices - 1
		{
		Edge e = pq.delMin();
		int v = e.either();
		int w = e.other(v);
		
		int s = find_root(sub, v);
		int d = find_root(sub, w);
		
		
		if (s != d) //if these two are not the same, then no cycle is created then union them
			{
			unite_if_no_cycle(sub,s,d);
			weight += e.weight();
			}
		}

	


	/* Add the weight of each edge in the minimum spanning tree
	   to totalWeight, which will store the total weight of the tree.
	*/
	int totalWeight = weight;
	/* ... Your code here ... */
		
	return totalWeight;
  }	
    


    public static void main(String[] args) {
	/* Code to test your implementation */
	/* You may modify this, but nothing in this function will be marked */
    	
  	

	int graphNum = 0;
	Scanner s;

	if (args.length > 0) {
	    //If a file argument was provided on the command line, read from the file
	    try {
		s = new Scanner(new File(args[0]));
	    }
	    catch(java.io.FileNotFoundException e) {
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else {
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}
		
	//Read graphs until EOF is encountered (or an error occurs)
	while(true) {
	    graphNum++;
	    if(!s.hasNextInt()) {
		break;
	    }
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();

	    int[][][] adj = new int[n][][];
	    
	    
	    
	    
	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++) {
		LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
		for (int j = 0; j < n && s.hasNextInt(); j++) {
		    int weight = s.nextInt();
		    if(weight > 0) {
			edgeList.add(new int[]{j, weight});
		    }
		    valuesRead++;
		}
		adj[i] = new int[edgeList.size()][2];
		Iterator it = edgeList.iterator();
		for(int k = 0; k < edgeList.size(); k++) {
		    adj[i][k] = (int[]) it.next();
		}
	    }
	    if (valuesRead < n * n) {
		System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }

	    // // output the adjacency list representation of the graph
	    // for(int i = 0; i < n; i++) {
	    // 	System.out.print(i + ": ");
	    // 	for(int j = 0; j < adj[i].length; j++) {
	    // 	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	    // 	}
	    // 	System.out.print("\n");
	    // }

	    int totalWeight = mst(adj);
	    System.out.printf("Graph %d: Total weight of MST is %d\n",graphNum,totalWeight);

				
	}
    }

    
}