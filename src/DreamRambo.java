import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class DreamRambo {

	public ArrayList<Node> flaggedNodes;
	public ArrayList<Node> updatedFlaggedNodes;
	public Node startNode;
	public Node endNode;
	int numberOfNodes;


	public DreamRambo() {

		flaggedNodes = new ArrayList<Node>();
		updatedFlaggedNodes = new ArrayList<Node>();

	}
	
	class NodeComparator implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			if (n1.accessCost > n2.accessCost) {
				return 1;
			}
			else {
				return -1;
			}
		}
		
	}
	class SpanningNodeComparator implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			if (n1.spanningCost > n2.spanningCost) {
				return 1;
			}
			else {
				return -1;
			}
		}
		
	}
	 
	class minDistanceComparator implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			if (n1.minCost > n2.minCost) {
				return 1;
			}
			else {
				return -1;
			}
		}
		
	}

	public void addFlaggedNode(Node n) {
		flaggedNodes.add(n);
	}
	public void setNumberOfNodes(int n) {
		this.numberOfNodes = n;
	}
	
	public void setStartNode(Node n) {
		this.startNode = n;
	}
	public void setEndNode(Node n) {
		this.endNode = n;
	}
	// this function finds the shortest distance between start and end nodes. 
	public int findMinDistance() {
		startNode.minCost = 0;
		PriorityQueue<Node> minHeap = new PriorityQueue<Node>(new minDistanceComparator());
		HashSet<Node> visitedNodes = new HashSet<Node>();
		minHeap.add(startNode);
		//Used dijkstra with adjacency list and min heap
		while (visitedNodes.size() < numberOfNodes) {

			if (minHeap.isEmpty()) {

				return -1;
			}
			Node currentNode = minHeap.peek();
			minHeap.remove();
			if (visitedNodes.contains(currentNode)) {
				continue;
			}

			for (Entry<Node, Integer> connectedNodePair: currentNode.connectedNodes) {
				
				int distance = connectedNodePair.getValue();
				Node adjNode = connectedNodePair.getKey();		

				if (visitedNodes.contains(adjNode)) { 
					continue;
				}
				
				if (currentNode.minCost + distance < adjNode.minCost) {
					adjNode.minCost = currentNode.minCost + distance;
					minHeap.add(adjNode);
				}				
			}		
			visitedNodes.add(currentNode);
			if (currentNode.name.equals(endNode.name)) {
				break;
			}		
		}
		
		return endNode.minCost;
	}
	
	// I used dijkstra algorithm as many as the number of flags and created another map consisting of flags and all flags are connected to each other.
	private boolean fillUpdatedFlaggedNodes() {
		// for each flag run the algorithm
		for (Node startNode: flaggedNodes) {
			startNode.accessCost = 0;
			PriorityQueue<Node> minCostHeap = new PriorityQueue<Node>(new NodeComparator());

			HashSet<Node> reachedNodes = new HashSet<Node>();
			minCostHeap.add(startNode);
			
			int visitedSize = 0;
			int counter = 0;

			while (visitedSize < numberOfNodes) {
				// stop the algorithm when all the flags are visited
				if (counter == flaggedNodes.size()) {
					break;
				}
				if (minCostHeap.isEmpty()) {
					return false;
				}
				Node currentNode = minCostHeap.peek();
				minCostHeap.remove();
				if (currentNode.visited) {
					continue;
				}
				if (currentNode.flagged) {
					if (!currentNode.name.equals(startNode.name)) {
						startNode.connectedFlags.add(new SimpleEntry<Node, Integer>(currentNode, currentNode.accessCost));
					}
				}
				for (Entry<Node, Integer> connectedNodePair: currentNode.connectedNodes) {
					
					int distance = connectedNodePair.getValue();
					Node adjNode = connectedNodePair.getKey();

					if (adjNode.visited) { 
						continue;
					}

					if (currentNode.accessCost + distance < adjNode.accessCost) {					
						adjNode.accessCost = currentNode.accessCost + distance;
						
						reachedNodes.add(adjNode);
						minCostHeap.add(adjNode);
					}														
				}						
				reachedNodes.add(currentNode);
				currentNode.visited = true;
	
				if (currentNode.flagged) {
					counter++;
				}
			}
			// reset the values for the next iteration
			for (Node n : reachedNodes) {
				n.accessCost = Integer.MAX_VALUE;
				n.visited = false;
			}
			
			
			// after setting all the adjacent flags, insert the current flag into the map. And do this for every flag
			updatedFlaggedNodes.add(startNode);

		}
		return true;
		
	}
	// now it is time to run Prim's algorithm to find MST and the minimum cost to visit all the flags. Since I extract only the flagged nodes in 
	// "fillUpdatedFlaggedNodes()" method all things are set to use this algorithm.
	public int findMinSpanningTree() {

		if (!fillUpdatedFlaggedNodes()) {
			return -1;
		}

		
		
		HashSet<Node> visitedNodes = new HashSet<Node>();
		
		PriorityQueue<Node> minDistanceHeap = new PriorityQueue<Node>(new SpanningNodeComparator());
		Node startNode = updatedFlaggedNodes.get(0);
		startNode.spanningCost = 0;
		minDistanceHeap.add(startNode);

		while (visitedNodes.size() < updatedFlaggedNodes.size()) {
			Node currentFlag = minDistanceHeap.peek();

			minDistanceHeap.clear();
			if (visitedNodes.contains(currentFlag)) {				
				continue;
			}

			for (Entry<Node, Integer> connectedFlagPair: currentFlag.connectedFlags) {
				Node adjFlag = connectedFlagPair.getKey();
				int distance = connectedFlagPair.getValue();
				if (!visitedNodes.contains(adjFlag)) {
					if (adjFlag.spanningCost > distance) {

						adjFlag.spanningCost = distance;
						
					}
					minDistanceHeap.add(adjFlag);

				}
			}

			
			
			visitedNodes.add(currentFlag);
		}
		int spanningSum = 0;
		
		for(Node node: updatedFlaggedNodes) {

			spanningSum += node.spanningCost;
		}
		return spanningSum;
	}
	

}
