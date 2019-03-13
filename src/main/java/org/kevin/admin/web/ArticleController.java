package org.kevin.admin.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @GetMapping("/list")
    @RequiresPermissions("article:view")
    public ResponseEntity<List<String>> list(){
        return ResponseEntity.ok(Arrays.asList("list"));
    }
}
