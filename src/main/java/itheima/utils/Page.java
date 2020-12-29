package itheima.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

	private List data = new ArrayList(); // 当前页中存放的记录,类型一般为List
	private long totalCount; // 总记录数
	private int index; // 当前页
	private int size;// 每页多少条
	private long pageCount;// 页数

	public Page() {

	}

	public Page(List data, long totalCount, int index, int size, long pageCount) {
		this.data = data;
		this.totalCount = totalCount;
		this.index = index;
		this.size = size;
		this.pageCount = pageCount;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
