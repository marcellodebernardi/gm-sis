Repository for GM-SIS system, by Team 31.

##TASKLIST:

### Everyone
1. Re-engineer UI code architecture
    1. Only one stage, never deleted
    2. Either only one scene that gets updated, or swapping the scene in the stage
    3. You can load whatever JavaFX you want from FXML, just depends on the root tag
        4. That means that you can exchange components in JavaFX nodes by loading the
        components out of an FXML file.
    4. Consistent naming and logic!
2. Add additional *lazy-loading* getters on domain classes


### Ebube
1. Move all popup UI into the main screen
2. Help standardize structure of UI and controller logic

### Dillon
1. Move all the popup UI into the main screen
2. Help standardize structure of UI and controller logic

### Marcello
1. Squash database bugs
2. Complete bookings UI
3. Improve database design (if there's time)
4. Improve domain model (if there's time)

### Shakib
1. Move all popup UI into the main screen
2. Help standardize structure of UI and controller logic

### Murad
1. Standardize look and feel of UI with others


## GUI architecture:
The Main class has private instance variables for the main Stage, which
is never deleted. It also has a private instance variable for the main
Scene, which is also never deleted. 

The main scene contains the "skeleton" FXML file, called ApplicationPane.fxml,
which defines the TabPane we're using for the tab behavior. MenuController is
the controller with event listeners for the tab buttons, and **only** the tab
buttons. These controllers are responsible for loading the default view of each
module page into the main area of the window. This includes the ribbon.

The main view of each module is defined in its own FXML file, with event listeners
in the module's controller. The module controllers are tasked **only** with
updating the area of the screen pertaining to the module.

Each controller can access methods in Main by calling Main.getInstance(), a
method added specifically for this purpose. Like this, controllers have access
to centralized logic for manipulating the screen. This includes methods like:

1. replaceTabContent(Pane pane)

This method replaces the PANE currently held inside the current tab. This is
the preferred way to update the screen. So, if your controller wants to change
the look of a screen, it will do it more or less like this:

````java
class Controller {
    public void someMethod() {
        // some logic
        Pane newPane = new BorderPane(FXMLLoader.load( ... ));
        // some more logic
        Main.getInstance().replaceTabContent(newPane);
    }
}
````

Obviously this ignores a lot of the actual details, but you get the point