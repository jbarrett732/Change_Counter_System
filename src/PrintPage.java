import javax.swing.*;
import java.awt.*;


import java.sql.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


//hello
//CLASS FOR DIAPLAYING FILES 
public class PrintPage extends JFrame {

	private File file1 = null;
	private File file2 = null;
	private String operation = "";

	//CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
	public PrintPage() {}
	public PrintPage(String op, String filePath, String filePath2) {

		//set up window
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 600);
		setBackground(Color.WHITE);
		setTitle("Print Page: " + op);

		file1 = new File(filePath);
		file2 = new File(filePath2);
		operation = op;

		//determine operation and execute
		if(!file1.exists()) {
			//error, file doesn't exist 
			JOptionPane.showMessageDialog(this, "The file \""+file1.getName()+"\" does not exist!", "Missing File", JOptionPane.ERROR_MESSAGE);
		} else if(op.equals("Listing")) { 
			addContent(printListing());
		} else if(op.equals("Changes")) {
		 	addContent(printChange());	
		} else if(op.equals("Report")) {
			addContent(printReport());
		} else {
			//error, not valid operation
			JOptionPane.showMessageDialog(this, "The operation \""+op+"\" is an invalid operation!", "Invalid Operation", JOptionPane.ERROR_MESSAGE);
		}

