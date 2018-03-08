package prol;

public class PayInfo {
	double salary;
	WorkInfo w;
	public PayInfo(double sal) {
		this.salary = sal;
	}
	
	public PayInfo(WorkInfo w) {
		this.w = w;
	}
	
	public double computeSalary(double hrsworked) {
		return salary*hrsworked;
	}
	public PayInfo() {
	}
	
}
