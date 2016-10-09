/* author zw
 * 创建日期 Nov 16, 2011
 */
package com.myframework.bean;

import java.io.File;
import java.io.OutputStream;

import jxl.write.WritableCellFormat;

public class JxlBean {

	private File templateFile;
	private File outFile;
	private OutputStream out;
	private JxlSheetBean[] sheetList;

	public JxlBean() {

	}

	public JxlBean(File outFile) {
		this.outFile = outFile;
	}

	public JxlBean(File outFile, JxlSheetBean[] sheetList) {
		this.outFile = outFile;
		this.sheetList = sheetList;
	}
	
	public JxlBean(OutputStream out, JxlSheetBean[] sheetList) {
		this.out = out;
		this.sheetList = sheetList;
	}

	public JxlBean(File templateFile, File outFile, JxlSheetBean[] sheetList) {
		this.templateFile = templateFile;
		this.outFile = outFile;
		this.sheetList = sheetList;
	}

	public JxlBean(File templateFile, OutputStream out, JxlSheetBean[] sheetList) {
		this.templateFile = templateFile;
		this.out = out;
		this.sheetList = sheetList;
	}

	public static class JxlSheetBean {
		private String sheetName;
		private int sheetIndex = 0;
		private JxlDataBean[] dataList;
		private JxlColumnWidthBean[] columnWidthList;
		private Integer[] insertRowList;
		private JxlImageBean[] imageList;
		private JxlMergeBean[] mergeList;

		public JxlSheetBean() {
			
		}
		
		public JxlSheetBean(int sheetIndex, String sheetName) {
			this.sheetIndex = sheetIndex;
			this.sheetName = sheetName;
		}

		public JxlSheetBean(int sheetIndex, String sheetName, JxlDataBean[] dataList) {
			this.sheetIndex = sheetIndex;
			this.sheetName = sheetName;
			this.dataList = dataList;
		}
		
		public JxlSheetBean(int sheetIndex, String sheetName, JxlDataBean[] dataList, JxlColumnWidthBean[] columnWidthList) {
			this.sheetIndex = sheetIndex;
			this.sheetName = sheetName;
			this.dataList = dataList;
			this.columnWidthList = columnWidthList;
		}

		public JxlSheetBean(int sheetIndex, String sheetName, JxlDataBean[] dataList, JxlColumnWidthBean[] columnWidthList, Integer[] insertRowList) {
			this.sheetIndex = sheetIndex;
			this.sheetName = sheetName;
			this.dataList = dataList;
			this.columnWidthList = columnWidthList;
			this.insertRowList = insertRowList;
		}
		
		public JxlSheetBean(int sheetIndex, String sheetName, JxlDataBean[] dataList, JxlColumnWidthBean[] columnWidthList, Integer[] insertRowList, JxlImageBean[] imageList) {
			this.sheetIndex = sheetIndex;
			this.sheetName = sheetName;
			this.dataList = dataList;
			this.columnWidthList = columnWidthList;
			this.insertRowList = insertRowList;
			this.imageList = imageList;
		}
		
		public JxlSheetBean(int sheetIndex, String sheetName, JxlDataBean[] dataList, JxlColumnWidthBean[] columnWidthList, Integer[] insertRowList, JxlImageBean[] imageList, JxlMergeBean[] mergeList) {
			this.sheetIndex = sheetIndex;
			this.sheetName = sheetName;
			this.dataList = dataList;
			this.columnWidthList = columnWidthList;
			this.insertRowList = insertRowList;
			this.imageList = imageList;
			this.mergeList = mergeList;
		}

		public String getSheetName() {
			return this.sheetName;
		}

		public JxlSheetBean setSheetName(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public int getSheetIndex() {
			return this.sheetIndex;
		}

		public JxlSheetBean setSheetIndex(int sheetIndex) {
			this.sheetIndex = sheetIndex;
			return this;
		}

		public JxlDataBean[] getDataList() {
			return this.dataList;
		}

		public JxlSheetBean setDataList(JxlDataBean[] dataList) {
			this.dataList = dataList;
			return this;
		}

		public JxlImageBean[] getImageList() {
			return this.imageList;
		}

		public JxlSheetBean setImageList(JxlImageBean[] imageList) {
			this.imageList = imageList;
			return this;
		}

		public JxlMergeBean[] getMergeList() {
			return this.mergeList;
		}

		public JxlSheetBean setMergeList(JxlMergeBean[] mergeList) {
			this.mergeList = mergeList;
			return this;
		}

		public Integer[] getInsertRowList() {
			return this.insertRowList;
		}

		public JxlSheetBean setInsertRowList(Integer[] insertRowList) {
			this.insertRowList = insertRowList;
			return this;
		}

		public JxlColumnWidthBean[] getColumnWidthList() {
			return columnWidthList;
		}

		public JxlSheetBean setColumnWidthList(JxlColumnWidthBean[] columnWidthList) {
			this.columnWidthList = columnWidthList;
			return this;
		}

	}

	public static class JxlMergeBean {
		private int startColumn;
		private int startRow;
		private int endColumn;
		private int endRow;

		public JxlMergeBean() {
			
		}
		
