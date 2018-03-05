package fmcr.display;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import fmcr.main.Client;

/**
 *
 * @author inah
 */
public class TypeInputSourcesDialog extends javax.swing.JPanel{
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<File> jarDependencies;
	public static ArrayList<File> srcDependencies;

	String JarTypeSolverRootDir;
	public static String SourceCodeTypeSolverRootDir;
	
	/**
     * Creates new form Dialog
     */
    public TypeInputSourcesDialog() {
        initComponents();
    }
                        
    private void initComponents() {

        JarTypeSolverRootDirButton = new javax.swing.JButton();
        SourceCodeTypeSolverRootDirButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField("", 15);
        jTextField1.setText(selectedRepoJar);
        
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        
        jTextField2 = new javax.swing.JTextField("",15);
        jTextField2.setText(selectedRepoSrc);
        
        JarTypeSolverRootDirButton.setText("browse");
        JarTypeSolverRootDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JarTypeSolverRootDirButtonAction(evt);
            }
        });

        SourceCodeTypeSolverRootDirButton.setText("browse");
        SourceCodeTypeSolverRootDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            		SourceCodeTypeSolverRootDirButtonAction(evt);
            }
        });

        jLabel1.setText("JarTypeSolverRootDir:");
        jLabel2.setText("SourceCodeTypeSolverRootDir:");
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new java.awt.GridLayout(2, 3));
		panel1.add(jLabel1);
		panel1.add(jTextField1);
		panel1.add(JarTypeSolverRootDirButton);
		panel1.add(jLabel2);
		panel1.add(jTextField2);
		panel1.add(SourceCodeTypeSolverRootDirButton);
        
		add(panel1);
        

    }                        

    private static String selectedRepoJar;
    private void JarTypeSolverRootDirButtonAction(java.awt.event.ActionEvent evt) {                                              
    		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("JarTypeSolverRootDir");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//.FILES_AND_DIRECTORIES);
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			
			if(selectedFile.isDirectory()) {
				selectedRepoJar = selectedFile.getAbsolutePath();
				jTextField1.setText(selectedRepoJar);
				
				Client.getDisplay().updateLogPage(".jar dependencies:", false);

				jarDependencies = new ArrayList<File>();			
				File[] files = chooser.getSelectedFile().listFiles();
				p = 0;
				c = 0;
				crawlJarFiles(files);
				Display.updateProgressComponent(100,"");
			}
			
		}    		
    }  
    
    private static String selectedRepoSrc = "";
    private void SourceCodeTypeSolverRootDirButtonAction(java.awt.event.ActionEvent evt) {                                              
    		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("SourceCodeTypeSolverRootDirDir");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//.FILES_AND_DIRECTORIES);
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			
			if(selectedFile.isDirectory()) {
				selectedRepoSrc = selectedFile.getAbsolutePath();
				jTextField2.setText(selectedRepoSrc);
				
				srcDependencies = new ArrayList<File>();			
				File file = chooser.getSelectedFile();
				srcDependencies.add(file);
				
//				Client.getDisplay().updateLogPage("src dependencies:"+selectedRepoSrc, false);
				Display.updateProgressComponent(100,"");
			}
		}  
    }

    public void processInputs() {  
    		JarTypeSolverRootDir = jTextField1.getText();
    		SourceCodeTypeSolverRootDir = jTextField2.getText();
    		
    		this.setVisible(false);
    }   
    
    double p = 0;
	double c = 0;
	DecimalFormat df = new DecimalFormat("####0.0");

	private void crawlJarFiles(File[] files) {
		
		c = c +files.length;

		for (File file : files) {
			if (file.isDirectory()) {
				crawlJarFiles(file.listFiles());
			} else {
				String[] s = file.toString().split(Pattern.quote("."));
				if(s.length >1) {
					if (s[s.length-1].equals("jar")) {
						jarDependencies.add(file);
						Client.getDisplay().updateLogPage(file.getPath(), false);
						p = p+1;
						double pc = (p/c)*100;

						Display.updateProgressComponent(new Double(pc).intValue(),df.format(pc)+"%");
					}
				}				
			}
		}
	}
	
//	private void crawlJavaFiles(File[] files) {
//		
//		c = c +files.length;
//
//		for (File file : files) {
//			if (file.isDirectory()) {
//				crawlJavaFiles(file.listFiles());
//			} else {
//				String[] s = file.toString().split(Pattern.quote("."));
//				if(s.length >1) {
//					if (s[s.length-1].equals("java")) {
//						boolean contained = false;
//						for(File f:srcDependencies) {
//							if(f.getPath().equals(file.getParentFile().getPath())) {
//								contained = true;
//								break;
//							}
//						}
//						if(!contained) {
//							srcDependencies.add(file.getParentFile());
//							Client.getDisplay().updateLogPage(file.getParentFile().getPath(), false);						
//						}
//						p = p+1;
//						double pc = (p/c)*100;
//
//						Display.updateProgressComponent(new Double(pc).intValue(),df.format(pc)+"%");
//					}
//				}				
//			}
//		}
//	}

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton JarTypeSolverRootDirButton;
    private javax.swing.JButton SourceCodeTypeSolverRootDirButton;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
}

