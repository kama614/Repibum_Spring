package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Admin;
import com.example.app.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AdminService adminService;
    private final HttpSession session;

    // ログイン画面を表示
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "login";
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(@Valid Admin admin, Errors errors) {
        // 入力エラーがある場合
        if (errors.hasErrors()) {
            return "login";
        }

        String loginId = admin.getLoginId();
        String loginPass = admin.getLoginPass();

        // 認証チェック
        if (!adminService.isCorrectIdAndPassword(loginId, loginPass)) {
            errors.rejectValue("loginId", "error.incorrect_id_password", "ログインIDまたはパスワードが正しくありません。");
            return "login";
        }

        // セッションにログイン情報を保存
        session.setAttribute("loginId", loginId);
        return "redirect:/recipe/list";  // 成功時にレシピ一覧へ
    }

    // ログアウト処理
    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // セッション破棄・削除
        return "redirect:/home";  // ホームページへリダイレクト
    }
}
