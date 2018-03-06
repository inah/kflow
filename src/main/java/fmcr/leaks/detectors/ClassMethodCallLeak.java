package fmcr.leaks.detectors;

import java.util.ArrayList;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class ClassMethodCallLeak extends MethodLeak{
	private String handlerType;
	private String methodName;
	private String methodReturnType;
	private String className;
	
	public ClassMethodCallLeak(String nodeId, String className, String handlerType, String methodName, String methodReturnType, String leakLine) {
		this.setNodeId(nodeId);
		this.className = className;
		this.handlerType = handlerType;
		this.methodName = methodName;
		this.setMethodReturnType(methodReturnType);
		this.setLeakLine(leakLine);
		
		tag = Tag.F;
		this.setGroupId(Client.getDisplay().filecounter);
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
		return "K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>([F]"+methodName+":"+methodReturnType+") "+getLeakLine();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public ArrayList<String> multiDescribe() {
		ArrayList<String> desc = new ArrayList<String>();
		if(!methodReturnType.equalsIgnoreCase("void")) {
			String s1 = "<html>K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>("+methodReturnType+")</html>";
			desc.add(s1);
		}
		String s2 = "<html>K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>("+methodName+")</html>";
		desc.add(s2);
		//return "<html>K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>("+methodName+":"+methodReturnType+")</html>";
		return desc;
	}

}
