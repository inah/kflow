package fmcr.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import fmcr.display.Display;
import fmcr.display.LeaksFactory;
import fmcr.visitors.CodeVisitor;

public class Client {

	private static File selectedSourceFile;
	private static String localDir;	
	private static CompilationUnit cu;
	private static Display display;	
    public static final ImageIcon blankIcon = new ImageIcon();
    private static boolean sourceFileLoaded;
    
    public static int totalLeaks;
    public static int l1Leaks;
    public static int l2Leaks;
    public static int l3Leaks;
    public static int l4Leaks;
    
	public static ArrayList<File> sourceFiles;
	public static boolean isDir;
	
	public static String [] protocolSuite;
	public static String [] evaluatedProtocols;
	public static HashMap<String, Double> protocolCost;

	public static boolean isSourceFileLoaded() {
		return sourceFileLoaded;
	}

	public static void setSourceFileLoaded(boolean sourceFileLoaded) {
		Client.sourceFileLoaded = sourceFileLoaded;
	}

	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				display = new Display();
				display.setVisible(true);
			}
		});
	}
	
	public static boolean loadCompilationUnit() {
		try {
			if(isDir) {
				if(selectedSourceFile == null) {
					return false;
				}
				else {
					try {
						cu = JavaParser.parse(new FileInputStream(selectedSourceFile));
						return true;
					} 
					catch (IOException e) {
						Client.getDisplay().updateLogPage("UNPARSABLE (IOE):"+selectedSourceFile, true);
					}
					catch(ParseProblemException e) {
						Client.getDisplay().updateLogPage("UNPARSABLE (PPE):"+selectedSourceFile, true);
					}
//					finally {
//						
//					}
					return false;
				}
			}
			else {
				String code = getDisplay().textArea.getText();
				cu = JavaParser.parse(code );
				if(cu == null) {
					return false;
				}
			}
			
			return true;
		}
		catch(ParseProblemException e) {
			List<Problem> problems = e.getProblems();
			try {
				String firstProblemdesc = problems.get(0).getMessage();
				String firstProblemBegin = problems.get(0).getLocation().get().toRange().get().begin.toString();
				String firstProblemEnd = problems.get(0).getLocation().get().toRange().get().end.toString();
				Client.getDisplay().updateLogPage(firstProblemdesc +":"+firstProblemBegin+"-"+firstProblemEnd, true);
				return false;
			} catch (Exception e1) {
				//e1.printStackTrace();
				return false;
			}
		}

	}
	
	public  static void doCodeAnalysis(){
		totalLeaks =0;
	    l1Leaks =0;
	    l2Leaks =0;
	    l3Leaks =0;
	    l4Leaks =0;
	    
		if(Client.getCompilationUnit() !=null) {			
			VoidVisitor<String> codeVisitor = new CodeVisitor();
			codeVisitor.visit(Client.getCompilationUnit(), null);
			
			Client.getDisplay().astview.showTree();
			
			if(CodeVisitor.leaks.size() >0) {
				Client.getDisplay().updateLeaksPage();
				LeaksFactory ckf = new LeaksFactory();
				ckf.displayReports(Client.getDisplay().leaksView);
			    ckf.updateLeaksTable();
				Client.getDisplay().updateLabels();
			}
			else {
				Client.getDisplay().updateLogPage("no information leak detected in program code", false);
				Client.getDisplay().jTabbedPane1.setSelectedIndex(0);
			}
			
		}			
	}
	

	public static File getSelectedSourceFile() {
		return selectedSourceFile;
	}

	public static void setSelectedSourceFile(File selectedSourceFile) {
		Client.selectedSourceFile = selectedSourceFile;
	}

	public static CompilationUnit getCompilationUnit() {
		return cu;
	}
	
	public static Display getDisplay() {
		return display;
	}

	public static void setDisplay(Display display) {
		Client.display = display;
	}

	public static String getLocalDir() {
		return localDir;
	}

	public static void setLocalDir(String localDir) {
		Client.localDir = localDir;
	}

}
