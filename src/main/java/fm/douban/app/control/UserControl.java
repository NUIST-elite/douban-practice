package fm.douban.app.control;

import fm.douban.model.User;
import fm.douban.model.UserLoginInfo;
import fm.douban.param.UserQueryParam;
import fm.douban.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class UserControl {
    @Autowired
    private UserService userService;

    private static Map returnData = new HashMap();

    @GetMapping(path="/sign")
    public String signPage(Model model) {
        return "sign";
    }

    @PostMapping(path="/register")
    public String registerAction(@RequestParam String name, @RequestParam String password, @RequestParam String mobile,@RequestParam String conPassword,
                              HttpServletRequest request, HttpServletResponse response) {
        Map returnData = new HashMap();
        // 判断登录名是否已存在
        User regedUser = getUserByLoginName(name);
        if (regedUser != null) {
            returnData.put("result", false);
            returnData.put("message", "login name already exist");
            return "sign";
        }

        User user = new User();
        user.setLoginName(name);
        user.setPassword(password);
        user.setMobile(mobile);
        User newUser = userService.add(user);
        if (newUser != null && StringUtils.hasText(newUser.getId()) && password.equals(conPassword)) {
            returnData.put("result", true);
            returnData.put("message", "register successful");
            return "login";
        } else {
            returnData.put("result", false);
            returnData.put("message", "register failed");
            return "sign";
        }
    }

    @GetMapping(path = "/login")
    public String loginPage(Model model) {
        if(returnData.size() == 0) {
            return "login";
        }
        if(!(boolean)returnData.get("result")) {
            model.addAttribute("message", returnData.get("message"));
        }
        return "login";
    }

    @PostMapping(path = "/authenticate")
    @ResponseBody
    public Map login(@RequestParam String name, @RequestParam String password, HttpServletRequest request,
                     HttpServletResponse response) throws IOException {
        // 根据登录名查询用户
        User regedUser = getUserByLoginName(name);

        // 找不到此登录用户
        if (regedUser == null) {
            returnData.put("result", false);
            returnData.put("message", "userName not correct");
            return returnData;
        }

        if (regedUser.getPassword().equals(password)) {
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            Random r = new Random();
            userLoginInfo.setUserId(String.valueOf(r.nextInt(100000)));
            userLoginInfo.setUserName(name);
            // 取得 HttpSession 对象
            HttpSession session = request.getSession();
            // 写入登录信息
            session.setAttribute("userLoginInfo", userLoginInfo);
            returnData.put("result", true);
            returnData.put("message", "login successful");
            response.sendRedirect("/index");
        } else {
            returnData.put("result", false);
            returnData.put("message", "用户名或密码错误");
        }

        return returnData;
    }

    private User getUserByLoginName(String loginName) {
        User regedUser = null;
        UserQueryParam param = new UserQueryParam();
        param.setLoginName(loginName);
        Page<User> users = userService.list(param);

        // 如果登录名正确，只取第一个，要保证用户名不能重复
        if (users != null && users.getContent() != null && users.getContent().size() > 0) {
            regedUser = users.getContent().get(0);
        }

        return regedUser;
    }
}
