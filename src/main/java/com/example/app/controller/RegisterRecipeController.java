package com.example.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.Recipe;
import com.example.app.service.RecipeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RegisterRecipeController {

    private final RecipeService recipeService;

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model) {
        // セッションの確認
        if (session.getAttribute("loginId") == null) {
            return "redirect:/login"; // ログイン画面にリダイレクト
        }

        try {
            // レシピ一覧データの取得
            List<Recipe> recipeList = recipeService.getAllRecipes();
            model.addAttribute("recipeList", recipeList);

            return "registerRecipe"; // Thymeleafのテンプレート
        } catch (Exception e) {
            e.printStackTrace(); // 運用環境ではロギングを使用
            throw new RuntimeException("レシピ一覧の取得中にエラーが発生しました。", e);
        }
    }

    @PostMapping("/register")
    public String registerRecipe(
            @RequestParam("name") String name,
            @RequestParam("detail") String detail,
            @RequestParam("url") String url,
            @RequestParam("images") MultipartFile file,
            HttpSession session) {

        // セッションの確認
        if (session.getAttribute("loginId") == null) {
            return "redirect:/login";
        }

        // バリデーション
        boolean isValid = !name.isBlank();
        String fileName = "";

        if (!file.isEmpty()) {
            String fileType = file.getContentType();
            if (fileType == null || !fileType.startsWith("image/")) {
                isValid = false;
            }
        }

        if (!isValid) {
            return "registerRecipe"; // エラー時は再表示
        }

        try {
            // 画像を保存
            if (!file.isEmpty()) {
                File uploadDir = new File("C:/Users/zd3N02/temp"); // 保存先パス
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                fileName = file.getOriginalFilename();
                file.transferTo(new File(uploadDir, fileName));
            }

            // レシピデータの作成
            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setDetail(detail);
            recipe.setUrl(url);
            recipe.setImages(fileName);

            // データベースに登録
            recipeService.addRecipe(recipe);

            return "redirect:/recipe/list"; // 登録完了後に一覧ページへ
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("レシピの登録中にエラーが発生しました。", e);
        }
    }
}
