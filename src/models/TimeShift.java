package models;


public class TimeShift {

	
	
	private int employeeId;
	private String firstName;
	private String lastName;
	private String clockIn;
	private String clockOut;
	private int punchInId;
	private int punchOutId;
	private String neutralPunch;
	private Double hoursWorked;
	
	
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
	
	public int getPunchInId() {
		return punchInId;
	}
	public void setPunchInId(int punchInId) {
		this.punchInId = punchInId;
	}
	public int getPunchOutId() {
		return punchOutId;
	}
	public void setPunchOutId(int punchOutId) {
		this.punchOutId = punchOutId;
	}
	public String getNeutralPunch() {
		return neutralPunch;
	}
	public void setNeutralPunch(String neutralPunch) {
		this.neutralPunch = neutralPunch;
	}
	public Double getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(Double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
}
