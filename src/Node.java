import java.util.Map.Entry;
import java.util.ArrayList;


public class Node {
	public String name;
	public ArrayList<Entry<Node, Integer>> connectedNodes;

	public ArrayList<Entry<Node, Integer>> connectedFlags;
	public int accessCost;
	public int spanningCost;
	public int minCost;
	public boolean flagged; 
	public boolean visited;
	
	public Node(String name, int flagNumber, int totalNodes) {
		
		this.name = name;
		connectedNodes = new ArrayList<Entry<Node, Integer>>(totalNodes);
		accessCost = Integer.MAX_VALUE;
		connectedFlags = new ArrayList<Entry<Node, Integer>>(flagNumber);
		spanningCost = Integer.MAX_VALUE;
		minCost = Integer.MAX_VALUE;
		flagged = false;
		visited = false;
	}
	
}
