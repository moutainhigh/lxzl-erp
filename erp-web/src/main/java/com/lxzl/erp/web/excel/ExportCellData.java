package com.lxzl.erp.web.excel;

public class ExportCellData {

	private int row;

	private int col;

	private int style = 0;// 0普通,1页头，需要其他样式ExportUtil中扩展

	private Object value = "";

	private Integer endRow;

	private Integer endCol;

	public ExportCellData(int row, int col, Object value, int style) {
		this.row = row;
		this.col = col;
		this.value = value;
		this.style = style;
	}

	public ExportCellData(int row, int col, Object value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	public ExportCellData(int row, int col, Object value, int style, int endRow, int endCol) {
		this.row = row;
		this.col = col;
		this.value = value;
		this.style = style;
		this.endRow = endRow;
		this.endCol = endCol;
	}

	public ExportCellData(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Object getValue() {
		return value;
	}

	public int getStyle() {
		return style;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public Integer getEndCol() {
		return endCol;
	}

}
