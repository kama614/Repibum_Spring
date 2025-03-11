package com.example.app.service;

import java.util.List;

import com.example.app.domain.Recipe;

public interface RecipeService {

	List<Recipe> getAllRecipes();

	Recipe getRecipeById(Integer id);

	void addRecipe(Recipe recipe);

	void updateRecipe(Recipe recipe);

	void deleteRecipe(Integer id);
}
