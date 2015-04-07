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
        private Color background_color = new Color(204,229,255);
        private Color border_color = new Color(128,128,128);

        //CONSTRUCTS PAGE WINDOW AND ADDS CONTENT
        public ProjectPage() {

                //set up window
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(540, 500);
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
			list_label.setText("No Project Folder Selected");	
		}
		addProjectList();


                //display window
                setVisible(true);
        }

	private void addHeading() {

		JPanel heading_panel = new JPanel();
		heading_panel.setPreferredSize(new Dimension(540, 60));
		JLabel heading_label = new JLabel("Change Counter Projects");
                heading_label.setFont(new Font("Serif", Font.BOLD, 32));
                heading_label.setForeground(border_color);
		heading_panel.add(heading_label);
		add(heading_panel, BorderLayout.NORTH);
	}

	private void addProjectList() {
		
		list_panel.setPreferredSize(new Dimension(500, 440));
		list_panel.setBackground(Color.WHITE);
                list_label.setFont(new Font("Serif", Font.BOLD, 24));
		if(list_label.getText().equals("No Project Folder Selected")) {
			
			list_panel.add(list_label);	
			add(list_panel, BorderLayout.WEST);	
			return;
		}
		else {
	
			list_label.setText(projects_dir.getAbsolutePath());
			JPanel label_panel = new JPanel();
			label_panel.setPreferredSize(new Dimension(500, 40));
			label_panel.setBackground(background_color);
			label_panel.add(list_label);	
			list_panel.setBackground(Color.WHITE);
			list_panel.add(label_panel);	
			add(label_panel, BorderLayout.WEST);	
		}
			
	}


        //DRIVER FOR CLASS TESTING
        public static void main (String args[]) {

		new ProjectPage();
        }
}

