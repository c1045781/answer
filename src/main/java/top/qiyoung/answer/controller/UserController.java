package top.qiyoung.answer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.qiyoung.answer.model.Pagination;
import top.qiyoung.answer.model.User;
import top.qiyoung.answer.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/check")
    public String checkUser(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "order", defaultValue = "id") String order,
                            Model model) {
        Pagination pagination = userService.getUserList(currentPage, size, search, type, order);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        return "user/user";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "user/add-user";
    }

    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(User user, Model model,
                              @RequestParam("avatarImg") MultipartFile avatarImg) throws IOException {
        String filename = avatarImg.getOriginalFilename();
        String[] split = filename.split("\\.");
        String suffix = split[split.length - 1];
        if (!avatarImg.isEmpty() && !(suffix.equals("jpg") || suffix.equals("png") || suffix.equals("jpeg"))) {
            model.addAttribute("massage", "图片格式错误");
            return "user/add-user";
        }

        if (user.getId() == null) {
            int insert = userService.insert(user, avatarImg);
            if (insert == 0) {
                model.addAttribute("massage", "添加失败,账号已存在");
                return "user/add-user";
            } else if (insert == 2) {
                model.addAttribute("massage", "图片上传失败请重新上传");
                return "user/add-user";
            } else {
                return "redirect:/user/check";
            }
        } else {
            userService.update(user, avatarImg);
            return "redirect:/user/check";
        }
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/add-user";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public void delete(@RequestBody User user) {
        userService.deleteById(user.getId());
    }
}
