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


//CLASS FOR DIAPLAYING FILES 
public class PrintPage extends JFrame {

	private File file1 = null;
	private File file2 = null;

	//CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
	public PrintPage(String op, String filePath, String filePath2) {

		//set up window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 600);
		setBackground(Color.WHITE);
		setTitle("Print Page: " + op);

		file1 = new File(filePath);
		file2 = new File(filePath2);

		//determine operation and execute
		if(!file1.exists()) {
			//error, file doesn't exist 
		} else if(op.equals("Listing")) { 
			addContent(printListing());
		} else if(op.equals("Change")) {
			//TODO - cycle 2
		} else if(op.equals("Report")) {
			addContent(printReport());
		} else {
			//error, not valid operation
		}

		//display window
		setVisible(true);
       	}


	//ADDS CONTENT TO PAGE 
	public void addContent(String fileString) {

		//create page heading panel
		String info = "File Name: " + file1.getName() + "<br>";
		info += "Last Modified: " + (new Date(file1.lastModified())).toString();
		JLabel heading = new JLabel("<html>" + info + "</html>");

		//create panel to add content to it
 		JTextPane file_content = new JTextPane();
 		file_content.setEditable(false);
 		file_content.setText(fileString);

		file_content.setBackground(Color.WHITE);
		//file_content.add(content);
	  	JScrollPane scroll = new JScrollPane(file_content);	
		scroll.setPreferredSize(new Dimension(500, 500));

		//add panel to window
		add(heading, BorderLayout.NORTH);	
		add(scroll, BorderLayout.WEST);	
    	}


	//PRINTS ONLY CONTENT OF FILE
        public String printListing() {

		//read file into array list and catch exceptions
		ArrayList<String> l = null;
		try {
		 	l = readFile(file1);
		} catch(IOException ioe) {}

		//turn list into on long string
		String fileContent = "";
		if(!l.isEmpty()) {
			Iterator<String> it = l.iterator();
			while(it.hasNext()) {
				fileContent += it.next() + "\n";
			}	
		}

		return fileContent;
 	}


	public String printReport() {

		//read file into array list and catch exceptions
		ArrayList<String> added_lines   = null;
		ArrayList<String> removed_lines = null;
		added_lines   = differ(file1, file2);
		removed_lines = differ(file2, file1);

		//turn list into on long string
		String fileContent = "New and Changed Lines:\n";
		if(!added_lines.isEmpty()) {
			Iterator<String> it1 = added_lines.iterator();
			while(it1.hasNext()) {
				fileContent += it1.next() + "\n";
			}	
		}
		fileContent += "\nRemoved Lines:\n";
		if(!removed_lines.isEmpty()) {
			Iterator<String> it2 = removed_lines.iterator();
			while(it2.hasNext()) {
				fileContent += it2.next() + "\n";
			}	
		}

		return fileContent;
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
		} catch(IOException ioe) {}

		while(new_file.hasNext()) {
			boolean match = false;
			String new_line = new_file.next();

			//iterate over the lines of older file
			Iterator<String> old_file = null;
			try {
				old_file = readFile(prev).iterator();	
			} catch(IOException ioe) {}

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
				added.add(new_line);	
			}
		}
		return added;
	}


	
	//DRIVER FOR CLASS TESTING
	public static void main (String args[]) {

		//supported operations so far "Listing", "Numbered Listing"
		if(args.length == 3)
			new PrintPage(args[0], args[1], args[2]);
	}
}
