### 4. USER MANUAL

#### 4.1. AUTHENTICATION
The user logs in by typing their user credentials and pressing enter on the password field.
Alternatively, the user can press the login button. An exit button is also provided.

Once logged in, the user can logout using the large logout button in the upper right corner
of the interface. This returns the user to the login screen.


#### 4.3. CUSTOMERS (author: Ebube)
Customer screen consists of three key sections, the left pane, central pane and a popup form.

Add customer form on the left pane allows user to add a new customer record. The ‘Save and Add Vehicle’ makes sure new customer has at least one vehicle record. Edit customer form allows user to edit or delete a customer’s record after the select customer from table and click the ‘Edit’ button. 

Top section of the central pane allows user for to search for a customer using their customer type, partial or full first name, surname or vehicle registration numbers.

The first table on the central pane displays a table list of all customers. Clicking a customer populated the ‘Vehicle(s)’ table with their vehicle record. Clicking a vehicle record populated the ‘Part(s)’ table with parts used on the vehicle and ‘Booking(s)’ table with past or future bookings made on the customer’s vehicle. 

Deleting a customer’s record has a cascading effect as the customer vehicle record, bookings made and parts installed per vehicle records are all deleted. 

Customer vehicle popup screen shows immediately after user clicks ‘Save and Add Vehicle’ button. This popup allows user to add the customer’s first vehicle record. This ensures that a customer must have at least one vehicle record else the customer record is not saved. 


#### 4.4 BOOKINGS (author: Marcello) 
The bookings screen consists of two main sections: the details pane on the left, for viewing,
adding and editing booking details, and the main bookings pane on the right, for viewing the
general bookings situation.

The main bookings pane has three modes of use: table mode, agenda mode and split table mode. 
The user can swap between the three with the buttons in the upper right corner of the pane. 

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

The split pane view shows two tables: a table of all vehicles, and a table of bookings. The table
of bookings will display all bookings of the selected vehicle.

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


#### 4.5 VEHICLES (author: Dillon)

The vehicle screen consists of 4 sections: **Edit/Add Vehicles**, **Vehicle Table**, **Part Lists** and **Customer/Bookings Details**.

The left panel is designed to **Add Vehicles** into the system. All fields have to entered in order to add a vehicle, if any field is blank or entered incorreclty they will turn light pink/red and display a erorr message at the bottom of the pane. There is a **Clear** button to reset the fields and a **Add** button to add the vehicle to the System. An confirmation box will appear to add the Vehicle, by pressing **OK** it will add to the system and **cancel** will go back allowing you to edit the vehicle details. If the vehicle registration already exists a pop up will be displayed to stop you from adding the vehicle, if they vehicle addition is successful then a message explaining this will be displayed. 

The right panel will display vehicle parts or bookings parts, the list auto refresh depending on the vehicle *selected** in the table and will display the registraion above the list so you know what vehicle the list it for. It also shows parts for a booking, this happens when you click on the **parts** button after selecting a booking. If there are no parts for a particualar booking or vehicle it will display no parts message.

The central pane displays the vehicles is table view, on opening the vehicle tab, it will auto load all the vehicles in the system, there is a search bar which allows you to search by registration, manufactuerer or vehicle type. There is also a view all button to redisplay all the vehicles after you want to clear a search. The button **Delete** will delete any vehicle selected in the table and **Edit** will display the vehicle detials on the left pane.

On click **Edit** the add vehicle pane on the left will turn into a edit vehicle pane. This allows you to edit details of the vehicle, you can edit anything apart from the registraion and the customer. There are 3 buttons at the bottom of the edit vehicle pane, the **Edit** button will save the vehicle details, clear the textfields and turn it into add vehicle then refresh the table. The **Delete** will delete the vehicle and **New** button will turn the edit vehicle into add vehicle pane.

The Pane below the table view displays the next booking date for the vehicle selected in the table as well as display the customer detials selected below this. At the bottom of the pane it displays past and future bookings for the vehicle.
This pane will auto refresh when a new vehicle is selected in the table.


#### 4.6 PARTS (author: Muhammad Hoque)

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


#### 4.7 SPECIALIST REPAIR BOOKINGS (author: Muhammad Murad Ahmed)

In the specialist repair booking, the user is able to add, delete, modify specialist repair bookings for both parts 
and vehicles. The user is also able to view a list of **installations** for a chosen vehicle from the list of repairs.
The user can query a list of **outstanding** bookings, which will return a list of items (both parts and vehicles) which
have not yet been returned to the garage. The user can also query a list of **returned** items which have been returned to
the garage. 

**Some rules apply to the adding and editing and deletion of specialist booking details**:

1.  A specialist repair booking**must**be linked to a diagnosis and repair booking.
2.  A Vehicle that is being sent to SPC**must**be a registered vehicle of the garage.
3.  A Part being that is being sent to SPC**must** be an installed part registered to a vehicle within the garage database.
4.  A SPC booking **must** have delivery and return dates, where delivery dates must be after the current date, and the return date after the delivery date.

When a booking is created, the cost of the repair (if any) will be recorded to the related diagnosis and repair booking. This is true
for when a user deletes  a SPC booking; the cost of that booking is reduced from the linked booking.

The user is able to query different information from the SPC Bookings window:
1. A list of outstanding repair items.
2. A list of part SPC repairs.
3. A list of returned items.
4. A list of installations, for a vehicle if the user has selected a vehicle repair.

If the user views a list of installations, they are able add, edit and delete installations from the given list. 

When adding an installation the user must perform the following:
1. Select a part type you wish to install.
2. Select a part serial ID which is available to install.
3. Enter a vehicle registration and click Enter.
4. Select the booking to which you wish to add the cost of the installaton 

**NOTE** IF THERE ARE NO INCOMPLETE BOOKINGS FOR THAT VEHICLE THE USER CANNOT ADD AN INSTALLATION
5. Select an installation date.
6. Click on the '+ Installation' button 



#### 4.8 SPECIALIST REPAIR CENTER MANAGEMENT (author: Muhammad Murad Ahmed)

In the specialist repair center management interface. The user is able to add, edit and modify existing SPCs to the GMSIS.
A user must fill in the following details when adding a new SPC:
1. The name of the SPC.
2. The address of the SPC.
3. The phone number of the SPC.
4. The email address of the SPC.

The following rules apply when modifying or adding a new SPC:
1. The name **must not** be empty. 
2. The address **must not** be empty.
3. The phone number **must** only contain 11 characters and no letters.
4. The email address **must** contain an '@' character.

The user is able to view, for a given SPC, a list of vehicles that were sent to the selected SPC. From this list the user is able to
view the customer who owns the selected vehicle.

#### 4.9 USER MANAGEMENT
