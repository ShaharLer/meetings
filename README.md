# Meetings

## Overview
A webservice to handle meetings, which offers the following APIs:

* API #1) Set Meeting - with parameters fromTime, ToTime, meetingTitle
* API #2) Remove Meeting - with parameter fromTime
* API #3) Remove Meeting - with parameter meetingTitle
* API #4) GetNextMeeting - with no parameters

#### The following rules are applied to the API:
* A meeting cannot last for more than 2 hours, and for less than 15 minutes
* Two meetings cannot overlap
* No meetings on Saturdays
* Meeting titles are not unique, so removing a meeting by title (API #3) might result in
removing more than one meeting
* There are up to 40 working hours a week (Sunday to Friday) - any meeting for the same
week exceeding 40h should be rejected with proper error
* There are up to 10 working hours a day - the last meeting on the same day must end up
to 10 hours after the first meeting started. Trying to set a meeting that contradicts this
should be rejected with proper error

## HTTP requests
The following HTTP methods are supported:

```POST``` **/meeting**

  * **Target**: Set a meeting

  * **Usage**: Requires Body request with the following fields: 
    - **fromTime** (must be in the format:  **"dd-MM-yyyy HH:mm"**)
    - **toTime** (must be in the format:  **"dd-MM-yyyy HH:mm"**)
    - **meetingTitle** (String value)

  * **Example**:
    {"fromTime": "06-07-2020 13:00", "toTime" : "06-07-2020 14:00", "meetingTitle" : "Meeting 1"}

```DELETE``` **/meeting/fromTime/{time}**

  * **Target**: Remove a meeting according to its start time
  
  * **Usage**: Using {time} as a path variable that represents the start time of a meeting, the variable must be in the format: **"dd-MM-yyyy HH:mm"**)

  * **Example**: /meeting/fromTime/07-07-2020 10:00

```DELETE``` **/meeting/title/{meetingTitle}**
  
  * **Target**: Remove a meeting according to its title (**will remove all the meetings with this title**)

  * **Usage**: Using {meetingTitle} as a path variable that represents the title of a meeting

  * **Example**: /meeting/title/Meeting 1

```GET``` **/meeting/next**

  * **Target**: Returns the meeting which is coming next (**if there is no future meeting returns a proper message**)


  * **Example**: /meeting/next



