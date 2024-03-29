package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;
import java.util.stream.Collectors;

import static DAO.JDBC.connection;

/**
 * This is the "Appointment DAO Implementation" class.
 * This class Implements the "Appointment DAO" class' method definitions.
 *
 * @author Lydia Strough
 */
public class AppointmentDaoImpl implements AppointmentDao {
    /**
     * This is the "all appointments" list.
     */
    ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    /**
     * This is the appointment object used in the "look up appointment" method.
     */
    public boolean apptFound;

    /**
     * This is the "get All Appointments" method.
     * This method accesses the database and returns all appointments. Each appointment is then added to an observable list, "allAppointments".
     *
     * @return allAppointments list
     */
    @Override
    public ObservableList<Appointment> getAllAppointments() {
        try {
            String sql = "SELECT * FROM appointments";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                int appointmentId = result.getInt("Appointment_ID");
                int customerId = result.getInt("Customer_ID");
                int userId = result.getInt("User_ID");
                int contactId = result.getInt("Contact_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                LocalDateTime startDateTime = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = result.getTimestamp("End").toLocalDateTime();
                LocalDate startDate = startDateTime.toLocalDate();
                LocalDate endDate = endDateTime.toLocalDate();
                LocalTime startTime = startDateTime.toLocalTime();
                LocalTime endTime = endDateTime.toLocalTime();
                Appointment appointment = new Appointment(appointmentId, customerId, userId, contactId, title, description,
                        location, type, startDateTime, endDateTime, startDate, endDate, startTime, endTime);
                allAppointments.add(appointment);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * This is the "get appointment" method.
     * This method searches the database for a specific appointment based on its unique appointment ID.
     *
     * @param appointmentId the appointment in questions' specific ID
     * @return the appointment in questions' information
     * @return no result (null), if no appointment with the specific ID exists
     */
    @Override
    public Appointment getAppointment(int appointmentId) {
        try {
            String sql = "SELECT * FROM appointments WHERE Appointment_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, appointmentId);

            ResultSet result = ps.executeQuery();
            Appointment apptResult = null;
            if (result.next()) {
                appointmentId = result.getInt("Appointment_ID");
                int customerId = result.getInt("Customer_ID");
                int userId = result.getInt("User_ID");
                int contactId = result.getInt("Contact_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                LocalDateTime startDateTime = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = result.getTimestamp("End").toLocalDateTime();
                LocalDate startDate = startDateTime.toLocalDate();
                LocalDate endDate = endDateTime.toLocalDate();
                LocalTime startTime = startDateTime.toLocalTime();
                LocalTime endTime = endDateTime.toLocalTime();
                apptResult = new Appointment(appointmentId, customerId, userId, contactId, title, description,
                        location, type, startDateTime, endDateTime, startDate, endDate, startTime, endTime);
            }
            return apptResult;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This is the "get appointment(s) by customer" method.
     * <p>This method accesses the database and filters a list of appointments, based on their related customer ID.
     * If an appointment is associated with the specific customer ID, it is added to a "customer appointments" list.</p>
     *
     * @param customerId the customer in questions' specific ID
     * @return customerAppts list
     */
    @Override
    public ObservableList<Appointment> getApptByCustomer(int customerId) {
        ObservableList<Appointment> customerAppts = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM appointments WHERE Customer_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);

            ResultSet result = ps.executeQuery();
            while (result.next()) {
                int appointmentId = result.getInt("Appointment_ID");
                customerId = result.getInt("Customer_ID");
                int userId = result.getInt("User_ID");
                int contactId = result.getInt("Contact_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                LocalDateTime startDateTime = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = result.getTimestamp("End").toLocalDateTime();
                LocalDate startDate = startDateTime.toLocalDate();
                LocalDate endDate = endDateTime.toLocalDate();
                LocalTime startTime = startDateTime.toLocalTime();
                LocalTime endTime = endDateTime.toLocalTime();
                Appointment appointment = new Appointment(appointmentId, customerId, userId, contactId, title, description,
                        location, type, startDateTime, endDateTime, startDate, endDate, startTime, endTime);
                customerAppts.add(appointment);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return customerAppts;
    }

    /**
     * This is the "get appointment(s) by contact" method.
     * <p>This method accesses the database and filters a list of appointments, based on their related contact ID.
     * If an appointment is associated with the specific contact ID, it is added to a "appointments by contact" list.</p>
     *
     * @param contactId the contact in questions' specific ID
     * @return apptsByContact list
     */
    @Override
    public ObservableList<Appointment> getApptByContact(int contactId) {
        ObservableList<Appointment> apptsByContact = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM appointments WHERE Contact_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, contactId);

            ResultSet result = ps.executeQuery();
            while (result.next()) {
                int appointmentId = result.getInt("Appointment_ID");
                int customerId = result.getInt("Customer_ID");
                int userId = result.getInt("User_ID");
                contactId = result.getInt("Contact_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                LocalDateTime startDateTime = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = result.getTimestamp("End").toLocalDateTime();
                LocalDate startDate = startDateTime.toLocalDate();
                LocalDate endDate = endDateTime.toLocalDate();
                LocalTime startTime = startDateTime.toLocalTime();
                LocalTime endTime = endDateTime.toLocalTime();
                Appointment appointment = new Appointment(appointmentId, customerId, userId, contactId, title, description,
                        location, type, startDateTime, endDateTime, startDate, endDate, startTime, endTime);
                apptsByContact.add(appointment);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return apptsByContact;
    }

    /**
     * This is the "update appointment" method.
     * This method updates all values of a selected appointment based on its unique appointment ID.
     *
     * @param appointmentId the appointment in questions' unique appointment ID
     * @param customerId the appointment in questions' desired associated customer ID
     * @param userId the appointment in questions' desired associated user ID
     * @param contactId the appointment in questions' desired associated contact ID
     * @param title the appointment in questions' desired appointment title
     * @param description the appointment in questions' desired appointment description
     * @param location the appointment in questions' desired appointment location
     * @param type the appointment in questions' desired appointment type
     * @param startDateTime the appointment in questions' desired appointment start date and time
     * @param endDateTime the appointment in questions' desired appointment end date and time
     * @return the number of affected database rows
     */
    @Override
    public int updateAppointment(int appointmentId, int customerId, int userId, int contactId, String title, String description,
                                 String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int rowsAffected = 0;
        try {
            String sql = "UPDATE appointments SET Customer_ID=?, User_ID=?, Contact_ID=?, Title=?, Description=?, " +
                    "Location=?, Type=?, Start=?, End=? WHERE Appointment_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setInt(3, contactId);
            ps.setString(4, title);
            ps.setString(5, description);
            ps.setString(6, location);
            ps.setString(7, type);
            ps.setTimestamp(8, Timestamp.valueOf(startDateTime));
            ps.setTimestamp(9, Timestamp.valueOf(endDateTime));
            ps.setInt(10, appointmentId);
            rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment UPDATE was successful!");
            } else {
                System.out.println("Appointment UPDATE Failed!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * This is the "delete appointment" method.
     * <p>This method accesses the database and deletes an with a specific appointment ID, customer ID, and appointment type.
     * If an appointment was successfully (or unsuccessfully) deleted, an alert message populates with the result of the query. </p>
     *
     * @param appointmentId the appointment in questions' unique appointment ID
     * @param customerId the appointment in questions' associated customer ID
     * @param type the appointment in questions appointment type
     * @return the number of affected database rows
     */
    @Override
    public int deleteAppointment(int appointmentId, int customerId, String type) {
        int rowsAffected = 0;
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID=? AND Customer_ID=? AND Type=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ps.setInt(2, customerId);
            ps.setString(3, type);
            rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment [" + appointmentId + "] " + type + " was successfully deleted!");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment DELETE");
                alert.setContentText("Appointment [" + appointmentId + "] " + type + " was successfully deleted!");
                alert.showAndWait();
            } else {
                System.out.println("Appointment DELETE failed!");

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment DELETE");
                alert.setContentText("Appointment [" + appointmentId + "] " + type + " failed to deleted!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * This is the "add appointment" method.
     * <p>This method accesses the database and adds an appointment to the system with the desired credentials (associated customer ID,
     * associated user ID, associated contact ID, appointment title, appointment description, appointment location, appointment type,
     * appointment desired start date and time, and desired appointment end date and time). </p>
     *
     * @param customerId associated customer ID
     * @param userId associated user ID
     * @param contactId associated contact ID
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param type appointment type
     * @param startDateTime appointment desired start date and time
     * @param endDateTime desired appointment end date and time
     * @return the number of affected database rows
     */
    @Override
    public int addAppointment(int customerId, int userId, int contactId, String title, String description, String location,
                              String type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int rowsAffected = 0;
        try {
            String sql = "INSERT INTO appointments (Customer_ID, User_ID, Contact_ID, Title, Description, Location, Type, " +
                    "Start, End) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, userId);
            ps.setInt(3, contactId);
            ps.setString(4, title);
            ps.setString(5, description);
            ps.setString(6, location);
            ps.setString(7, type);
            ps.setTimestamp(8, Timestamp.valueOf(startDateTime));
            ps.setTimestamp(9, Timestamp.valueOf(endDateTime));
            rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment INSERT was successful!");
            } else {
                System.out.println("Appointment INSERT failed!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * This is the "look up appointments" method.
     * <p>This method searches the allAppointments list for a filtered list of appointments that are associated with a
     * specific (local) start date.</p>
     *
     * @param selDate (local) appointment start date in question
     * @return <p>the allAppointments list, if there are no matching appointments with the specified (local) start date;
     * Or the filteredAppts list of appointments associated with the (local) start date</p>
     */
    @Override
    public ObservableList<Appointment> lookUpAppointment(LocalDate selDate) {
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        apptFound = false;

        for (Appointment appointment : allAppointments) {
            if (appointment.getStartDate().equals(selDate)) {
                filteredAppts.add(appointment);
            }
        }
        if (filteredAppts.isEmpty()) {
            return allAppointments;
        }
        apptFound = true;
        return filteredAppts;
    }

    /**
     * This is the "upcoming appointment alert" method.
     * <p>This method populates an alert message after the user initially logs in with a either: a list of upcoming appointments,
     * or a message stating the fact that there are no appointments scheduled to take place in the next 15 minutes. </p>
     *
     * <p>The method filters through all appointments stored in the database using the (local) date and time at login.
     * If an appointment meets the credentials it is then added to the "upcoming appointments" list, which is displayed in the alert box.
     * If the "upcoming appointments" list is empty, the other message populates (no upcoming appointments) instead.</p>
     *
     * @param ldt the operating systems local date and time at the users initial log in
     */
    @Override
    public void upcomingApptAlert(LocalDateTime ldt) {
        try {
            ObservableList<Appointment> upcomingAppts = FXCollections.observableArrayList();
            ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
            AppointmentDao appointmentDao = new AppointmentDaoImpl();
            allAppts = appointmentDao.getAllAppointments();

            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime loginZDT = ldt.atZone(zoneId);
            LocalDateTime apptStart = ldt.plusMinutes(15);

            for (Appointment appt : allAppts) {
                ZonedDateTime zonedAppt = ZonedDateTime.from(appt.getStartDateTime().atZone(zoneId));
                if (zonedAppt.isAfter(loginZDT) && zonedAppt.isBefore(apptStart.atZone(zoneId))) {
                    upcomingAppts.add(appt);
                    System.out.println("Upcoming appointment: " + appt);
                }
            }

            if (upcomingAppts.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Upcoming Appointments");
                alert.setContentText("There are no appointments scheduled to begin in the next 15 minutes!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upcoming Appointments");
                alert.setHeaderText("The following Appointments are scheduled to begin in the next 15 minutes:");
                String alertText = "";

                for (Appointment upAppt : upcomingAppts) {
                    alertText = ("Appointment: [" + upAppt.getAppointmentId() + "] at " + upAppt.getStartTime() +
                            " (" + upAppt.getStartDate() + ")\n") + alertText;
                }
                alert.setContentText(alertText);
                alert.showAndWait();
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This is the "upcoming appointments for the next week" method.
     * <p>LAMBDA EXPRESSION: This method searches the all appointments list for a filtered list of appointments that will be occurring over
     * the next 7 days (including the date of the initial login). If an appointment in the database matched the credentials,
     * the lambda expression added the appointment to the filtered list, "filteredAppts."</p>
     *
     * <p>WHY I CHOSE TO USE A LAMBDA IN THIS SCENARIO: Using a lambda function required less code in this scenario compared to manually appending
     * each appointment to a filtered appointments list. </p>
     *
     * @param dateAtLogin the operating systems local date at the users initial log in
     * @return filteredAppts list
     */
    @Override
    public FilteredList<Appointment> upcomingApptsWeek(LocalDate dateAtLogin) {
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        allAppts = getAllAppointments();
        FilteredList<Appointment> filteredAppts = new FilteredList<>(allAppts);

        filteredAppts.setPredicate(appointment -> {
            LocalDate apptDate = appointment.getStartDate();

            return ((apptDate.isEqual(dateAtLogin) || apptDate.isAfter(dateAtLogin)) &&
                    apptDate.isBefore(dateAtLogin.plusDays(7)));
        });
        return filteredAppts;
    }

    /**
     * This is the "upcoming appointments for the rest of the month" method.
     * <p>LAMBDA EXPRESSION: This method searches the all appointments list for a filtered list of appointments that will be occurring
     * throughout the remaining of the current month (including the date of the initial login). If an appointment in the database matched the credentials,
     * the lambda expression added the appointment to the filtered list, "filteredAppts."</p>
     *
     * <p>WHY I CHOSE TO USE A LAMBDA IN THIS SCENARIO: Using a lambda function required less code in this scenario compared to manually appending
     * each appointment to a filtered appointments list. </p>
     *
     * @param dateAtLogin the operating systems local date at the users initial log in
     * @return filteredAppts list
     */
    @Override
    public FilteredList<Appointment> upcomingApptsMonth(LocalDate dateAtLogin) {
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        allAppts = getAllAppointments();
        FilteredList<Appointment> filteredAppts = new FilteredList<>(allAppts);

        filteredAppts.setPredicate(appointment -> {
            LocalDate apptDate = appointment.getStartDate();

            return (apptDate.isEqual(dateAtLogin) || apptDate.isAfter(dateAtLogin)) &&
                    apptDate.getMonth().equals(dateAtLogin.getMonth());
        });
        return filteredAppts;
    }

    /**
     * This is the "check appointment start time" method.
     * <p>This method checks a desired appointment start time to see if the selected time is within "business hours" (0800 - 2200 EST).
     * The method takes the desired start time and converts it from local time to EST and checks to see if it is within business hours.
     * The method also checks to see if the time occurs before "closing" time. If the time is within business hours, and meets the other credentials,
     * the method returns true.</p>
     * @param apptStartTime the desired appointment start time that is in question
     * @return true, if the desired time meets the credentials
     */
    @Override
    public boolean checkApptStartTime(LocalDateTime apptStartTime) {
        ZonedDateTime apptZone = apptStartTime.atZone(ZoneId.systemDefault());
        apptZone = apptZone.withZoneSameInstant(ZoneId.of("US/Eastern"));
        apptStartTime = apptZone.toLocalDateTime();

        LocalTime businessOpen = LocalTime.of(8, 0);
        LocalTime businessClose = LocalTime.of(22, 0);
        return ((apptStartTime.toLocalTime().isAfter(businessOpen) || apptStartTime.toLocalTime().equals(businessOpen)) &&
                (apptStartTime.toLocalTime().isBefore(businessClose)));
    }

    /**
     * This is the "check appointment end time" method.
     * <p>This method checks a desired appointment end time to see if the selected time is within "business hours" (0800 - 2200 EST).
     * The method takes the desired start time and converts it from local time to EST and checks to see if it is within business hours.
     * The method also checks to see if the time occurs after "opening" time. If the time is within business hours, and meets the other credentials,
     * the method returns true.</p>
     * @param apptEndTime the desired appointment end time that is in question
     * @return true, if the desired time meets the credentials
     */
    @Override
    public boolean checkApptEndTime(LocalDateTime apptEndTime) {
        ZonedDateTime apptZone = apptEndTime.atZone(ZoneId.systemDefault());
        apptZone = apptZone.withZoneSameInstant(ZoneId.of("US/Eastern"));
        apptEndTime = apptZone.toLocalDateTime();

        LocalTime businessOpen = LocalTime.of(8, 0);
        LocalTime businessClose = LocalTime.of(22, 0);
        return ((apptEndTime.toLocalTime().isAfter(businessOpen)) &&
                (apptEndTime.toLocalTime().isBefore(businessClose) || apptEndTime.toLocalTime().equals(businessClose)));    //can end at close!
    }

    /**
     * This is the "check new customer appointment for appointment overlap" method.
     * <p>This method calls the "getApptByCustomer" method and searches this list of customer appointments to see if there
     * will be any appointment conflict. This method filters the customer appointments list and checks a number of requirements to see if there is any overlap.</p>
     *
     * The checks include:
     * <p>-whether or not the appointments start or end on the same day</p>
     * <p>-whether or not the appointments start at the same time</p>
     * <p>-whether or not any of the current customers' appointments occur within the new appointments selected start and end times</p>
     * <p>-whether or not the new appointment starts before the old appointments starts, and simultaneously, does the new
     * appointment end after the old appointment starts.</p>
     *
     *<p>If any of these checks come back true, then an overlap DOES occur with the selected appointment date and times</p>
     *
     * @param customerId the appointment in questions' associated customer ID
     * @param selStartDate the appointment in questions' desired start date
     * @param selEndDate the appointment in questions' desired end date
     * @param selStartTime the appointment in questions' desired start time
     * @param selEndTime the appointment in questions' desired end time
     * @return true, if there is any appointment overlap
     * @return false, if there is no appointment overlap
     */
    @Override
    public boolean checkNewApptForOverlap(int customerId, LocalDate selStartDate, LocalDate selEndDate, LocalTime selStartTime,
                                          LocalTime selEndTime) {
        AppointmentDao apptDao = new AppointmentDaoImpl();
        ObservableList<Appointment> customerAppts = apptDao.getApptByCustomer(customerId);
        boolean overlap = false;

        for (Appointment appt : customerAppts) {
            //start or end on same day
            if ((appt.getStartDate().isEqual(selStartDate)) || (appt.getEndDate().isEqual(selEndDate))) {
                //start at the same time
                if (appt.getStartTime().equals(selStartTime)) {
                    overlap = true;
                    break;
                 //old appt starts after new appt begins & old appt starts before old ends
                } else if(appt.getStartTime().isAfter(selStartTime) && appt.getStartTime().isBefore(selEndTime)) {
                    overlap = true;
                    break;
                    //new appt starts before old starts & new appt ends after old appt starts
                }else if(selStartTime.isBefore(appt.getStartTime()) && (selEndTime.isAfter(appt.getStartTime()))) {
                    overlap = true;
                    break;
                }
            }
        }
        return overlap;
    }

    /**
     * This is the "check (modified) customer appointment for appointment overlap" method.
     * <p>This method calls the "getApptByCustomer" method and searches this list of customer appointments to see if there is an
     * appointment in the database with a matching appointment ID, appointment start and end date, as well as appointment start and end time.
     * If there IS an appointment with these matching credentials, then there will be no appointment overlap, because the times/dates have not changed.
     * </p>
     * <p>However, if there is no match, then the "checkNewApptForOverlap" method is called and the updated appointment date and times will be checked for
     * appointment overlap. If there is no overlap, the method returns false. If there is overlap, the method returns true.</p>
     *
     * @param customerId the appointment in questions' associated customer ID
     * @param selStartDate the appointment in questions' desired start date
     * @param selEndDate the appointment in questions' desired end date
     * @param selStartTime the appointment in questions' desired start time
     * @param selEndTime the appointment in questions' desired end time
     * @param apptId the appointment in questions' unique appointment ID
     * @return true, if there is any appointment overlap
     * @return false, if there is no appointment overlap
     */
    @Override
    public boolean checkUpdatedApptForOverlap(int customerId, LocalDate selStartDate, LocalDate selEndDate, LocalTime selStartTime,
                                              LocalTime selEndTime, int apptId) {
        AppointmentDao apptDao = new AppointmentDaoImpl();
        ObservableList<Appointment> customerAppts = apptDao.getApptByCustomer(customerId);
        boolean overlap = false;

        for(Appointment appt : customerAppts) {
            //check to see if appt times changed
            if((appt.getAppointmentId() == apptId) && (selStartTime.equals(appt.getStartTime()) && (selEndTime.equals(appt.getEndTime())))) {
                break;
             //if appt time DID change, go through overlap check
            }else {
                checkNewApptForOverlap(customerId, selStartDate, selEndDate, selStartTime, selEndTime);
            }
        }
        return overlap;
    }
}
