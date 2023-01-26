import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;


public class project4 {
	public static void main(String args[]) {
		DreamRambo dr = new DreamRambo();
		int numberOfNodes = 0; 
		int nodesWithFlag = 0;
		
		HashMap<String, Node> container = new HashMap<String, Node>();
		
		try {
		      FileReader file = new FileReader(args[0]);
		      BufferedReader br = new BufferedReader(file);
		      FileWriter myWriter = new FileWriter(args[1]);
		      int line = 0;
		      
		      while (br.ready()) {
		    	  
		        String fullLine = br.readLine();
		        String data[] = fullLine.split(" ");
		        
		        if (line == 0) {
		        	numberOfNodes = Integer.parseInt(data[0]);
		        	dr.setNumberOfNodes(numberOfNodes);
		        }
		        
		        else if (line == 1) {
		        	nodesWithFlag = Integer.parseInt(data[0]);
		        }
		        
		        else if (line == 2) {
		        	Node startNode = new Node(data[0], nodesWithFlag, data.length/2);
		        	Node endNode = new Node( data[1], nodesWithFlag, data.length/2);
		        	container.put(data[0], startNode);
		        	container.put(data[1], endNode);
		        	dr.setStartNode(startNode);
		        	dr.setEndNode(endNode);
		        }
		        
		        else if (line == 3) {
		        	for (String elem: data) {
		        		Node n;
		        		if (!container.containsKey(elem)) {
		        			n = new Node(elem, nodesWithFlag, data.length/2);
		        			container.put(elem, n);
		        		}
		        		else {
		        			n = container.get(elem);
		        		}
		        		n.flagged = true;
		        		dr.addFlaggedNode(n);
		        	}
		        }
		        
		        else {
		        	String firstName = data[0];
		        	Node initialNode;
		        	if (!container.containsKey(firstName)) {
		        		initialNode = new Node(firstName, nodesWithFlag, data.length/2);
		        		container.put(firstName, initialNode);
		        	}
		        	else {
		        		initialNode = container.get(firstName);
		        	}

		        	int name = 1;
		        	int distance = name + 1;
		        	while (distance < data.length) {
		        		String nodeName = data[name];
		        		int nodeWeight = Integer.parseInt(data[distance]);
		        		Node currentNode;
		        		if (!container.containsKey(nodeName)) {
		        			currentNode = new Node(nodeName, nodesWithFlag, data.length/2);
			        		container.put(nodeName, currentNode);
			        	}
			        	else {
			        		currentNode = container.get(nodeName);
			        	}
		        		initialNode.connectedNodes.add(new SimpleEntry<Node, Integer>(currentNode, nodeWeight));
		        		currentNode.connectedNodes.add(new SimpleEntry<Node, Integer>(initialNode, nodeWeight));
		        		name += 2;
		        		distance += 2;
		        	}
		        }
		        
		        line++;
		        
		      }
		      
		      myWriter.write(Integer.toString(dr.findMinDistance()) + "\n");
		      myWriter.write(Integer.toString(dr.findMinSpanningTree()));
		      br.close();
		      myWriter.close();
		      
	    } catch (Exception e) {

	      e.printStackTrace();
	    }
		

	}
}
