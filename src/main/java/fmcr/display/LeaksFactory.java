package fmcr.display;

import java.awt.Rectangle;
import java.util.ArrayList;

import fmcr.leaks.detectors.CascadeObjectCreationLeak;
import fmcr.leaks.detectors.Leak;
import fmcr.leaks.detectors.MethodLeak;
import fmcr.leaks.detectors.SensitivityLevel;
import fmcr.main.Client;
import fmcr.visitors.CodeVisitor;

public class LeaksFactory {

	LeaksView ckv;
	
	public void displayReports(final LeaksView ckv){
		this.ckv = ckv;
	}
	
	public void updateLeaksTable() {
		Client.getDisplay().jTabbedPane1.setSelectedIndex(1);

		for(Leak leak:CodeVisitor.leaks) {
			String fileName = "";
			if(Client.getSelectedSourceFile() !=null) {
				fileName = Client.getSelectedSourceFile().getName();
			}
			
			
			if(leak instanceof MethodLeak) {
				ArrayList<String> descs = ((MethodLeak) leak).multiDescribe();
				for(String s:descs) {
					Client.totalLeaks = Client.totalLeaks +1;
					if(leak.getSensitivityLevel() == SensitivityLevel.L1) {
						Client.l1Leaks = Client.l1Leaks +1;
					}
					else if(leak.getSensitivityLevel() == SensitivityLevel.L2) {
						Client.l2Leaks = Client.l2Leaks +1;
					}
					else if(leak.getSensitivityLevel() == SensitivityLevel.L3) {
						Client.l3Leaks = Client.l3Leaks +1;
					}
					else if(leak.getSensitivityLevel() == SensitivityLevel.L4) {
						Client.l4Leaks = Client.l4Leaks +1;
					}
					
					ckv.model.addRow(new Object[]{Client.totalLeaks, s,
							 leak.getClass().getSimpleName(),leak.getLeakLine(), 
							 leak.tag, leak.getSensitivityLevel(), leak.getCodeSource(), fileName});
					
					ckv.model.fireTableDataChanged();
					Rectangle cellBounds1 = ckv.table.getCellRect(ckv.table.getRowCount() - 1, 0, true);
					ckv.table.scrollRectToVisible(cellBounds1);
					
					ckv.repaint();
					ckv.updateUI();
				}
					
			}
			else {
				Client.totalLeaks = Client.totalLeaks +1;
				if(leak.getSensitivityLevel() == SensitivityLevel.L1) {
					Client.l1Leaks = Client.l1Leaks +1;
				}
				else if(leak.getSensitivityLevel() == SensitivityLevel.L2) {
					Client.l2Leaks = Client.l2Leaks +1;
				}
				else if(leak.getSensitivityLevel() == SensitivityLevel.L3) {
					Client.l3Leaks = Client.l3Leaks +1;
				}
				else if(leak.getSensitivityLevel() == SensitivityLevel.L4) {
					Client.l4Leaks = Client.l4Leaks +1;
				}				
				
				if(leak instanceof CascadeObjectCreationLeak) {
					String desc = leak.describe();
					desc = "<html>"+desc+"</html>";
					ckv.model.addRow(new Object[]{Client.totalLeaks, desc,
							 leak.getClass().getSimpleName(),leak.getLeakLine(), 
							 leak.tag, leak.getSensitivityLevel(), leak.getCodeSource(), fileName});
				}
				else {
					ckv.model.addRow(new Object[]{Client.totalLeaks, leak.describe(),
							 leak.getClass().getSimpleName(),leak.getLeakLine(), 
							 leak.tag, leak.getSensitivityLevel(), leak.getCodeSource(), fileName});
				}
				
				
				ckv.model.fireTableDataChanged();
				Rectangle cellBounds1 = ckv.table.getCellRect(ckv.table.getRowCount() - 1, 0, true);
				ckv.table.scrollRectToVisible(cellBounds1);
				
				ckv.repaint();
				ckv.updateUI();
			}
			
		}
		
	}
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
}
