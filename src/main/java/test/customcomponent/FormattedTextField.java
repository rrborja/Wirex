package test.customcomponent;

import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

public class FormattedTextField extends VTextField {

    protected FormatSpec formatSpec;
    public static final String FORMAT_PROPERTY = "format";
    protected BoundedPlainDocument document;

    public FormattedTextField() {
	this(null, null, 0, null);
    }

    public FormattedTextField(String text) {
	this(null, text, 0, null);
    }

    public FormattedTextField(String text, FormatSpec spec) {
	this(null, text, 0, spec);
    }

    public FormattedTextField(int columns, FormatSpec spec) {
	this(null, null, columns, spec);
    }

    public FormattedTextField(String text, int columns, FormatSpec spec) {
	this(null, text, columns, spec);
    }

    public FormattedTextField(Document doc, String text, int columns,
	    FormatSpec spec) {
	super(doc, text, columns);
	if (spec != null) {
	    setFormatSpec(spec);
	}
    }

    public void updateUI() {
	setUI(new FormattedTextFieldUI());
    }

    public FormatSpec getFormatSpec() {
	return formatSpec;
    }

    public void setFormatSpec(FormattedTextField.FormatSpec formatSpec) {
	FormatSpec oldFormatSpec = this.formatSpec;

	// Do nothing if no change to the format specification
	if (formatSpec.equals(oldFormatSpec) == false) {
	    this.formatSpec = formatSpec;

	    // Limit the input to the number of markers.
	    Document doc = getDocument();
	    document.setformatSpec(formatSpec);
	    if (doc instanceof BoundedPlainDocument) {
		((BoundedPlainDocument) doc).setMaxLength(formatSpec
			.getMarkerCount());
	    }

	    // Notify a change in the format spec
	    firePropertyChange(FORMAT_PROPERTY, oldFormatSpec, formatSpec);
	}
    }

    // Use a model that bounds the input length
    protected Document createDefaultModel() {
	document = new BoundedPlainDocument();
	document
		.addInsertErrorListener(new BoundedPlainDocument.InsertErrorListener() {
	    public void insertFailed(BoundedPlainDocument doc,
		    int offset, String str, AttributeSet a) {
		// Beep when the field is full
		Toolkit.getDefaultToolkit().beep();
	    }
	});
	return document;
    }

    public static class FormatSpec {

	private String format;
	private String mask;
	private String pattern;
	private int formatSize;
	private int markerCount;
	public static final char MARKER_CHAR = '*';

	public FormatSpec(String format, String mask, String pattern) {
	    this.format = format;
	    this.mask = mask;
	    this.pattern = pattern;
	    this.formatSize = format.length();
	    if (formatSize != mask.length()) {
		throw new IllegalArgumentException(
			"Format and mask must be the same size");
	    }

	    for (int i = 0; i < formatSize; i++) {
		if (mask.charAt(i) == MARKER_CHAR) {
		    markerCount++;
		}
	    }
	}

	public String getFormat() {
	    return format;
	}

	public String getMask() {
	    return mask;
	}

	public String getPattern() {
	    return pattern;
	}

	public int getFormatSize() {
	    return formatSize;
	}

	public int getMarkerCount() {
	    return markerCount;
	}

	public boolean equals(Object fmt) {
	    return fmt != null && (fmt instanceof FormatSpec)
		    && ((FormatSpec) fmt).getFormat().equals(format)
		    && ((FormatSpec) fmt).getMask().equals(mask);
	}

	public int hashCode() {
	    assert false : "hashCode not designed";
	    return 0; // any arbitrary constant will do
	}

	public String toString() {
	    return "FormatSpec with format <" + format + ">, mask <" + mask
		    + ">, pattern <" + pattern + ">";
	}
    }
}
