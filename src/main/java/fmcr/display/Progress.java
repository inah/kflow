package fmcr.display;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A used to update user on how FMAR is progressing with source code analysis>
 *
 * @author  Inah Omoronyia
 * @version 1.0
 */
public class Progress extends JPanel {
	private static final long serialVersionUID = -792351501911847629L;

	JProgressBar pbar;

	  static final int MY_MINIMUM = 0;

	  static final int MY_MAXIMUM = 100;

	  public Progress() {
	    // initialize Progress Bar
	    pbar = new JProgressBar();
//	    pbar.setBackground(Color.darkGray);
	    pbar.setMinimum(MY_MINIMUM);
	    pbar.setMaximum(MY_MAXIMUM);
	    pbar.setBorderPainted(false);
//	    setBackground(Color.darkGray);
	    // add to JPanel
	    add(pbar);
	    
	  }

	  public void updateBar(int newValue) {
		pbar.setIndeterminate(false);
	    pbar.setValue(newValue);
	  }
	  
	  public void updateBar(){
		pbar.setIndeterminate(true);
	  }
	}