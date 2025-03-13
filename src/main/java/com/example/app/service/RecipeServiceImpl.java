package com.example.app.service;

import java.io.File;
import java.io.IOException;
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

		// 画像が選択されている場合の処理
		MultipartFile upfile = recipe.getUpfile();
		if (!upfile.isEmpty()) {
			String photo = upfile.getOriginalFilename();
			recipe.setImages(photo); // 画像名をセット

			// ファイル保存ディレクトリの作成
			String uploadDir = "C:/Users/zd3N02/uploads/"; // 設定ファイルで管理するのが望ましい
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs(); // ディレクトリが存在しない場合は作成
			}

			// 画像ファイルの保存
			File dest = new File(uploadDir + photo);
			try {
				upfile.transferTo(dest);
			} catch (IOException e) {
				throw new RuntimeException("画像の保存に失敗しました", e);
			}
		}

		// recipeテーブルへの追加（画像情報を含める）
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
