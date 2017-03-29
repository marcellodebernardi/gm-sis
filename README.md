Repository for GM-SIS system, by Team 31.

## TEAM 31 - GARAGE MANAGEMENT SYSTEM

### 1. CONTRIBUTORS
1. Ebubechukwu Abara (ec14177)
2. Dillon Vaghela (ec15147)
3. Marcello De Bernardi (ec15265)
4. Muhammad Hoque (ec15157)
5. Muhammad Ahmed (ec15496)

### 2. DEPLOYMENT INFORMATION
Requires **JRE1.8.x** to run. On the ITL machines this means running the application on Windows 10.

### 3. LOGIN CREDENTIALS
The system includes a variety of pre-added users. For testing, you may use
<table>
<tr><th>User string</th><th>Password</th><th>Access rights</th></tr>
<tr><td>00000</td><td>password</td><td>ADMINISTRATOR</td></tr>
<tr><td>10000</td><td>password</td><td>STANDARD USER</td></tr>
</table>


### 4. USER MANUAL

#### 4.1. AUTHENTICATION
The user logs in by typing their user credentials and pressing enter on the password field.
Alternatively, the user can press the login button. An exit button is also provided.

Once logged in, the user can logout using the large logout button in the upper right corner
of the interface. This returns the user to the login screen.

#### 4.2. TODAY

#### 4.3. CUSTOMERS - Ebube

#### 4.4 BOOKINGS - Marcello 
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
Parts required for a booking can conveniently be added and removed in this screen too, using the greed
and red buttons by the small parts table.

**Some rules apply to the adding and editing of booking details**:

1.  A booking exists in the system in two states: ongoing and complete.
2.  The "save" button applies to ongoing bookings.
3.  The "complete" button completes a booking. A completed booking can no longer be modified.
4.  A booking only needs a customer, a vehicle, a mechanic and a diagnosis appointment to be saved.
5.  A booking can only be completed, however, once a repair appointment has been carried out and
    a new mileage value recorded.

#### 4.5 VEHICLES - Dillon

#### 4.6 PARTS - Muhammad Hoque

The parts screen consists of two tab views: **Available Parts** and **Part Installation History**

The first tab is the **Available Parts**, this section on the interface displays all of the current stock items
available in the garage. In order to add to this stock list, the user must complete all of the appropriate fields
which are located on the left hand side and must press the **Add Part** button.
The table should then automatically update showing the new stock item. The user can increase/decrease the stock
by selecting on a row and pressing either the **+Stock** or the **-Stock** button. This will increase/decrease
the stock by 1 and will also add/remove an occurrence for the selected part.

The available parts section also consists of a search bar which allows the user to search by part name,
this is done by entering full or partial part name and pressing the **Search** button. The user can also edit
this table by double-clicking on the area they wish to edit and pressing enter, to commit the changes and
then pressing the **Save** button which will save to database and update the table. Also, the delete function
has been implemented and the user can select a part which they wish to delete and press the **Delete** button.

The second tab which is the **Part Installation History** displays all of the installations which have used a part
from the inventory. The user can add installations by filling out the fields and selecting the part they wish to add
and also choosing an available occurrence in the garage. This table also allows the user to search by full or partial
registration, customer first name and surname and then by pressing the **Search** button the table shows the
information.

A **part calculator** has also been implemented to show the total part cost of the selected items:

1.  Select a part from the first combo-box, this will display the ID for the part and also display the name.
2.  The selected part will show the price in the opposite text field.
3.  A quantity **must** be entered even if the user wishes to select only 1, in order for the total to be shown.
4.  Press the **Calculate Total** button so that the total part cost is given to the user.

#### 4.7 SPECIALIST REPAIR BOOKINGS (SPC PART 1) - Muhammad Ahmed

In the specialist repair booking, the user is able to add, delete, modify specialist repair bookings for both parts 
and vehicles. The user is also able to view a list of **installations** for a chosen vehicle from the list of repairs.
The user can query a list of **outstanding** bookings, which will return a list of items (both parts and vehicles) which
have not yet been returned to the garage. The user can also query a list of **returned** items which have been returned to
the garage. 

**Some rules apply to the adding and editing and deletion of specialist booking details**:

1.  A specialist repair booking **MUST** be linked to a diagnosis and repair booking.
2.  A
3.  The "complete" button completes a booking. A completed booking can no longer be modified.
4.  A booking only needs a customer, a vehicle, a mechanic and a diagnosis appointment to be saved.
5.  A booking can only be completed, however, once a repair appointment has been carried out and
    a new mileage value recorded.


#### 4.8 SPECIALIST REPAIR CENTER MANAGEMENT (SPC PART 2) - Muhammad Ahmed

#### 4.9 USER MANAGEMENT
