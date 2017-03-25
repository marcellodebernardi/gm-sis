Repository for GM-SIS system, by Team 31.

## TEAM 31 - GARAGE MANAGEMENT SYSTEM

### CONTRIBUTORS
1. Ebubechukwu Abara (ecXXXXX)
2. Dillon Vaghela (ecXXXXX)
3. Marcello De Bernardi (ec15265)
4. Muhammad Hoque (ecXXXXX)
5. Muhammad Ahmed (ecXXXXX)

### DEPLOYMENT INFORMATION
Requires **JRE1.8.x** to run. On the ITL machines this means running the application on Windows 10.

### LOGIN CREDENTIALS
The system includes a variety of pre-added users. For testing, you may use
<table>
<tr><th>User string</th><th>Password</th><th>Access rights</th></tr>
<tr><td>00000</td><td>password</td><td>ADMINISTRATOR</td></tr>
<tr><td>10000</td><td>password</td><td>STANDARD USER</td></tr>
</table>


### USER MANUAL

#### AUTHENTICATION
The user logs in by typing their user credentials and pressing enter on the password field.
Alternatively, the user can press the login button. An exit button is also provided.

Once logged in, the user can logout using the large logout button in the upper right corner
of the interface. This returns the user to the login screen.


#### TODAY

#### CUSTOMERS - Ebube

#### BOOKINGS - Marcello 
The bookings screen consists of two main sections: the details pane on the left, for viewing,
adding and editing booking details, and the main bookings pane on the right, for viewing the
general bookings situation.

The main bookings pane has two modes of use: table mode and agenda mode. The user can swap
between the two with the buttons in the upper right corner of the pane. 

The table view is good for searching and quickly viewing details. It features a smart search bar, 
which searches by **vehicle registration number**, **customer name**, **vehicle manufacturer** 
and **vehicle model** -- just start typing and everything is taken care of. It also features display 
filters, allowing the user to view bookings for the **past**/**future**/**all time**, on a **daily**/
**weekly**/**monthly** basis. The user can also change the time range being viewed with a date picker, 
which will result in the list being filtered to whatever day/week/month the selected day falls within.
Selecting an item in the table populates the fields in the details pane on the left.

The agenda presents less detailed information, but is very useful for seeing at a glance the booking
situation. It's particularly helpful when making new bookings, as it makes it extremely easy to
identify available time slots for a particular mechanic. The agenda is in a weekly format, but a
month-formatted date picker allows selecting the week to be displayed, which makes navigation of
monthly bookings easy. A dropdown allows selecting the mechanic whose schedule the user is interested
in. Double-clicking on a booking in the agenda will populate the details pane with its details.

The details pane allows the user to **view**, **edit**, **add**, **delete** and **complete** bookings.
To view an existing booking, select it from the list or agenda. To edit it, simply change some values
in its fields and click **save**. To delete a booking, select it and click **delete**. To complete a
booking, press **complete**. At any time, you may press **new** to clear all the fields and allow
inputting a new booking.

In the details pane, to select a customer, type into the customer search bar. The search bar helps
you by featuring **auto-completion suggestions**. Select one of the suggestions as you keep typing.
You can enter a single space character into the search bar to view all possible customer choices.
Having selected a customer, select a vehicle from the dropdown and enter any other relevant details.
Parts required for a booking can conveniently be added and removed in this screen too, using the + and
- buttons by the small parts table.

**Some rules apply to the adding and editing of booking details**:
1. A booking exists in the system in two states: ongoing and complete.
2. The "save" button applies to ongoing bookings.
3. The "complete" button completes a booking. A completed booking can no longer be modified.
4. A booking only needs a customer, a vehicle, a mechanic and a diagnosis appointment to be saved.
5. A booking can only be completed, however, once a repair appointment has been carried out and
    a new mileage value recorded.

#### VEHICLES - Dillon

#### PARTS - Muhammad Hoque

#### SPECIALIST REPAIR BOOKINGS (SPC PART 1) - Muhammad Ahmed

#### SPECIALIST REPAIR CENTER MANAGEMENT (SPC PART 2) - Muhammad Ahmed

#### USER MANAGEMENT