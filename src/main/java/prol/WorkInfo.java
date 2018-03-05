package prol;

public class WorkInfo {
	public static double hrsworked;
	public WorkInfo(double hrsworked) {
		this.hrsworked = hrsworked;
	}
	public WorkInfo(PayInfo pinf) {
		//do nothnig
	}
//	public double getHrsworked() {
//		return hrsworked;
//	}
	public static double getHrsworked() {
		return hrsworked;
	}
	public void setHrsworked(double hrsworked) {
		this.hrsworked = hrsworked;
	}
}
