import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//import java.sql.Date;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.BufferedWriter;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


//CLASS FOR EXECUTING OPERATIONS ON FILES 
public class MainPage extends JFrame {

	private File location  = null;
	private File curr_path = null;
	private ButtonGroup file_selection = new ButtonGroup();
	private ButtonGroup dir_selection  = new ButtonGroup();
	private HashMap<String, File> file_map = new HashMap<String, File>();
	private HashMap<String, File> dir_map  = new HashMap<String, File>();
	private JPanel left_panel;
	private JPanel right_panel;
 	private JPanel file_panel = new JPanel(new GridBagLayout());
 	private JPanel dir_panel  = new JPanel(new GridBagLayout());
 	private JPanel changes_panel  = new JPanel(new GridBagLayout());
	private JLabel title = new JLabel();
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints r = new GridBagConstraints();
	private	Color background_color = new Color(204,229,255);
	private	Color border_color = new Color(128,128,128);

	//CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
	public MainPage(String dirPath) {

		//set up window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(730, 740);
		setBackground(border_color);
		setTitle("Main Page");

		location = new File(dirPath);
		curr_path = location;

		//determine operation and execute
		if(!location.exists()) {
			//error, direcotry doesn't exist 
		}
		else if(!location.isDirectory()) {
			//error, path is no a directory 
		}
		else {
			//success, add content to main page
			addContent_leftPanel(curr_path);
			addContent_rightPanel();
		}

		//display window
		setVisible(true);
       	}


 	//ADDS CONTENT TO WINDOW
	public void addContent_leftPanel(File path) {

		//create panel for file list 
 		left_panel = new JPanel(new GridBagLayout());
		left_panel.setBackground(background_color);
		left_panel.setBorder(new LineBorder(border_color, 5));
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;

		createListItems(path);

		//add panel to window
		add(left_panel, BorderLayout.WEST);	

		//add header to window
		String head_string = "Change Counter System\n";
		JLabel head_title = new JLabel(head_string);
		head_title.setFont(new Font("Serif", Font.BOLD, 32));
		head_title.setForeground(background_color);
		JPanel head_title_panel = new JPanel();
		head_title_panel.add(head_title);
		head_title_panel.setBackground(border_color);
		
		add(head_title_panel, BorderLayout.NORTH);
    	}

 	//ADDS CONTENT TO WINDOW
	public void addContent_rightPanel() {

		//create panel for file list 
 		right_panel = new JPanel(new GridBagLayout());
		right_panel.setBackground(background_color);
		right_panel.setBorder(new LineBorder(border_color, 5));
		r.anchor = GridBagConstraints.LINE_START;
		r.gridx = 0;
		r.gridy = 0;

		String changes_string = "<html>Recent Changes<br></html>";
		JLabel changes_title = new JLabel(changes_string);
		changes_title.setFont(new Font("Serif", Font.BOLD, 24));
		right_panel.add(changes_title, r);

		updateChanges();

		changes_panel.setBackground(Color.WHITE);
		changes_panel.setPreferredSize(new Dimension(335,560));
		JScrollPane scroll_changes = new JScrollPane(changes_panel);
		scroll_changes.setPreferredSize(new Dimension(350, 565));
		r.gridy = 1;
		right_panel.add(scroll_changes, r);

		//add panel to window
		add(right_panel, BorderLayout.EAST);	
	}

