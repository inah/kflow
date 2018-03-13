package fmcr.leaks.detectors;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class ClassVariableLeak extends Leak{
	private String variableName;
	private String variableType;
	private String className;
	
	public ClassVariableLeak(String nodeId, String variableName, String variableType, String className, String leakLine) {
		this.setNodeId(nodeId);
		this.variableName = variableName;
		this.variableType = variableType;
		this.className = className;
		this.setLeakLine(leakLine);
		
		tag = Tag.A;
		this.setGroupId(Client.getDisplay().filecounter);
	}

	
	public String getVariableName() {
		return variableName;
	}


	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}


	public String getVariableType() {
		if(variableType == null || isPrimitiveLeak()) {
			return variableName;
		}
		return variableType;
	}


	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		return "K<sub>"+className+"</sub>([A]"+variableType+") "+getLeakLine();
	}


	@Override
	public String describe() {
		return "<html>K<sub>"+className+"</sub>("+getVariableType()+")</html>";
	}

}
