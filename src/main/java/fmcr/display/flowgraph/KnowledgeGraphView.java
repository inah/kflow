package fmcr.display.flowgraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

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


public class KnowledgeGraphView extends JPanel {
	private static final long serialVersionUID = 1L;
	static Graph graph;
	Viewer viewer;
		
	public KnowledgeGraphView() {		

		setBackground(Color.white);

		if(graph == null || viewer == null) {
			System.setProperty("org.graphstream.ui.renderer","org.graphstream.ui.j2dviewer.J2DGraphRenderer");
			
			JPanel panel = new JPanel(new GridLayout()){
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize() {
					return new Dimension(540, 350);
				}
			};
			
			graph = new SingleGraph("KnowledgeGraph");
			
			viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

			ViewPanel viewPanel = viewer.addDefaultView(false);
			viewer.enableAutoLayout();

			panel.add(viewPanel);
			panel.setBorder(BorderFactory.createLineBorder(Color.blue, 2));			
			add(panel);

			ViewerPipe pipe = viewer.newViewerPipe();
			NodeClickListener clistener = new NodeClickListener(pipe,viewer.getDefaultView(), graph);
			pipe.addViewerListener((ViewerListener)clistener);
			
			graph.addAttribute("ui.stylesheet", "graph { fill-color: rgb(255, 255, 242); }"+"node { fill-color: red;"
					+ "fill-mode: plain;"
					+ "stroke-mode: plain;"
					+ "text-color: #ff0000;"
					+ "text-mode: hidden;"
					+ "stroke-color: blue;}"
					
					+ "node:clicked {text-mode: normal;"
					+ "text-color: black;}"
					+ "node:selected {stroke-width: 5;"
					+ "stroke-color: black;"
					+ "stroke-mode: plain;}");
		}
		else {
			graph.clear();
			graph.addAttribute("ui.stylesheet", "graph { fill-color: rgb(255, 255, 242); }"+"node { fill-color: red;"
					+ "fill-mode: plain;"
					+ "stroke-mode: plain;"
					+ "text-color: #ff0000;"
					+ "text-mode: hidden;"
					+ "stroke-color: blue;}"
					
					+ "node:clicked {text-mode: normal;"
					+ "text-color: black;}"
					+ "node:selected {stroke-width: 5;"
					+ "stroke-color: black;"
					+ "stroke-mode: plain;}");			
		}

	

	}

	
//	public void addRepoColorLabel(String repoName, int keyindex) {
//		JLabel label = new JLabel();
//		if(!QSourceInputDialog.isUsingGithubRepository()) {
//			if(repoName.contains("/")) {
//				String [] dirs = repoName.split("/");
//				repoName = dirs[dirs.length-1];
//			}
//		}
//		
//		label.setText("<html> <div style=\'font-size: small; font-family: sans-serif\'><p style=\' background-color: "+NodeColorPick.getColor(keyindex)+"\'>"+repoName+"</p>\n" + 
//				"</div></html>");
//		keypanel.add(label);
//		keypanel.revalidate();
//		keypanel.repaint();
//	}

	private static int keyindex;
	public static int getKeyindex() {
		return keyindex;
	}

	public static void setKeyindex(int keyindex) {
		KnowledgeGraphView.keyindex = keyindex;
	}

	public void addCallEdge(Leak r) {
		//Client.getDisplay().updateLogPage(r.toString(), false);
	
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
				
		
	}
	
	private void addCallEdge(String node1Name, String node2Name, Leak leak) {

		Node node1 = null;
		Node node2 = null;
				
		node1 = graph.getNode(node1Name);
		if(node1 == null) {
			node1 = graph.addNode(node1Name );
			node1.addAttribute("ui.label", node1Name);	
			ArrayList<Leak> leaks = new ArrayList<Leak>();
			leaks.add(leak);
			node1.addAttribute("leaks", leaks); //essentially the number of information leaks related to this node
			node1.addAttribute("ui.style", "fill-color: "+NodeColorPick.getColor(keyindex)+";");
			node1.addAttribute("ui.style", "stroke-color: "+NodeColorPick.getColor(keyindex)+";");	
			double size = 10;
			node1.addAttribute("ui.size", size); 
			node1.addAttribute("ui.style" , ("size: "+size+"px, "+size+"px; "));	
		}
		else {
			ArrayList<Leak> leaks = node1.getAttribute("leaks");
			leaks.add(leak);
			node1.addAttribute("leaks", leaks);
			
			double size = node1.getAttribute("ui.size");
			size = size +0.1;
			node1.addAttribute("ui.size", size);	
			node1.addAttribute("ui.style" , ("size: "+size+"px, "+size+"px; "));
			
		}

		if(node1Name.equals(node2Name)) {
			node2 = node1;
		}
		else {
			node2 = graph.getNode(node2Name);
			if(node2 == null) {
				node2 = graph.addNode(node2Name);
				node2.addAttribute("ui.label", node2Name);	
				ArrayList<Leak> leaks = new ArrayList<Leak>();
				leaks.add(leak);
				node2.addAttribute("leaks", leaks);
				node2.addAttribute("ui.style", "fill-color: "+NodeColorPick.getColor(keyindex)+";");
				node2.addAttribute("ui.style", "stroke-color: "+NodeColorPick.getColor(keyindex)+";");		
				double size = 10;
				node2.addAttribute("ui.size", size); 
				node2.addAttribute("ui.style" , ("size: "+size+"px, "+size+"px; "));

			}
			else {
				ArrayList<Leak> leaks = node2.getAttribute("leaks");
				leaks.add(leak);
				node2.addAttribute("leaks", leaks);
				double size = node2.getAttribute("ui.size");
				size = size +0.1;
				node2.addAttribute("ui.size", size);	
				node2.addAttribute("ui.style" , ("size: "+size+"px, "+size+"px; "));
			}	
		}

		Edge edge = graph.getEdge(node1Name+"->"+node2Name);
		if(edge == null) {
			edge = graph.addEdge(node1Name+"->"+node2Name,node1, node2, true);
			double size = 0.5;
			edge.addAttribute("ui.size", size);	
			edge.addAttribute("ui.style" , ("size: "+size+"px, "+size+"px; "));
		}
		else {
			double size = edge.getAttribute("ui.size");
			size = size +0.5;
			edge.addAttribute("ui.size", size);	
			edge.addAttribute("ui.style" , ("size: "+size+"px, "+size+"px; "));
		}
	}
	
	public void clearGraph() {
		graph.clear();
	}

}


