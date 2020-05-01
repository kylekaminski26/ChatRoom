package ChatRoomServer;

import java.util.Date;

public class Member {
	private String name;
	private String address;
	private Date joinDate;
	private boolean active;

	public Member(String name, String address, Date joinDate) {
		this.setName(name);
		this.setAddress(address);
		this.setJoinDate(joinDate);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}