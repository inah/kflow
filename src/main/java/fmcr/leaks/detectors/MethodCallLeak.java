package fmcr.leaks.detectors;

import java.util.ArrayList;

/**
 * This is
 * @author inah
 *
 */
public class MethodCallLeak extends MethodLeak{
	private String handlerType;
	private String methodName;
	private String methodReturnType;
	
	public MethodCallLeak(String nodeId, String handlerType, String methodName, String methodReturnType, String leakLine) {
		this.setNodeId(nodeId);
		this.handlerType = handlerType;
		this.methodName = methodName;
		this.setMethodReturnType(methodReturnType);
		this.setLeakLine(leakLine);
		
		tag = Tag.F;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodReturnType() {
		return methodReturnType;
	}

	public void setMethodReturnType(String methodReturnType) {
		this.methodReturnType = methodReturnType;
	}
	
	@Override
	public String toString() {
		return "K<sub>"+handlerType+"</sub>([F]"+methodName+":"+methodReturnType+") "+getLeakLine();
	}

	@Override
	public ArrayList<String> multiDescribe() {
		ArrayList<String> desc = new ArrayList<String>();
		if(!methodReturnType.equalsIgnoreCase("void")) {
			String s1 = "<html>K<sub>"+handlerType+"</sub>("+methodReturnType+")</html>";
			desc.add(s1);
		}
		String s2 = "<html>K<sub>"+handlerType+"</sub>("+methodName+")</html>";
		desc.add(s2);
		
		return desc;
	}

}
