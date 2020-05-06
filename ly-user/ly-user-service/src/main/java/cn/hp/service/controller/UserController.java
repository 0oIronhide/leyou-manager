package cn.hp.service.controller;

import cn.hp.user.pojo.User;
import cn.hp.utils.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ironhide
 * @create 2020-05-06-10:14
 */
@RequestMapping("user")
@RestController
public class UserController {

    @GetMapping("list")
    public ResponseEntity<PageResult<User>> userList(){
        return null;
    }

}
