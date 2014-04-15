package com.yeep.floatreader.model;

import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.yeep.floatreader.service.HTMLConverter;

/**
 * Controller for handler operation for page
 */
public class PageController
{
	protected static final int OUT_OF_INDEX = -1;
	protected static final int BEGIN_OF_INDEX = 0;
	protected String charset;

	private String fileName;
	private String fileContent;
	private List<Line> lines = new ArrayList<Line>();
	private int beginCharIndexOfContent = OUT_OF_INDEX;
	private int endCharIndexOfContent = OUT_OF_INDEX;
	private int currentCharIndexOfContent = OUT_OF_INDEX;
	private int beginLineIndexOfCurrentPage = OUT_OF_INDEX;
	private int endLineIndexOfCurrentPage = OUT_OF_INDEX;
	private int lineLimit = 1000;
	private static Page currentPage = new Page();

	// Container attributes
	private FontMetrics fontMetrics;
	private int pageWidth;
	private int pageHeight;
	private int lineCountPerPage;

	// Constructor
	public PageController(String fileName, String charset, int width, int height, FontMetrics fontMetrics,
			int currentIndex) throws IOException
	{
		this.fileName = fileName;
		this.charset = charset;
		this.currentCharIndexOfContent = currentIndex;
		this.beginLineIndexOfCurrentPage = BEGIN_OF_INDEX;
		this.endLineIndexOfCurrentPage = BEGIN_OF_INDEX;
		loadContent();
		setPageMetrics(width, height, fontMetrics);
		initialize();
	}

	public PageController(String fileName, String charset, int width, int height, FontMetrics fontMetrics)
			throws IOException
	{
		this(fileName, charset, width, height, fontMetrics, BEGIN_OF_INDEX);
	}

	/**
	 * Re-initialize by the given attribute
	 * @throws IOException 
	 */
	public void reload(int width, int height, FontMetrics fontMetrics) throws IOException
	{
		setPageMetrics(width, height, fontMetrics);
		initialize();
	}

	/**
	 * Recalculate lines for the given char index
	 * @param currentIndex
	 * @throws IOException 
	 */
	public void reload(String fileName, int currentIndex) throws IOException
	{
		this.fileName = fileName;
		loadContent();
		reload(currentIndex);
	}

	/**
	 * Recalculate lines for the given char index
	 * @param currentIndex
	 * @throws IOException 
	 */
	public void reload(int currentIndex) throws IOException
	{
		this.currentCharIndexOfContent = currentIndex;
		initialize();
	}

	/**
	 * Apply the charset
	 * @param charset
	 * @throws IOException 
	 */
	public void applyCharset(String charset) throws IOException
	{
		if (this.charset.equalsIgnoreCase(charset))
			return;

		this.charset = charset;
		loadContent();
		initialize();
	}

	/**
	 * Initialize page controller
	 * @throws IOException 
	 */
	private void initialize() throws IOException
	{
		if (!hasContents())
			return;

		loadLines();
	}

	/**
	 * Set metrics for page
	 * @param width
	 * @param height
	 * @param fontMetrics
	 */
	private void setPageMetrics(int width, int height, FontMetrics fontMetrics)
	{
		this.pageHeight = height;
		this.pageWidth = width;
		this.fontMetrics = fontMetrics;
		this.lineCountPerPage = calculateLineCount();
	}

	/**
	 * Load the content of the selected file
	 * 
	 * @param filename
	 * @throws IOException 
	 */
	private void loadContent() throws IOException
	{
		FileInputStream fi = new FileInputStream(this.fileName);
		InputStreamReader is = new InputStreamReader(fi, Charset.forName(this.charset));
		BufferedReader br = new BufferedReader(is);
		try
		{
			StringBuffer sb = new StringBuffer();
			String row;
			while ((row = br.readLine()) != null)
			{
				sb.append(row).append(HTMLConverter.SYMBOL_NEW_LINE);
			}
			this.fileContent = sb.toString();
		}
		finally
		{
			if (fi != null)
				fi.close();

			if (is != null)
				is.close();

			if (br != null)
				br.close();
		}
	}

