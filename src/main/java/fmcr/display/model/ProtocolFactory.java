package fmcr.display.model;

import java.awt.Rectangle;
import java.text.DecimalFormat;

import fmcr.display.util.ArrayCleaner;
import fmcr.main.Client;

public class ProtocolFactory {
	
	public static void displayProtocols(final ProtocolView pv){
//		PSatClient.netDeseraliseConfigInstance();
//		if(Display.instance.protocolSuite == null){
//			PSatClient.netInitProtocolSuite();
//			
//			PSatClient.netDeseraliseConfigInstance();
//		}
		Thread queryThread2 = new Thread() {
			public void run() {				
//				int k=1;
				for(String p:Client.protocolSuite){
					boolean checked = false;
					for(String ev:Client.evaluatedProtocols){
						String s1 ="";
						if(ev != null){
							s1 = ev.split(" \\(")[1];
						}
						String s2 = p.split(" \\(")[1];
						if(s1.equals(s2)){
							checked = true;
							break;
						}
					}

					DecimalFormat df = new DecimalFormat("####0.00");
					Double cost = new Double(df.format(ProtocolView.getCost(p)));
					pv.model.addRow(new Object[]{checked,cost,p});
					pv.model.fireTableDataChanged();
					Rectangle cellBounds = pv.table.getCellRect(pv.table.getRowCount() - 1, 0, true);
					pv.table.scrollRectToVisible(cellBounds);
					
//					k = k+1;
				}				
			}
		};
		queryThread2.start();
	}
	

	public static boolean addToEvaluatedProtocols(String protocol){

		ArrayCleaner.clean(Client.evaluatedProtocols);

		if(protocol !=null){
			boolean exist = false;
			for(String s:Client.evaluatedProtocols){
				if(s.equals(protocol)){
					exist = true;
					break;
				}
			}
			if(!exist){
				String temp [] = new String[Client.evaluatedProtocols.length+1];			
				for(int i=0;i<Client.evaluatedProtocols.length;i++){
					temp[i] = Client.evaluatedProtocols[i];
				}
				temp[Client.evaluatedProtocols.length] = protocol;			
				Client.evaluatedProtocols = temp;	

				return true;
			}
			
		}
		return false;
	}
	
	public static boolean removeFromEvaluatedProtocols(String protocol){
		ArrayCleaner.clean(Client.evaluatedProtocols);
		
		boolean exist = false;
		for(String s:Client.evaluatedProtocols){
			if(s.equals(protocol)){
				exist = true;
				break;
			}
		}
		
		if(exist){
			String temp [] = new String[Client.evaluatedProtocols.length-1];	
			int j= 0;
			for(int i=0;i<Client.evaluatedProtocols.length;i++){
				String s = Client.evaluatedProtocols[i];
				if(!s.equals(protocol)){
					temp[j] = Client.evaluatedProtocols[i];
					j = j+1;
				}			
			}	
			
			Client.evaluatedProtocols = temp;

			return true;
		}	
		else{
			return false;
		}
	}
	
}
