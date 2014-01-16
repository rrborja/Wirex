package test.customcomponent;

import java.util.regex.Pattern;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class BoundedPlainDocument extends PlainDocument {

	protected InsertErrorListener errorListener; // Unicast listener
	protected int maxLength;
	protected FormattedTextField.FormatSpec formatSpec;
	protected StringBuffer strKeySearch = new StringBuffer("");

	public BoundedPlainDocument() {
		// Default constructor - must use setMaxLength later
		setMaxLength(0);
	}

	public BoundedPlainDocument(int maxLength) {
		setMaxLength(maxLength);
	}

	public BoundedPlainDocument(AbstractDocument.Content content, int maxLength) {
		super(content);
		if (content.length() > maxLength) {
			throw new IllegalArgumentException(
					"Initial content larger than maximum size");
		}
		setMaxLength(maxLength);
	}

	public void setMaxLength(int maxLength) {
		if (getLength() > maxLength) {
			throw new IllegalArgumentException(
					"Current content larger than new maximum size");
		}

		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet a)
			throws BadLocationException {
		if (str == null) {
			throw new IllegalArgumentException("str must not be null");
		}

		// Note: be careful here - the content always has a
		// trailing newline, which should not be counted!
		int capacity = maxLength + 1 - getContent().length();
		if (capacity >= str.length()) {
			// It all fits
			if (formatSpec != null) {
				if (formatSpec.getPattern() == null) {
					String txt = getText();
					if (!str.equals("\b")
							&& (txt.isEmpty() || txt.equals("Find"))) {
						super.insertString(offset, str, a);
					} else if (str.equals("\b")) {
						int leng = txt.length();
						if (leng > 1) {
							str = txt.substring(0, leng - 1);
							super.insertString(offset, str, a);
						}
					}
				} else {
					String pattern = formatSpec.getPattern() + "+";
					if (Pattern.matches(pattern, str)) {
						super.insertString(offset, str, a);
					}
				}
			} else {
				super.insertString(offset, str, a);
			}
		} else {
			// It doesn't all fit. Add as much as we can.
			if (capacity > 0) {
				if (formatSpec != null) {
					if (formatSpec.getPattern() == null) {
						String txt = getText();
						if (!str.equals("\b")
								&& (txt.isEmpty() || txt.equals("Find"))) {
							super.insertString(offset, str.substring(0,
									capacity), a);
						} else if (str.equals("\b")) {
							int leng = txt.length();
							if (leng > 1) {
								str = txt.substring(0, leng - 1);
								super.insertString(offset, str.substring(0,
										capacity), a);
							}
						}
					} else {
						String pattern = formatSpec.getPattern() + "+";
						if (Pattern.matches(pattern, str)) {
							super.insertString(offset, str.substring(0,
									capacity), a);
						}
					}
				} else {
					super.insertString(offset, str.substring(0, capacity), a);
				}
			}

			// finally, signal an error.
			if (errorListener != null) {
				errorListener.insertFailed(this, offset, str, a);
			}
		}
	}

	public String getText() throws BadLocationException {
		return getText(0, getLength());
	}

	public void addInsertErrorListener(InsertErrorListener l) {
		if (errorListener == null) {
			errorListener = l;
			return;
		}
		throw new IllegalArgumentException(
				"InsertErrorListener already registered");
	}

	public void removeInsertErrorListener(InsertErrorListener l) {
		if (errorListener == l) {
			errorListener = null;
		}
	}

	public interface InsertErrorListener {
		public abstract void insertFailed(BoundedPlainDocument doc, int offset,
				String str, AttributeSet a);
	}

	public void setformatSpec(FormattedTextField.FormatSpec formatSpec) {
		this.formatSpec = formatSpec;
	}
}
