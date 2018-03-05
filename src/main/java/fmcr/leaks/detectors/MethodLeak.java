package fmcr.leaks.detectors;

import java.util.ArrayList;

public  abstract class MethodLeak extends Leak{

	@Override
	public String describe() {
		return "invalid describe for methods leaks";
	}
	
	public abstract ArrayList<String> multiDescribe();

}
