package controllers.booking;

import domain.DiagRepBooking;
import domain.PartAbstraction;
import domain.PartOccurrence;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.booking.BookingSystem;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import persistence.DatabaseRepository;

/**
 * @author Marcello De Bernardi
 */
public class PartsPopOverController {
    private BookingController master;
    private BookingSystem bookingSystem;
    private VehicleSys vehicleSystem;
    private PartsSystem partsSystem;

    @FXML private TableView<PartAbstraction> partsTable;
    @FXML private TableColumn<PartAbstraction, String> partAbstractionID;
    @FXML private TableColumn<PartAbstraction, String> partName;
    @FXML private TableColumn<PartAbstraction, String> partDescription;
    @FXML private TableColumn<PartAbstraction, Double> partPrice;
    @FXML private TableColumn<PartAbstraction, Integer> partStockLevel;

    private ObservableList<PartAbstraction> parts;


    public PartsPopOverController() {
        master = BookingController.getInstance();

        bookingSystem = BookingSystem.getInstance();
        vehicleSystem = VehicleSys.getInstance();
        partsSystem = PartsSystem.getInstance(DatabaseRepository.getInstance());

        fetch();
    }

    @FXML private void initialize() {
        master.setController(PartsPopOverController.class, this);

        setPartsTableCellValueFactories();
        setColumnWidths();
        populateTable();
        addTableSelectionListener();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     EVENT LISTENERS                                                 //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** handles the selection of an item in the parts table */
    private void selectPart(ObservableValue<? extends PartAbstraction> obs, PartAbstraction oldSelection,
                            PartAbstraction newSelection) {
        if (newSelection.getPartStockLevel() > 0) {
            PartOccurrence pOcc = newSelection.getOccurrenceList().get(0);
            newSelection.getOccurrenceList().remove(pOcc);
            newSelection.setPartStockLevel(newSelection.getPartStockLevel() - 1);
            partsTable.refresh();

            DiagRepBooking booking = ((DetailsController) master.getController(DetailsController.class))
                    .getSelectedBooking();

            booking.addRequiredPart(pOcc);
            ((DetailsController) master.getController(DetailsController.class))
                    .populateParts(booking.getRequiredPartsList());
        }
    }

    /** Fetches the current state of the database, filtering out parts that are not free */
    void fetch() {
        parts = FXCollections.observableArrayList(partsSystem.getPartAbstractions());
        parts.forEach(partAbs ->
                partAbs.getOccurrenceList().removeIf(pOcc ->
                        pOcc.getInstallationID() != 0 || pOcc.getBookingID() != 0)
        );
    }

    /** Commits to database all part abstractions held in the state cache */
    void save() {
        parts.forEach(part -> partsSystem.commitAbstraction(part));
    }

    /** Returns a part to the list of available parts */
    void restore(PartOccurrence occurrence) {
        parts.forEach(pAbs -> {
            if (pAbs.getPartAbstractionID() == occurrence.getPartAbstractionID()) {
                pAbs.setPartStockLevel(pAbs.getPartStockLevel() + 1);
                pAbs.getOccurrenceList().add(occurrence);
            }
        });
        partsTable.refresh();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD POPULATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** populates the table of parts with part abstractions */
    private void populateTable() {
        partsTable.setItems(FXCollections.observableArrayList(parts));
        partsTable.refresh();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     TABLE CONSTRUCTION                                              //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** sets the cell value factories for the parts table */
    private void setPartsTableCellValueFactories() {
        partAbstractionID.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>("" + p.getValue().getPartAbstractionID())
        );
        partName.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartName())
        );
        partDescription.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartDescription())
        );
        partPrice.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartPrice())
        );
        partStockLevel.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartStockLevel())
        );


        partsTable.getColumns().setAll(partAbstractionID, partName, partDescription, partPrice, partStockLevel);
    }

    /** sets the column widths of the parts table */
    private void setColumnWidths() {
        DoubleBinding binding = partsTable.widthProperty().divide(5);

        partAbstractionID.prefWidthProperty().bind(binding);
        partName.prefWidthProperty().bind(binding);
        partDescription.prefWidthProperty().bind(binding);
        partPrice.prefWidthProperty().bind(binding);
        partStockLevel.prefWidthProperty().bind(binding);
    }

    /** adds a selection listener to the table */
    private void addTableSelectionListener() {
        partsTable.getSelectionModel().selectedItemProperty().addListener(this::selectPart);
    }
}
