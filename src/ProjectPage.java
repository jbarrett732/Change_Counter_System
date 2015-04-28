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
public class ProjectPage extends JFrame {

        private File projects_dir; 
        private JLabel list_label = new JLabel(); 
        private JPanel list_panel = new JPanel(); 
        private JPanel buttons_panel = new JPanel(); 
        private JPanel projects_panel = new JPanel(new GridBagLayout()); 
	private HashMap<String, File> projects_map = new HashMap<String, File>();
	private ButtonGroup projects_selection = new ButtonGroup();
        private Color background_color = new Color(204,229,255);
        private Color border_color = new Color(128,128,128);

        //CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
        public ProjectPage() {

                //set up window
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(380, 580);
                setBackground(background_color);
                setTitle("Projects Page");

		//add stuff here
		//add heading
		addHeading();

		//get file path
    		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a Folder for Your Projects");
    		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    		int returnVal = chooser.showOpenDialog(this);
    		if(returnVal == JFileChooser.APPROVE_OPTION) {
            		projects_dir = chooser.getSelectedFile();
    		} else {
			//error
			list_label.setText("No Project Folder Selected");	
		}
		addProjectList();


                //display window
                setVisible(true);
        }

	private void addHeading() {

		JPanel heading_panel = new JPanel();
		heading_panel.setPreferredSize(new Dimension(360, 60));
		JLabel heading_label = new JLabel("Change Counter Projects");
                heading_label.setFont(new Font("Serif", Font.BOLD, 32));
                heading_label.setForeground(border_color);
		heading_panel.add(heading_label);
		add(heading_panel, BorderLayout.NORTH);
	}

	private void addProjectList() {
		
		projects_panel.setBackground(Color.WHITE);
		list_panel.setPreferredSize(new Dimension(360, 440));
		list_panel.setBackground(Color.WHITE);
                list_label.setFont(new Font("Serif", Font.BOLD, 24));
		if(list_label.getText().equals("No Project Folder Selected")) {
			
			list_panel.add(list_label);	
			add(list_panel, BorderLayout.WEST);	
			return;
		}
		else {
	
			list_label.setText(projects_dir.getName());
			JPanel label_panel = new JPanel();
			label_panel.setPreferredSize(new Dimension(360, 40));
			label_panel.setBackground(Color.WHITE);
			label_panel.add(list_label);	

			//add projects heading
			list_panel.setBackground(Color.WHITE);
			list_panel.add(label_panel);
	
			//add button panel here
			buttons_panel.setPreferredSize(new Dimension(360, 40));
			buttons_panel.setBackground(Color.WHITE);
		        createButtons();
			list_panel.add(buttons_panel);

			//add projects list	
			JScrollPane scroll_projects = new JScrollPane(projects_panel);
			scroll_projects.setPreferredSize(new Dimension(360, 400));
			list_panel.add(scroll_projects);	

			updateProjects();
			add(list_panel, BorderLayout.WEST);	
		}
			
	}

	private void updateProjects() {
		
                GridBagConstraints p = new GridBagConstraints();
                p.anchor = GridBagConstraints.LINE_START;
                p.gridx = 0; p.gridy = 0;

		projects_panel.removeAll();
		projects_map.clear();
		projects_selection = new ButtonGroup();

                //add contents from path in proper group
                File[] file_list = projects_dir.listFiles();
                for(File item : file_list) {
                        if(item.isDirectory()) {
                                projects_map.put(item.getName(), item);
                                JRadioButton button = new JRadioButton(item.getName());
                                button.setPreferredSize(new Dimension(360, 20));    
                                projects_selection.add(button);
                                p.gridy += 1;
                                projects_panel.add(button, p);
                        }
                }

		//update ui component
                projects_panel.revalidate();
                projects_panel.repaint();
		
	}

        private String getSelectedItem() {

                //find selected component and return string
                String selected = null;
                Enumeration b_enum;
                b_enum = projects_selection.getElements();
                while(b_enum.hasMoreElements()) {
                        JRadioButton b = (JRadioButton) b_enum.nextElement()    ;
                        if(b.isSelected()) {
                                selected = b.getText();
                                break;
                        }
                }
                return selected;
        }	

	private void createButtons() {

                //add action buttons
                JButton button_add = new JButton("New Project");
                button_add.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                		//create dialog window to get user input
                		String new_filename = (String) JOptionPane.showInputDialog("Enter New Project Name");
                               
               			//comment operation cancelled
                		if(new_filename == null)
                        	return;

				new_filename = projects_dir.getAbsolutePath() + "/" + new_filename;
				File temp_file = new File(new_filename);
				if(!temp_file.exists()) { 
					temp_file.mkdir();
				}

				updateProjects();
                        }
                });
                JButton button_delete = new JButton("Delete Project");
                button_delete.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
				//need recursive function to remove all contents before dir
				File rm = new File(projects_dir + "/" + getSelectedItem());
				clean_dir(rm);
				updateProjects();
                        }
                });
                JButton button_open = new JButton("Open Project");
                button_open.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
				new MainPage(projects_dir + "/" + getSelectedItem());
                        }
                });

		button_add.setPreferredSize(new Dimension(115, 30));
		button_delete.setPreferredSize(new Dimension(115, 30));
		button_open.setPreferredSize(new Dimension(115, 30));
		
		buttons_panel.add(button_add);
		buttons_panel.add(button_delete);
		buttons_panel.add(button_open);
	}

	private void clean_dir(File rm_dir) {
		if(rm_dir.exists()) {
                   	File[] all_files = rm_dir.listFiles();
			for(File item : all_files) {
				if(item.isDirectory()) {
					clean_dir(item);
				} else if(item.isFile()) {
					item.delete();
				}
			} 
			rm_dir.delete();
		}	
	}


        //DRIVER FOR CLASS TESTING
        public static void main (String args[]) {

		new ProjectPage();
        }
}

