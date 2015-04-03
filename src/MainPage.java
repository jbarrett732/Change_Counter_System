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
	private ButtonGroup dir_selection  = new ButtonGroup();
	private HashMap<String, File> file_map = new HashMap<String, File>();
	private HashMap<String, File> dir_map  = new HashMap<String, File>();
	private JPanel left_panel;
	private GridBagConstraints c = new GridBagConstraints();

	//CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
	public MainPage(String dirPath) {

		//set up window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
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
 		left_panel = new JPanel(new GridBagLayout());
		left_panel.setBackground(Color.WHITE);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;

		createListItems(location);

		//add action buttons 
		JButton button_add = new JButton("<html>Add Version<br>To Database</html>");
        	button_add.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//TODO
            		}
        	});
		JButton button_delete = new JButton("<html>Delete Version<br>From Database</html>");
        	button_delete.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//TODO
            		}
        	});
		JButton button_listing = new JButton("<html>Print Program<br>Listing</html>");
        	button_listing.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				String s = getSelectedItem(false);
				if(s != null) 
					new PrintPage("Listing", file_map.get(s).getAbsolutePath(), "");
            		}
        	});
		JButton button_changes = new JButton("<html>Print Listing<br>With Changes</html>");
        	button_changes.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				String s = getSelectedItem(false);
				//if(s != null) 
				//	new PrintPage("Changes", file_map.get(s).getAbsolutePath(), file_map.get(s).getAbsolutePath());
            		}
        	});
		JButton button_report = new JButton("<html>Print Program<br>Change Report</html>");
        	button_report.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				String s = getSelectedItem(false);
				//if(s != null) 
				//	new PrintPage("Report", file_map.get(s).getAbsolutePath() , prev version);
            		}
        	});

		button_add.setPreferredSize(new Dimension(140, 50));
		button_delete.setPreferredSize(new Dimension(140, 50));
		button_listing.setPreferredSize(new Dimension(140, 50));
		button_changes.setPreferredSize(new Dimension(140, 50));
		button_report.setPreferredSize(new Dimension(140, 50));

		JPanel button_panel1 = new JPanel();
		button_panel1.setBackground(Color.WHITE);
		button_panel1.setPreferredSize(new Dimension(350, 60));
		button_panel1.add(button_add, BorderLayout.WEST);	
		button_panel1.add(button_delete, BorderLayout.EAST);	
		
		JPanel button_panel2 = new JPanel();
		button_panel2.setBackground(Color.WHITE);
		button_panel2.setPreferredSize(new Dimension(350, 60));
		button_panel2.add(button_listing, BorderLayout.WEST);	
		button_panel2.add(button_changes, BorderLayout.EAST);	

		JPanel button_panel3 = new JPanel();
		button_panel3.setBackground(Color.WHITE);
		button_panel3.setPreferredSize(new Dimension(350, 60));
		button_panel3.add(button_report, BorderLayout.WEST);	

		c.gridy += 1;
		left_panel.add(button_panel1, c);
		c.gridy += 1;
		left_panel.add(button_panel2, c);
		c.gridy += 1;
		left_panel.add(button_panel3, c);

		//add panel to window
		add(left_panel, BorderLayout.WEST);	

		//add header to window
		String head_string = "<html>Change Counter System<br></html>";
		JLabel head_title = new JLabel(head_string);
		head_title.setFont(new Font("Serif", Font.BOLD, 32));
		add(head_title, BorderLayout.NORTH);
    	}


	private void createListItems(File loc) {
		
		GridBagConstraints f = new GridBagConstraints();
		f.anchor = GridBagConstraints.LINE_START;
		f.gridx = 0;
		f.gridy = 0;
		GridBagConstraints d = new GridBagConstraints();
		d.anchor = GridBagConstraints.LINE_START;
		d.gridx = 0;
		d.gridy = 0;

		//create title for file list
		String title_string = "<html>"+ loc.getName();
		title_string += "<br><hr size=2></html>";
		JLabel title = new JLabel(title_string);
		title.setFont(new Font("Serif", Font.BOLD, 24));
		left_panel.add(title, c);

		//create selection button for each file in the list 
 		JPanel file_panel = new JPanel(new GridBagLayout());
 		JPanel dir_panel  = new JPanel(new GridBagLayout());
		File[] file_list = loc.listFiles();
		for(File item : file_list) {
			if(item.isDirectory()) {
				dir_map.put(item.getName(), item);
				JRadioButton button = new JRadioButton(item.getName());
				dir_selection.add(button);	
				d.gridy += 1;
				dir_panel.add(button, d);
			} else if(item.isFile()) {
				file_map.put(item.getName(), item);
				JRadioButton button = new JRadioButton(item.getName());
				file_selection.add(button);	
				f.gridy += 1;
				file_panel.add(button, f);
			}
		}
		JScrollPane scroll_dir  = new JScrollPane(dir_panel);
		JScrollPane scroll_file = new JScrollPane(file_panel);
		scroll_dir.setPreferredSize(new Dimension(350, 150));
		scroll_file.setPreferredSize(new Dimension(350, 150));

		//create heading for file scroll pane
		String title_file = "<html>Files</html>";
		JLabel file_label = new JLabel(title_file);
		file_label.setFont(new Font("Serif", Font.BOLD, 18));
		JPanel file_heading_panel = new JPanel();
		file_heading_panel.setPreferredSize(new Dimension(350, 40));
		file_heading_panel.add(file_label);

		c.gridy = 1;
		left_panel.add(createFolderNavigation(), c);
		c.gridy = 2;
		left_panel.add(scroll_dir,  c);
		c.gridy = 3;
		left_panel.add(file_heading_panel, c);
		c.gridy = 4;
		left_panel.add(scroll_file, c);

		return;
	}

	private JPanel createFolderNavigation() {

		//create heading for directory scroll pane
		String title_dir = "<html>Folders</html>";
		JLabel dir_label = new JLabel(title_dir);
		dir_label.setFont(new Font("Serif", Font.BOLD, 18));

		//add action buttons 
		JButton button_back = new JButton("Back");
        	button_back.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//TODO
            		}
        	});
		JButton button_open = new JButton("Open");
        	button_open.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//TODO
            		}
        	});
		button_back.setPreferredSize(new Dimension(80, 30));
		button_open.setPreferredSize(new Dimension(80, 30));

		JPanel dir_heading_panel = new JPanel(new GridBagLayout());
		dir_heading_panel.setPreferredSize(new Dimension(350, 40));
		GridBagConstraints dh = new GridBagConstraints();
		dh.anchor = GridBagConstraints.LINE_START;
		dh.gridx = 0; 
		dh.gridy = 0;

		dir_heading_panel.add(dir_label, dh);
		dh.gridx = 1; 
		dir_heading_panel.add(button_back, dh);
		dh.gridx = 2; 
		dir_heading_panel.add(button_open, dh);

		return dir_heading_panel;
	}

	private String getSelectedItem(boolean directoryItem) {
		String selected = null;
		Enumeration b_enum;
		if(directoryItem) {
			b_enum = dir_selection.getElements();
		} else {
			b_enum = file_selection.getElements();
		}
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
