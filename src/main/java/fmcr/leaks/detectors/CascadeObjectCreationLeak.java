package fmcr.leaks.detectors;

import fmcr.main.Client;

public class CascadeObjectCreationLeak extends Leak{

	private String containedObjectName;
	private String containedObjectType;
	private String containingObjectName;
	private String containingObjectType;
	private CascadeObjectCreationLeak cascadeLeak;

	public CascadeObjectCreationLeak(String nodeId, String containedObjectName, String containedObjectType, String containingObjectName, String containingObjectType, CascadeObjectCreationLeak cascadeLeak, String leakLine) {
		this.setNodeId(nodeId);
		this.setContainedObjectName(containedObjectName);
		this.containedObjectType = containedObjectType;
		this.setContainingObjectName(containingObjectName);
		this.containingObjectType = containingObjectType;
		this.cascadeLeak = cascadeLeak;
		this.setLeakLine(leakLine);

		tag = Tag.A;
		this.setGroupId(Client.getDisplay().filecounter);
	}

	@Override
	public String describe() {
		if(cascadeLeak == null) {
			return "K<sub>"+containingObjectType+"</sub>("+getContainedObjectType()+")";
		}
		else {
			return "K<sub>"+containingObjectType+"</sub>"+cascadeLeak.describe();
		}
	}
	
	public CascadeObjectCreationLeak getCascadeObjectCreationLeak() {
		return cascadeLeak;
	}

	public String getContainingObjectName() {
		return containingObjectName;
	}

	public void setContainingObjectName(String containingObjectName) {
		this.containingObjectName = containingObjectName;
	}

	public String getContainedObjectName() {
		return containedObjectName;
	}

	public void setContainedObjectName(String containedObjectName) {
		this.containedObjectName = containedObjectName;
	}
	
	public String getContainingObjectType() {
		return containingObjectType;
	}
	
	public String getContainedObjectType() {
		if(containedObjectType == null) {
			return containedObjectName;
		}
		return containedObjectType;
	}
}

