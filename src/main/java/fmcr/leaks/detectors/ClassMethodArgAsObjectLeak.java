package fmcr.leaks.detectors;

/**
 * This is
 * @author inah
 *
 */
public class ClassMethodArgAsObjectLeak extends Leak{
	private String className;
	private String parameterType;
	private String prameterName;
	private String handlerType;
	
	public ClassMethodArgAsObjectLeak(String nodeId, String className, String handlerType, String parameterType, String parameterName, String leakLine) {
		this.className = className;
		this.setNodeId(nodeId);
		this.handlerType = handlerType;
		this.setLeakLine(leakLine);
		this.setParameterType(parameterType);
		this.setPrameterName(parameterName);
		
		tag = Tag.A;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	@Override
	public String toString() {
		return "K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>("+prameterName+":"+parameterType+") "+getLeakLine();
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String describe() {
		return "<html>K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>("+parameterType+")</html>";
//		return "<html>K<sub>"+className+"</sub>K<sub>"+handlerType+"</sub>("+prameterName+":"+parameterType+")</html>";
	}
	
	
}
