package fmcr.display;

import java.util.ArrayList;
import java.util.List;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;

/**
 * A foldable directed acyclic graph (DAG) where each child has only one parent.
 * AKA a Tree.
 * 
 * @author Inah Omoronyia
 *
 */
public class FoldableTree extends mxGraph {
	
	@Override
	public boolean isCellFoldable(Object cell, boolean collapse) {
		boolean result = super.isCellFoldable(cell, collapse);
		if (!result) {
			return this.getOutgoingEdges(cell).length > 0;
		}
		return result;
	}

	@Override
	public Object[] foldCells(boolean collapse, boolean recurse, Object[] cells, boolean checkFoldable) {
		// super.foldCells does this so I will too...
		if (cells == null) {
			cells = getFoldableCells(getSelectionCells(), collapse);
		}

		this.getModel().beginUpdate();

		try {
			toggleSubtree(this, cells[0], !collapse);
			this.model.setCollapsed(cells[0], collapse);
			fireEvent(new mxEventObject(mxEvent.FOLD_CELLS, "cells", cells, "collapse", collapse, "recurse", recurse));
		} finally {
			this.getModel().endUpdate();
		}

		return cells;
	}

	private void toggleSubtree(mxGraph graph, Object cellSelected, boolean show) {
		List<Object> cellsAffected = new ArrayList<>();
		graph.traverse(cellSelected, true, new mxICellVisitor() {
			@Override
			public boolean visit(Object vertex, Object edge) {
				if (vertex != cellSelected) {
					cellsAffected.add(vertex);
				}
				return vertex == cellSelected || !graph.isCellCollapsed(vertex);
			}
		});

		graph.toggleCells(show, cellsAffected.toArray(), true/* includeEdges */);
	}
}
