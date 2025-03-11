package com.example.app.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // アップロードディレクトリのパスを application.properties から取得
    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/home")
    public String showHome(Model model) {
        // ファイル一覧を取得
        File filePath = new File(uploadDir);
        File[] fileList = filePath.listFiles();
        
        // モデルにファイルリストを渡す
        model.addAttribute("fileList", fileList);

        // home.html (Thymeleaf) を表示
        return "home";
    }
}
