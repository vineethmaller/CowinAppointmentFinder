# CowinAppointmentFinder

Capability:
* Poll frequently/once for dose availability
* Filter result by date, age group, pin code or district name
* Provides list of sessions where doses are available for the given preference.
* Multiple users can be informed of dose availability via mail

Setup:
1. Export source as Runnable jar file
2. Copy ExecuteFinder.bat, parameters.txt and web_drivers/chromedriver.exe(only the file) to the exported path.
3. Run ExecuteFinder.bat

Searching for vaccine doses:
You can change the search options by updating parameters in the 'parameters.txt'.

Communication:
For the application to notify you via mail, ensure the details are correctly filled in 'parameters.txt'. 