		//display window
		setVisible(true);
       	}


	//ADDS CONTENT TO PAGE 
	public void addContent(ArrayList<String> fileString) {

		//create page heading panel
		String info = ""; 
		if(operation.equals("Report") || operation.equals("Changes")) {
			int numOld = lineCount(file1);
			int numNew = lineCount(file2);
			info = "File Name: " + file2.getName() + "<br>";
			info += "Total Lines: " + Integer.toString(numNew) + "<br>"; 
			info += "Lines Added: " + Integer.toString(numNew-numOld) + "<br>"; 
			info += "Recent Change: " + (new Date(file2.lastModified())).toString() + "<br>";
			info += "Previous Change: " + (new Date(file1.lastModified())).toString() + "<br>";
		} else {
			info = "File Name: " + file1.getName() + "<br>";
			info += "Total Lines: " +  Integer.toString(lineCount(file1)) + "<br>"; 
			info += "Recent Change: " + (new Date(file1.lastModified())).toString() + "<br>";
		}
		JLabel heading = new JLabel("<html>" + info + "</html>");

		//create panel to add content to it
		JPanel file_content = numberContent(fileString);

		file_content.setBackground(Color.WHITE);
	  	JScrollPane scroll = new JScrollPane(file_content);	
		scroll.setPreferredSize(new Dimension(500, 500));

		//add panel to window
		add(heading, BorderLayout.NORTH);	
		add(scroll, BorderLayout.WEST);	
    	}

	public JPanel numberContent(ArrayList<String> slist) {
 		JPanel file_content = new JPanel(new GridBagLayout());
                GridBagConstraints fc = new GridBagConstraints();
                fc.anchor = GridBagConstraints.LINE_START;
                fc.gridx = 0; fc.gridy = 0;

		Iterator<String> it = slist.iterator();
		int i;
		for(i=1; it.hasNext(); i++) {
			String cdl = "<html>" + it.next() + "</html>";
			JLabel label = new JLabel(cdl);
			label.setPreferredSize(new Dimension(500, 20));
			fc.gridy = i-1;
			file_content.add(label, fc);
		}
		
		return file_content;
	}

	//PRINTS ONLY CONTENT OF FILE
        public ArrayList<String> printListing() {

		//read file into array list and catch exceptions
		ArrayList<String> l = null;
		try {
		 	l = readFile(file1);
		} catch(IOException ioe) {
			//error
		}

		//turn list into on long string
		if(!l.isEmpty()) {
			int i = l.size();
			for(i=0; i<l.size(); i++) {
				l.set(i, "<font color='black'>"+Integer.toString(i+1)+"&nbsp;&nbsp;&nbsp;&nbsp;"+l.get(i)+"</font>");
			}	
		}

		return l;
 	}


	//public String printReport() {
	public ArrayList<String> printReport() {

		//read file into array list and catch exceptions
		ArrayList<String> rt_lines   = new ArrayList<String>();
		ArrayList<String> added_lines   = null;
		ArrayList<String> removed_lines = null;
		added_lines   = differ(file1, file2);
		removed_lines = differ(file2, file1);

		//turn list into on long string
		if(!added_lines.isEmpty()) {
			int i;
			for(i=0; i<added_lines.size(); i++) {
				rt_lines.add("<font color='green'>"+added_lines.get(i)+"</font>");
			}	
		}
		if(!removed_lines.isEmpty()) {
			int i;
			for(i=0; i<removed_lines.size(); i++) {
				rt_lines.add("<font color='red'>"+removed_lines.get(i)+"</font>");
			}	
		}

		return rt_lines;
	}


	public ArrayList<String>printChange() {

		//read file into array list and catch exceptions
		ArrayList<String> all_lines   = null; 
		try {
			all_lines = readFile(file2);	
		} catch(IOException ioe) {
			//error
		}
		ArrayList<String> other_lines   = null; 
		try {
			other_lines = readFile(file1);	
		} catch(IOException ioe) {
			//error
		}
		ArrayList<String> added_lines   = null;
		ArrayList<String> removed_lines = null;
		added_lines   = differ(file1, file2);
		removed_lines = differ(file2, file1);

		int j;
		for(j=0; j<all_lines.size(); j++) {
			all_lines.set(j,Integer.toString(j+1)+"&nbsp;&nbsp;&nbsp;&nbsp;"+all_lines.get(j));
			int k;
			for(k=0; k<added_lines.size(); k++) {
				String str_1 = all_lines.get(j).substring(all_lines.get(j).indexOf("&"));
				String str_2 = added_lines.get(k).substring(added_lines.get(k).indexOf("&"));
				if(str_1.equals(str_2)) {
					all_lines.set(j,"<font color='green'>"+added_lines.get(k)+"</font>");
				}
			}
		}	

		for(j=other_lines.size()-1; j>-1; j--) {
			other_lines.set(j,Integer.toString(j+1)+"&nbsp;&nbsp;&nbsp;&nbsp;"+other_lines.get(j));
			int k;
			for(k=0; k<removed_lines.size(); k++) {
				String str_1 = other_lines.get(j).substring(other_lines.get(j).indexOf("&"));
				String str_2 = removed_lines.get(k).substring(removed_lines.get(k).indexOf("&"));
				if(str_1.equals(str_2)) {
					all_lines.add(j,"<font color='red'>"+removed_lines.get(k)+"</font>");
				}
			}
		}	

		return all_lines;
	}


	//READS FILE INTO AN ARRAY BY LINE 
	public ArrayList<String> readFile(File file) throws IOException {

		//create input stream for reading file
		FileInputStream fin = new FileInputStream(file);
		BufferedReader read = new BufferedReader(new InputStreamReader(fin));
		
		//read file into array list by line
		ArrayList<String> list = new ArrayList<String>();
		String line = null;
		while((line = read.readLine()) != null) {
			list.add(line);
		}
		return list;
	}
 

	//RETURNS ALL NON-MATCHING LINES 
	//use differ(old,new) for added/changed lines
	//use differ(new,old) for removed lines
	public ArrayList<String> differ(File prev, File curr) {

		//create file for added/changes lines
		ArrayList<String> added = new ArrayList<String>();

		//iterate over the lines of newer file
		Iterator<String> new_file = null; 
		try {
			new_file = readFile(curr).iterator();	
		} catch(IOException ioe) { 
			//error
		}

		int j;
		for(j=1; new_file.hasNext(); j++) {
			boolean match = false;
			String new_line = new_file.next();

			//iterate over the lines of older file
			Iterator<String> old_file = null;
			try {
				old_file = readFile(prev).iterator();	
			} catch(IOException ioe) {
				//error
			}

			while(old_file.hasNext()) {
				String old_line = old_file.next();	
				if(old_line.equals(new_line)) {
					//line is unchanged
					match = true;
					break;
				}
			}
			
			//add new line to return array
			if(!match) {
				added.add(Integer.toString(j)+"&nbsp;&nbsp;&nbsp;&nbsp;"+new_line);	
			} 
		}
		return added;
	}

	
	//COUNTS THE NUMBER OF LINES IN A FILE
	public int lineCount(File f) {
		int lines = 0;
		ArrayList<String> list = null;
		try {
			list = readFile(f);	
		} catch(IOException ioe) {
			//error
		}
		return list.size();
	}


	
	//DRIVER FOR CLASS TESTING
	public static void main (String args[]) {

		//supported operations so far "Listing", "Numbered Listing"
		if(args.length == 3)
			new PrintPage(args[0], args[1], args[2]);
	}
}
