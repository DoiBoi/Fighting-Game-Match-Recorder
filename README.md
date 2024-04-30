# Fighting Game Personal Recorder



For this project, I want to design a program where someone who plays fighting games can **access** and **record** the
outcomes of the game played. When recording the data, the game, the characters used, will be part of the data along
with the outcome. Using the recorded data, I want to be able to show the history of games and provide 
statistic on the match-up between the characters (i.e. Based on the data, what is the percentage of matches where Ryu 
wins against Ken). Another component I would want to implement is a combo and moves recorder with a total damage and 
also what sort of follow-ups (set-ups) possible after the combo.

The users of the program could be and not limited to:
- A player who wants to see which character has the highest win rate against his character so they could learn how to 
fight against that character
- A group of players who want to record the outcomes and create a match-up chart (a chart showing the win rate of 
each match-up)
- Someone who wants to learn a character and want to record combo from opponents and execute the combo themselves.


This is a project that I want to do as it is something that would be convenient for me to be able to use the recorded
data and adapt based on it. 

## User Stories
- As a user, I want to be able to add a game outcome to the games list
- As a user, I want to be able to remove a game outcome from the games list
- As a user, I want to be able to view the list of games played and it's outcomes
- As a user, I want to be able to select a character and see the win-rate of the character
- As a user, I want to add useful information about combos learned and possibilities after the combo
- As a user, I want to be able to retrieve combos or additional information from a specific match
- As a user, I want to be able to save the list of matches to a file and be prompted to do so before I quit
- As a user, I want to be given to option to load the list of matches from a file

## Instructions for Grader
- You can add a match by clicking on "Add" tab, then add in the information and press submit
- You can filter the matches by clicking on "View" tab, type in the name of the character to filter and 
press the filter button
- You can get info about a specific match by clicking on the "View" tab then click on the row of the match
- You can locate my visual component by starting the app and the splash screen is a loading animation
- You can save the state of my application by clicking the save button on the "Save/Load file" tab
- You can reload the state of my application by clicking the load button on the "Save/Load file" tab

## Phase 4: Task 2
```Wed Nov 29 16:33:28 PST 2023
Added a match
Wed Nov 29 16:33:28 PST 2023
Added a match
Wed Nov 29 16:33:29 PST 2023
Retrieved the records
Wed Nov 29 16:33:30 PST 2023
Retrieved the records
Wed Nov 29 16:33:30 PST 2023
Retrieved the records
Wed Nov 29 16:33:34 PST 2023x
Filtered matches
```
## Phase 4: Task 3
From the UML, I can see that splash is not connected to anything. That class was simply made to create a new window 
that plays a gif file that acts like a loading splash screen. Most of that the method's usage is in its constructor. So
I would refactor that by having a few methods in the RecorderApp that does what the constructor in the Splash class 
does.
