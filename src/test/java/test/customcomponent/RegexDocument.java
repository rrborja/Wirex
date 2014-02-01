package test.customcomponent;

import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class RegexDocument extends PlainDocument {

	private final Pattern pattern;

	public RegexDocument(String regex) {
		super();
		
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		super.insertString(offs, str, a);
		String text = getText();
		if (!isValidText(text)) {
			super.remove(offs, str.length());
			alert();
		}
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		String content = getText(offs, len);
		super.remove(offs, len);
		String text = getText();
		if (!isValidText(text)) {
			super.insertString(offs, content, null);
			alert();
		}
	}

	public String getText() throws BadLocationException {
		int offset = 0, length = getLength();
		return this.getText(offset, length);
	}

	public boolean isValidText(String text) {
		return pattern.matcher(text).matches();
	}

	private void alert() {
		//Toolkit.getDefaultToolkit().beep();
	}

}