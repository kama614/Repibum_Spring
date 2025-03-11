// アノテーションを使わず、MyBatisのXMLマッピングでSQLを定義する前提のシンプルなインターフェース
package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.Recipe;

public interface RecipeMapper {
	
	List<Recipe> findAll();

	Recipe findById(Integer id);

	void add(Recipe recipe);

	void update(Recipe recipe);

	void delete(Integer id);
}
