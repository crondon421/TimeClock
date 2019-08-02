package models;


public class TimePunch {

	
	private int punchId;
	private int employeeId;
	private String neutralPunch;
	private boolean punchType;
	
	
	
	public int getPunchId() {
		return punchId;
	}
	public void setPunchId(int punchId) {
		this.punchId = punchId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getNeutralPunch() {
		return neutralPunch;
	}
	public void setNeutralPunch(String neutralPunch) {
		this.neutralPunch = neutralPunch;
	}
	public boolean isPunchType() {
		return punchType;
	}
	public void setPunchType(boolean punchType) {
		this.punchType = punchType;
	}
}
