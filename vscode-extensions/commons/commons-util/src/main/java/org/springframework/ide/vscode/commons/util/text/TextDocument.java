package org.springframework.ide.vscode.commons.util.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.springframework.ide.vscode.commons.util.BadLocationException;
import org.springframework.ide.vscode.commons.util.text.linetracker.DefaultLineTracker;
import org.springframework.ide.vscode.commons.util.text.linetracker.ILineTracker;

import javolution.text.Text;

public class TextDocument implements IDocument {

	ILineTracker lineTracker = new DefaultLineTracker();
	private static final Pattern NEWLINE = Pattern.compile("\\r|\\n|\\r\\n|\\n\\r");
	
	private final String uri;
	private Text text = new Text("");

	public TextDocument(String uri) {
		this.uri = uri;
	}

	private TextDocument(TextDocument other) {
		this.uri = other.uri;
		this.text = other.text;
		this.lineTracker.set(text.toString());
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String get() {
		return getText().toString();
	}

	private synchronized Text getText() {
		return text;
	}

	public synchronized void setText(String text) {
		this.text = new Text(text);
		this.lineTracker.set(text);
	}
	
	public void apply(TextDocumentContentChangeEvent change) throws BadLocationException {
		Range rng = change.getRange();
		if (rng==null) {
			//full sync mode
			setText(change.getText());
		} else {
			int start = toOffset(rng.getStart());
			int end = toOffset(rng.getEnd());
			replace(start, end-start, change.getText());
		}
	}

	/**
	 * Convert a simple offset+length pair into a vscode range. This is a method on
	 * TextDocument because it requires splitting document into lines to determine
	 * line numbers from offsets.
	 */
	public Range toRange(int offset, int length) throws BadLocationException {
		int end = Math.min(offset + length, getLength());
		Range range = new Range();
		range.setStart(toPosition(offset));
		range.setEnd(toPosition(end));
		return range;
	}

	/**
	 * Determine the line-number a given offset (i.e. what line is the offset inside of?)
	 */
	private int lineNumber(int offset) throws BadLocationException {
		return lineTracker.getLineNumberOfOffset(offset);
	}


	public Position toPosition(int offset) throws BadLocationException {
		int line = lineNumber(offset);
		int startOfLine = startOfLine(line);
		int column = offset - startOfLine;
		Position pos = new Position();
		pos.setCharacter(column);
		pos.setLine(line);
		return pos;
	}

	private int startOfLine(int line) throws BadLocationException {
		IRegion region = lineTracker.getLineInformation(line);
		return region.getOffset();
	}

	@Override
	public IRegion getLineInformationOfOffset(int offset) {
		try {
			if (offset<=getLength()) {
				int line = lineNumber(offset);
				return getLineInformation(line);
			}
		} catch (BadLocationException e) {
			//outside document.
		}
		return null;
	}

	@Override
	public int getLength() {
		return text.length();
	}

	@Override
	public String get(int start, int len) throws BadLocationException {
		try {
			return text.subtext(start, start+len).toString();
		} catch (Exception e) {
			throw new BadLocationException(e);
		}
	}

	@Override
	public int getNumberOfLines() {
		return lineTracker.getNumberOfLines();
	}

	@Override
	public String getDefaultLineDelimiter() {
		Matcher newlineFinder = NEWLINE.matcher(text);
		if (newlineFinder.find()) {
			return text.subtext(newlineFinder.start(), newlineFinder.end()).toString();
		}
		return System.getProperty("line.separator");
	}

	@Override
	public char getChar(int offset) throws BadLocationException {
		if (offset>=0 && offset<text.length()) {
			return text.charAt(offset);
		}
		throw new BadLocationException();
	}

	@Override
	public int getLineOfOffset(int offset) throws BadLocationException {
		return lineTracker.getLineNumberOfOffset(offset);
	}

	@Override
	public IRegion getLineInformation(int line) {
		try {
			return lineTracker.getLineInformation(line);
		} catch (BadLocationException e) {
			//line doesn't exist
		}
		return null;
	}

	@Override
	public int getLineOffset(int line) throws BadLocationException {
		return lineTracker.getLineOffset(line);
	}

	public int toOffset(Position position) throws BadLocationException {
		IRegion region = lineTracker.getLineInformation(position.getLine());
		int lineStart = region.getOffset();
		return lineStart + position.getCharacter();
	}

	@Override
	public synchronized void replace(int start, int len, String ins) throws BadLocationException {
		int end = start+len;
		text = text
			.delete(start, end)
			.insert(start, new Text(ins));
		lineTracker.replace(start, len, ins);
	}

	public synchronized TextDocument copy() {
		return new TextDocument(this);
	}

	@Override
	public String textBetween(int start, int end) throws BadLocationException {
		return get(start, end-start);
	}

	@Override
	public String toString() {
		return "TextDocument(uri="+uri+",\n"+this.text+"\n)";
	}

}
