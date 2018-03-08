package fmcr.display.flowgraph;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import fmcr.leaks.detectors.Leak;
import fmcr.main.Client;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.PolygonRenderer;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.GraphicsLib;
import prefuse.util.PrefuseLib;
import prefuse.util.collections.IntIterator;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.DecoratorItem;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * Demo application showcasing the use of AggregateItems to
 * visualize groupings of nodes with in a graph visualization.
 * It also shows:
 * - Decorators for edges 
 * - Decorators for nodes 
 * - Decorators for aggregates (only on mouseOver) 
 * - using a label-field which contains the String to be drawn by the decorators 
 * - currently, only one Renderer (a LabelRenderer) ist used for all the 
 * 		decorator groups (so all decorators show a text) 
 * - using different text formatting for each decorator group 
 * - using any shape for the nodes (ShapeRenderer on the nodes) together with a 
 * 		label (LabelRenderer on the nodes decorators) 
 * - all decorator groups use the same Layout, they are centered on the item 
 * 		they decorate
 * 
 * This class uses the AggregateLayout class to compute bounding
 * polygons for each aggregate and the AggregateDragControl to
 * enable drags of both nodes and node aggregates.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author Björn Kruse
 * @author Inah Omoronyia
 */
public class KnowledgeGraphView extends Display{
	private static final long serialVersionUID = -4910589796925187366L;
	
	public static final String GRAPH = "graph";
	public static final String NODES = "graph.nodes";
	public static final String EDGES = "graph.edges";
	public static final String AGGR = "aggregates";
	
    public static final String EDGE_DECORATORS = "edgeDeco";
    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema(); 
    static { 
    	DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false); 
    	DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128)); 
    	DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma",8));
    }
    
	private LabelRenderer m_nodeRenderer;
//	private EdgeRenderer m_edgeRenderer;
    private String m_label = "label";

	public KnowledgeGraphView() {		

		// initialize display and data
		super(new Visualization());
		cgid = -1;
		initDataGroups();

		// set up the renderers
		// draw the nodes as basic shapes
		Renderer nodeR = new ShapeRenderer(20);
		// draw aggregates as polygons with curved edges
		Renderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
		((PolygonRenderer)polyR).setCurveSlack(0.15f);

 
		// -- set up renderers --
		m_nodeRenderer = new LabelRenderer(m_label);
        m_nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_DRAW_AND_FILL);
        m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
        m_nodeRenderer.setRoundedCorner(8,8);
        
//        m_edgeRenderer = new EdgeRenderer();
        
		DefaultRendererFactory drf = new DefaultRendererFactory();
		drf.setDefaultRenderer(nodeR);
