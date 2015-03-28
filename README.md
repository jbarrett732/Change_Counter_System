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
	java PrintPage "Listing" "/Users/johnbarrett/Desktop/file.txt" 
Results1:
	Should display a window with file.txt info heading and contents

Test2:
	java PrintPage "Numbered Listing" "/Users/johnbarrett/Desktop/file.txt"
Results2:
	Should display a window with file.txt info heading and numbered contents



Testing main page
-----------------
Setup:
	Same as print page but use full path to directory

Test1:
	java MainPage "/Users/johnbarrett/Desktop" 
Results1:
	Should display a window with a list of files to choose from
	"Print Listing Button" should display file contents using PrintPage 

