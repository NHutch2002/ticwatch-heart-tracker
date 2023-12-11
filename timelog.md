# Timelog

* Heart Rate Recovery Tracking for Fitness on a Smartwatch
* Nathan Hutchison
* 2556961H
* Xianghua (Sharon) Ding

## Guidance

* This file contains the time log for your project. It will be submitted along with your final dissertation.
* **YOU MUST KEEP THIS UP TO DATE AND UNDER VERSION CONTROL.**
* This timelog should be filled out honestly, regularly (daily) and accurately. It is for *your* benefit.
* Follow the structure provided, grouping time by weeks.  Quantise time to the half hour.

## Week 1 - 18th September - 24th September

### 19th Sep 2023 - 4.5 Hours

* Worked on researching basic idea of development of a Smartwatch App
* Looked into the Android Studio IDE
* Prepared Questions for initial supervisor meeting
* Had meeting with supervisor (4:00 - 4:30pm) to kickstart the project and outline rough ideas#

### 21st Sep 2023 - 2.5 Hours

* Worked on development approach for application.
* Looked at the idea of co-development of a smartwatch and smartphone app

## Week 2 - 25th September - 1st October

### 25th Sep - 2 Hours

* Worked on further research on App Development
* Started to plan foreseeable issues and solutions
* Created meeting plan for September 26, 2023
* Started data gathering on TicWatch Pro

### 26th Sep - 30 Minutes

* Had meeting to discuss ideas
* Came up with a plan for the rest of the week

### 28th Sep - 1 Hour

* Emailed Mobvoi to enquire about Stress data API and check if there similar open source projects somewhere
* Started data gathering for the design of the smartwatch app

## Week 3 - 2nd October - 8th October

### 3rd Oct - 30 Mins

* Had meeting to discuss progress and ideas
* Arranged for a Plan B is Stress proved too complex

### 6th Oct - 7 Hours

* Worked on setting up test Android Studio project to get more comfotable with the setup
* Initialised a project with Jetpack Compose
* Managed to deploy test application to the Live Watch for tracking

## Week 4 - 9th October - 15th October

### 9th Oct - 6 Hours

* Investigated Heart Rate Monitor more
* Got a working test implementation of a Heart Rate Monitor to show the users current heart rate
* Found Stress Tracking was unfortunately impossible due to the restrictions on the Heart Rate Variability (HRV) API

### 10th Oct - 30 Mins

* Had meeting to discuss the impossibility of the project
* Decided on a plan B, where I would focus on Heart Rate Recovery
* Plan on implementing this into a workout tracking app

### 13th Oct - 5 Hours

* Started work on wireframe designing for the project
* Created initial mock up flow of the design
* Set out basics for development such as colour schemes and font families

## Week 5 - 16th October - 22nd October

### 16th Oct - 5 Hours

* Further refined design, including starting design for the potential mobile aspect of the project

### 17th Oct - 30 Mins

* Had a meeting to discuss mock designs of the new project goal
* Set out next steps for refining design and gathering user feedback on usability
* Also discussed refining smartwatch design to have more functionality, leaving less focus on the mobile aspect

### 21st Oct - 7 Hours

* Polished up Wireframe designs to help improve intuitiveness
* Added more functionality to the smartwatch application, such as the Workout History Page
* Generated some questions to ask about the design to help improve the design over time
* Created a google form to distribute

## Week 6 - 23rd October - 29th October

### 23rd Oct - 7 Hours

* Worked on initial designs, focusing on designing basic homepage and wireframe designs
* Got landing page functional with the design matched to the initial wireframes
* Set up basic navigational methods between landing page and workout page

### 24th Oct - 30 Mins

* Had a meeting to discuss progress made so far
* Set out plans for the following week including more personal interviews for user feedback
* Also planned out further development, including having a basic workout flow implemented

### 25th Oct - 2 Hours

* Set up GitHub for the project, including making initial commit and transferring locally stored meeting minutes and time log into the version control

### 27th Oct - 4 Hours

* Set up Development Environment on laptop
* Looked further into creating digital prototypes on ProtoPie

## Week 7 - 30th Ocotber - 5th November

### 30th Oct - 4 Hours

* Created designs on the watch itself
* Implemented stopwatch feature, although buggy at this stage
* Implemented Heart Rate monitor, also has minor bugs

### 31st Oct - 30 Minutes

* Had a meeting to discuss progress made so far
* Set out plans for the following week, including focusing more on the functionality of the application and fixing the minor bugs stated earlier
* Discussed the idea of taking multiple HRR measurements across a workout for any time the user was resting for more than a minute

### 4th Nov - 5 Hours

* Created MoSCoW requirements for the project to ensure correct prioritisation for the following weeks
* Researched graphing packages and their implementation on WearOS
* Researched a database structure for Android

## Week 8 - 6th November - 12th November

### 6th Nov - 8 Hours

* Fixed the Heart Rate Reading and Stopwatch bugs
* Implemented auto-pausing functionality dependant on the movement of the watch dropping below a certain threshold over an approx. 3 second period
* Started implemented HRR reading, however currently it reads heart rates for a given period (5 secs) then returns the average Heart Rate

### 7th - 30 Minutes

* Had a meeting to discuss progress made
* Set out plans for the following week, including a code refactor, some sort of HRR measurement and some sort of graph

### 12th Nov - 7 Hours

* Done a code refactor to improve readability of the project
* Implemented Heart Rate Recovery measurement on workout completion, using a temporary theoretical max of 150 BPM
* Implemented Progress Ring for measurement after workout that is animated while the HRR reading is taking place

## Week 9 - 13th November - 19th November

### 13th Nov - 4 Hours

* Added animations for the HRR reading
* Added percentages and current Heart Rate to the measurement page
* Created basic flow on workout completion page

### 15th Nov - 30 Mins 

* Updating timelog.md
* Adding Licensing to project

### 18th Nov - 4 Hours

* Fixing bug for HRR Timer when screen times out
* Added swipeability on Workout End page

## Week 10 - 20th November - 26th November

### 24th Nov - 4 Hours

* Adjusted Styling of several pages
* Finalised HRR Calculation Stage

### 26th Nov - 6 Hours

* Fixing Swipeability issue on EndWorkout screen
* Further Improved clarity on Active HRR Measurement
* Sample Graph Implemented

## Week 11 - 27th November - 3rd December

### 1st Dec - 6 Hours

* Further Stylistic changes
* Added Sample Bar Chart to compare two different chart types

## Week 12 - 4th December - 10th December

### 4th Dec - 7 Hours

* Adding Animations on graph entry
* Started work on making Shared ViewModel for Heart Rate measuring

### 5th Dec - 4 Hours

* Preparing for meeting with Sharon
* Analysing semesters work for effective round up of progress
* Generating plan for the rest of the project into next semester

## Week 13 - 11th December - 17th December

### 11th Dec - 6 Hours

* Backdating Timelog
* Investigating Android Room Database Structure
* Conntinuing on Shared ViewModel implementation
