package controller;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Contact;
import model.Customer;
import model.User;
import utilities.TimeManager;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the "Add Appointment" controller.
 *
 *<p>This class allows the user to add an appointment to the database. The user gives the appointment a title, description, location, and type,
 * and selects an associated customer ID, user ID, and contact ID. Finally, the user selects an appointment start and end date,
 * as well as an appointment start and end time.</p>
 *
 * @author Lydia Strough
 */
public class AddAppointment implements Initializable {
    private Stage stage;
    private Parent scene;

    /**
     * This is the appointment title text field.
     */
    public TextField titleTxt;
    /**
     * This is the appointment description text field.
     */
    public TextField descriptionTxt;
    /**
     * This is the appointment location text field.
     */
    public TextField locationTxt;
    /**
     * This is the appointment type text field.
     */
    public TextField typeTxt;
    /**
     * This is the contact combo box.
     */
    public ComboBox<Contact> contactComboBx;
    /**
     * This is the customer combo box.
     */
    public ComboBox<Customer> customerComboBx;
    /**
     * This is the user combo box.
     */
    public ComboBox<User> userComboBx;
    /**
     * This is the appointment start date date picker.
     */
    public DatePicker startDatePicker;
    /**
     * This is the appointment end date date picker.
     */
    public DatePicker endDatePicker;
    /**
     * This is the appointment start time combo box.
     */
    public ComboBox<LocalTime> startTimeComboBx;
    /**
     * This is the appointment end time combo box.
     */
    public ComboBox<LocalTime> endTimeComboBx;

    /**
     * This is the appointment title error message label.
     */
    public Label titleE;
    /**
     * This is the appointment description error message label.
     */
    public Label descriptionE;
    /**
     * This is the appointment location error message label.
     */
    public Label locationE;
    /**
     * This is the appointment type error message label.
     */
    public Label typeE;
    /**
     * This is the appointment associated contact error message label.
     */
    public Label contactE;
    /**
     * This is the appointment associated customer error message label.
     */
    public Label customerE;
    /**
     * This is the appointment associated user error message label.
     */
    public Label userE;

    /**
     * desired associated customer ID
     */
    public int customerId;
    /**
     * desired associated user ID
     */
    public int userId;
    /**
     * desired associated contact ID
     */
    public int contactId;
    /**
     * desired appointment title
     */
    public String title;
    /**
     * desired appointment description
     */
    public String description;
    /**
     * desired appointment location
     */
    public String location;
    /**
     * desired appointment type
     */
    public String type;
    /**
     * desired appointment (local) start date
     */
    public LocalDate startDate;
    /**
     * desired appointment (local) end date
     */
    public LocalDate endDate;
    /**
     * appointment (local) start time
     */
    public LocalTime startTime;
    /**
     * desired appointment (local) end time
     */
    public LocalTime endTime;
    /**
     * desired appointment (local) start date and time
     */
    public LocalDateTime startDateTime;
    /**
     * desired appointment (local) end date and time
     */
    public LocalDateTime endDateTime;

