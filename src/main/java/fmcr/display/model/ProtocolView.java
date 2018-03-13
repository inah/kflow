package fmcr.display.model;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fmcr.main.Client;
import fmcr.main.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JSplitPane;

public class ProtocolView extends JPanel {
	private static final long serialVersionUID = -4185867590936639241L;

	public JTable table;
	public ProtocolsTableModel model;


	//number of processes in transaction
	private static final int REQUEST = 1;
	private static final int CONSENT1 = 3;
	private static final int CONSENT2 = 2;
	private static final int CONSENT3 = 1;
	private static final int CONSENT4 = 2;

	private static final int SENT1 = 2;
	private static final int SENT2 = 1;
	private static final int NOTICE1SU = 2;
	private static final int NOTICE2SU = 1;
	private static final int NOTICE1R = 2;
	private static final int NOTICE2R = 1;

	//	private static final double WRNCPLEVEL =1;
	//	private static final double WNCPLEVEL =0.924;
	//	private static final double WRNPLEVEL =0.913;
	//	private static final double WRCPLEVEL =0.769;
	//	private static final double WNPLEVEL =0.769;
	//	private static final double WCPLEVEL =0.192;
	//	private static final double WRPLEVEL =0.00009;
	//	private static final double FPLEVEL =0.00007;	

	private static final double WRNCPLEVEL =1;
	private static final double WNCPLEVEL =1;
	private static final double WRNPLEVEL =1;
	private static final double WRCPLEVEL =1;
	private static final double WNPLEVEL =1;
	private static final double WCPLEVEL =1;
	private static final double WRPLEVEL =1;
	private static final double FPLEVEL =1;

	/**
	 * Create the panel.
	 */

