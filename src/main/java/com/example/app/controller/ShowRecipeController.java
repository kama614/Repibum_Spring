package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.service.RecipeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class ShowRecipeController {

	private final RecipeService recipeService;

	@GetMapping("/show/{id}")
	public String showRecipe(
			@PathVariable int id, // URL のパスの一部をメソッドの引数として取得
			HttpSession session, // 現在のセッション情報を取得・管理
			Model model) { // テンプレート（Thymeleaf など）にデータを渡す

		// セッションの確認
		if (session.getAttribute("loginId") == null) {
			return "redirect:/login"; // ログイン画面にリダイレクト
		}

		try {
			model.addAttribute("recipe", recipeService.getRecipeById(id));

			// レシピ詳細画面に遷移
			return "showRecipe"; // Thymeleafのビュー名
		} catch (Exception e) {
			e.printStackTrace(); // 運用環境ではロギングを使用
			throw new RuntimeException("レシピ詳細の取得中にエラーが発生しました。", e);
		}
	}
}

/*

 			// IDに基づくレシピの詳細を取得
            Recipe recipe = recipeService.getRecipeById(id);
            // レシピデータをModelに追加
            model.addAttribute("recipe", recipe);
            
           　↓　短縮記述可能
            model.addAttribute("recipe",recipeService.getRecipeById(id));


  
 */