		public JxlMergeBean(int startColumn, int startRow, int endColumn, int endRow) {
			this.setStartColumn(startColumn);
			this.setStartRow(startRow);
			this.setEndColumn(endColumn);
			this.setEndRow(endRow);
		}

		public int getStartColumn() {
			return this.startColumn;
		}

		public JxlMergeBean setStartColumn(int startColumn) {
			this.startColumn = startColumn;
			return this;
		}

		public int getStartRow() {
			return this.startRow;
		}

		public JxlMergeBean setStartRow(int startRow) {
			this.startRow = startRow;
			return this;
		}

		public int getEndColumn() {
			return this.endColumn;
		}

		public JxlMergeBean setEndColumn(int endColumn) {
			this.endColumn = endColumn;
			return this;
		}

		public int getEndRow() {
			return this.endRow;
		}

		public JxlMergeBean setEndRow(int endRow) {
			this.endRow = endRow;
			return this;
		}

	}

	public static class JxlImageBean {
		private int startRow;
		private int startColumn;
		private int columnWidth;
		private int rowHeight;
		private File imageFile;

		public JxlImageBean() {
			
		}
		
		public JxlImageBean(int startColumn, int startRow, int columnWidth, int rowHeight, File file) {
			this.setColumnWidth(columnWidth);
			this.setRowHeight(rowHeight);
			this.setStartColumn(startColumn);
			this.setStartRow(startRow);
			this.setImageFile(file);
		}

		public int getStartRow() {
			return this.startRow;
		}

		public JxlImageBean setStartRow(int startRow) {
			this.startRow = startRow;
			return this;
		}

		public int getStartColumn() {
			return this.startColumn;
		}

		public JxlImageBean setStartColumn(int startColumn) {
			this.startColumn = startColumn;
			return this;
		}

		public int getColumnWidth() {
			return this.columnWidth;
		}

		public JxlImageBean setColumnWidth(int columnWidth) {
			this.columnWidth = columnWidth;
			return this;
		}

		public int getRowHeight() {
			return this.rowHeight;
		}

		public JxlImageBean setRowHeight(int rowHeight) {
			this.rowHeight = rowHeight;
			return this;
		}

		public File getImageFile() {
			return this.imageFile;
		}

		public JxlImageBean setImageFile(File imageFile) {
			this.imageFile = imageFile;
			return this;
		}
	}

	public static class JxlDataBean {
		private String position;
		private String content;
		private WritableCellFormat format;
		private int column;
		private int row;

		public JxlDataBean() {
			
		}
		
		public JxlDataBean(String position) {
			this.setPosition(position);
		}

		public JxlDataBean(String position, String content) {
			this.setPosition(position);
			this.setContent(content);
		}

		public JxlDataBean(String position, String content, WritableCellFormat format) {
			this.setPosition(position);
			this.setFormat(format);
			this.setContent(content);
		}

		public JxlDataBean(int column, int row) {
			this.setColumn(column);
			this.setRow(row);
		}

		public JxlDataBean(int column, int row, String content) {
			this.setColumn(column);
			this.setRow(row);
			this.setContent(content);
		}

		public JxlDataBean(int column, int row, String content, WritableCellFormat format) {
			this.setColumn(column);
			this.setRow(row);
			this.setContent(content);
			this.setFormat(format);
		}

		public int getColumn() {
			return this.column;
		}

		public JxlDataBean setColumn(int column) {
			this.column = column;
			return this;
		}

		public int getRow() {
			return this.row;
		}

		public JxlDataBean setRow(int row) {
			this.row = row;
			return this;
		}

		public String getPosition() {
			return this.position;
		}

		public JxlDataBean setPosition(String position) {
			this.position = position;
			return this;
		}

		public String getContent() {
			return this.content;
		}

		public JxlDataBean setContent(String content) {
			this.content = content;
			return this;
		}

		public WritableCellFormat getFormat() {
			return this.format;
		}

		public JxlDataBean setFormat(WritableCellFormat format) {
			this.format = format;
			return this;
		}
	}

	public static class JxlColumnWidthBean {
		private int column;
		private int width;

		public JxlColumnWidthBean() {
			
		}
		
		public JxlColumnWidthBean(int column, int width) {
			this.column = column;
			this.width = width;
		}

		public int getColumn() {
			return column;
		}

		public JxlColumnWidthBean setColumn(int column) {
			this.column = column;
			return this;
		}

		public int getWidth() {
			return width;
		}

		public JxlColumnWidthBean setWidth(int width) {
			this.width = width;
			return this;
		}
	}

	public File getTemplateFile() {
		return this.templateFile;
	}

	public JxlBean setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
		return this;
	}

	public File getOutFile() {
		return this.outFile;
	}

	public JxlBean setOutFile(File outFile) {
		this.outFile = outFile;
		return this;
	}

	public OutputStream getOut() {
		return this.out;
	}

	public JxlBean setOut(OutputStream out) {
		this.out = out;
		return this;
	}

	public JxlSheetBean[] getSheetList() {
		return this.sheetList;
	}

	public JxlBean setSheetList(JxlSheetBean[] sheetList) {
		this.sheetList = sheetList;
		return this;
	}

}
