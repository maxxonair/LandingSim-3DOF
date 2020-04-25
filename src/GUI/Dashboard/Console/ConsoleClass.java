package GUI.Dashboard.Console;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.*;

import GUI.Dashboard.DashboardPlotPanel;


public class ConsoleClass extends DashboardPlotPanel {
	
    private JPanel mainPanel;
    private  JTextPane textPane;
    private PrintStream con;
    private DefaultStyledDocument doc;
    
    int maxLines = 1000;
    
    Color labelColor = new Color(77,255,195);    	// Label Color
   	Color backgroundColor = new Color(41,41,41);		// Background Color
   	
    Color[] keywordColor = {new Color(255,140,0), 
    							new Color(255,215,0),
    							new Color(255,0,0),
    							new Color(0,250,0),
    							new Color(0,51,255),
    							new Color(240,191,35)};
    
    Color numericalColor = new Color(0,191,255);
    
    private String[][] strKeywords = { {"SIMULATION", "Simulation", "Simulator", "RUN","simulation"}, 		// Keywords
    									   {"Read","READ","Reading", "Write", "WRITE","Writing","Action", "Update", "Updated", "complete", "completed", "Warning", "WARNING", "warning"},							 	// File/Info Read/Write processes
    									   {"Error","ERROR","error"},							  					// Errors and warnings
    									   {"Start","start","START","Launch","LAUNCH","launch"},	  					// Process start ups 
    									   {"Propulsion","PROP"},
    									   {"AERO","Aerodynamic"}};								// Propulsion data 
	 
    private int ID=3; 
    protected int type=3;
    
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    public ConsoleClass () {
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BorderLayout());

        final StyleContext cont = StyleContext.getDefaultStyleContext();
         List<AttributeSet> attrList = new ArrayList<AttributeSet>();
         for(int i=0;i<strKeywords.length;i++) { 
        	 	AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, keywordColor[i]);
        	 	attrList.add(attr);
         }
         AttributeSet numAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, numericalColor);
         
        final AttributeSet attrLabel = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, labelColor);

        String[] strKey = new String[strKeywords.length];
        for(int i=0;i<strKeywords.length;i++) { 
            strKey[i] = "(\\W)*(";
            	for (int j=0;j<strKeywords[i].length;j++) { if(j<strKeywords[i].length-1) {strKey[i] += strKeywords[i][j] +"|";} else {strKey[i] += strKeywords[i][j];}}
            strKey[i] += ")";
        	}
        
         doc = new DefaultStyledDocument(){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                int length = getLength();
                super.insertString(length, str, a);
                int thisOffset = length;

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, thisOffset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, thisOffset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    	boolean matchFound = false;
                    	// Check for Keywords:
                    	for(int i=0;i<strKeywords.length;i++) { 
                    			if (text.substring(wordL, wordR).matches(strKey[i])) {
                                    setCharacterAttributes(wordL, wordR - wordL, attrList.get(i), false);
        	                        		matchFound = true; 
                        		} 
                    	}
                    	// Check for numerical data:
        		        boolean numeric = true;
        		        String testString="";
        		        try {
        		        		 testString = text.substring(wordL, wordR);
        		            @SuppressWarnings("unused")
							Double num = Double.parseDouble(testString);
        		        } catch (NumberFormatException e) {
        		            numeric = false;
        		        }
        		        if(numeric) {
                        setCharacterAttributes(wordL, wordR - wordL, numAttr, false);
                        matchFound = true;
        		        } 
        		        // If no keywords found > set standard:
                    	if(matchFound == false) {
                       setCharacterAttributes(wordL, wordR - wordL, attrLabel, false);
                    	}
                        wordL = wordR;
                    	}
                    wordR++;
                }
               // textPane.setCaretPosition(0);
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);
                boolean matchFound = false;
            		for(int i=0;i<strKeywords.length;i++) { 
	            		if (text.substring(before, after).matches(strKey[i])) {
	            			setCharacterAttributes(before, after - before, attrList.get(i), false);
	            			matchFound = true;
	            		} 
            		}
            		if(matchFound == false) {
	                    setCharacterAttributes(before, after - before, attrLabel, false);
	            }
            }
        };
        
        con=new PrintStream(new TextAreaOutputStream(doc, maxLines));

        //System.setOut(new PrintStream(taOutputStream));  
        textPane = new JTextPane(doc);
        textPane.setBackground(backgroundColor);
        textPane.setEditable(false);
        mainPanel.add(new JScrollPane(textPane));
    }
    

    public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
		textPane.setForeground(labelColor);
		textPane.revalidate();
		textPane.repaint();
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		textPane.setBackground(backgroundColor);
		textPane.revalidate();
		textPane.repaint();
	}
	
	
	
	public DefaultStyledDocument getDoc() {
		return doc;
	}

	public void setDoc(DefaultStyledDocument doc) {
		this.doc = doc;
		textPane.removeAll();
		textPane.setDocument(doc);
		
		this.textPane.revalidate();
		this.textPane.repaint();
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}

	@SuppressWarnings("resource")
	public void setCON(PrintStream  con) {

		this.textPane.revalidate();
		this.textPane.repaint();
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}
	
	public void linkConPrintOut() {
        System.setOut(con);
	}
	
	public void linkConPrintErr() {
        System.setErr(con);
	}

	public PrintStream getCON() {
		return con;
	}

	@Override
	public int getID() {
		return ID;
	}
	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override 
	public void refresh() {

	}

/**
 * 
 * 
 * Unit Tester 
 * @param args
 */
	public static void main (String args[]) {
    	
		JFrame frame = new JFrame("Component Tester - Console ");
		frame.setSize(450,450);
		frame.setLayout(new BorderLayout());
        ConsoleClass console = new ConsoleClass();
        frame.add(console.getMainPanel());
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
        
		for(int i=0;i<10;i++) {
			System.out.println("Output 2.215>55 test> Reading "+i+" protected ");
		}
		
		for(int i=100;i>0;i--) {
			System.out.println("INPUT test> Reading "+i+" error read ");
		}
    }
    
    
}