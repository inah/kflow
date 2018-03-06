package fmcr.leaks.detectors;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class CascadeObjectCreation extends Leak{
	private String containedObjectName;
	private String containedObjectType;
	private String containingObjectName;
	private String containingObjectType;
	
	public CascadeObjectCreation(String nodeId, String nodeParentId, String containedObjectName, String containedObjectType, String containingObjectName, String containingObjectType, String leakLine) {
		this.setNodeId(nodeId);
		this.setNodeParentId(nodeParentId);
		this.containedObjectName = containedObjectName;
		this.containedObjectType = containedObjectType;
		this.containingObjectName = containingObjectName;
		this.containingObjectType = containingObjectType;
		this.setLeakLine(leakLine);
		
		tag = Tag.A;
		this.setGroupId(Client.getDisplay().filecounter);
	}

	
	public CascadeObjectCreationLeak transform() {
//		if(!containedObjectType.equals(containingObjectType)) {
//			return new CascadeObjectCreationLeak( this.getNodeId(),  containedObjectName,  containedObjectType,  containingObjectName,  containingObjectType,null, this.getLeakLine());
//		}
//		else {
//			return null;
//		}
		return new CascadeObjectCreationLeak( this.getNodeId(),  containedObjectName,  containedObjectType,  containingObjectName,  containingObjectType,null, this.getLeakLine());

	}
	public String getContainedbjectName() {
		return containedObjectName;
	}


	public void setContainedObjectName(String containedObjectName) {
		this.containedObjectName = containedObjectName;
	}


	public String getContainedObjectType() {
		return containedObjectType;
	}


	public void setContainedObjectType(String containedObjectType) {
		this.containedObjectType = containedObjectType;
	}


	public String getContainingObjectName() {
		return containingObjectName;
	}

	public void setContainingObjectName(String containingObjectName) {
		this.containingObjectName = containingObjectName;
	}
	@Override
	public String toString() {
		return "K<sub>"+containingObjectType+"</sub>([A]"+containedObjectType+") "+getLeakLine();
	}


	@Override
	public String describe() {
		return "<html>K<sub>"+containingObjectType+"</sub>("+containedObjectType+")</html>";
	}


	public String getContainingObjectType() {
		return containingObjectType;
	}


	public void setContainingObjectType(String containingObjectType) {
		this.containingObjectType = containingObjectType;
	}

}
