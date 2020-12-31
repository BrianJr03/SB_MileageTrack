# SB_MileageTrack

This application allows one to keep track of mileage by utilizing the Google Sheets API to store / retrieve data.

To run this project: Run through Gradle: On the right-hand side of the IntelliJ window, there is a section entitled "Gradle."
Click on this, then expand the project directory if that is not done. 
Expand Tasks > Application, and double-click on the run operation to begin running this application.

There are 3 supressed "unused" warnings:

  - SBMT_Sheet.addEntryToSheet()
  - SBMT.updateSheet()
    - These methods have supressed warnings as they both contain a variable that is used to execute an action. 
    - The value of the methods end with '.execute()', and they are actually being used.
  - SheetEntry class
    - This class has a supressed warning as there are methods that aren't used. However, removing these methods will cause the program to crash.
