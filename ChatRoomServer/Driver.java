package ChatRoomServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

	private ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
	private Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

	}

//---------------------------------------------------------------------------------------------------//	

	public void addChatRoom() {
		ChatRoom room = new ChatRoom();
		String name;

		System.out.println("Enter room name: ");
		name = sc.nextLine();

		while (roomNameOk(name) == false) {
			System.out.println("re-enter unused chatroom name: ");
			name = sc.nextLine();
		}

		room.setRoomName(name);
		room.setRoomNumber(chatRooms.size() + 1);
		chatRooms.add(room);
	}

	public boolean roomNameOk(String name) {
		boolean validName = false;

		for (ChatRoom cr : chatRooms) {
			if (cr.getRoomName().contentEquals(name) == false) {
				validName = true;
			}
		}
		return validName;
	}

	public ChatRoom selectRoom() {
		System.out.println("Select a chatroom to start:");
		System.out.println(chatRooms.toString());

		int selection = sc.nextInt();

		while (roomSelectOk(selection) == false) {
			System.out.println("re-enter valid room number: ");
			selection = sc.nextInt();
		}

		return chatRooms.get(selection - 1);

	}

	public boolean roomSelectOk(int selection) {
		boolean validSelect = false;

		if ((selection <= chatRooms.size()) && (selection > 0)) {
			validSelect = true;
		}

		return validSelect;
	}

	public void startChatRoom(int roomIndex) throws IOException {
		int port = 12000;
		selectRoom();
		ServerSocket server = new ServerSocket(port);

		Socket socket;

	}

}
