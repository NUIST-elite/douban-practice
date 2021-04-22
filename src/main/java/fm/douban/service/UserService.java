package fm.douban.service;

import fm.douban.model.User;
import fm.douban.param.UserQueryParam;
import org.springframework.data.domain.Page;

public interface UserService {
    // 增加用户
    User add(User user);

    // 根据主键查询
    User get(String id);

    // 支持分页的条件查询
    Page<User> list(UserQueryParam param);

    // 修改用户信息
    boolean modify(User user);

    // 删除用户
    boolean delete(String id);
}
