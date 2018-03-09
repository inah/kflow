package fmcr.leaks.detectors;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class ClassVariableAccessLeak extends Leak{
	private String accessVariableName;
	private String accessVariableType;
	private String className;
	
	public ClassVariableAccessLeak(String nodeId, String accessVariableName, String accessVariableType, String className, String leakLine) {
		this.setNodeId(nodeId);
		this.accessVariableName = accessVariableName;
		this.accessVariableType = accessVariableType;
		this.className = className;
		this.setLeakLine(leakLine);
		
		tag = Tag.A;
		this.setGroupId(Client.getDisplay().filecounter);
	}

	
	public String getAccessVariableName() {
		return accessVariableName;
	}


	public void setAccessVariableName(String accessVariableName) {
		this.accessVariableName = accessVariableName;
	}


	public String getAccessVariableType() {
		if(accessVariableType == null || isPrimitiveLeak()) {
			return accessVariableName;
		}
		return accessVariableType;
	}


	public void setAccessVariableType(String accessVariableType) {
		this.accessVariableType = accessVariableType;
	}


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		return "K<sub>"+className+"</sub>([A]"+accessVariableType+") "+getLeakLine();
	}


	@Override
	public String describe() {
		return "<html>K<sub>"+className+"</sub>("+getAccessVariableType()+")</html>";
	}

}
