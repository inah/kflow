package oose.vcs;

public class CFG {

	boolean accelerating;
	boolean breaking;
	double dts; //distanceToStopping
	double dfo; //distanceFromObject
	boolean alarmon;
	boolean airbagactive;
	
	public void collisionDetector() {
		dts = computeDTS();
		dfo = computeDFP();
		breaking = false;
		airbagactive = false;
		alarmon = false;
		
		while(accelerating) {
			dts = computeDTS();
			dfo = computeDFP();
			
			if(dts < 10) {
				alarmon = true;
			}
			
			if(dts == dfo) {
				airbagactive = true;
			}
			else {
				airbagactive =false;
			}			
		}
		
		printstatus();
		
	}

	private void printstatus() {
		
	}

	private double computeDFP() {
		return 0;
	}

	private double computeDTS() {
		return 0;
	}
}
