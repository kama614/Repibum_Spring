package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Recipe;
import com.example.app.mapper.RecipeMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

	private final RecipeMapper recipeMapper;

	@Override
	public List<Recipe> getAllRecipes() {
		return recipeMapper.findAll();
	}

	@Override
	public Recipe getRecipeById(Integer id) {
		return recipeMapper.findById(id);
	}

	@Override
	public void addRecipe(Recipe recipe) {
		recipeMapper.add(recipe);
	}

	@Override
	public void updateRecipe(Recipe recipe) {
		recipeMapper.update(recipe);
	}

	@Override
	public void deleteRecipe(Integer id) {
		recipeMapper.delete(id);
	}

}
