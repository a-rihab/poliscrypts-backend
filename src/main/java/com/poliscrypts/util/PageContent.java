package com.poliscrypts.util;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageContent<T> {
	private List<T> content = new ArrayList<>();
	private long totalElements = 0;
}