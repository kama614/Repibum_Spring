package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Recipe;
import com.example.app.service.RecipeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class ShowRecipeController {

    private final RecipeService recipeService;

    @GetMapping("/show")
    public String showRecipe(
            @RequestParam("id") int id,
            HttpSession session,
            Model model) {

        // セッションの確認
        if (session.getAttribute("loginId") == null) {
            return "redirect:/login"; // ログイン画面にリダイレクト
        }

        try {
            // IDに基づくレシピの詳細を取得
            Recipe recipe = recipeService.getRecipeById(id);

            // レシピデータをModelに追加
            model.addAttribute("recipe", recipe);

            // レシピ詳細画面に遷移
            return "showRecipe"; // Thymeleafのビュー名
        } catch (Exception e) {
            e.printStackTrace(); // 運用環境ではロギングを使用
            throw new RuntimeException("レシピ詳細の取得中にエラーが発生しました。", e);
        }
    }
}
