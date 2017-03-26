package controllers.booking;

import domain.DiagRepBooking;
import domain.PartAbstraction;
import domain.PartOccurrence;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.booking.BookingSystem;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import persistence.DatabaseRepository;

import java.util.List;

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


    public PartsPopOverController() {
        master = BookingController.getInstance();

        bookingSystem = BookingSystem.getInstance();
        vehicleSystem = VehicleSys.getInstance();
        partsSystem = PartsSystem.getInstance(DatabaseRepository.getInstance());
    }

    @FXML private void initialize() {
        master.setController(PartsPopOverController.class, this);

        setPartsTableCellValueFactories();
        setColumnWidths();
        populateTable(partsSystem.getPartAbstractions());
        addTableSelectionListener();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD POPULATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // populates the table of parts with part abstractions
    private void populateTable(List<PartAbstraction> parts) {
        ObservableList<PartAbstraction> bookingsObservable = FXCollections.observableArrayList(parts);
        partsTable.setItems(bookingsObservable);
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

    // adds a selection listener to the table
    private void addTableSelectionListener() {
        partsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection.getPartStockLevel() > 0) {
                PartOccurrence pO = partsSystem.getAllFreeOccurrences(newSelection).get(0);
                DiagRepBooking booking
                        = ((DetailsController) master.getController(DetailsController.class)).getSelectedBooking();
                booking.addRequiredPart(pO);
                ((DetailsController) master.getController(DetailsController.class)).populateParts(booking.getRequiredPartsList());
            }
        });
    }
}
