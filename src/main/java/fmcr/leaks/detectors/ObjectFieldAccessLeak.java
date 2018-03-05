package fmcr.leaks.detectors;

/**
 * This is
 * @author inah
 *
 */
public class ObjectFieldAccessLeak extends Leak{
	private String fieldHandlerName;
	private String fieldHandlerType;
	private String fieldType;
	private String fieldName;
	
	public ObjectFieldAccessLeak(String nodeId, String fieldHandlerName, String fieldHandlerType, String fieldName, String fieldType, String leakLine) {
		this.setNodeId(nodeId);
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
		return "K<sub>"+fieldHandlerType+"</sub>([A]"+fieldName+":"+fieldType+") "+getLeakLine();
	}


	@Override
	public String describe() {
		return "<html>K<sub>"+fieldHandlerType+"</sub>("+fieldType+")</html>";
//		return "<html>K<sub>"+fieldHandlerType+"</sub>("+fieldName+":"+fieldType+")</html>";
	}

}