//        drf.add(new InGroupPredicate(EDGES), m_edgeRenderer);
        drf.add(new InGroupPredicate(EDGE_DECORATORS), new LabelRenderer(VisualItem.LABEL));
        drf.add(new InGroupPredicate(NODES), m_nodeRenderer);

		drf.add("ingroup('aggregates')", polyR);
		m_vis.setRendererFactory(drf);

		DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(0));
        m_vis.addDecorators(EDGE_DECORATORS, EDGES, DECORATOR_SCHEMA);
        
		// set up the visual operators
		// first set up all the color actions
		ColorAction nStroke = new ColorAction(NODES, VisualItem.STROKECOLOR);
		nStroke.setDefaultColor(ColorLib.gray(100));
		nStroke.add("_hover", ColorLib.gray(50));

		ColorAction nFill = new ColorAction(NODES, VisualItem.FILLCOLOR);
		nFill.setDefaultColor(ColorLib.gray(255));
		nFill.add("_hover", ColorLib.gray(200));

		ColorAction nEdges = new ColorAction(EDGES, VisualItem.STROKECOLOR);
		nEdges.setDefaultColor(ColorLib.gray(100));

		ColorAction aStroke = new ColorAction(AGGR, VisualItem.STROKECOLOR);
		aStroke.setDefaultColor(ColorLib.gray(200));
		aStroke.add("_hover", ColorLib.rgb(255,100,100));

        ColorAction arrow = new ColorAction(EDGES, VisualItem.FILLCOLOR, ColorLib.gray(100));

		ColorAction text = new ColorAction(NODES,VisualItem.TEXTCOLOR, ColorLib.gray(0));
		
		int[] palette = new int[] {
				ColorLib.rgba(255,200,200,150),
				ColorLib.rgba(200,255,200,150),
				ColorLib.rgba(200,200,255,150)
		};
		ColorAction aFill = new DataColorAction(AGGR, "id",Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
		
        
		// bundle the color actions
		ActionList colors = new ActionList();
		colors.add(nStroke);
		colors.add(nFill);
		colors.add(nEdges);
		colors.add(aStroke);
		colors.add(aFill);
		colors.add(text);
		colors.add(arrow);
		

		// now create the main layout routine
		ActionList layout = new ActionList(Activity.INFINITY);
		layout.add(colors);
		layout.add(new ForceDirectedLayout(GRAPH, true));
		layout.add(new AggregateLayout(AGGR));
		layout.add(new RepaintAction());
		layout.add(new LabelLayout2(EDGE_DECORATORS));
		m_vis.putAction("layout", layout);

		// set up the display
		setSize(400,370);
		pan(250, 250);
		setHighQuality(true);
		addControlListener(new AggregateDragControl());
		addControlListener(new ZoomControl());
		addControlListener(new PanControl());

		//put action to repaint
		ActionList repaint = new ActionList();
		repaint.add(new RepaintAction());
		m_vis.putAction("repaint", repaint);

		// set things running
		m_vis.run("layout");

	}
	
	
	AggregateItem aitem;
	private static int cgid = -1;
	public void addLeaks(Leak leak) {
		m_vis.setInteractive(EDGES, null, false);
		m_vis.setValue(NODES, null, VisualItem.SHAPE, new Integer(Constants.SHAPE_ELLIPSE));

		ArrayList<Node> resultantNodes = KnowledgeGraph.addCallEdge(leak);
		for(Node node:resultantNodes){
			String label = (String)node.get(KnowledgeGraph.LABEL);
			node.setString(VisualItem.LABEL, label);
		}
		Integer elabeli = (Integer)KnowledgeGraph.focusEdge.get(KnowledgeGraph.EDGESIZE);
		String elabel = elabeli.toString();
		KnowledgeGraph.focusEdge.setString(VisualItem.LABEL, elabel);
		
		if(leak.getGroupId() != cgid) {
			aitem = (AggregateItem)at.addItem();
			aitem.setInt("id", leak.getGroupId());
			cgid = leak.getGroupId();
		}
		
		 Iterator nodes = vg.nodes();
		 while(nodes.hasNext()){
			 VisualItem vitem = (VisualItem)nodes.next();
			 for(Node node:resultantNodes){
				 String vitem_l = (String)vitem.get(KnowledgeGraph.LABEL);
				 String node_l = (String)node.get(KnowledgeGraph.LABEL);
				 if(vitem_l !=null && node_l !=null){
					 if(vitem_l.equals(node_l)){
			             aitem.addItem(vitem);
					 } 
				 }
				 else{
					 aitem.addItem(vitem);
				 }
			}			 
		 }
		
		m_vis.run("repaint");
		
		m_vis.run("color");
		m_vis.run("layout");
	}
		
	VisualGraph vg;
	AggregateTable at;
	private void initDataGroups() {

		// add visual data groups
		vg = m_vis.addGraph(GRAPH, KnowledgeGraph.produceNewInstance());
		KnowledgeGraph.getInstance().addColumn(VisualItem.LABEL, String.class);

//		m_vis.setInteractive(EDGES, null, false);
//		m_vis.setValue(NODES, null, VisualItem.SHAPE, new Integer(Constants.SHAPE_ELLIPSE));

		at = m_vis.addAggregates(AGGR);
		at.addColumn(VisualItem.POLYGON, float[].class);
		at.addColumn("id", int.class);
		
		
		

//		// add nodes to aggregates
//		// create an aggregate for each 3-clique of nodes
//		Iterator nodes = vg.nodes();
//		for ( int i=0; i<3; ++i ) {
//			AggregateItem aitem = (AggregateItem)at.addItem();
//			aitem.setInt("id", i);
//			for ( int j=0; j<3; ++j ) {
//				aitem.addItem((VisualItem)nodes.next());
//			}
//		}
	}


}

