package fmcr.leaks.detectors;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class ClassObjectCreationLeak extends Leak{
	private String createdObjectName;
	private String createdObjectType;
	private String className;
	
	public ClassObjectCreationLeak(String nodeId, String createdObjectName, String createdObjectType, String className, String leakLine) {
		this.setNodeId(nodeId);
		this.createdObjectName = createdObjectName;
		this.createdObjectType = createdObjectType;
		this.className = className;
		this.setLeakLine(leakLine);
		
		tag = Tag.A;
		this.setGroupId(Client.getDisplay().filecounter);
	}

	
	public String getCreatedObjectName() {
		return createdObjectName;
	}


	public void setCreatedObjectName(String createdObjectName) {
		this.createdObjectName = createdObjectName;
	}


	public String getCreatedObjectType() {
		return createdObjectType;
	}


	public void setCreatedObjectType(String createdObjectType) {
		this.createdObjectType = createdObjectType;
	}


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		return "K<sub>"+className+"</sub>([A]"+createdObjectType+") "+getLeakLine();
	}


	@Override
	public String describe() {
		return "<html>K<sub>"+className+"</sub>("+createdObjectType+")</html>";
	}

}
