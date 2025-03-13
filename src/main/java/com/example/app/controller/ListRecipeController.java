package com.example.app.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.domain.Recipe;
import com.example.app.service.RecipeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class ListRecipeController {

	private final RecipeService recipeService;
	private static final String UPLOAD_DIRECTORY = "C:/Users/zd3N02/uploads";

	 @GetMapping("/list")
	    public String listRecipes(HttpSession session, Model model) {
	        // セッションの確認
	        if (session.getAttribute("loginId") == null) {
	            return "redirect:/login"; // ログイン画面にリダイレクト
	        }
		
		File uploadsDirectory = new File(UPLOAD_DIRECTORY);
		File[] fileList = uploadsDirectory.listFiles();

		List<String> fileNames = Arrays.stream(fileList)
				.map(file -> file.getName()).toList();

		model.addAttribute("fileNames", fileNames);

		try {
			// レシピデータの取得をServiceに依頼
			List<Recipe> recipeList = recipeService.getAllRecipes();

			// レシピデータをModelに追加
			model.addAttribute("recipeList", recipeList);

			// レシピ一覧画面に遷移
			return "listRecipe"; // Thymeleafで解決されるビュー名

		} catch (Exception e) {
			e.printStackTrace(); // ロギングを実装するのが推奨
			throw new RuntimeException("レシピ一覧の取得中にエラーが発生しました。", e);
		}
	}
}