    /**
     * This is the "Save Appointment" method.
     *
     * <p>User modifies text fields, combo boxes, and date pickers with desired values. The save method then checks to see if each
     *text field is blank. If the text fields are blank, the "errorMessage" method is called to populate each correlated text fields'
     * error message label with an error prompt.</p>
     *
     * <p>If none of the text fields are blank, the selected appointment start date and time is checked to see if it is within
     * business hours. If it is, then the selected appointment end date and time is checked. If either of these tests fail, then the "errorMessage"
     * method is called again, which populates an alert dialogue box, prompting the user to select a different time.</p>
     *
     * <p>If both start and end times are within business hours, then the program checks to see if the selected start time is before
     * the selected end time. If it is not, then the "errorMessage" method is called again, populating another alert dialogue box with
     * another error message.</p>
     *
     * <p>If the selected start time is before if selected end time, then the "checkNewApptForOverlap" method is called to check and see
     * if there are any other appointments associated with the selected customer ID that would overlap with this appointment.
     * If there is an overlap between appointments, then the "errorMessage" method is called again, and another alert dialogue box
     * is populated with another error message.</p>
     *
     * <p>If the appointments credentials do not overlap with any other appointments associated with the selected customer ID, then
     * the add appointment method is called, and the appointment is added to the database. The Main Appointments Menu re-populates.</p>
     *
     * @param actionEvent save button is pushed
     */
    public void onActionSaveAppt(ActionEvent actionEvent) {
        System.out.println("Save Button clicked!");

        try{
            boolean formatError = false;
            title = titleTxt.getText();
            description = descriptionTxt.getText();
            location = locationTxt.getText();
            type = typeTxt.getText();
            contactId = contactComboBx.getSelectionModel().getSelectedItem().getContactId();
            customerId = customerComboBx.getSelectionModel().getSelectedItem().getCustomerId();
            userId = userComboBx.getSelectionModel().getSelectedItem().getUserId();
            startDate = startDatePicker.getValue();
            endDate = endDatePicker.getValue();
            startTime = startTimeComboBx.getSelectionModel().getSelectedItem();
            endTime = endTimeComboBx.getSelectionModel().getSelectedItem();
            startDateTime = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(),
                    startTime.getHour(), startTime.getMinute());
            endDateTime = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(),
                    endTime.getHour(), endTime.getMinute());

            if(title.isBlank()) {
                errorMessage(1);
                formatError = true;
            } else if(description.isBlank()) {
                errorMessage(2);
                formatError = true;
            } else if(location.isBlank()) {
                errorMessage(3);
                formatError = true;
            } else if(type.isBlank()) {
                errorMessage(4);
                formatError = true;
            }