	/**
	 * Load lines
	 */
	private void loadLines()
	{
		this.beginCharIndexOfContent = this.currentCharIndexOfContent;
		this.endCharIndexOfContent = this.currentCharIndexOfContent;
		int beginIndex = this.currentCharIndexOfContent;
		int pageWidth = getPageWidth();
		int totalLineCount = 0;

		if (!hasContents())
			return;

		this.lines.clear();
		if (beginIndex > 0)
		{
			int startIndex = beginIndex - 1;
			while (!isBOFContent())
			{
				Line line = previousLine(startIndex, pageWidth);
				startIndex = line.getBeginIndex() - 1;
				this.beginCharIndexOfContent = line.getBeginIndex();
				this.lines.add(0, line);

				if (++totalLineCount >= this.lineLimit / 2)
					break;
			}
		}

		this.beginLineIndexOfCurrentPage = this.lines.size();
		int startIndex = beginIndex;
		while (!isEOFContent())
		{
			Line line = nextLine(startIndex, pageWidth);
			startIndex = line.getEndIndex() + 1;
			this.endCharIndexOfContent = line.getEndIndex();
			this.lines.add(line);

			if (++totalLineCount >= this.lineLimit)
				break;
		}

		if (this.beginLineIndexOfCurrentPage + this.lineCountPerPage >= this.lines.size())
			this.endLineIndexOfCurrentPage = this.lines.size() - 1;
		else
			this.endLineIndexOfCurrentPage = this.beginLineIndexOfCurrentPage + this.lineCountPerPage - 1;
	}

	/**
	 * Get next page
	 * @return
	 */
	public Page nextPage()
	{
		if (this.lines == null || this.lines.isEmpty())
			return Page.BLANK_PAGE;

		if (this.endLineIndexOfCurrentPage + 1 >= this.lines.size())
		{
			loadLines();

			if (this.endLineIndexOfCurrentPage + 1 >= this.lines.size())
				return Page.BLANK_PAGE;
		}

		this.beginLineIndexOfCurrentPage = this.endLineIndexOfCurrentPage + 1;

		return currentPage();
	}

	/**
	 * Get next page
	 * @return
	 */
	public Page previousPage()
	{
		if (this.lines == null || this.lines.isEmpty())
			return Page.BLANK_PAGE;

		if (this.beginLineIndexOfCurrentPage <= 0)
		{
			loadLines();

			if (this.beginLineIndexOfCurrentPage <= 0)
				return Page.BLANK_PAGE;
		}

		if (this.beginLineIndexOfCurrentPage - this.lineCountPerPage < 0)
		{
			this.beginLineIndexOfCurrentPage = BEGIN_OF_INDEX;
		}
		else
		{
			this.beginLineIndexOfCurrentPage = this.beginLineIndexOfCurrentPage - this.lineCountPerPage;
		}

		return currentPage();
	}

	/**
	 * Get current page
	 * @return
	 */
	public Page currentPage()
	{
		if (this.lines == null || this.lines.isEmpty())
			return Page.BLANK_PAGE;

		if (this.lineCountPerPage <= 0)
			return Page.BLANK_PAGE;

		if (this.beginLineIndexOfCurrentPage == OUT_OF_INDEX || this.beginLineIndexOfCurrentPage > this.lines.size() - 1)
			return Page.BLANK_PAGE;

		this.endLineIndexOfCurrentPage = currentPage(this.lineCountPerPage);
		this.currentCharIndexOfContent = currentPage.getBeginIndexOfPage();
		return currentPage;
	}

	/**
	 * Get current page by the given line count, return the endlineIndex
	 * @param lineCount
	 */
	private int currentPage(int lineCount)
	{
		int count = lineCount;
		int originLineCount = currentPage.getLineCount();
		int actualAddingLineCount = 0;
		int endLineIndex = this.beginLineIndexOfCurrentPage;

		for (; endLineIndex < this.lines.size() && --count >= 0; endLineIndex++)
		{
			currentPage.addLast(this.lines.get(endLineIndex));
			actualAddingLineCount++;
		}

		int removeLineCount = originLineCount;
		while (removeLineCount-- > 0)
		{
			currentPage.removeFirst();
		}

		return --endLineIndex;
	}

