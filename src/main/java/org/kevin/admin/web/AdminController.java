package org.kevin.admin.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.kevin.admin.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/me")
    @RequiresRoles(logical = Logical.OR, value = { "superAdmin", "admin"})
    public ResponseEntity<UserDto> me(){
        Subject subject = SecurityUtils.getSubject();
        return ResponseEntity.ok((UserDto) subject.getPrincipal());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> roles(){
        Subject subject = SecurityUtils.getSubject();
        UserDto user = (UserDto) subject.getPrincipal();
        return ResponseEntity.ok(user.getRoles());

    }

}
