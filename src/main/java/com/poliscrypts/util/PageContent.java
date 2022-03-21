package com.poliscrypts.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageContent<T> {
	private List<T> content = new ArrayList<>();
	private long totalElements = 0;
}