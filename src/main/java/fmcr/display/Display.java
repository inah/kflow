package fmcr.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.mxgraph.view.mxGraphView;

import fmcr.display.flowgraph.KnowledgeGraphView;
import fmcr.display.model.ProtocolFactory;
import fmcr.display.model.ProtocolView;
import fmcr.main.Client;
import fmcr.main.ResourceLoader;

public class Display extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double zoom =1;
	public int filecounter = 1; //increment by 1 after every java file that is analysed
	DecimalFormat df = new DecimalFormat("####0.0");

	public Display() {
		try {
			initComponents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() throws IOException {
		this.setResizable(true);
		this.setTitle("Dipro:Verifying Information Disclosure Protocols in Object Oriented Software");
		jSplitPane1 = new javax.swing.JSplitPane();
		jSplitPane2 = new javax.swing.JSplitPane();

		jTabbedPane1 = new javax.swing.JTabbedPane();
		
		createLogTextPane();
		jScrollPane1 = new JScrollPane(logTextPane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setViewportView(logTextPane);
		jTabbedPane1.addTab("Logs", jScrollPane1);
		jTabbedPane1.addTab("InformationLeaks", createLeaksPage());
		jTabbedPane1.addTab("disclosure protocol suite", createProtocolPage());
		jTabbedPane1.addTab("privacy requirements", null);//createAssertionsPage());
		
		astPanel = new JPanel(new BorderLayout());
		JPanel zoompanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		jLabel1 = new javax.swing.JLabel("Zoom:");
		zoompanel.add(jLabel1);
		jSlider = new javax.swing.JSlider(javax.swing.JSlider.HORIZONTAL, 1, 250, 100);
				
		jSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!jSlider.getValueIsAdjusting()) {
					double z = (double)jSlider.getValue();
					jLabel2.setText(jSlider.getValue()+"%");
					zoom = z/100;
					if(astview.graph != null) {
						mxGraphView view = astview.graph.getView();
						view.setScale(zoom);					
						Client.getDisplay().updateASTView();
					}
				}				
			}
		});
		zoompanel.add(jSlider);
		jLabel2 = new javax.swing.JLabel(jSlider.getValue() +"%");
		zoompanel.add(jLabel2);		
		astPanel.add(zoompanel,BorderLayout.NORTH);
		
		createASTView();
		astJScrollPane = new JScrollPane(astview,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		astJScrollPane.setViewportView(astview);		
		astPanel.add(astJScrollPane,BorderLayout.CENTER);
		
		jTabbedPane1.addTab("Source AST",astPanel);
		
		
		createSourceCodeEditorPane();
		
		createKnowledgeGraphView();		
		jScrollPane3 = new javax.swing.JScrollPane(kgview);

		jToolBar1 = new javax.swing.JToolBar();
		

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		jSplitPane2.setLeftComponent(sourcecodePanel);
//		jSplitPane2.setLeftComponent(jScrollPane2);
		jSplitPane2.setRightComponent(jScrollPane3);

		jSplitPane1.setTopComponent(jSplitPane2);
		jSplitPane1.setBottomComponent(jTabbedPane1);
//		jSplitPane1.setBottomComponent(jScrollPane1);

		jSplitPane1.setDividerLocation(380);
		jSplitPane2.setDividerLocation(585);


		jToolBar1.setRollover(true);
		InputStream stream1 = ResourceLoader.load("img/setup.png");
		ImageIcon jb1ic = new ImageIcon(ImageIO.read(stream1));
		JButton jb1 = new JButton("Setup", jb1ic);
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				typeSolverSetup();				
			}

		});
		jToolBar1.add(jb1);
		
		InputStream stream2 = ResourceLoader.load("img/open.png");
		ImageIcon jb2ic = new ImageIcon(ImageIO.read(stream2));
		JButton jb2 = new JButton("Open", jb2ic);
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.getDisplay().readLocalRepository();				
			}

		});
		jToolBar1.add(jb2);
		
		InputStream stream3 = ResourceLoader.load("img/clear.png");
		ImageIcon jb3ic = new ImageIcon(ImageIO.read(stream3));
		JButton jb3 = new JButton("New", jb3ic);
		jb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSourceStatements();	
				astview.setVisible(false);
				leaksView.model.setRowCount(0);
				clearKnowledgeGraph();
				clearLogPane(logTextPane);
			}

		});
		jToolBar1.add(jb3);
		
		InputStream stream4 = ResourceLoader.load("img/typesolver.png");
		ImageIcon jb4ic = new ImageIcon(ImageIO.read(stream4));
		JButton jb4 = new JButton("Analyse", jb4ic);
		jb4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filecounter = 1;
				analyseProgram();				
			}

		});
		jToolBar1.add(jb4);
				
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		pane_bottom = new JPanel();
		pane_bottom.setPreferredSize(new Dimension(230, 30));
		pane_bottom.setLayout(new BorderLayout());
		pane_bottom.add(createProgressComponent(), BorderLayout.NORTH);
		jToolBar1.add(pane_bottom);
		jToolBar1.add(Box.createHorizontalStrut(710)); //just increase this to make the frame wider
		jToolBar1.add(Box.createHorizontalGlue());

