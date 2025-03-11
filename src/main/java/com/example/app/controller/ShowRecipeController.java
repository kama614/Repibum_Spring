package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Recipe;
import com.example.app.service.RecipeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

	// レシピデータの編集
	@PostMapping("/update{id}")
	public String updatePost(
			@PathVariable Integer id,
			@Valid Recipe recipe,
			Errors errors,
			RedirectAttributes rd,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("title", "レシピ情報の変更");
			model.addAttribute("types", recipeService.getRecipeById(id));
			return "save";
		}
		recipe.setId(id); //更新に必要なレシピID をセット
		recipeService.updateRecipe(recipe);
		rd.addFlashAttribute("statusMessage", "レシピ情報を更新しました。");
		return "redirect:/show/{id}";
	}

	// レシピデータの削除
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id,
			RedirectAttributes rd) {
		recipeService.deleteRecipe(id);
		rd.addFlashAttribute("statusMessage", "レシピを削除しました");
		return "redirect:/recipe/list";
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
