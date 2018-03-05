package fmcr.leaks.detectors;

public abstract class Leak {
	private String leakLine;
	private String nodeId;
	private String nodeParentId;
	private SensitivityLevel sensitivityLevel = SensitivityLevel.L4;
	
	public Tag tag = Tag.EMPTY;
	private String codeSource = " ";
	
	public String getLeakLine() {
		return leakLine;
	}

	public void setLeakLine(String leakLine) {
		this.leakLine = leakLine;
	}	

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public SensitivityLevel getSensitivityLevel() {
		return sensitivityLevel;
	}

	public void setSensitivityLevel(SensitivityLevel sensitivityLevel) {
		this.sensitivityLevel = sensitivityLevel;
	}
	public abstract String describe();

	public String getCodeSource() {
		return codeSource;
	}

	public void setCodeSource(String codeSource) {
		this.codeSource = codeSource;
	}

	public String getNodeParentId() {
		return nodeParentId;
	}

	public void setNodeParentId(String nodeParentId) {
		this.nodeParentId = nodeParentId;
	}
	
}
