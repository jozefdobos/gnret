# Change Log

### v0.7.3 (17/02/2011)
- Removed rank from T1 practice progress waiting.

### v0.7.2 (15/02/2011)
- Removed rank from T1 practice round.

### v0.7.1 (11/02/2011)
- Added more reconnection attempts in hope of being able to recover from socket write error.
- Added Local passive mode tick box in FTP Login Window.
- Set data timeout to 2.5 min in Admin Window.

### v0.7 (10/02/2011)
- BugFix: Removed timeout on the Questionnaire.
- Added reconnection request modal dialog in Admin Window when exporting data.
- Increased minimal JVM heap size to 128m.
- JVM heap size printed to console.

### v0.6.9 (10/02/2011)
- Added fields in Questionnaire for age, field of study and drop downs for countries. There is also option to specify into a text field if suitable selection is not present.

### v0.6.8 (07/02/2011)
- Tried to fix T1 rank delivery.
- Each new connected computer has now unique ID based on previous highest ID on the server +1.
- Console messages are now logged into gnret.log file under ~\My Documents\gnret\gnret.log
- BugFix: Fixed export of data table to CSV (commas now sanitised).

### v0.6.7 (06/02/2011)
- Finalised for Monday (07/02/2011) testing.
- Added cess_gnret.jnlp file.

### v0.6.6 (06/02/2011)
- BugFix: Missing NOOP in Experiment Window.

### v0.6.5 (06/02/2011)
- Added select all files functionality.
- On refresh, gnret folder is automatically expanded if present.
- BugFix: Missing column 6 in rank calculation of Experiment Window.
- BugFix: Other comments text field did not propagate answers in the Questionnaire.
- BugFix: Rank display in T1 group. Now rank calculation happens in a serial call after results uploaded to the server.
- BugFix: There used to be a memory leak of loading dictionary 7 times.

### v0.6.4 (05/02/2011)
- All catch statements in Experiment window now log into console of FTP window.
- Added delete files popupmenu in the filetree as well as toolbar and manu bar of Amin window.
- Modified reconnection attempts within Experiment window.
- Added 5 min connection timeout

### v0.6.3 (03/02/2011)
- All error messages now bubble up to FTP Login window.
- Rank tiebreak resolved randomly based on current round as seed number.

### v0.6.2 (03/02/2011)
- Added support for going forwards (Shift + Right Arrow Key) and backwards (Shift + Left Arrow Key) in the experiment window.
- Modified textual labels within experiment as requested
- BugFix: When long delay from starting up the experiment window to actual pressing of the start button happened, window got disconnected experiencing socket connection error from which it could not recover. Fixed by forcing disconnection after window appears on the screen and reconnecting on button press.
- BugFix: If file upload error in experiment window occurred, it used to pass error message as file name on retry.
- BugFix: Experiment window did not set time hence no NOOP messages were sent.

### v0.6.1 (03/02/2011)
- Changed visual appearance of Questionnaire
- Added support for questionnaire in the Admin window

### v0.6 (02/02/2011)
- Redesigned the screen switching algorithm
- Added automatic rank delivery for T1 group
- Added randomised NOOP messages
- Added automatic TCP/IP port selection when field left blank in FTP Login window
- BugFix: Fixed timing delay for progress check
- BugFix: Fixed message on waiting progress bar in Admin window
- BugFix: Fixed double timer speed (time used to progress faster than desired)
- BugFix: Reset cursor when selecting About dialog from Admin window menu bar

### v0.5 (01/02/2011)
- Transformed into multithreaded application to make GUI more responsive
- Added progressive file loading in Admin window
- Added hand made letter grids for all 7 (6+1) rounds
- Added improved dictionary with manually prunned 2 and 3 letter words
- Added 'Start experiment' button in Admin window
- Added automatic RANK calculation
- Added Start/Stop experiment functionality for each round separately

### v0.4 (27/01/2011)
- Added table printing functionality
- Refresh and Reconnect functionality in Admin window implemented
- Added Admin window

