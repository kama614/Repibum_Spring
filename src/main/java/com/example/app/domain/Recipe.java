package com.example.app.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Recipe {

	private Integer id;
	private String name;
	private String detail;
	private String url;
	private String images;
	
	
	
}