/**
 * Layout algorithm that computes a convex hull surrounding
 * aggregate items and saves it in the "_polygon" field.
 */
class AggregateLayout extends Layout {

	private int m_margin = 5; // convex hull pixel margin
	private double[] m_pts;   // buffer for computing convex hulls

	public AggregateLayout(String aggrGroup) {
		super(aggrGroup);
	}

	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(double frac) {

		AggregateTable aggr = (AggregateTable)m_vis.getGroup(m_group);
		// do we have any  to process?
		int num = aggr.getTupleCount();
		if ( num == 0 ) return;

		// update buffers
		int maxsz = 0;
		for ( Iterator aggrs = aggr.tuples(); aggrs.hasNext();  )
			maxsz = Math.max(maxsz, 4*2*((AggregateItem)aggrs.next()).getAggregateSize());
		if ( m_pts == null || maxsz > m_pts.length ) {
			m_pts = new double[maxsz];
		}

		// compute and assign convex hull for each aggregate
		Iterator aggrs = m_vis.visibleItems(m_group);
		while ( aggrs.hasNext() ) {
			AggregateItem aitem = (AggregateItem)aggrs.next();

			int idx = 0;
			if ( aitem.getAggregateSize() == 0 ) continue;
			VisualItem item = null;
			Iterator iter = aitem.items();
			while ( iter.hasNext() ) {
				item = (VisualItem)iter.next();
				if ( item.isVisible() ) {
					addPoint(m_pts, idx, item, m_margin);
					idx += 2*4;
				}
			}
			// if no aggregates are visible, do nothing
			if ( idx == 0 ) continue;

			// compute convex hull
			double[] nhull = GraphicsLib.convexHull(m_pts, idx);

			// prepare viz attribute array
			float[]  fhull = (float[])aitem.get(VisualItem.POLYGON);
			if ( fhull == null || fhull.length < nhull.length )
				fhull = new float[nhull.length];
			else if ( fhull.length > nhull.length )
				fhull[nhull.length] = Float.NaN;

			// copy hull values
			for ( int j=0; j<nhull.length; j++ )
				fhull[j] = (float)nhull[j];
			aitem.set(VisualItem.POLYGON, fhull);
			aitem.setValidated(false); // force invalidation
		}
	}

	private static void addPoint(double[] pts, int idx, 
			VisualItem item, int growth)
	{
		Rectangle2D b = item.getBounds();
		double minX = (b.getMinX())-growth, minY = (b.getMinY())-growth;
		double maxX = (b.getMaxX())+growth, maxY = (b.getMaxY())+growth;
		pts[idx]   = minX; pts[idx+1] = minY;
		pts[idx+2] = minX; pts[idx+3] = maxY;
		pts[idx+4] = maxX; pts[idx+5] = minY;
		pts[idx+6] = maxX; pts[idx+7] = maxY;
	}

} // end of class AggregateLayout


/**
 * Interactive drag control that is "aggregate-aware"
 */
class AggregateDragControl extends ControlAdapter {

	private VisualItem activeItem;
	protected Point2D down = new Point2D.Double();
	protected Point2D temp = new Point2D.Double();
	protected boolean dragged;

	/**
	 * Creates a new drag control that issues repaint requests as an item
	 * is dragged.
	 */
	public AggregateDragControl() {
	}

