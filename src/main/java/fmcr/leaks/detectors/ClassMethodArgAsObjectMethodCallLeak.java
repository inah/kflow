package fmcr.leaks.detectors;

import java.util.ArrayList;

import fmcr.main.Client;

/**
 * This is
 * @author inah
 *
 */
public class ClassMethodArgAsObjectMethodCallLeak extends MethodLeak{
	private String className;
	private String parameterType;
	private String handlerType;
	private String methodName;
	private String methodReturnType;
	
	public ClassMethodArgAsObjectMethodCallLeak(String nodeId, String className, String handlerType, String methodName, String methodReturnType, String leakLine, String parameterType) {
		this.className = className;
		this.setNodeId(nodeId);
		this.handlerType = handlerType;
		this.methodName = methodName;
		this.setMethodReturnType(methodReturnType);
		this.setLeakLine(leakLine);
		this.setParameterType(parameterType);
		
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
		if(methodReturnType == null || isPrimitiveLeak()) {
			return methodName;
		}
		return methodReturnType;
	}

	public void setMethodReturnType(String methodReturnType) {
		this.methodReturnType = methodReturnType;
	}
		
	@Override
	public String toString() {
		return "K<sub>"+className+"</sub>K<sub>"+parameterType+"</sub>K<sub>"+handlerType+"</sub>([F]"+methodName +":"+methodReturnType+") "+getLeakLine();
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
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
			String s1 = "<html>K<sub>"+className+"</sub>K<sub>"+parameterType+"</sub>K<sub>"+handlerType+"</sub>("+getMethodReturnType()+")</html> ";;
			desc.add(s1);
		}
		String s2 = "<html>K<sub>"+className+"</sub>K<sub>"+parameterType+"</sub>K<sub>"+handlerType+"</sub>("+methodName+")</html> ";
		desc.add(s2);
		//return "<html>K<sub>"+className+"</sub>K<sub>"+parameterType+"</sub>K<sub>"+handlerType+"</sub>("+methodName +":"+methodReturnType+")</html> ";
		
		return desc;
	}
	
	
}
