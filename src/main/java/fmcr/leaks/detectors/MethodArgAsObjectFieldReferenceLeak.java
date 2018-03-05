package fmcr.leaks.detectors;

/**
 * This is
 * @author inah
 *
 */
public class MethodArgAsObjectFieldReferenceLeak extends Leak{
	private String handlerType; //method handler
	private String methodName;
	private String methodReturnType;
	private String fieldHandlerName;
	private String fieldHandlerType;
	private String fieldType;
	private String fieldName;
	
	public MethodArgAsObjectFieldReferenceLeak(String nodeId, String handlerType, String methodName, 
												String methodReturnType, String fieldHandlerName, String fieldHandlerType, 
												String fieldType, String fieldName,String leakLine) {
		this.setNodeId(nodeId);
		this.handlerType = handlerType;
		this.methodName = methodName;
		this.setMethodReturnType(methodReturnType);
		this.fieldHandlerName = fieldHandlerName;
		this.fieldHandlerType = fieldHandlerType;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.setLeakLine(leakLine);
		
		tag = Tag.A;
	}

	public String getFieldHandlerName() {
		return fieldHandlerName;
	}

	public void setFieldHandlerName(String fieldHandlerName) {
		this.fieldHandlerName = fieldHandlerName;
	}

	public String getFieldHandlerType() {
		return fieldHandlerType;
	}

	public void setFieldHandlerType(String fieldHandlerType) {
		this.fieldHandlerType = fieldHandlerType;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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
		return "K<sub>"+handlerType+"</sub>K<sub>"+fieldHandlerType+"</sub>([A]"+fieldName +":"+fieldType+") "+getLeakLine();
	}

	@Override
	public String describe() {
		return "<html>K<sub>"+handlerType+"</sub>K<sub>"+fieldHandlerType+"</sub>("+fieldType+")</html>";
//		return "<html>K<sub>"+handlerType+"</sub>K<sub>"+fieldHandlerType+"</sub>("+fieldName +":"+fieldType+")</html>";
	}

	
	
}