	/**
	 * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemEntered(VisualItem item, MouseEvent e) {
		Display d = (Display)e.getSource();
		d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		activeItem = item;
		if ( !(item instanceof AggregateItem) )
			setFixed(item, true);
	}

	/**
	 * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemExited(VisualItem item, MouseEvent e) {
		if ( activeItem == item ) {
			activeItem = null;
			setFixed(item, false);
		}
		Display d = (Display)e.getSource();
		d.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemPressed(VisualItem item, MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		dragged = false;
		Display d = (Display)e.getComponent();
		d.getAbsoluteCoordinate(e.getPoint(), down);
		if ( item instanceof AggregateItem )
			setFixed(item, true);
	}

	/**
	 * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemReleased(VisualItem item, MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		if ( dragged ) {
			activeItem = null;
			setFixed(item, false);
			dragged = false;
		}            
	}

	/**
	 * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemDragged(VisualItem item, MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		dragged = true;
		Display d = (Display)e.getComponent();
		d.getAbsoluteCoordinate(e.getPoint(), temp);
		double dx = temp.getX()-down.getX();
		double dy = temp.getY()-down.getY();

		move(item, dx, dy);

		down.setLocation(temp);
	}

	protected static void setFixed(VisualItem item, boolean fixed) {
		if ( item instanceof AggregateItem ) {
			Iterator items = ((AggregateItem)item).items();
			while ( items.hasNext() ) {
				setFixed((VisualItem)items.next(), fixed);
			}
		} else {
			item.setFixed(fixed);
		}
	}

	protected static void move(VisualItem item, double dx, double dy) {
		if ( item instanceof AggregateItem ) {
			Iterator items = ((AggregateItem)item).items();
			while ( items.hasNext() ) {
				move((VisualItem)items.next(), dx, dy);
			}
		} else {
			double x = item.getX();
			double y = item.getY();
			item.setStartX(x);  item.setStartY(y);
			item.setX(x+dx);    item.setY(y+dy);
			item.setEndX(x+dx); item.setEndY(y+dy);
		}
	}

} // end of class AggregateDragControl

/**
 * Layout algorithm that computes a convex hull surrounding
 * aggregate items and saves it in the "_polygon" field.
 */
class AggregateLayout2 extends Layout {
    
    private int m_margin = 5; // convex hull pixel margin
    private double[] m_pts;   // buffer for computing convex hulls
    
    public AggregateLayout2(String aggrGroup) {
        super(aggrGroup);
    }
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(double frac) {
        
        AggregateTable aggr = (AggregateTable)m_vis.getGroup(m_group);
        // do we have any  to process?
        int num = aggr.getTupleCount();
        if ( num == 0 ) return;
        
        // update buffers
        int maxsz = 0;
        for ( Iterator aggrs = aggr.tuples(); aggrs.hasNext();  )
            maxsz = Math.max(maxsz, 4*2*
                    ((AggregateItem)aggrs.next()).getAggregateSize());
        if ( m_pts == null || maxsz > m_pts.length ) {
            m_pts = new double[maxsz];
        }
        
        // compute and assign convex hull for each aggregate
        Iterator aggrs = m_vis.visibleItems(m_group);
        while ( aggrs.hasNext() ) {
            AggregateItem aitem = (AggregateItem)aggrs.next();
            int idx = 0;
            if ( aitem.getAggregateSize() == 0 ) continue;
            VisualItem item = null;
            Iterator iter = aitem.items();
            while ( iter.hasNext() ) {
                item = (VisualItem)iter.next();
                if ( item.isVisible() ) {
                    addPoint(m_pts, idx, item, m_margin);
                    idx += 2*4;
                }
            }
            // if no aggregates are visible, do nothing
            if ( idx == 0 ) continue;
            // compute convex hull
            double[] nhull = GraphicsLib.convexHull(m_pts, idx);
            
            // prepare viz attribute array
            float[]  fhull = (float[])aitem.get(VisualItem.POLYGON);
            if ( fhull == null || fhull.length < nhull.length )
                fhull = new float[nhull.length];
            else if ( fhull.length > nhull.length )
                fhull[nhull.length] = Float.NaN;
            
            // copy hull values
            for ( int j=0; j<nhull.length; j++ )
                fhull[j] = (float)nhull[j];
            aitem.set(VisualItem.POLYGON, fhull);
            aitem.setValidated(false); // force invalidation
        }
    }
    
