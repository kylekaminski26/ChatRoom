package ChatRoomServer;

import java.util.ArrayList;

public class ChatRoom {

	private String roomName;
	private String welcomeMessage;
	private int roomNumber;
	private ArrayList<Member> memberList = new ArrayList<Member>();
	private ArrayList<Message> messageLog = new ArrayList<Message>();

	public ChatRoom() {

	}

	public ChatRoom(String name, String message) {
		this.roomName = name;
		this.welcomeMessage = message;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public ArrayList<Member> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<Member> memberList) {
		this.memberList = memberList;
	}

	public void addMember(Member member) {
		memberList.add(member);
	}

	public void removeMember(Member member) {
		memberList.remove(member);
	}

	public ArrayList<Message> getMessageLog() {
		return messageLog;
	}

	public void setMessageLog(ArrayList<Message> messageLog) {
		this.messageLog = messageLog;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public String toString() {
		return String.format(roomNumber + " " + roomName);
	}

}