	public ProtocolView() {
		initProtocolSuite();
		
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0,0));

		JPanel panelaw1 = new JPanel(new BorderLayout(0,0));

		InputStream stream1 = ResourceLoader.load("img/protocolsuite.png");
		ImageIcon protocolSuiteIcon = null;
		try {
			protocolSuiteIcon = new ImageIcon(ImageIO.read(stream1));
		} catch (IOException e) {
			e.printStackTrace();
		}

		JLabel protocolSuiteLabel =new JLabel();
		protocolSuiteLabel.setIcon(protocolSuiteIcon);
		protocolSuiteLabel.setHorizontalAlignment(JLabel.CENTER);
		protocolSuiteLabel.setVerticalAlignment(JLabel.CENTER);
		panelaw1.setBackground(Color.white);
		panelaw1.add(protocolSuiteLabel, BorderLayout.CENTER);

		final JCheckBox selectAll= new JCheckBox("select all");
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rowCount = model.getRowCount();
				if(selectAll.isSelected()){
					for(int i=0;i<rowCount;i++){
						model.setManualValueAt(true, i,0);
					}  
				}
				else{
					for(int i=0;i<rowCount;i++){
						model.setManualValueAt(false, i,0);
					}
				}

			}          
		});
		panelaw1.add(selectAll, BorderLayout.SOUTH);


		model = new ProtocolsTableModel();

		model.setRowCount(0);

		table = new JTable(model);
		table.setFillsViewportHeight(true);		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -8305142193885321738L;
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(row%2==0 ? Color.LIGHT_GRAY : new Color(226,225,213));                        
				return c;
			};
		});
		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(){
			private static final long serialVersionUID = -8305142193885321738L;
			@Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table,value, isSelected, hasFocus, row, column);
					c.setBackground(row%2==0 ? Color.LIGHT_GRAY : new Color(226,225,213));                        
	            return c;
	        };
	    };
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );

		
		TableColumn column = null;
		for (int i = 0; i < 3; i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == 0) 
				column.setMaxWidth(30);
			if (i == 1)
				column.setMaxWidth(35);
			if (i == 2)
				column.setMaxWidth(6000);
		}

		JPanel panelaw2 = new JPanel(new BorderLayout(0,0));


		JScrollPane scrollPane_1 = new JScrollPane(table);
		panelaw2.add(scrollPane_1, BorderLayout.CENTER);
		//		scrollPane_1.setViewportView(viableKSTable);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,panelaw1, panelaw2);
		add(splitPane, BorderLayout.CENTER);

	}


	public class ProtocolsTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -4775356027813366490L;

		public ProtocolsTableModel() {
			super(new Object[]{"", "Cost","disclosure protocol"}, 0);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			Class clazz = String.class;
			switch (columnIndex) {
			case 0:
				clazz = Boolean.class;
				break;
			case 1:
				clazz = Double.class;
				break;
			case 2:
				clazz = String.class;
				break;
			}
			return clazz;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 0;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (aValue instanceof Boolean && column == 0) {
				boolean b = new Boolean(aValue.toString()).booleanValue();

				Vector rowData = (Vector)getDataVector().get(row);

				String p = (String)rowData.get(2);

				if(p !=null){
					if(b){
						ProtocolFactory.addToEvaluatedProtocols(p);	
					}
					else{
						ProtocolFactory.removeFromEvaluatedProtocols(p);	
					}
				}
				rowData.set(0, (Boolean)aValue);
				fireTableCellUpdated(row, column);
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void setManualValueAt(boolean value, int row, int column) {
			if (column == 0) {
				Vector rowData = (Vector)getDataVector().get(row);

				String p = (String)rowData.get(2);

				if(p !=null){
					if(value){
						ProtocolFactory.addToEvaluatedProtocols(p);	
					}
					else{
						ProtocolFactory.removeFromEvaluatedProtocols(p);	
					}
				}
				rowData.set(0,value);
				fireTableCellUpdated(row, column);
			}
		}

	}
	
	public static String[] getEvaluatedProtocols(){
		return Client.evaluatedProtocols;
	}
	
	public static String[] getProtocolSuite(String sessionid){
		return Client.protocolSuite;
	}
	
	public static String getProtocolId(String protocolDesc){
		String protocolId = null;
		
		for(String p: Client.protocolSuite){
			String pats1[] = p.split(" \\(");
			String pid = pats1[0];
			
			String pdesc = pats1[1].replace(")", "");
			
			if(pdesc.equals(protocolDesc)){
				protocolId = pid;
				break;
			}
		}
		
		return protocolId;
	}
	
	public static String getProtocolDesc(String protocolId){
		String protocolDesc = null;

		for(String p: Client.protocolSuite){
			String pats1[] = p.split(" \\(");
			String pid = pats1[0];
			
			String pdesc = pats1[1].replace(")", "");
			
			if(pid.equals(protocolId)){
				protocolDesc = pdesc;
				break;
			}
		}
		
		return protocolDesc;
	}
	
	public static boolean initProtocolSuite(){
		Client.protocolSuite = new String[140];
		Client.protocolCost = new HashMap<String, Double>();
		double cost = 0;
		double noprocesses = 0;
		double maxNoProcesses = 10;
		double dof =0;
		
		Client.protocolSuite[0] = "1 (Request,Sent1)";
		noprocesses = REQUEST+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[0], cost);
		
		Client.protocolSuite[1] = "2 (Request,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[1], cost);
		
		Client.protocolSuite[2] = "3 (Request,Sent1,Notice1-su)";
		noprocesses = REQUEST+SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[2], cost);
		
		Client.protocolSuite[3] = "4 (Request,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[3], cost);
		
		Client.protocolSuite[4] = "5 (Request,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+SENT1+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[4], cost);
		
		Client.protocolSuite[5] = "6 (Request,Sent1,Notice2-su)";
		noprocesses = REQUEST+SENT1+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[5], cost);
		
		Client.protocolSuite[6] = "7 (Request,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[6], cost);
		
		Client.protocolSuite[7] = "8 (Request,Sent2)";
		noprocesses = REQUEST+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[7], cost);
		
		Client.protocolSuite[8] = "9 (Request,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[8], cost);
		
		Client.protocolSuite[9] = "10 (Request,Sent2,Notice2-su)";
		noprocesses = REQUEST+SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[9], cost);
		
		Client.protocolSuite[10] = "11 (Request,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[10], cost);
		
		Client.protocolSuite[11] = "12 (Request,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[11], cost);
		
		Client.protocolSuite[12] = "13 (Request,Sent2,Notice1-su)";
		noprocesses = REQUEST+SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[12], cost);
		
		Client.protocolSuite[13] = "14 (Request,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[13], cost);
		
		Client.protocolSuite[14] = "15 (Request,Consent1,Sent1)";
		noprocesses = REQUEST+CONSENT1+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[14], cost);
		
		Client.protocolSuite[15] = "16 (Request,Consent1,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[15], cost);
		
		Client.protocolSuite[16] = "17 (Request,Consent1,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[16], cost);
		
		Client.protocolSuite[17] = "18 (Request,Consent1,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[17], cost);
		
		Client.protocolSuite[18] = "19 (Request,Consent1,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[18], cost);
		
		Client.protocolSuite[19] = "20 (Request,Consent1,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[19], cost);
		
		Client.protocolSuite[20] = "21 (Request,Consent1,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[20], cost);
		
		Client.protocolSuite[21] = "22 (Request,Consent1,Sent2)";
		noprocesses = REQUEST+CONSENT1+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[21], cost);
		
		Client.protocolSuite[22] = "23 (Request,Consent1,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[22], cost);
		
		Client.protocolSuite[23] = "24 (Request,Consent1,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[23], cost);
		
		Client.protocolSuite[24] = "25 (Request,Consent1,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[24], cost);
		
		Client.protocolSuite[25] = "26 (Request,Consent1,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[25], cost);
		
		Client.protocolSuite[26] = "27 (Request,Consent1,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[26], cost);
		
		Client.protocolSuite[27] = "28 (Request,Consent1,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[27], cost);
		
		Client.protocolSuite[28] = "29 (Request,Consent2,Sent1)";
		noprocesses = REQUEST+CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[28], cost);
		
		Client.protocolSuite[29] = "30 (Request,Consent2,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[29], cost);
		
		Client.protocolSuite[30] = "31 (Request,Consent2,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[30], cost);
		
		Client.protocolSuite[31] = "32 (Request,Consent2,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[31], cost);
		
		Client.protocolSuite[32] = "33 (Request,Consent2,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[32], cost);
		
		Client.protocolSuite[33] = "34 (Request,Consent2,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT2+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[33], cost);
		
		Client.protocolSuite[34] = "35 (Request,Consent2,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[34], cost);
		
		Client.protocolSuite[35] = "36 (Request,Consent2,Sent2)";
		noprocesses = REQUEST+CONSENT2+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[35], cost);
		
		Client.protocolSuite[36] = "37 (Request,Consent2,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[36], cost);
		
		Client.protocolSuite[37] = "38 (Request,Consent2,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[37], cost);
		
		Client.protocolSuite[38] = "39 (Request,Consent2,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[38], cost);
		
		Client.protocolSuite[39] = "40 (Request,Consent2,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[39], cost);
		
		Client.protocolSuite[40] = "41 (Request,Consent2,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[40], cost);
		
		Client.protocolSuite[41] = "42 (Request,Consent2,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[41], cost);
		
		Client.protocolSuite[42] = "43 (Request,Consent3,Sent1)";
		noprocesses = REQUEST+CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[42], cost);
		
		Client.protocolSuite[43] = "44 (Request,Consent3,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[43], cost);
		
		Client.protocolSuite[44] = "45 (Request,Consent3,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[44], cost);
		
		Client.protocolSuite[45] = "46 (Request,Consent3,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[45], cost);
		
		Client.protocolSuite[46] = "47 (Request,Consent3,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[46], cost);
		
		Client.protocolSuite[47] = "48 (Request,Consent3,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[47], cost);
		
		Client.protocolSuite[48] = "49 (Request,Consent3,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[48], cost);
		
		Client.protocolSuite[49] = "50 (Request,Consent3,Sent2)";
		noprocesses = REQUEST+CONSENT3+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[49], cost);
		
		Client.protocolSuite[50] = "51 (Request,Consent3,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[50], cost);
		
		Client.protocolSuite[51] = "52 (Request,Consent3,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[51], cost);
		
		Client.protocolSuite[52] = "53 (Request,Consent3,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[52], cost);
		
		Client.protocolSuite[53] = "54 (Request,Consent3,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[53], cost);
		
		Client.protocolSuite[54] = "55 (Request,Consent3,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[54], cost);
		
		Client.protocolSuite[55] = "56 (Request,Consent3,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[55], cost);
		
		Client.protocolSuite[56] = "57 (Request,Consent4,Sent1)";
		noprocesses = REQUEST+CONSENT4+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[56], cost);
		
		Client.protocolSuite[57] = "58 (Request,Consent4,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[57], cost);
		
		Client.protocolSuite[58] = "59 (Request,Consent4,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[58], cost);
		
		Client.protocolSuite[59] = "60 (Request,Consent4,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[59], cost);
		
		Client.protocolSuite[60] = "61 (Request,Consent4,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[60], cost);
		
		Client.protocolSuite[61] = "62 (Request,Consent4,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[61], cost);
		
		Client.protocolSuite[62] = "63 (Request,Consent4,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[62], cost);
		
		Client.protocolSuite[63] = "64 (Request,Consent4,Sent2)";
		noprocesses = REQUEST+CONSENT4+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[63], cost);
		
		Client.protocolSuite[64] = "65 (Request,Consent4,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[64], cost);
		
		Client.protocolSuite[65] = "66 (Request,Consent4,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[65], cost);
		
		Client.protocolSuite[66] = "67 (Request,Consent4,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[66], cost);
		
		Client.protocolSuite[67] = "68 (Request,Consent4,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[67], cost);
		
		Client.protocolSuite[68] = "69 (Request,Consent4,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[68], cost);
		
		Client.protocolSuite[69] = "70 (Request,Consent4,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[69], cost);
		
		//
		Client.protocolSuite[70] = "71 (Sent1)";
		noprocesses = SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[70], cost);
		
		Client.protocolSuite[71] = "72 (Sent1,Notice1-su,Notice1-r)";
		noprocesses = SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[71], cost);
		
		Client.protocolSuite[72] = "73 (Sent1,Notice1-su)";
		noprocesses = SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[72], cost);
		
		Client.protocolSuite[73] = "74 (Sent1,Notice1-su,Notice2-r)";
		noprocesses = SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[73], cost);
		
		Client.protocolSuite[74] = "75 (Sent1,Notice2-su,Notice2-r)";
		noprocesses = SENT1+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[74], cost);
		
		Client.protocolSuite[75] = "76 (Sent1,Notice2-su)";
		noprocesses = SENT1+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[75], cost);
		
		Client.protocolSuite[76] = "77 (Sent1,Notice2-su,Notice1-r)";
		noprocesses = SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[76], cost);
		
		Client.protocolSuite[77] = "78 (Sent2)";
		noprocesses = SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[77], cost);
		
		Client.protocolSuite[78] = "79 (Sent2,Notice2-su,Notice2-r)";
		noprocesses = SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[78], cost);
		
		Client.protocolSuite[79] = "80 (Sent2,Notice2-su)";
		noprocesses = SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[79], cost);
		
		Client.protocolSuite[80] = "81 (Sent2,Notice2-su,Notice1-r)";
		noprocesses = SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[80], cost);
		
		Client.protocolSuite[81] = "82 (Sent2,Notice1-su,Notice1-r)";
		noprocesses = SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[81], cost);
		
		Client.protocolSuite[82] = "83 (Sent2,Notice1-su)";
		noprocesses = SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[82], cost);
		
		Client.protocolSuite[83] = "84 (Sent2,Notice1-su,Notice2-r)";
		noprocesses = SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[83], cost);
		
		Client.protocolSuite[84] = "85 (Consent1,Sent1)";
		noprocesses = CONSENT1+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[84], cost);
		
		Client.protocolSuite[85] = "86 (Consent1,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT1+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[85], cost);
		
		Client.protocolSuite[86] = "87 (Consent1,Sent1,Notice1-su)";
		noprocesses = CONSENT1+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[86], cost);
		
		Client.protocolSuite[87] = "88 (Consent1,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT1+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[87], cost);
		
		Client.protocolSuite[88] = "89 (Consent1,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT1+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[88], cost);
		
		Client.protocolSuite[89] = "90 (Consent1,Sent1,Notice2-su)";
		noprocesses = CONSENT1+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[89], cost);
		
		Client.protocolSuite[90] = "91 (Consent1,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT1+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[90], cost);
		
		Client.protocolSuite[91] = "92 (Consent1,Sent2)";
		noprocesses = CONSENT1+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[91], cost);
		
		Client.protocolSuite[92] = "93 (Consent1,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT1+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[92], cost);
		
		Client.protocolSuite[93] = "94 (Consent1,Sent2,Notice2-su)";
		noprocesses = CONSENT1+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[93], cost);
		
		Client.protocolSuite[94] = "95 (Consent1,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT1+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[94], cost);
		
		Client.protocolSuite[95] = "96 (Consent1,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT1+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[95], cost);
		
		Client.protocolSuite[96] = "97 (Consent1,Sent2,Notice1-su)";
		noprocesses = CONSENT1+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[96], cost);
		
		Client.protocolSuite[97] = "98 (Consent1,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT1+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[97], cost);
		
		Client.protocolSuite[98] = "99 (Consent2,Sent1)";
		noprocesses = CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[98], cost);
		
		Client.protocolSuite[99] = "100 (Consent2,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT2+SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[99], cost);
		
		Client.protocolSuite[100] = "101 (Consent2,Sent1,Notice1-su)";
		noprocesses = CONSENT2+SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[100], cost);
		
		Client.protocolSuite[101] = "102 (Consent2,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT2+SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[101], cost);
		
		Client.protocolSuite[102] = "103 (Consent2,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT2+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[102], cost);
		
		Client.protocolSuite[103] = "104 (Consent2,Sent1,Notice2-su)";
		noprocesses = CONSENT2+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[103], cost);
		
		Client.protocolSuite[104] = "105 (Consent2,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT2+SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[104], cost);
		
		Client.protocolSuite[105] = "106 (Consent2,Sent2)";
		noprocesses = CONSENT2+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[105], cost);
		
		Client.protocolSuite[106] = "107 (Request,Consent2,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[106], cost);
		
		Client.protocolSuite[107] = "108 (Request,Consent2,Sent2,Notice2-su)";
		noprocesses = CONSENT2+SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[107], cost);
		
		Client.protocolSuite[108] = "109 (Request,Consent2,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[108], cost);
		
		Client.protocolSuite[109] = "110 (Consent2,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[109], cost);
		
		Client.protocolSuite[110] = "111 (Consent2,Sent2,Notice1-su)";
		noprocesses = CONSENT2+SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[110], cost);
		
		Client.protocolSuite[111] = "112 (Consent2,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[111], cost);
		
		Client.protocolSuite[112] = "113 (Consent3,Sent1)";
		noprocesses = CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[112], cost);
		
		Client.protocolSuite[113] = "114 (Consent3,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT3+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[113], cost);
		
		Client.protocolSuite[114] = "115 (Consent3,Sent1,Notice1-su)";
		noprocesses = CONSENT3+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[114], cost);
		
		Client.protocolSuite[115] = "116 (Consent3,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT3+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[115], cost);
		
		Client.protocolSuite[116] = "117 (Consent3,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT3+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[116], cost);
		
		Client.protocolSuite[117] = "118 (Consent3,Sent1,Notice2-su)";
		noprocesses = CONSENT3+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[117], cost);
		
		Client.protocolSuite[118] = "119 (Consent3,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT3+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[118], cost);
		
		Client.protocolSuite[119] = "120 (Consent3,Sent2)";
		noprocesses = CONSENT3+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[119], cost);
		
		Client.protocolSuite[120] = "121 (Consent3,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT3+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[120], cost);
		
		Client.protocolSuite[121] = "122 (Consent3,Sent2,Notice2-su)";
		noprocesses = CONSENT3+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[121], cost);
		
		Client.protocolSuite[122] = "123 (Consent3,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT3+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[122], cost);
		
		Client.protocolSuite[123] = "124 (Consent3,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT3+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[123], cost);
		
		Client.protocolSuite[124] = "125 (Consent3,Sent2,Notice1-su)";
		noprocesses = CONSENT3+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[124], cost);
		
		Client.protocolSuite[125] = "126 (Consent3,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT3+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[125], cost);
		
		Client.protocolSuite[126] = "127 (Consent4,Sent1)";
		noprocesses = CONSENT4+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[126], cost);
		
		Client.protocolSuite[127] = "128 (Consent4,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT4+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[127], cost);
		
		Client.protocolSuite[128] = "129 (Consent4,Sent1,Notice1-su)";
		noprocesses = CONSENT4+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[128], cost);
		
		Client.protocolSuite[129] = "130 (Consent4,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT4+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[129], cost);
		
		Client.protocolSuite[130] = "131 (Consent4,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT4+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[130], cost);
		
		Client.protocolSuite[131] = "132 (Consent4,Sent1,Notice2-su)";
		noprocesses = CONSENT4+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[131], cost);
		
		Client.protocolSuite[132] = "133 (Consent4,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT4+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[132], cost);
		
		Client.protocolSuite[133] = "134 (Consent4,Sent2)";
		noprocesses = CONSENT4+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[133], cost);
		
		Client.protocolSuite[134] = "135 (Consent4,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT4+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[134], cost);
		
		Client.protocolSuite[135] = "136 (Consent4,Sent2,Notice2-su)";
		noprocesses = CONSENT4+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[135], cost);
		
		Client.protocolSuite[136] = "137 (Consent4,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT4+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[136], cost);
		
		Client.protocolSuite[137] = "138 (Consent4,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT4+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[137], cost);
		
		Client.protocolSuite[138] = "139 (Consent4,Sent2,Notice1-su)";
		noprocesses = CONSENT4+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[138], cost);
		
		Client.protocolSuite[139] = "140 (Consent4,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT4+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		Client.protocolCost.put(Client.protocolSuite[139], cost);
		
		
		Client.evaluatedProtocols = new String[0]; 
		
		return true;
	}
	
	public static double getCost(String protocol) {
		double cost =0;
		for (Map.Entry<String, Double> entry : Client.protocolCost.entrySet()){
			String key = entry.getKey();
			if(key.equals(protocol)) {
				cost = entry.getValue();
				break;
			}
		}
		return cost;
	}
	
	public static double getMaxCost(){
		double maxvalue = 0;
		for (Map.Entry<String, Double> entry : Client.protocolCost.entrySet()){
			//String key = entry.getKey();
			double value = entry.getValue();

			if(value > maxvalue){
				maxvalue = value;
			}
		}
		return maxvalue;
	}

}
