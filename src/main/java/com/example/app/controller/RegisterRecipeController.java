package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class RegisterRecipeController {

	private final RecipeService recipeService;

	// レシピ登録フォーム表示
	@GetMapping("/register")
	public String showRegisterForm(HttpSession session, Model model) {
		// セッションの確認
		if (session.getAttribute("loginId") == null) {
			return "redirect:/login"; // ログイン画面にリダイレクト
		}
		model.addAttribute("recipe", new Recipe());
		return "register";
	}

	@PostMapping("/register")
	public String registerRecipe(
			HttpSession session,
			@Valid Recipe recipe,
			Errors errors,
			RedirectAttributes ra,
			Model model) {

		// バリデーション
		MultipartFile upfile = recipe.getUpfile();
		if (!upfile.isEmpty()) {
			// 画像か否か判定する
			String type = upfile.getContentType();
			if (!type.startsWith("image/")) {
				// 画像ではない場合、エラーメッセージを表示
				errors.rejectValue("upfile", "error.not_image_file");
			}
		}

		if (errors.hasErrors()) {
			model.addAttribute("recipeList", recipeService.getAllRecipes());
			return "register";
		}

		// データベースに登録
		recipeService.addRecipe(recipe);

		ra.addFlashAttribute("statusMessage", "レシピを追加しました。");
		return "redirect:/recipe/list"; // 登録完了後に一覧ページへ

	}
}

/*
	
*/