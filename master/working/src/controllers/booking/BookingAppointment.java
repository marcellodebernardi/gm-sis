package controllers.booking;

import domain.DiagRepBooking;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;

/**
 * @author Marcello De Bernardi
 *         <p>
 *         Implementation of the JFXtras Appointment interface designed to bridge the Agenda class and
 *         the GMSIS domain model.
 */
public class BookingAppointment implements Agenda.Appointment {
    private int bookingID;
    private boolean isRepair;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    private Agenda.AppointmentGroup appointmentGroup;
    private String summary;
    private String description;


    public BookingAppointment() {
    }

    public BookingAppointment asDiagnosis(DiagRepBooking booking) {
        bookingID = booking.getBookingID();
        startTime = booking.getDiagnosisStart();
        endTime = booking.getDiagnosisEnd();
        isRepair = false;
        return this;
    }

    public BookingAppointment asRepair(DiagRepBooking booking) {
        bookingID = booking.getBookingID();
        startTime = booking.getRepairStart();
        endTime = booking.getRepairEnd();
        isRepair = true;
        return this;
    }


    public int getBookingID() {
        return bookingID;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartLocalDateTime() {
        return startTime.toLocalDateTime();
    }

    public void setStartLocalDateTime(LocalDateTime localDateTime) {
        startTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
    }

    public LocalDateTime getEndLocalDateTime() {
        return endTime.toLocalDateTime();
    }

    public void setEndLocalDateTime(LocalDateTime localDateTime) {
        endTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
    }

    public Agenda.AppointmentGroup getAppointmentGroup() {
        return appointmentGroup;
    }

    public void setAppointmentGroup(Agenda.AppointmentGroup appointmentGroup) {
        this.appointmentGroup = appointmentGroup;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////// UNSUPPORTED OPERATIONS ///////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public Calendar getStartTime() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setStartTime(Calendar calendar) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Calendar getEndTime() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setEndTime(Calendar calendar) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Temporal getStartTemporal() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setStartTemporal(Temporal temporal) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Temporal getEndTemporal() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setEndTemporal(Temporal temporal) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public String getLocation() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setLocation(String location) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Boolean isWholeDay() throws UnsupportedOperationException {
        return false;
    }

    public void setWholeDay(Boolean wholeDay) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
