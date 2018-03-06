package fmcr.display.flowgraph;

import java.util.ArrayList;
import java.util.Iterator;

import fmcr.leaks.detectors.CascadeObjectCreationLeak;
import fmcr.leaks.detectors.ClassCascadingObjectCreationLeak;
import fmcr.leaks.detectors.ClassMethodArgAsObjectFieldReferenceLeak;
import fmcr.leaks.detectors.ClassMethodArgAsObjectLeak;
import fmcr.leaks.detectors.ClassMethodArgAsObjectMethodCallLeak;
import fmcr.leaks.detectors.ClassMethodCallLeak;
import fmcr.leaks.detectors.ClassObjectCreationLeak;
import fmcr.leaks.detectors.ClassObjectFieldAccessLeak;
import fmcr.leaks.detectors.ClassVariableAccessLeak;
import fmcr.leaks.detectors.Leak;
import fmcr.leaks.detectors.MethodArgAsObjectFieldReferenceLeak;
import fmcr.leaks.detectors.MethodArgAsObjectLeak;
import fmcr.leaks.detectors.MethodArgAsObjectMethodCallLeak;
import fmcr.leaks.detectors.MethodCallLeak;
import fmcr.leaks.detectors.ObjectFieldAccessLeak;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;

public class KnowledgeGraph {
	private static Graph g;
	private KnowledgeGraph() {
		// prevent instantiation
	}

	public static Graph getInstance() {
		if(g == null) {
			g = new Graph();
			g.getNodeTable().addColumns(NODE_SCHEMA);	
			g.getEdgeTable().addColumns(EDGE_SCHEMA);
		}
		        
		return g;
	}

	private static Node addNode(String label, Leak leak) {
		Node node = g.addNode();

		@SuppressWarnings("unchecked")
		ArrayList<Leak> leaks = (ArrayList<Leak>)node.get(LEAKS);
		leaks.add(leak);
		node.set(LEAKS, leaks);

		node.setString(LABEL, label);
		
		return node;
	}

	public static Node includeNode(String label, Leak leak) {
		Node node = null;
		@SuppressWarnings("rawtypes")
		Iterator iter = g.nodes();
		
		while(iter.hasNext()) {
			Node node_ = (Node)iter.next();
			String nlabel = (String)node_.get(LABEL);

			if(nlabel.equals(label)) {
				@SuppressWarnings("unchecked")
				ArrayList<Leak> leaks = (ArrayList<Leak>)node_.get(LEAKS);
				leaks.add(leak);
				node_.set(LEAKS, leaks);

				double nsize = (Double)node_.get(NODESIZE);
				nsize = nsize +0.1;
				node_.set(NODESIZE, nsize);
				node = node_;
				
				break;
			}
		}
		if(node ==null) {
			node = addNode(label, leak);
		}
		
		return node;
	}
	
	public static Edge addEdge(Node source, Node target) {
		Edge edge = null;
		String sourcelabel = (String)source.get(LABEL);
		String targetlabel = (String)target.get(LABEL);
		String stlabel = sourcelabel+"-"+targetlabel;
		
		@SuppressWarnings("rawtypes")
		Iterator iter = g.edges();		
		while(iter.hasNext()) {
			Edge edge_ = (Edge)iter.next();
			String elabel = (String)edge_.get(LABEL);
			if(elabel.equals(stlabel)) {
				double esize = (Double)edge_.get(EDGESIZE);
				esize = esize +0.1;
				edge_.set(NODESIZE, esize);
				edge = edge_;
				
				break;
			}
		}
		if(edge == null) {
			edge = g.addEdge(source, target);
			edge.set(LABEL, stlabel);
		}
		
		return edge;
	}
	

	private static void addCallEdge(String node1Name, String node2Name, Leak leak) {
		Node node1 = includeNode(node1Name, leak);
		Node node2 = includeNode(node2Name, leak);
		
		addEdge(node1, node2);
		rnodes.add(node1);
		rnodes.add(node2);
	}
	
