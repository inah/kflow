package fmcr.display;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import fmcr.main.Client;
import fmcr.visitors.CodeVisitor;

public class ASTView extends JPanel {
	private static final long serialVersionUID = -2707712944901661771L;

	public FoldableTree graph;
	private mxCompactTreeLayout layout;
	
	private HashMap<String,String> parentChildRelations;
	
	public Object createVertex(String identifier, String name) {
		Object vertex = null;
		
		String vertexCodeAttribution = CodeVisitor.getVertexCodeAttribution(identifier);
		String codeAttributionOfInterest = Client.getDisplay().selectedLine;
		if(codeAttributionOfInterest !=null ) {
			vertexCodeAttribution = vertexCodeAttribution.replaceAll("\\s+","");
			vertexCodeAttribution = vertexCodeAttribution.replace(",", "");
			vertexCodeAttribution = vertexCodeAttribution.replaceAll("[\\s|\\u00A0]+", "");
			codeAttributionOfInterest = codeAttributionOfInterest.replaceAll("\\s+","");
			codeAttributionOfInterest = codeAttributionOfInterest.replace(",", "");
			codeAttributionOfInterest = codeAttributionOfInterest.replaceAll("[\\s|\\u00A0]+", "");
			
		}
		
		if(codeAttributionOfInterest !=null && vertexCodeAttribution != null && vertexCodeAttribution.contains(codeAttributionOfInterest)) {		
			vertex= graph.insertVertex(graph.getDefaultParent(), identifier, name, 0, 0, 60+(name.length()*4), 39,"ROUNDED;strokeColor=black;fillColor=yellow");
		}
		else {
			vertex= graph.insertVertex(graph.getDefaultParent(), identifier, name, 0, 0, 60+(name.length()*4), 39,"ROUNDED;strokeColor=black;fillColor=white");
		}
		
		return vertex;
	}
	
	public Object getVertex(String identifier) {
		Object object = null;
		Object[] cells = graph.getChildVertices(graph.getDefaultParent());
		for (Object c : cells){
		    mxCell cell = (mxCell) c;
		    if(cell.getId().equals(identifier)) {
		    		object = c;
		    		break;
		    }
		}
		return object;
	}
	
	public String getParentIdentifier(String childId) {
		
		String parentId = null;	    
	    for (Map.Entry<String, String> entry : parentChildRelations.entrySet()) {
	        String temp_childId = entry.getKey();
	        if(childId.equals(temp_childId)) {
	        		parentId = entry.getValue();
	        }	 
	    }
		return parentId;
	}
	
	public String getVertexLabel(String vertextId) {
		String label = null;
		Object[] cells = graph.getChildVertices(graph.getDefaultParent());
		for (Object c : cells){
		    mxCell cell = (mxCell) c;
		    if(cell.getId().equals(vertextId)) {
		    		label = cell.getValue().toString();
		    		break;
		    }
		}
		return label;
	}
	
	
	public void addEdge(Object parent, Object child, String parentId, String childId) {
		graph.insertEdge(graph.getDefaultParent(), null, "", parent, child);
		parentChildRelations.put(childId, parentId);
	}
	
	public void showTree() {
		layout.execute(graph.getDefaultParent()); 
		graph.getModel().endUpdate();
		
		mxGraphView view = graph.getView();
		view.setScale(Client.getDisplay().zoom);				
		Client.getDisplay().updateASTView();
	}
	
	public void clearGraph() {
		Object[] cells = graph.getChildVertices(graph.getDefaultParent());
		graph.removeCells(cells);
        graph.getModel().beginUpdate();

	}
	
	public ASTView() {
		setBackground(Color.white);
		
		graph = new FoldableTree();
		graph.setBorder(0);
		layout = new mxCompactTreeLayout(graph, false);
		layout.setUseBoundingBox(false);
		layout.setEdgeRouting(false);
		layout.setLevelDistance(30);
		layout.setNodeDistance(10);
		
		parentChildRelations = new HashMap<String, String>();

//		mxStylesheet stylesheet = new mxStylesheet();
//		stylesheet.
//		graph.setStylesheet(arg0);

		graph.addListener(mxEvent.FOLD_CELLS, new mxIEventListener() {
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				layout.execute(graph.getDefaultParent());
			}
		});

		mxGraphComponent graphComponent = new mxGraphComponent(graph);

		add(graphComponent);
	}
	
//	@Override
//	public void paintComponent(Graphics g) {
//	    g.setColor(Color.blue);
//	    g.fillRect(0, 0, getWidth(), getHeight());
//	}

//	public static void main(String[] args) {
//		ChampsTree frame = new ChampsTree();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(400, 320);
//		frame.setVisible(true);
//	}
}
