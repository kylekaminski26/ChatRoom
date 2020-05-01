package ChatRoomServer;

import java.util.Date;

public class Message {
	private String author;
	private String text;
	private Date date;

	public Message(String author, String text, Date date) {
		this.setAuthor(author);
		this.setText(text);
		this.setDate(date);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}