	/**
	 * Generate next line
	 * @param beginIndex
	 * @return
	 */
	private Line nextLine(int beginIndex, int lineWidth)
	{
		StringBuffer sb = new StringBuffer();

		int endIndex = beginIndex;
		outer: for (; endIndex < this.fileContent.length(); endIndex++)
		{
			char ch = this.fileContent.charAt(endIndex);
			int charWidth = getCharWidth(ch);

			lineWidth = lineWidth - charWidth;
			if (lineWidth > 0)
			{
				String temp = HTMLConverter.toHTML(ch);

				if (HTMLConverter.isHTMLNewLine(temp))
				{
					endIndex++;
					break outer;
				}
				else
					sb.append(temp);
			}
			else
			{
				break outer;
			}
		}
		sb.append(HTMLConverter.HTML_TAG_BR);

		return new Line(beginIndex, --endIndex, sb.toString());
	}

	/**
	 * Generate previous line
	 * @param beginIndex
	 */
	private Line previousLine(int beginIndex, int lineWidth)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(new StringBuffer(HTMLConverter.HTML_TAG_BR).reverse());

		int endIndex = beginIndex;
		outer: for (; endIndex >= 0; endIndex--)
		{
			char ch = this.fileContent.charAt(endIndex);
			int charWidth = getCharWidth(ch);
			lineWidth = lineWidth - charWidth;

			if (lineWidth > 0)
			{
				String temp = HTMLConverter.toHTML(ch);
				if (HTMLConverter.isHTMLNewLine(temp))
				{
					if (endIndex != beginIndex)
					{
						endIndex--;
						break outer;
					}
				}
				else
				{
					sb.append(new StringBuffer(temp).reverse());
				}
			}
			else
			{
				break outer;
			}
		}

		return new Line(++endIndex, beginIndex, sb.reverse().toString());
	}

	/**
	 * Calculate the count of line per page
	 */
	private int calculateLineCount()
	{
		int lineHeight = getCharHeight();
		int pageHeight = this.pageHeight;
		int count = 0;

		do
		{
			count++;
			pageHeight = pageHeight - lineHeight;
		}
		while (pageHeight / lineHeight >= 1);

		return count;
	}

	/**
	 * Return true if reach the end of the content
	 */
	private boolean isEOFContent()
	{
		return !hasContents() || this.endCharIndexOfContent >= this.fileContent.length() - 1;
	}

	/**
	 * Return true if reach the beginning of the content
	 */
	private boolean isBOFContent()
	{
		return !hasContents() || this.beginCharIndexOfContent <= 0;
	}

	/**
	 * Return true if the content isn't empty
	 */
	private boolean hasContents()
	{
		return this.fileContent != null && this.fileContent.length() > 0;
	}

	/**
	 * Get the width of the given char
	 * @param ch
	 * @return
	 */
	public int getCharWidth(char ch)
	{
		return this.fontMetrics.charWidth(ch);
	}

	/**
	 * Get the height of the given char
	 * @return
	 */
	public int getCharHeight()
	{
		return this.fontMetrics.getHeight();
	}

	/**
	 * Get the first char index of the current page.
	 * <p>
	 * If the file has no content, then return OUT_OF_INDEX (-1)
	 * @return
	 */
	public int getCurrentCharIndex()
	{
		if (this.lines == null || this.lines.isEmpty())
			return BEGIN_OF_INDEX;

		return this.lines.get(beginLineIndexOfCurrentPage).getBeginIndex();
	}

	/**
	 * Get the first line of current page
	 * <p>
	 * If the file has no content, then return OUT_OF_INDEX (-1)
	 * @return
	 */
	public Line getCurrentLine()
	{
		if (this.lines == null || this.lines.isEmpty())
			return null;

		return this.lines.get(beginLineIndexOfCurrentPage);
	}

	// Setters and Getters
	public int getPageWidth()
	{
		return this.pageWidth;
	}

	public int getPageHeight()
	{
		return this.pageHeight;
	}

	public int getLineCountPerPage()
	{
		return lineCountPerPage;
	}

	public String getFileContent()
	{
		return fileContent;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public FontMetrics getFontMetrics()
	{
		return fontMetrics;
	}

	public void setFontMetrics(FontMetrics fontMetrics)
	{
		this.fontMetrics = fontMetrics;
	}
}
