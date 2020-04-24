package com.tuanzhang.dianping.info;

import java.util.HashMap;
import java.util.Map;

public class PageInfo {

	/**
	 * �?页显示的条数
	 */
	private int	pageSize	= 10;

	/**
	 * 总记录数
	 */
	private int	totalCount	= -1;

	/**
	 * 当前页码
	 */
	private int	pageNo		= 1;

	/**
	 * 总页�?
	 */
	private int	pageCount	= 1;

	/**
	 * 前置偏移�?
	 */
	private int	preOffset	= -4;

	/**
	 * 后置偏移�?
	 */
	private int	sufOffset	= 5;

	/**
	 * �?始显示的页数
	 */
	private int	startPage	= 1;

	/**
	 * �?后显示的页数
	 */
	private int	endPage;

	/** 上一�? */
	private int	prePage;

	/**
	 * 下一�?
	 */
	private int	nextPage;

	/**
	 * 是否计算标志
	 */
	private int	calcFlag	= 1;

	/** �?要获取条�? */
	private Integer	limit;

	/** 偏移�? */
	private Integer	offset;

	/** 查询条件 */
	private Map<String, String> queryMap = new HashMap<String, String>();

	/**
	 * 获取�?始显示的页数
	 * 
	 * @param pageNo 当前页数
	 * @param pageCount 总页�?
	 * @param preOffset 前置偏移�?
	 * @param sufOffset 后置偏移�?
	 * @return �?始显示的页数
	 */
	public static int getStartPageNo(int pageNo, int pageCount, int preOffset, int sufOffset) {
		// 如果要显示的页数大于等于总页数，则开始页数为第一�?
		if (sufOffset - preOffset >= pageCount) {
			return 1;
		}
		// 如果当前页数+后置偏移量已经大于�?�页数，则超出的部分计入前置偏移�?
		if (pageNo + sufOffset > pageCount) {
			preOffset += pageCount - (pageNo + sufOffset);
		}
		// 如果当前页面+前置偏移量小于等�?1，则�?始页数为第一�?
		if (pageNo + preOffset <= 1) {
			return 1;
		}
		return pageNo + preOffset;
	}

	/**
	 * 获取�?后显示的页数
	 * 
	 * @param pageNo 当前页数
	 * @param pageCount 总页�?
	 * @param preOffset 前置偏移�?
	 * @param sufOffset 后置偏移�?
	 * @return �?后显示的页数
	 */
	public static int getEndPageNo(int pageNo, int pageCount, int preOffset, int sufOffset) {
		// 如果要显示的页数大于等于总页数，则结束页数为�?后一�?
		if (sufOffset - preOffset >= pageCount) {
			return pageCount;
		}
		// 如果当前页数+前置偏移量小于第�?页，则超出的部分计入后置偏移�?
		if (pageNo + preOffset < 1) {
			sufOffset = sufOffset + 1 - (pageNo + preOffset);
		}
		// 如果当前页面+后置偏移量大于最大页数，则最后页数为�?后一�?
		if (pageNo + sufOffset > pageCount) {
			return pageCount;
		}
		return pageNo + sufOffset;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
		this.limit = this.getPageSize();
		this.offset = this.getPageSize() * (this.getPageNo() - 1);
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		if (totalCount >= 0) {
			this.totalCount = totalCount;
			int count = (totalCount - 1) / pageSize + 1;
			setPageCount(count);
		}
	}

	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		if (pageNo < 1) {
			pageNo = 1;
		}
		return pageNo;
	}

	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		this.limit = this.getPageSize();
		this.offset = this.getPageSize() * (this.getPageNo() - 1);
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		if (pageNo > pageCount) {
			setPageNo(pageCount);
		}
	}

	/**
	 * @return the preOffset
	 */
	public int getPreOffset() {
		return preOffset;
	}

	/**
	 * @param preOffset the preOffset to set
	 */
	public void setPreOffset(int preOffset) {
		this.preOffset = preOffset;
	}

	/**
	 * @return the sufOffset
	 */
	public int getSufOffset() {
		return sufOffset;
	}

	/**
	 * @param sufOffset the sufOffset to set
	 */
	public void setSufOffset(int sufOffset) {
		this.sufOffset = sufOffset;
	}

	/**
	 * @return the startPage
	 */
	public int getStartPage() {
		if (this.calcFlag == 1) {
			return getStartPageNo(pageNo, pageCount, preOffset, sufOffset);
		}
		return startPage;
	}

	/**
	 * @param startPage the startPage to set
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	/**
	 * @return the endPage
	 */
	public int getEndPage() {
		if (this.calcFlag == 1) {
			return getEndPageNo(pageNo, pageCount, preOffset, sufOffset);
		}
		return endPage;
	}

	/**
	 * @param endPage the endPage to set
	 */
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	/**
	 * @return the prePage
	 */
	public int getPrePage() {
		prePage = this.getPageNo() - 1;
		if (prePage <= 0) {
			prePage = 1;
		}
		return prePage;
	}

	/**
	 * @param prePage the prePage to set
	 */
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	/**
	 * @return the nextPage
	 */
	public int getNextPage() {
		if (pageNo >= pageCount) {
			nextPage = pageCount;
			return nextPage;
		}
		nextPage = pageNo + 1;
		return nextPage;
	}

	/**
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the calcFlag
	 */
	public int getCalcFlag() {
		return calcFlag;
	}

	/**
	 * @param calcFlag the calcFlag to set
	 */
	public void setCalcFlag(int calcFlag) {
		this.calcFlag = calcFlag;
	}

	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}

}
