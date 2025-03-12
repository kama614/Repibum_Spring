package com.example.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public void updateRecipeWithImage(Recipe recipe, MultipartFile images) {
		// 画像アップロード処理
		if (!images.isEmpty()) {
			try {
				String fileName = System.currentTimeMillis() + "-" + images.getOriginalFilename();
				String uploadDir = "src/main/resources/static/images"; // 画像保存先ディレクトリ
				Path path = Paths.get(uploadDir, fileName);
				Files.createDirectories(path.getParent()); // 保存先ディレクトリを作成
				images.transferTo(path.toFile()); // 画像ファイルを保存

				// 画像パスをレシピオブジェクトにセット
				recipe.setImages("/images/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("画像のアップロードに失敗しました。");
			}
		} else {
			// 画像が選択されなかった場合、既存の画像を保持
			Recipe existingRecipe = recipeMapper.findById(recipe.getId());
			recipe.setImages(existingRecipe.getImages());
		}

		recipeMapper.update(recipe);
	}

	@Override
	public void deleteRecipe(Integer id) {
		recipeMapper.delete(id);
	}

}