            if(!formatError) {
                AppointmentDao apptDao = new AppointmentDaoImpl();
                if(apptDao.checkApptStartTime(startDateTime) && apptDao.checkApptEndTime(endDateTime)) {
                    if (startDateTime.toLocalTime().isBefore(endDateTime.toLocalTime())) {
                        if(!apptDao.checkNewApptForOverlap(customerId, startDate, endDate, startTime, endTime)) {
                            apptDao.addAppointment(customerId, userId, contactId, title, description, location, type,
                                    startDateTime, endDateTime);

                            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(getClass().getResource("/view/MainAppointments.fxml"));
                            stage.setScene(new Scene(scene));
                            stage.show();
                            JDBC.closeConnection();
                        }else {
                            errorMessage(7);
                        }
                    } else {
                        errorMessage(6);
                    }
                }else {
                    errorMessage(5);
                }
            }
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This is the "Return to Main" method.
     *
     * <p>A confirmation dialog box populates: "All changes will be forgotten, do you wish to continue?".
     *  If the user hits the OK button, the scene shifts to the Main Appointments Menu. If cancel is selected, the user
     *  stays in the Add Appointment page.</p>
     *
     * @param actionEvent cancel button is pushed
     * */
    public void onActionReturnToMain(ActionEvent actionEvent) throws IOException {
        System.out.println("Cancel Button clicked!");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel \"Add Appointment\"");
        alert.setContentText("All changes will be forgotten, do you wish to continue?");
        alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainAppointments.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            JDBC.closeConnection();
        }
    }

    /**
     * This is the "error message" method.
     * <p>LAMBDA EXPRESSION: When this method is called, it either populates an error message in a label or an alert dialogue box.</p>
     * <p>WHY I CHOSE TO USE A LAMBDA IN THIS SCENARIO: This was a simple and effective way to generate unique error messages. The lambda
     * function also helped to cut down on the amount of code needed for the method as the case number associated with the "->"
     * symbol directly returned the associated error message.</p>
     * @param errorNum error message case number
     */
    public void errorMessage(int errorNum) {
        switch (errorNum) {
            case 1 -> {
                titleE.setText("\"Title\" cannot be empty!");
            }
            case 2 -> {
                descriptionE.setText("\"Description\" cannot be empty!");
            }
            case 3 -> {
                locationE.setText("\"Location\" cannot be empty!");
            }
            case 4 -> {
                typeE.setText("\"Type\" cannot be empty!");
            }
            case 5 -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Start or End Time");
                alert.setContentText("You have selected an appointment time that is outside of \"Business Hours\". Please select a time that is " +
                        "between 08:00 and 22:00 EST!");
                alert.showAndWait();
            }
            case 6 -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Start or End Time");
                alert.setContentText("\"Start Date/Time\" must come BEFORE \"End Date/Time\". Please try again!");
                alert.showAndWait();
            }
            case 7 -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Overlapping Appointment");
                alert.setContentText("Customer has overlapping appointments. Please select a different time!");
                alert.showAndWait();
            }
        }
    }

    /**
     * This is the "AddAppointment" controller initialize method.
     *
     * <p>This is the first method called when the screen populates.</p>
     *
     * <p>This method assigns values to variables that are used as parameters for the "dynamicBusinessHoursInit" method
     * (osZId is assigned to the systems default zone ID; businessZId is assigned to EST; startTime is set to the local time of 08:00;
     * and the number of work hours displayed is assigned to the number 13.). </p>
     *
     * <p>The database connection is opened, and the "getAllContacts" method is called from the ContactDao class. The contact combo box is
     * populated with the "allContacts" list, and the first item in the contact combo box is selected. The "getAllCustomers" method is then called
     * from the CustomerDao class. The customer combo box is populated with the "allCustomers" list, and the first item in the customer combo box is
     * selected as well. The "getAllUsers" method is then called from the UserDao class, and the "allUsers" list is populated in
     * the user combo box. The first item in the user combo box is selected.</p>
     *
     * <p>Next, the start date date picker is set to the operating systems current local date. The end date date picker is
     * assigned the same value.</p>
     *
     * <p>The appointment start time combo box is populated with the first 13 "business hours" (from 08:00 EST, on). These business hours are
     * converted from EST to the operating systems local zoned times. This is done because the "dynamicBusinessHoursInit"
     * method was called from the TimeManager class. The first item in the appointment start time combo box is selected (08:00 EST time
     * value, converted to local time and displayed). The same process was done to the appointment end time combo box - the only difference being that the end time combo boxes'
     * start time was assigned to 09:00. The first item in the appointment end time combo box (09:00 EST time, converted to local time) is selected. </p>
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add Appointment: I am initialized!");
        try {
            ZoneId osZId = ZoneId.systemDefault();
            ZoneId businessZId = ZoneId.of("America/New_York");
            LocalTime startTime = LocalTime.of(8, 0);
            int workHours = 13;

            JDBC.openConnection();
            ContactDao contactDao = new ContactDaoImpl();
            CustomerDao customerDao = new CustomerDaoImpl();
            UserDao userDao = new UserDaoImpl();

            contactComboBx.setItems(contactDao.getAllContacts());
            contactComboBx.getSelectionModel().selectFirst();
            customerComboBx.setItems(customerDao.getAllCustomers());
            customerComboBx.getSelectionModel().selectFirst();
            userComboBx.setItems(userDao.getAllUsers());
            userComboBx.getSelectionModel().selectFirst();
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(LocalDate.now());
            startTimeComboBx.setItems(TimeManager.dynamicBusinessHoursInit(osZId, businessZId, startTime, workHours));
            startTimeComboBx.getSelectionModel().selectFirst();
            endTimeComboBx.setItems(TimeManager.dynamicBusinessHoursInit(osZId, businessZId, LocalTime.of(9, 0), workHours));
            endTimeComboBx.getSelectionModel().selectFirst();
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
