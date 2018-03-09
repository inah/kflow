package prol;

public class PaySlip {
	
	public static void main(String ards[]) {
//		PayInfo P6 = new PayInfo(new Double(1.99));
//		PayInfo p1 = new PayInfo(new WorkInfo(10));
//		WorkInfo w = new WorkInfo(new  Double(5));
//		PayInfo p2 = new PayInfo(w);
//		PayInfo p3 = new PayInfo(new WorkInfo(p));
//		PayInfo p4 = new PayInfo(w.getHrsworked());
//		PayInfo p5 = new PayInfo(WorkInfo.hrsworked);
		PayInfo p = new PayInfo(WorkInfo.getHrsworked());
//		double salary = p.computeSalary(new WorkInfo(5).getHrsworked());
		
		double salary1 = p.computeSalary(WorkInfo.hrsworked);
		
		//DONE: double salary = p.computeSalary(w);
		//DONE: double salary = p.computeSalary(w.getHrsworked());
		//DONE: double salary = p.computeSalary(w.hrsworked);
		//DONE: double salary = p.computeSalary(WorkInfo.hrsworked);
		//DONE: double salary = p.computeSalary(WorkInfo.getHrsworked());
		//DONE: PayInfo p = new PayInfo();
		//DONE: PayInfo p = new PayInfo(new WorkInfo());
		//DONE: PayInfo p = new PayInfo(new WorkInfo(new PayInfo(X)));
		//DONE: PayInfo p = new PayInfo(new WorkInfo(xx));
		//DONE: PayInfo p2 = new PayInfo(new WorkInfo(p));
		//DONE: PayInfo p = new PayInfo(w);
		//DONE: PayInfo p = new PayInfo(w.getHrsworked());
		//DONE: PayInfo p = new PayInfo(WorkInfo.hrsworked);
		//DONE: PayInfo p = new PayInfo(WorkInfo.getHrsworked());
		//DONE: double salary = p.computeSalary(new xx..);
		//TODO: REPLACE THE NULL IN TO STRING BY SEARCHING TYPE FROM LEAKS LIB
	}
	
}
