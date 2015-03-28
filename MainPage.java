import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.sql.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Enumeration;


//CLASS FOR EXECUTING OPERATIONS ON FILES 
public class MainPage extends JFrame {

	private File location = null;
	private ButtonGroup file_selection = new ButtonGroup();
	private HashMap<String, File> file_map = new HashMap<String, File>();

	//CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
	public MainPage(String dirPath) {

		//set up window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 600);
		setBackground(Color.WHITE);
		setTitle("Main Page");

		location = new File(dirPath);

		//determine operation and execute
		if(!location.exists()) {
			//error, direcotry doesn't exist 
		}
		else if(!location.isDirectory()) {
			//error, path is no a directory 
		}
		else {
			//success, add content to main page
			addContent();
		}

		//display window
		setVisible(true);
       	}


 	//ADDS CONTENT TO WINDOW
	public void addContent() {

		//create panel for file list 
 		JPanel file_panel = new JPanel(new GridBagLayout());
		file_panel.setBackground(Color.WHITE);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;

		//create title for file list
		String title_string = "<html>"+ location.getName();
		title_string += "<br><hr size=2></html>";
		JLabel title = new JLabel(title_string);
		title.setFont(new Font("Serif", Font.BOLD, 18));
		file_panel.add(title, c);

		//create selection button for each file in the list 
		c.gridy = 1;
		File[] file_list = location.listFiles();
		for(File item : file_list) {
			file_map.put(item.getName(), item);
			JRadioButton button = new JRadioButton(item.getName());
			file_selection.add(button);	
			c.gridy += 1;
			file_panel.add(button, c);
		}

		//add action buttons 
		JButton button_listing = new JButton("Print Program Listing");
        	button_listing.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//new PrintPage("Listing","/Users/johnbarrett/Desktop/GradSchool/SE580/Code/Change_Counter_System/file.txt");
				String s = getSelectedItem();
				if(s != null) 
					new PrintPage("Listing", file_map.get(s).getAbsolutePath());
            		}
        	});
		c.gridy += 1;
		file_panel.add(button_listing, c);

		//add panel to window
		add(file_panel, BorderLayout.WEST);	
    	}

	private String getSelectedItem() {
		String selected = null;
		//Enumeration<JRadioButton> b_enum = file_selection.getElements();
		Enumeration b_enum = file_selection.getElements();
		while(b_enum.hasMoreElements()) {
			JRadioButton b = (JRadioButton) b_enum.nextElement();
			if(b.isSelected()) {
				selected = b.getText();	
				break;
			}
		}
		return selected;
	}

	
	//DRIVER FOR CLASS TESTING
	public static void main (String args[]) {

		if(args.length == 1)
			new MainPage(args[0]);
	}
}
