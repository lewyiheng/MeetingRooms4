# MeetingRooms 4

Welcome to the final version of MeetingRooms!

***Requirements for usage:***
- Working internet at all times during usage
- FireStore database connected to the app

***Setbacks***
Currently there is no way of registering for an account within the app. Any additional users must be added straight to the database

All data are stored in FireStore itself! Nothing is local.

This app includes:

- ***Book Now function*** <br/>

  Allows the user to schedule a booking at the current half-hour interval. (E.g. Booking at 11:36am would schedule the meeting to start at 11:30am)

- ***Book by date function*** <br/>

  Lets the user book a room for at a future date. (Able to book on current date as well)

- ***Book by room function*** <br/>

  Lets the user book a time slot for a certain room.

- ***Occupied room/time slot feature*** <br/>

  Would display whenever user has made a conflicting booking with an existing booking.

- ***Call function*** <br/>

  A call button would be located on the above feature. The user would be brought to the phone's phone app with the occupant's number pre-dialled.

---

***Firebase structure***
All fields are neccessary unless described otherwise.

Collection | Document (title example) | Document fields
--- | --- | ---
booking | 000001 | bks_id <br/> book_date <br/> book_purpose <br/> end_time <br/> start_time <br/> room_id <br/> timestamp* <br/> user_id
booking_status | 1 | bks_status
room | BIS discussion room | room_capacity <br/> room_description* <br/> room_group* <br/> room_name <br/> room_status* <br/>
user | 000001 | employment_status* <br/> group_name* <br/> mobile_number <br/> name <br/> office_number* <br/> password <br/> role <br/> username <br/>

_* - optional as not used by app as of latest update_

---

**Security**

Please note that this is just a project done by a student meant to be submitted as an assignment. Usernames and passwords are stored in the database which means it is accessible by anyone with permission to the database. It is not very secured.

>Lew Yiheng 15/01/2020

