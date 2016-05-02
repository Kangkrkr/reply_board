package com.pilot.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListSizeDTO {
	private Integer totalSize;
	private Integer maxPageSize;

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getMaxPageSize() {
		return maxPageSize;
	}

	public void setMaxPageSize(Integer maxPageSize) {
		this.maxPageSize = maxPageSize;
	}

}
