package ep.ui.vo;

public class ComboBoxKVP {
	private String displayText;
	private String value;
	
	public ComboBoxKVP(String displayText, String value){
		setDisplayText(displayText);
		setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	@Override
	public String toString() {
		return displayText;
	}
}
