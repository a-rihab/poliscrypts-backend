package com.poliscrypts.util;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageContent<T> {
	private List<T> content = new ArrayList<>();
	private long totalElements = 0;

}