import java.io.Serializable;

/**
 * BasicClass
 */
public class BasicClass implements Serializable {
	private String attribute;
	private int timestamp;
	private boolean active;

	public BasicClass() {
	}

	public BasicClass(String attribute, int timestamp, boolean active) {
		this.attribute = attribute;
		this.timestamp = timestamp;
		this.active = active;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
