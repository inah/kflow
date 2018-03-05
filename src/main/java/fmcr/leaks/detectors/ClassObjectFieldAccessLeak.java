package fmcr.leaks.detectors;

/**
 * This is
 * @author inah
 *
 */
public class ClassObjectFieldAccessLeak extends Leak{
	private String className;
	private String fieldHandlerName;
	private String fieldHandlerType;
	private String fieldType;
	private String fieldName;
	
	public ClassObjectFieldAccessLeak(String nodeId, String className, String fieldHandlerName, String fieldHandlerType, String fieldName, String fieldType, String leakLine) {
		this.setNodeId(nodeId);
		this.className = className;
		this.fieldHandlerName = fieldHandlerName;
		this.fieldHandlerType = fieldHandlerType;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.setLeakLine(leakLine);
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


	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	@Override
	public String toString() {
		return "K<sub>"+className+"</sub>K<sub>"+fieldHandlerType+"</sub>([A]"+fieldName+":"+fieldType+") "+getLeakLine();
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	@Override
	public String describe() {
		return "<html>K<sub>"+className+"</sub>K<sub>"+fieldHandlerType+"</sub>("+fieldType+")</html>";
//		return "<html>K<sub>"+className+"</sub>K<sub>"+fieldHandlerType+"</sub>("+fieldName+":"+fieldType+")</html>";
	}

}
