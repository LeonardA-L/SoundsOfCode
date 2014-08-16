Sounds Of Code
=========================
A musical representation of GitHub's activity timeline.

... Or at least an attempt to make it musical.
****
This project is divided in several part. A data gathering part from the GitHub archive on google BigQuery to get information about GitHub's activity over the last two years. Then a data mining part where the results of the SQL queries is processed via a spreadsheet. Finally those elements are piped into a Java program that processes them into a wav file.

This generates a musical log journal of GitHub activity.

## Get me places

To learn about the project you can :

* **Get to the demonstration website**
* Get the list of the SQL queries used to harvest data
* Get the data mining spreadsheet
* Check out the project's wiki page where you can get use[ful|less] information about the project, the elements in it and DIY instructions

## Quick start

```
git clone https://github.com/LeonardA-L/SoundsOfCode.git
cd SoundsOfCode
./build.sh
./run.sh
```

****

Any comment, contribution, question, feedback is welcome. Pull resquests, issues, you know how it works.

This project is licensed under a MIT license.