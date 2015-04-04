# Change_Counter_System
System that tracks changes made to documents for SE580

Team 5
Members: John, Fahad, Lauren, Mohammed, Zixi

This repo contains source code and automated tests. 
You will need Eclipse with the junit plugin to run the automated testing.


Testing print page
------------------
Setup:
	Download zip file from github repo
	Unpackage on your computer somewhere
	Open Terminal application for Mac (or use windows equivalent)
 	Navigate to the directory where you unpacked it
	Execute "javac PrintPage.java" in terminal
	Get full path to a file for testing ex. "/Users/johnbarrett/Desktop/file.txt"	

Test1:
	java PrintPage "Listing" "/Users/johnbarrett/Desktop/file.txt" "" 
Results1:
	Should display a window with file.txt info heading and contents

Test2:
	java PrintPage "Report" "/Users/johnbarrett/Desktop/file.txt" "/Users/johnbarrett/Desktop/file2.txt"
Results2:
	Should display a window with added and removed lines from file.txt to file2.txt 


Testing main page
------------------
Setup:
	Download zip file from github repo
	Unpackage on your computer somewhere
	Open Terminal application for Mac (or use windows equivalent)
 	Navigate to the directory where you unpacked it
	Execute "javac MainPage.java" in terminal
	Get full path to a folder for testing ex. "/Users/johnbarrett/Desktop"	

Test1:
	java MainPage "/Users/johnbarrett/Desktop"  
Results1:
	Should display a window with a list of files and directories. You should be able to navigate through folders to find files. Then you can select a file and use the "Print Program Listing" button to open the Print page. You can also add/delete versions that create a "backup" directory with all old versions. It will also prompt you for a comment that will be stored in a comments file. Both "backup" and "comments.txt" will be generated and stored in the director passed from command line.   


