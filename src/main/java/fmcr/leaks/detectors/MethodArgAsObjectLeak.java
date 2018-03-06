package fmcr.leaks.detectors;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class MethodArgAsObjectLeak extends Leak{
	private String parameterType;
	private String prameterName;
	private String handlerType;
	
	public MethodArgAsObjectLeak(String nodeId, String handlerType, String parameterType, String parameterName, String leakLine) {
		this.setNodeId(nodeId);
		this.handlerType = handlerType;
		this.setLeakLine(leakLine);
		this.setParameterType(parameterType);
		this.setPrameterName(parameterName);
		
		tag = Tag.A;
		this.setGroupId(Client.getDisplay().filecounter);
	}

	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	@Override
	public String toString() {
		return "K<sub>"+handlerType+"</sub>("+prameterName+":"+parameterType+") "+getLeakLine();
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getPrameterName() {
		return prameterName;
	}

	public void setPrameterName(String prameterName) {
		this.prameterName = prameterName;
	}

	@Override
	public String describe() {
		return "<html>K<sub>"+handlerType+"</sub>("+parameterType+")</html>";
//		return "<html>K<sub>"+handlerType+"</sub>("+prameterName+":"+parameterType+")</html>";
	}
	
	
}
