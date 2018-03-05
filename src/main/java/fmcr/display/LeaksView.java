package fmcr.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fmcr.leaks.detectors.SensitivityLevel;
import fmcr.leaks.detectors.Tag;

public class LeaksView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public JTable table;
	public LeaksReportModel model;
	
	/**
	 * Create the panel.
	 */	
	public LeaksView() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0,0));
				
		model = new LeaksReportModel();						
		model.setRowCount(0);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -8305142193885321738L;
			@Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table,value, isSelected, hasFocus, row, column);
					c.setBackground(row%2==0 ? Color.LIGHT_GRAY : new Color(226,225,213));                        
	            return c;
	        };
	    });
		
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
		table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		//table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		//table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
//		table.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
		table.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
		
		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));
		
		TableColumn column = null;
        for (int i = 0; i < 8; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0)
                column.setMaxWidth(40);
            if (i == 1)
                column.setMaxWidth(350);
            if (i == 2) 
                column.setMaxWidth(250);
            if (i == 3)
                column.setMaxWidth(200);
            if (i == 4)
                column.setMaxWidth(50);
            if (i == 5)
                column.setMaxWidth(100);
            if (i == 6)
                column.setMaxWidth(300);
            if (i == 7)
                column.setMaxWidth(150);
            
        }
        
		JPanel panelaw = new JPanel(new BorderLayout(0,0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.getViewport().add(table);
		panelaw.add(scrollPane_1, BorderLayout.CENTER);
		add(panelaw, BorderLayout.CENTER);
		
	}
		
	 public class LeaksReportModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

			public LeaksReportModel() {
		      super(new Object[]{"SN ", "Leaks", "Types","LineNo.","Tag", "SensitivityLevel","Source", "File"}, 0);
		    }
						
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
			
		    @SuppressWarnings("rawtypes")
			@Override
		    public Class<?> getColumnClass(int columnIndex) {
		      Class clazz = String.class;
		      switch (columnIndex) {
		      	case 0:
				  clazz = String.class;
			      break;
			    case 1:
		          clazz = String.class;
		          break;
		        case 2:
		          clazz = String.class;
		          break;
		        case 3:
				  clazz = String.class;
				  break;		
		        case 4:
		        	  clazz = Tag.class;
				  break;	
		        case 5:
				  clazz = SensitivityLevel.class;
				  break;	
		        case 6:
				  clazz = String.class;
				  break;	
		        case 7:
		        	  clazz = String.class;
				  break;
		      }
		      return clazz;
		    }

	 }
}
