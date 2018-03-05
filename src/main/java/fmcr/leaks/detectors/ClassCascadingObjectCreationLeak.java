package fmcr.leaks.detectors;

import java.util.ArrayList;
import java.util.LinkedList;

import fmcr.visitors.CodeVisitor;

public class ClassCascadingObjectCreationLeak extends Leak{
	private String containingClassName;
	private LinkedList<CascadeObjectCreation> cascades;
	
	public ClassCascadingObjectCreationLeak(String nodeId, String containingClassName, String leakLine) {
		this.setNodeId(nodeId);
		this.setContainingClassName(containingClassName);
		this.setLeakLine(leakLine);
		
		cascades = new LinkedList<CascadeObjectCreation>();
		
		tag = Tag.A;
	}
	
	
	
	public void addObjectCreation(CascadeObjectCreation cascade) {
		boolean contained = false;
		for(CascadeObjectCreation c: cascades) {
			if(c.describe().equals(cascade.describe())) {
				contained = true;
				break;
			}
		}
		if(!contained) {
			cascades.add(cascade);
		}
	}
	
	public LinkedList<CascadeObjectCreation> getCascades(){
		return cascades;
	}

	@Override
	public String describe() {
		return null;
	}

	public String getContainingClassName() {
		return containingClassName;
	}

	public void setContainingClassName(String containingClassName) {
		this.containingClassName = containingClassName;
	}
	
	public ArrayList<CascadeObjectCreationLeak> extractLeaks(){
		ArrayList<CascadeObjectCreationLeak> leaks = new ArrayList<CascadeObjectCreationLeak>();
		CascadeObjectCreationLeak lastLeak = null;
//		for(int i=cascades.size()-1;i>=0; i--) {
		for(int i=0; i <cascades.size(); i++) {
			CascadeObjectCreation cascade = cascades.get(i);
			if(lastLeak == null) {
				CascadeObjectCreationLeak leak = cascade.transform();
				if(leak !=null) {
					leaks.add(leak);
					lastLeak = leak;
					
					CascadeObjectCreationLeak classleak = new CascadeObjectCreationLeak( this.getNodeId(),  null,  null,  null, CodeVisitor.className ,leak, this.getLeakLine());
					leaks.add(classleak);
				}
				
			}
			else {
				CascadeObjectCreationLeak leakx = cascade.transform();
				if(leakx !=null) {
					leaks.add(leakx);
				}
				CascadeObjectCreationLeak leak = new CascadeObjectCreationLeak( this.getNodeId(),  null,  null,  null,  cascade.getContainingObjectType(),lastLeak, this.getLeakLine());
				leaks.add(leak);
				lastLeak = leak;

				CascadeObjectCreationLeak classleak = new CascadeObjectCreationLeak( this.getNodeId(),  null,  null,  null, CodeVisitor.className ,lastLeak, this.getLeakLine());
				leaks.add(classleak);
				
			}
		}
		return leaks;
	}

}
