package models;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimePunch {

	
	
	int employeeId;
	String firstName;
	String lastName;
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	String clockIn;
	String clockOut;
	Double hoursWorked;
	
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public DateTimeFormatter getMyFormatObj() {
		return myFormatObj;
	}
	public void setMyFormatObj(DateTimeFormatter myFormatObj) {
		this.myFormatObj = myFormatObj;
	}
	public String getClockIn() {
		return clockIn;
	}
	public void setClockIn(String clockIn) {
		this.clockIn = clockIn;
	}
	public String getClockOut() {
		return clockOut;
	}
	public void setClockOut(String clockOut) {
		this.clockOut = clockOut;
	}
	public Double getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(Double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	
	
	
	
}