    private static void addPoint(double[] pts, int idx, 
                                 VisualItem item, int growth)
    {
        Rectangle2D b = item.getBounds();
        double minX = (b.getMinX())-growth, minY = (b.getMinY())-growth;
        double maxX = (b.getMaxX())+growth, maxY = (b.getMaxY())+growth;
        pts[idx]   = minX; pts[idx+1] = minY;
        pts[idx+2] = minX; pts[idx+3] = maxY;
        pts[idx+4] = maxX; pts[idx+5] = minY;
        pts[idx+6] = maxX; pts[idx+7] = maxY;
    }
    
} // end of class AggregateLayout2
/**
 * Interactive drag control that is "aggregate-aware"
 */
class AggregateDragControl3 extends ControlAdapter {
    private VisualItem activeItem;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged;
    /**
     * Creates a new drag control that issues repaint requests as an item
     * is dragged.
     */
    public AggregateDragControl3() {
    }
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( !(item instanceof AggregateItem) )
            setFixed(item, true);
    }
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            setFixed(item, false);
        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
    }
    /**
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = false;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), down);
        if ( item instanceof AggregateItem )
            setFixed(item, true);
    }
    /**
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( dragged ) {
            activeItem = null;
            setFixed(item, false);
            dragged = false;
        }            
    }
    /**
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = true;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), temp);
        double dx = temp.getX()-down.getX();
        double dy = temp.getY()-down.getY();
        move(item, dx, dy);
        down.setLocation(temp);
    }
    protected static void setFixed(VisualItem item, boolean fixed) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                setFixed((VisualItem)items.next(), fixed);
            }
        } else {
            item.setFixed(fixed);
        }
    }
    protected static void move(VisualItem item, double dx, double dy) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                move((VisualItem)items.next(), dx, dy);
            }
        } else {
            double x = item.getX();
            double y = item.getY();
            item.setStartX(x);  item.setStartY(y);
            item.setX(x+dx);    item.setY(y+dy);
            item.setEndX(x+dx); item.setEndY(y+dy);
        }
    }
} // end of class AggregateDragControl

/**
 * Interactive drag control that is "aggregate-aware"
 */
class AggregateDragControl2 extends ControlAdapter {
    private VisualItem activeItem;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged;
    
    /**
     * Creates a new drag control that issues repaint requests as an item
     * is dragged.
     */
    public AggregateDragControl2() {
    }
        
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( !(item instanceof AggregateItem) )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            setFixed(item, false);
        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
    }
    
    /**
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = false;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), down);
        if ( item instanceof AggregateItem )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( dragged ) {
            activeItem = null;
            setFixed(item, false);
            dragged = false;
        }            
    }
    
    /**
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = true;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), temp);
        double dx = temp.getX()-down.getX();
        double dy = temp.getY()-down.getY();
        
        move(item, dx, dy);
        
        down.setLocation(temp);
    }
    protected static void setFixed(VisualItem item, boolean fixed) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                setFixed((VisualItem)items.next(), fixed);
            }
        } else {
            item.setFixed(fixed);
        }
    }
    
    protected static void move(VisualItem item, double dx, double dy) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                move((VisualItem)items.next(), dx, dy);
            }
        } else {
            double x = item.getX();
            double y = item.getY();
            item.setStartX(x);  item.setStartY(y);
            item.setX(x+dx);    item.setY(y+dy);
            item.setEndX(x+dx); item.setEndY(y+dy);
        }
    }
    
} // end of class AggregateDragControl2
/**
 * Set label positions. Labels are assumed to be DecoratorItem instances,
 * decorating their respective nodes. The layout simply gets the bounds
 * of the decorated node and assigns the label coordinates to the center
 * of those bounds.
 */
class LabelLayout2 extends Layout {
    public LabelLayout2(String group) {
        super(group);
    }
    public void run(double frac) {
        Iterator iter = m_vis.items(m_group);
        while ( iter.hasNext() ) {
            DecoratorItem decorator = (DecoratorItem)iter.next();
            VisualItem decoratedItem = decorator.getDecoratedItem();
            Rectangle2D bounds = decoratedItem.getBounds();
            double x = bounds.getCenterX();
            double y = bounds.getCenterY();
            /* modification to move edge labels more to the arrow head
            double x2 = 0, y2 = 0;
            if (decoratedItem instanceof EdgeItem){
            	VisualItem dest = ((EdgeItem)decoratedItem).getTargetItem(); 
            	x2 = dest.getX();
            	y2 = dest.getY();
            	x = (x + x2) / 2;
            	y = (y + y2) / 2;
            }
            */
            setX(decorator, null, x);
            setY(decorator, null, y);
        }
    }
} // end of inner class LabelLayout
