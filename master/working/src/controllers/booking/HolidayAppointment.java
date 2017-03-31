package controllers.booking;

import jfxtras.scene.control.agenda.Agenda;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.Calendar;

/**
 * @author Marcello De Bernardi
 */
public class HolidayAppointment implements Agenda.Appointment {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    private Agenda.AppointmentGroup appointmentGroup;
    private String summary;
    private String description;


    public HolidayAppointment() {
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

    public HolidayAppointment onDate(LocalDate date) {
        startTime = ZonedDateTime.of(LocalDateTime.of(date, LocalTime.MIN), ZoneId.systemDefault());
        endTime = startTime.plusDays(1);
        return this;
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

    public HolidayAppointment withAppointmentGroup(Agenda.AppointmentGroup group) {
        this.appointmentGroup = group;
        return this;
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
