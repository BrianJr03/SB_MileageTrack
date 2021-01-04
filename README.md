# SB_MileageTrack

This application allows one to keep track of mileage by utilizing the Google Sheets API to store / retrieve data.

The Google Sheet being used : https://docs.google.com/spreadsheets/d/1IbU92yUWtT9w_kG3iCt8HTw5rmYbwgpwHPE6TUPVXIg/edit#gid=0

If you manually delete a row from any column from the Sheet, and run the program again, the sheet will display "44195" in place 
of entry dates. I am not sure why this occurs, but a work around is re-cloning the application again, and it should show the correct 
date. This typically happens when you need to "reset" the sheet to it's inital state.

To run this project: Run through Gradle: On the right-hand side of the IntelliJ window, there is a section entitled "Gradle."
Click on this, then expand the project directory if that is not done. 
Expand Tasks > Application, and double-click on the run operation to begin running this application.

A high mileage warning will be sent when the user's total mileage reaches a 250 mile threshold. For example, if your total mileage 
is 0, and overtime, it reaches 250, a warning will be sent. This will occur when the user reaches 250, 500, 750 total miles and so on.
Go to settings (from the main UI) to update contact information. If you do not wish to receieve a warning, leave the fields blank.

There are 3 supressed "unused" warnings:

  - SBMT_Sheet.addEntryToSheet()
  - SBMT.updateSheet()
    - These methods have supressed warnings as they both contain a variable that is used to execute an action. 
    - The value of the methods end with '.execute()', and they are actually being used.
  - SheetEntry class
    - This class has a supressed warning as there are methods that aren't used. However, removing these methods will cause the program to crash.