	public void addCommentDialog(String name_file) {
		
		//create dialog window to get user input
		String comment = (String)JOptionPane.showInputDialog(this, "Add Comments for "+name_file, "Add Comments", JOptionPane.PLAIN_MESSAGE);

		//comment operation cancelled
		if(comment == null)
			return;

		//write user input in comments file with date and time
		try {
			File comment_file = new File(location.getAbsolutePath() + "/comments.txt");
			if(!comment_file.exists()) {
				comment_file.createNewFile();
			}

			//add date, time, and file name to comment line
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			comment = dateFormat.format(date) + " " + name_file + " " + comment + "\n";

			//append comment to comment file
			FileWriter fileWriter = new FileWriter(comment_file.getAbsolutePath(), true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(comment);
			bufferedWriter.close();

		} catch(IOException ioe) {}
	}

	public void addVersion(File curr_f, File new_f) throws IOException {

		//create streams to copy file contents
		InputStream input = null;
    		OutputStream output = null;
    		try {
        		input = new FileInputStream(curr_f);
        		output = new FileOutputStream(new_f);
        		byte[] buf = new byte[1024];
        		int bytesRead;
        		while ((bytesRead = input.read(buf)) > 0) {
            			output.write(buf, 0, bytesRead);
        		}
    		} finally {
        		input.close();
        		output.close();
    		}
		
	}

	private void createListItems(File loc) {

		//create title for file list
		updateListTitle(loc); 
		left_panel.add(title, c);

		//create selection button for each file in the list 
		updateLists(loc); 

		//dir_panel.setPreferredSize(new Dimension(340, dir_panel.getHeight()));
		//file_panel.setPreferredSize(new Dimension(340, file_panel.getHeight()));
		dir_panel.setBackground(Color.WHITE);
		file_panel.setBackground(Color.WHITE);
		JScrollPane scroll_dir  = new JScrollPane(dir_panel);
		JScrollPane scroll_file = new JScrollPane(file_panel);
		scroll_dir.setPreferredSize(new Dimension(350, 150));
		scroll_file.setPreferredSize(new Dimension(350, 150));

		//add components
		c.gridy = 1;
		JPanel box_folder = new JPanel(new GridBagLayout());
		box_folder.setBorder(new TitledBorder(new LineBorder(Color.black, 2), "Folders"));
		box_folder.setBackground(background_color);
		GridBagConstraints bf1 = new GridBagConstraints();
		bf1.gridx = 1; bf1.gridy = 1;
		box_folder.add(createFolderNavigation(), bf1);	
		bf1.gridy = 2;
		box_folder.add(scroll_dir, bf1);	
		left_panel.add(box_folder, c);

		c.gridy = 2;
		JPanel box_file = new JPanel(new GridBagLayout());
		box_file.setBorder(new TitledBorder(new LineBorder(Color.black, 2), "Files"));
		box_file.setBackground(background_color);
		GridBagConstraints bf2 = new GridBagConstraints();
		bf2.gridx = 1; bf2.gridy = 1;
		box_file.add(scroll_file, bf2);	
		bf2.gridy = 2;
		box_file.add(createActionButtons(), bf2);

		left_panel.add(box_file, c);

		return;
	}

	private void updateListTitle(File loc) {
		String title_string = "<html>"+ loc.getName() + "<br></html>";
		title.setText(title_string);
		title.setFont(new Font("Serif", Font.BOLD, 24));
	}

	private void updateLists(File loc) {

		//create positioning elements
		GridBagConstraints f = new GridBagConstraints();
		f.anchor = GridBagConstraints.LINE_START;
		f.gridx = 0; f.gridy = 0;
		GridBagConstraints d = new GridBagConstraints();
		d.anchor = GridBagConstraints.LINE_START;
		d.gridx = 0; d.gridy = 0;

		//remove all references to old content
		dir_panel.removeAll();
		file_panel.removeAll();
		file_map.clear(); 
		dir_map.clear();  
		dir_selection = new ButtonGroup(); 
		file_selection = new ButtonGroup(); 

		//add contents from path in proper group
		File[] file_list = loc.listFiles();
		for(File item : file_list) {
			if(item.isDirectory()) {
				dir_map.put(item.getName(), item);
				JRadioButton button = new JRadioButton(item.getName());
				button.setPreferredSize(new Dimension(330, 20));
				dir_selection.add(button);	
				d.gridy += 1;
				dir_panel.add(button, d);
			} else if(item.isFile()) {
				file_map.put(item.getName(), item);
				JRadioButton button = new JRadioButton(item.getName());
				button.setPreferredSize(new Dimension(330, 20));
				file_selection.add(button);	
				f.gridy += 1;
				file_panel.add(button, f);
			}
		}

		//update ui component
		dir_panel.revalidate();
		dir_panel.repaint();
		file_panel.revalidate();
		file_panel.repaint();
	}

	private void updateChanges() {

		//remove all content
		changes_panel.removeAll();

		//read comments file and add contents to scroll
		File file = new File(location.getAbsolutePath() + "/comments.txt");
		if(!file.exists()) {
			return;
		}

                ArrayList<String> list = new ArrayList<String>();
		try {
                	//create input stream for reading file
                	FileInputStream fin = new FileInputStream(file);
                	BufferedReader read = new BufferedReader(new InputStreamReader(fin));       
                
                	//read file into array list by line
  	        	String line = null;
                	while((line = read.readLine()) != null) {
                		list.add(line);
                	} 
		} catch(IOException ioe) {}

		GridBagConstraints cp = new GridBagConstraints();
		cp.anchor = GridBagConstraints.LINE_START;
		cp.gridx = 0; cp.gridy = 0;

		//reads list from end to begining into a long string
		String all_changes = "";
		int index; 
		for(index = list.size()-1; index >= 0; index--) {
			JLabel temp_change = new JLabel(list.get(index));		
			temp_change.setPreferredSize(new Dimension(335, 20));
			changes_panel.add(temp_change, cp);		
			cp.gridy += 1;
		} 

		//update ui
		changes_panel.revalidate();
		changes_panel.repaint();
	}

	private JPanel createFolderNavigation() {

		//add action buttons 
		JButton button_back = new JButton("Back");
        	button_back.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				if(!(curr_path.getAbsolutePath()).equals(location.getAbsolutePath())) {
					File new_path = curr_path.getParentFile();
					if(new_path != null) {
						curr_path = new_path;
						updateListTitle(curr_path); 
						updateLists(curr_path); 
					}
				}
            		}
        	});
		JButton button_open = new JButton("Open");
        	button_open.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				curr_path = dir_map.get(getSelectedItem(true)); 
				updateListTitle(curr_path); 
				updateLists(curr_path); 
            		}
        	});
		button_back.setPreferredSize(new Dimension(80, 30));
		button_open.setPreferredSize(new Dimension(80, 30));

		//make area to add buttons with position elements
		JPanel dir_heading_panel = new JPanel(new GridBagLayout());
		dir_heading_panel.setPreferredSize(new Dimension(350, 40));
		dir_heading_panel.setBackground(background_color);	
		GridBagConstraints dh = new GridBagConstraints();
		dh.anchor = GridBagConstraints.LINE_START;
		dh.gridx = 0; 
		dh.gridy = 0;

		//add components
		dir_heading_panel.add(button_back, dh);
		dh.gridx = 1; 
		dir_heading_panel.add(button_open, dh);

		return dir_heading_panel;
	}

	private String getSelectedItem(boolean directoryItem) {

		//find selected component and return string
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

 	private JPanel createActionButtons() {

		//add action buttons 
		JButton button_add = new JButton("<html>Add Version<br>To Database</html>");
        	button_add.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//make directory for older versions if doesn't exist
				File backup = new File(location.getAbsolutePath() +"/backup");
				if(!backup.exists()) {
					backup.mkdir();
				}

				//copy version to backup directory
				int v = 1;
				String s = getSelectedItem(false);
				File curr_file = file_map.get(s);
				try {
					File new_file = new File(location.getAbsolutePath() + "/backup/" + Integer.toString(v) + "_" + curr_file.getName()); 
					for(v=2; !new_file.createNewFile(); v++) {
						new_file = new File(location.getAbsolutePath() + "/backup/" + Integer.toString(v) + "_" + curr_file.getName()); 
					}
					addCommentDialog(curr_file.getName());
					addVersion(curr_file, new_file); 	
            			} 
				catch (IOException ioe) {}

				updateLists(curr_path); 
				updateChanges();
			}
        	});
		JButton button_delete = new JButton("<html>Delete Version<br>From Database</html>");
        	button_delete.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				//TODO
				String s = getSelectedItem(false);
				File file = file_map.get(s);
				file.delete();	
				updateLists(curr_path); 
            		}
        	});
		JButton button_listing = new JButton("<html>Print Program<br>Listing</html>");
        	button_listing.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				String s = getSelectedItem(false);
				if(s != null) { 
					new PrintPage("Listing", file_map.get(s).getAbsolutePath(), "");
				}
            		}
        	});
		JButton button_changes = new JButton("<html>Print Listing<br>With Changes</html>");
        	button_changes.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				String s = getSelectedItem(false);
				if(s != null) { 
					File prev = getPreviousFile(file_map.get(s).getName()); 
					if(prev != null)
						new PrintPage("Changes", prev.getAbsolutePath(), file_map.get(s).getAbsolutePath());
				}
            		}
        	});
		JButton button_report = new JButton("<html>Print Program<br>Change Report</html>");
        	button_report.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				String s = getSelectedItem(false);
				if(s != null) { 
					File prev = getPreviousFile(file_map.get(s).getName()); 
					if(prev != null)
						new PrintPage("Report", prev.getAbsolutePath(), file_map.get(s).getAbsolutePath());
						//new PrintPage("Report", file_map.get(s).getAbsolutePath(), prev.getAbsolutePath());
				}
            		}
        	});

		//set size of buttons
		button_add.setPreferredSize(new Dimension(140, 50));
		button_delete.setPreferredSize(new Dimension(140, 50));
		button_listing.setPreferredSize(new Dimension(140, 50));
		button_changes.setPreferredSize(new Dimension(140, 50));
		button_report.setPreferredSize(new Dimension(140, 50));

		//create panels to place buttons on
		JPanel button_panel1 = new JPanel();
		button_panel1.setBackground(background_color);
		button_panel1.setPreferredSize(new Dimension(350, 60));
		button_panel1.add(button_add, BorderLayout.WEST);	
		button_panel1.add(button_delete, BorderLayout.EAST);	
		
		JPanel button_panel2 = new JPanel();
		button_panel2.setBackground(background_color);
		button_panel2.setPreferredSize(new Dimension(350, 60));
		button_panel2.add(button_listing, BorderLayout.WEST);	
		button_panel2.add(button_changes, BorderLayout.EAST);	

		JPanel button_panel3 = new JPanel();
		button_panel3.setBackground(background_color);
		button_panel3.setPreferredSize(new Dimension(350, 60));
		button_panel3.add(button_report, BorderLayout.WEST);	

		JPanel button_panel_all = new JPanel(new GridBagLayout());
		GridBagConstraints bpa = new GridBagConstraints();
		bpa.gridx = 0; bpa.gridy = 0;
		button_panel_all.add(button_panel1, bpa);
		bpa.gridy += 1;
		button_panel_all.add(button_panel2, bpa);
		bpa.gridy += 1;
		button_panel_all.add(button_panel3, bpa);

		return button_panel_all;
	}

	private File getPreviousFile(String filename) {
		int version = 1;
		String backup_path = location.getAbsolutePath() + "/backup/";
		File old_file = new File(backup_path + "1_" + filename);
		for(version = 2; old_file.exists(); version++) {
			old_file = new File(backup_path + Integer.toString(version) + "_" + filename);
		}
		old_file = new File(backup_path + Integer.toString(version-2) + "_" + filename);
		
		if(version == 2)
			return null;
		else
			return old_file; 
	}
	
	//DRIVER FOR CLASS TESTING
	public static void main (String args[]) {

		if(args.length == 1)
			new MainPage(args[0]);
	}
}
