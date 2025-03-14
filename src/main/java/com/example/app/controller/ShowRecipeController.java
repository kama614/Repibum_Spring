package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
			@PathVariable Integer id, // URL のパスの一部をメソッドの引数として取得
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
	@GetMapping("/update/{id}")
	public String updateGet(@PathVariable Integer id, Model model) {
		model.addAttribute("title", "レシピの編集");
		model.addAttribute("recipe", recipeService.getRecipeById(id));
		return "saveRecipe";
	}

	@PostMapping("/update/{id}")
	public String updatePost(
			@PathVariable Integer id,
			@Valid @ModelAttribute Recipe recipe,
			Errors errors,
			@RequestParam(value = "uploads", required = false) MultipartFile images,
			RedirectAttributes rd,
			Model model) {

		// 入力内容にエラーがある場合は編集画面に戻る
		if (errors.hasErrors()) {
			model.addAttribute("title", "レシピの編集");
			model.addAttribute("recipe", recipe);
			return "saveRecipe";
		}

		// 画像が選択されていない場合は、既存の画像を保持
		if (images != null && !images.isEmpty()) {
			Recipe existingRecipe = recipeService.getRecipeById(id);
			recipe.setImages(existingRecipe.getImages());
		}

		// レシピIDを設定して更新
		recipe.setId(id); // 更新に必要なレシピIDをセット
		recipeService.updateRecipe(recipe);

		// 更新完了メッセージ
		rd.addFlashAttribute("statusMessage", "レシピ情報を更新しました。");
		System.out.println("Redirecting to: /show/" + id);
		return "redirect:/recipe/show/" + id; // 更新後に詳細ページにリダイレクト
	}

	// レシピデータの削除
	@GetMapping("/delete/{id}")
	public String getDelete(@PathVariable Integer id,
			RedirectAttributes rd) {
		recipeService.deleteRecipe(id);
		rd.addFlashAttribute("statusMessage", "レシピを削除しました");
		return "redirect:/recipe/list";
	}

	@PostMapping("/delete/{id}")
	public String postDelete(@PathVariable Integer id, RedirectAttributes rd) {
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
