package com.example.app.domain;

import org.springframework.web.multipart.MultipartFile;

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

	// 画像のアップロード
	private MultipartFile upfile;

}
