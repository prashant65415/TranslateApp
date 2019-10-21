# TranslateApp
Android application for translation

At the start of the application, we request permissions and download csv file from free server using DownloadManager.
We have translation texts in strings.xml and csv file. First we check for string translations in csv file. 
If not found then we load from strings.xml translations. When we click on any language button, same callback function is called.
Then we check for all the text views, and check their id in csv file to translate the strings.

Locale.csv file is also sumbitted to check in this project.

The Localisation system is dynamic. To add more text views, please find below the steps:
1)Add new text view in UI xml and define the string in strings.xml
2)refer the string from strings.xml
3)Use the textview id as the string identifier in csv file and provide translations for it.

To add more languages, please find below steps:
1)Add new language button in the UI, and select onclick as onClickBtn.
2)Add the language string in strings.xml and give locale value.
3)Add the language string in csv file, and write translation strings.		

Implemented test cases in MainActivityTest.java file.

Please let me know if you need any other information.
			