	private static ArrayList<Node> rnodes;
	public static ArrayList<Node> addCallEdge(Leak r) {
		rnodes = new ArrayList<Node>();
		
		if(r instanceof CascadeObjectCreationLeak) {
			r  = (CascadeObjectCreationLeak)r;

			CascadeObjectCreationLeak cascadeLeak = ((CascadeObjectCreationLeak) r).getCascadeObjectCreationLeak();
			if(cascadeLeak == null) {
				String node1Name = ((CascadeObjectCreationLeak) r).getContainingObjectType();
				String node2Name =((CascadeObjectCreationLeak) r).getContainedObjectType();
				addCallEdge(node1Name,node2Name,r);
			}
			else {
				String node1Name = ((CascadeObjectCreationLeak) r).getContainingObjectType();
				String node2Name = cascadeLeak.getContainedObjectType();
				addCallEdge(node1Name,node2Name,r);

				while(cascadeLeak !=null) {
					node1Name = cascadeLeak.getContainingObjectType();
					if(cascadeLeak.getCascadeObjectCreationLeak() == null) {
						node2Name = cascadeLeak.getContainedObjectType();
						addCallEdge(node1Name,node2Name,r);
					}
					else {
						node2Name = cascadeLeak.getCascadeObjectCreationLeak().getContainedObjectType();
						addCallEdge(node1Name,node2Name,r);
					}					
					cascadeLeak = cascadeLeak.getCascadeObjectCreationLeak();
				}
			}
		}

		else if(r instanceof MethodArgAsObjectFieldReferenceLeak) {
			r  = (MethodArgAsObjectFieldReferenceLeak)r;
			String node1Name = ((MethodArgAsObjectFieldReferenceLeak) r).getHandlerType();
			String node2Name = ((MethodArgAsObjectFieldReferenceLeak) r).getFieldHandlerType();
			String node3Name = ((MethodArgAsObjectFieldReferenceLeak) r).getFieldType();
			addCallEdge(node1Name,node2Name,r);
			addCallEdge(node2Name,node3Name,r);
		}
		else if(r instanceof MethodArgAsObjectLeak) {
			r  = (MethodArgAsObjectLeak)r;
			String node1Name = ((MethodArgAsObjectLeak) r).getHandlerType();
			String node2Name = ((MethodArgAsObjectLeak) r).getParameterType();
			addCallEdge(node1Name,node2Name,r);
		}
		else if(r instanceof MethodArgAsObjectMethodCallLeak) {
			r  = (MethodArgAsObjectMethodCallLeak)r;
			String node1Name =((MethodArgAsObjectMethodCallLeak) r).getParameterType();
			String node2Name = ((MethodArgAsObjectMethodCallLeak) r).getHandlerType();
			String node3Name = ((MethodArgAsObjectMethodCallLeak) r).getMethodReturnType();
			String node4Name = ((MethodArgAsObjectMethodCallLeak) r).getMethodName();
			addCallEdge(node1Name,node2Name,r);
			if(!node3Name.equalsIgnoreCase("void")) {
				addCallEdge(node2Name,node3Name,r);
			}			
			addCallEdge(node2Name,node4Name,r);
		}
		else if(r instanceof MethodCallLeak) {
			r  = (MethodCallLeak)r;
			String node1Name = ((MethodCallLeak) r).getHandlerType();
			String node2Name = ((MethodCallLeak) r).getMethodReturnType();
			String node3Name = ((MethodCallLeak) r).getMethodName();
			if(!node2Name.equalsIgnoreCase("void")) {
				addCallEdge(node1Name,node2Name,r);	
			}			
			addCallEdge(node1Name,node3Name,r);
		}
		else if(r instanceof ObjectFieldAccessLeak) {
			r  = (ObjectFieldAccessLeak)r;
			String node1Name = ((ObjectFieldAccessLeak) r).getFieldHandlerType();
			String node2Name = ((ObjectFieldAccessLeak) r).getFieldType();
			addCallEdge(node1Name,node2Name,r);
		}

		else if(r instanceof ClassCascadingObjectCreationLeak) {
			//secondary as it is converted to CascadingObjectCreationLeak
		}
		else if(r instanceof ClassMethodArgAsObjectFieldReferenceLeak) {
			r  = (ClassMethodArgAsObjectFieldReferenceLeak)r;	
			String node1Name = ((ClassMethodArgAsObjectFieldReferenceLeak) r).getClassName();
			String node2Name = ((ClassMethodArgAsObjectFieldReferenceLeak) r).getHandlerType();
			String node3Name = ((ClassMethodArgAsObjectFieldReferenceLeak) r).getFieldHandlerType();
			String node4Name = ((ClassMethodArgAsObjectFieldReferenceLeak) r).getFieldType();
			addCallEdge(node1Name,node2Name,r);
			addCallEdge(node2Name,node3Name,r);
			addCallEdge(node3Name,node4Name,r);
		}
		else if(r instanceof ClassMethodArgAsObjectLeak) {
			r  = (ClassMethodArgAsObjectLeak)r;
			String node1Name = ((ClassMethodArgAsObjectLeak) r).getClassName();
			String node2Name = ((ClassMethodArgAsObjectLeak) r).getHandlerType();
			String node3Name = ((ClassMethodArgAsObjectLeak) r).getParameterType();
			addCallEdge(node1Name,node2Name,r);
			addCallEdge(node2Name,node3Name,r);

		}
		else if(r instanceof ClassMethodArgAsObjectMethodCallLeak) {

			r  = (ClassMethodArgAsObjectMethodCallLeak)r;
			String node1Name = ((ClassMethodArgAsObjectMethodCallLeak) r).getClassName();
			String node2Name = ((ClassMethodArgAsObjectMethodCallLeak) r).getParameterType();
			String node3Name = ((ClassMethodArgAsObjectMethodCallLeak) r).getHandlerType();
			String node4Name = ((ClassMethodArgAsObjectMethodCallLeak) r).getMethodReturnType();
			String node5Name = ((ClassMethodArgAsObjectMethodCallLeak) r).getMethodName();
			addCallEdge(node1Name,node2Name,r);
			addCallEdge(node2Name,node3Name,r);
			if(!node4Name.equalsIgnoreCase("void")) {
				addCallEdge(node3Name,node4Name,r);
			}
			addCallEdge(node3Name,node5Name,r);
		}
		else if(r instanceof ClassMethodCallLeak) {
			r  = (ClassMethodCallLeak)r;
			String node1Name = ((ClassMethodCallLeak) r).getClassName();
			String node2Name = ((ClassMethodCallLeak) r).getHandlerType();
			String node3Name = ((ClassMethodCallLeak) r).getMethodReturnType();
			String node4Name = ((ClassMethodCallLeak) r).getMethodName();
			addCallEdge(node1Name,node2Name,r);
			if(!node3Name.equalsIgnoreCase("void")) {
				addCallEdge(node2Name,node3Name,r);
			}
			addCallEdge(node2Name,node4Name,r);
		}
		else if(r instanceof ClassObjectCreationLeak) {
			r  = (ClassObjectCreationLeak)r;
			String node1Name = ((ClassObjectCreationLeak) r).getClassName();
			String node2Name = ((ClassObjectCreationLeak) r).getCreatedObjectType();
			addCallEdge(node1Name,node2Name,r);

		}
		else if(r instanceof ClassObjectFieldAccessLeak) {
			r  = (ClassObjectFieldAccessLeak)r;
			String node1Name = ((ClassObjectFieldAccessLeak) r).getClassName();
			String node2Name = ((ClassObjectFieldAccessLeak) r).getFieldHandlerType();
			String node3Name = ((ClassObjectFieldAccessLeak) r).getFieldType();
			addCallEdge(node1Name,node2Name,r);
			addCallEdge(node2Name,node3Name,r);

		}
		else if(r instanceof ClassVariableAccessLeak) {
			r  = (ClassVariableAccessLeak)r;
			String node1Name = ((ClassVariableAccessLeak) r).getClassName();
			String node2Name = ((ClassVariableAccessLeak) r).getAccessVariableType();
			addCallEdge(node1Name,node2Name,r);			
		}

		return rnodes;
	}
		
	public static void clearNodes() {
		g.clear();
	}


	/** Data fields included in generated Graphs */
	public static final String LABEL = "label";
	public static final String NODESIZE = "nodesize";
	public static final String EDGESIZE = "edgesize";
	public static final String LEAKS = "leaks";

	/** Node table schema used for generated Graphs */
	public static final Schema NODE_SCHEMA = new Schema();
	static {
		NODE_SCHEMA.addColumn(LABEL, String.class, "");
		NODE_SCHEMA.addColumn(NODESIZE, Double.class, 5);
		NODE_SCHEMA.addColumn(LEAKS, ArrayList.class, new ArrayList<Leak>());

	}
	public static final Schema EDGE_SCHEMA = new Schema();
	static {
		EDGE_SCHEMA.addColumn(LABEL, String.class, "");
		EDGE_SCHEMA.addColumn(EDGESIZE, Double.class, 1);
	}
}