//		jToolBar1.add(jLabel1);
//		jToolBar1.add(jSlider);
//		jToolBar1.add(jLabel2);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 883, Short.MAX_VALUE)
								.addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
						.addContainerGap())
				);

		pack();
		
		updateProtocolPage();

	}
	
	private JPanel infopf = new  JPanel();
	private static final JLabel totalLeakslabel = new JLabel("<html><font size='3'>Total Leaks:</font></html>");
	private static final JLabel l1Leakslabel = new JLabel("<html><font size='3'>Level 1:</font></html>");
	private static final JLabel l2Leakslabel = new JLabel("<html><font size='3'>Level 2:</font></html>");
	private static final JLabel l3Leakslabel = new JLabel("<html><font size='3'>Level 2:</font></html>");
	private static final JLabel l4Leakslabel = new JLabel("<html><font size='3'>Level 2:</font></html>");
	
	private static JPanel leaksPanel;	
	public JComponent createLeaksPage(){
		infopf.setLayout(new BoxLayout(infopf,BoxLayout.X_AXIS));
		leaksPanel = new JPanel(new BorderLayout(0,0));	

		jTabbedPane1.revalidate();
		jTabbedPane1.repaint();

		return leaksPanel;
	}
	
	public void updateLabels() {
		totalLeakslabel.setText("<html><font size='3'>Total Leaks:"+Client.totalLeaks+"</font></html>");
		l1Leakslabel.setText("<html><font size='3'>Level 1:"+Client.l1Leaks+"</font></html>");
		l2Leakslabel.setText("<html><font size='3'>Level 2:"+Client.l2Leaks+"</font></html>");
		l3Leakslabel.setText("<html><font size='3'>Level 3:"+Client.l3Leaks+"</font></html>");
		l4Leakslabel.setText("<html><font size='3'>Level 4:"+Client.l4Leaks+"</font></html>");
		
		infopf.revalidate();
		infopf.repaint();
		jTabbedPane1.setSelectedIndex(1);
	}
	
	public JPanel getLeaksPanel() {
		return leaksPanel;
	}
	
	public LeaksView leaksView =null;
	public void updateLeaksPage(){
		if(leaksView ==null) {
			leaksPanel.removeAll();
			leaksView = new LeaksView();

			infopf.add(totalLeakslabel);
			infopf.add(l1Leakslabel);
			infopf.add(l2Leakslabel);
			infopf.add(l3Leakslabel);
			infopf.add(l4Leakslabel);
			
			leaksPanel.add(infopf,BorderLayout.SOUTH);
			leaksPanel.add(leaksView,BorderLayout.CENTER);		
		
		}
		jTabbedPane1.setSelectedIndex(1);
	}
	
	private static JPanel protocolPanel;	
	public JComponent createProtocolPage(){
		protocolPanel = new JPanel(new BorderLayout(0,0));	
		
		ProtocolView pv = new ProtocolView();
		protocolPanel.add(pv,BorderLayout.CENTER);
		
		return protocolPanel;
	}
	
	public static void updateProtocolPage(){

		Thread queryThread = new Thread() {
			public void run() {
				protocolPanel.removeAll();				
				ProtocolView psView = new ProtocolView();				
				protocolPanel.add(psView,BorderLayout.CENTER);
				ProtocolFactory.displayProtocols(psView);
			}
		};
		queryThread.start();
		
	}
		

	private void typeSolverSetup() {
		TypeInputSourcesDialog sourceInput_d = new TypeInputSourcesDialog();
		sourceInput_d.setBounds(0, 0, 400, 450);
		sourceInput_d.setBackground(Color.white);

		int result = JOptionPane.showConfirmDialog(null, sourceInput_d, 
				"Source and Jar dependency files location", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			sourceInput_d.processInputs();
		}
		
	}

	public KnowledgeGraphView kgview;
	public void createKnowledgeGraphView() {
		kgview = new KnowledgeGraphView();
	}
	public void updateKnowledgeGraphView() {
		kgview.revalidate();
		kgview.repaint();
	}
	
	public ASTView astview;
	public void createASTView() {
		astview = new ASTView();
	}
	public void updateASTView() {
		astview.revalidate();
		astview.repaint();
	}

	public RSyntaxTextArea textArea;
	JPanel sourcecodePanel;
	public String selectedLine= null;
	private void createSourceCodeEditorPane() {
		sourcecodePanel = new JPanel(new BorderLayout());

		textArea = new RSyntaxTextArea(20, 60);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		sourcecodePanel.add(sp);

		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1) {
					return;
				}
				if (e.getClickCount() != 2) {
					return;
				}

				int offset = textArea.viewToModel(e.getPoint());

				try {
					int rowStart = Utilities.getRowStart(textArea, offset);
					int rowEnd = Utilities.getRowEnd(textArea, offset);
					selectedLine = textArea.getText().substring(rowStart, rowEnd);
					selectedLine = selectedLine.replace("{", "");
					analyseProgram();

				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	private void clearSourceStatements() {
		textArea.setText("");
	}
	public void addSourceStatement(String statement) {
		textArea.append(statement+"\n");
	}

	int nofiles;
	private void crawlFiles(File[] files) {
		nofiles = nofiles +files.length;

		for (File file : files) {
			if (file.isDirectory()) {
				crawlFiles(file.listFiles());
			} else {
				String[] s = file.toString().split(Pattern.quote("."));
				if(s.length >1) {
					if (s[s.length-1].equals("java")) {
						Client.sourceFiles.add(file);
					}
				}				
			}
		}
	}
	
	File selectedFile;
	/**
	 * Reads a local folder to select .java files for program analysis 
	 * user can select from <code>1</code> to <code>n</code>.java files
	 */
	public void readLocalRepository() {
		JFileChooser chooser = new JFileChooser();
		if(selectedFile!=null) {
			chooser.setCurrentDirectory(selectedFile);
		}
		chooser.setDialogTitle("Select source file/directory to analyse");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".java", "java", "java");
		chooser.setFileFilter(filter);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			Thread queryThread2 = new Thread() {
				public void run() {		
					clearSourceStatements();
					astview.setVisible(false);
					
					BufferedReader buff = null;
					selectedFile = chooser.getSelectedFile();
					
					if(selectedFile.isDirectory()) {
						Client.isDir = true;

						Client.sourceFiles = new ArrayList<File>();						
						File[] files = chooser.getSelectedFile().listFiles();
						nofiles =0;
						crawlFiles(files);
						updateLogPage(nofiles+ " java files loaded", false);
					}
					else {
						Client.isDir = false;
						String[] s = chooser.getSelectedFile().toString().split(Pattern.quote("."));
						if(!s[s.length-1].equals("java")) {
							return;
						}
						Client.setSelectedSourceFile(chooser.getSelectedFile());

						try {	
							buff = new BufferedReader(new FileReader(Client.getSelectedSourceFile()));
							String str;
							while ((str = buff.readLine()) != null) {
								addSourceStatement(str);

							}
						}
						catch (IOException e) {
							System.err.print(e);
						} 

						Client.setSourceFileLoaded(true);
					}

					
				}
			};
			queryThread2.start();				
		}        
	}  
	
	/**
	 * clears knowledge graph
	 */
	private void clearKnowledgeGraph(){
		jScrollPane3.remove(kgview);
		kgview = null;
		createKnowledgeGraphView();		
		jScrollPane3.setViewportView(kgview);	
		jScrollPane3.revalidate();
		jScrollPane3.repaint();
		jSplitPane2.setRightComponent(jScrollPane3);
		jSplitPane2.setDividerLocation(585);
		jSplitPane2.revalidate();
		jSplitPane2.repaint();
		revalidate();
		repaint();
		
		updateKnowledgeGraphView();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes program analysis
	 * @param button press event (ActionEvent).
	 */
	private void analyseProgram() {
		if(leaksView !=null) {
			leaksView.model.setRowCount(0);
			clearKnowledgeGraph();
			clearLogPane(logTextPane);
		}
		
		Thread queryThread2 = new Thread() {
			public void run() {
				if(Client.isDir){
					int i=0;
					updateProgressComponent(i,"%");

					for(File file: Client.sourceFiles){
						Client.setSelectedSourceFile(file);
						addSourceStatement(file.getAbsolutePath());
						boolean loaded = Client.loadCompilationUnit();
						if(loaded) {
							astview.setVisible(true);
							astview.clearGraph();
							Client.doCodeAnalysis();
						}
						else {
							createASTView();
							astJScrollPane = new JScrollPane(astview,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							astJScrollPane.setViewportView(astview);		
							astPanel.add(astJScrollPane,BorderLayout.CENTER);

							updateLogPage("No CompilationUnit", true);
						}
						filecounter = filecounter+1;
						i =i+1;
						updateLabels();
						i = i+1;
						double pc = (i/ Client.sourceFiles.size()) * 100;						
						updateProgressComponent(new Double(pc).intValue(), df.format(pc) + "%");
					}
					
					updateProgressComponent(100,"");
				}
				else{
					if (textArea.getText() != null && textArea.getText().length()>0) {
						boolean loaded = Client.loadCompilationUnit();
						if(loaded) {
							astview.setVisible(true);
							astview.clearGraph();
							Client.doCodeAnalysis();
						}
						else {
							createASTView();
							astJScrollPane = new JScrollPane(astview,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							astJScrollPane.setViewportView(astview);		
							astPanel.add(astJScrollPane,BorderLayout.CENTER);

							updateLogPage("No CompilationUnit", true);
						}
						updateLabels();
					}
				}
				
			}
		};
		queryThread2.start();
	}

	public JComponent createProgressComponent(){
		tempProgress = new JLabel("");		
		progressPanel = new Progress();
		progressPanel.setVisible(false);
		return progressPanel;
	}

	private static JLabel tempProgress;
	private static Progress progressPanel;
	private static Container pane_bottom;
	public static void updateProgressComponent(final int newvalue, final String info){

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				//	        	tempProgress.setText(info);
				//		    tempProgress.setText("<html>&#x2600;"+info+"</html>");	        	
				tempProgress.setText("<html>"+info+"</html>");	        	

				Font font = new Font("Verdana", Font.ITALIC, 8);
				tempProgress.setFont(font);
				tempProgress.setForeground(Color.blue );

				pane_bottom.revalidate();
				pane_bottom.repaint();

				progressPanel.setVisible(true);
				progressPanel.add(tempProgress);
				if(newvalue >=0){
					progressPanel.updateBar(newvalue);
				}
				else{
					progressPanel.updateBar();	
				}

				if(newvalue ==100){
					try {
						java.lang.Thread.sleep(400);
						progressPanel.setVisible(false);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});	
	}


	//	private JPanel logPanel;
	private static JTextPane logTextPane;
	private StyleSheet styleSheet = new StyleSheet();
	private HTMLDocument htmlDocument;
	private HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
	//	private Element bodyElement;
	JPopupMenu clearpop;
	/**
	 * program analysis logging
	 */
	public void createLogTextPane(){
		//		logPanel = new JPanel(new BorderLayout());

		styleSheet.addRule("ul.tree, ul.tree ul {list-style: none; margin: 0; padding: 0; } ");
		styleSheet.addRule("ul.tree ul { margin-left: 10px; }");
		styleSheet.addRule("ul.tree li { margin: 0; padding: 0 7px; line-height: 20px; color: #369; font-weight: bold; border-left:1px solid rgb(100,100,100);}");
		styleSheet.addRule("ul.tree li:last-child { border-left:none; }");
		styleSheet.addRule("ul.tree li:before {position:relative; top:-0.3em; height:1em; width:12px; color:white; border-bottom:1px solid rgb(100,100,100); content:''; display:inline-block; left:-7px; }");
		styleSheet.addRule("ul.tree li:last-child:before { border-left:1px solid rgb(100,100,100); }");

		Font font = new Font("Verdana", Font.PLAIN, 10);
		String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";
		styleSheet.addRule(bodyRule);

		htmlEditorKit.setStyleSheet(styleSheet);
		htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
		try {
			htmlEditorKit.insertHTML(htmlDocument, htmlDocument.getLength(),"", 0, 0, null);
		} catch (BadLocationException | IOException e1) {
			e1.printStackTrace();
		}

		logTextPane = new JTextPane();
		logTextPane.setEditorKit(htmlEditorKit);
		logTextPane.setDocument(htmlDocument);

		try {

			Container contentPane = getContentPane();
			contentPane.add(logTextPane, BorderLayout.CENTER);
			super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		clearpop = new JPopupMenu();
		JMenuItem clearmi = new JMenuItem("Clear");
		clearmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearLogPane(logTextPane);
			}

		}); 

		clearpop.add(clearmi);
		PopupMenuListener pml = new PopupMenuListener();
		logTextPane.addMouseListener(pml);
		logTextPane.setEditable(false);
	}

	class PopupMenuListener extends MouseAdapter {
		public void mousePressed(MouseEvent me) {
			showPopup(me);
		}

		public void mouseReleased(MouseEvent me) {
			showPopup(me);
		}

		private void showPopup(MouseEvent me) {
			if (me.isPopupTrigger()) {
				clearpop.show(me.getComponent(),
						me.getX(), me.getY());
			}
		}
	}

	public static void clearLogPane(JTextPane tp){
		tp.setText("");
	}

	public  void appendToTreePane(StringBuffer structure){

		try {
			htmlEditorKit.insertHTML(htmlDocument, htmlDocument.getLength(), structure.toString(), 0, 0, null);	
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}

		int len = logTextPane.getDocument().getLength();
		logTextPane.setCaretPosition(len);
		logTextPane.replaceSelection(structure.toString());	

		logTextPane.revalidate();
		logTextPane.repaint();
	}


	/**
	 * program analysis log update
	 */
	public  void appendToPane(String msg, boolean isError) throws Exception{
		if(isError){
			msg = "<html><font color='red'>"+msg+"</font><br></html>";
		}
		else{
			msg = "<html>"+msg+"<br></html>";
		}

		try {
			htmlEditorKit.insertHTML(htmlDocument, htmlDocument.getLength(), msg, 0, 0, null);	
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}

		int len = logTextPane.getDocument().getLength();
		logTextPane.setCaretPosition(len);
		logTextPane.replaceSelection(msg);	

		logTextPane.revalidate();
		logTextPane.repaint();
	}


	/**
	 * program analysis log update
	 */
	public void updateLogPage(final String text, final boolean isError){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					appendToPane(text, isError);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

	private javax.swing.JScrollPane jScrollPane1;
//	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JSlider jSlider;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private JPanel astPanel;
	private javax.swing.JScrollPane astJScrollPane;
	public JTabbedPane jTabbedPane1;
}
