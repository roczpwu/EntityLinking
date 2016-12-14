package com.roc.core.user;

import com.opensymphony.xwork2.ActionContext;
import com.roc.core.BLMessage;
import com.roc.core.Utils.NumberUtil;
import com.roc.core.Utils.StringUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * User: rocwu
 * Date: 2016/10/24
 * Time: 11:01 am
 * Desc: 用户对象类
 */
public class UserManager {

    // session中登录用户信息的key
    private static final String LOGIN_USER_KEY = "loginUser";
    // cookie中authKey的key
    public static final String AUTH_CODE_KEY = "authKey";

    private static UserBL userBL;

    static {
        userBL = (UserBL) WebApplicationContextUtils.
                getRequiredWebApplicationContext(
                        ServletActionContext.getRequest().
                                getSession().getServletContext()
                ).getBean("userBL");
    }

    /**
     * 获取当前session中的登录用户
     * @return 登录用户信息
     */
    public static UserDTO getUser() {
        ActionContext context = ActionContext.getContext();
        UserDTO loginUser = null;
        if (context.getSession().containsKey(UserManager.LOGIN_USER_KEY))
            loginUser = (UserDTO) context.getSession().get(UserManager.LOGIN_USER_KEY);
        return loginUser;
    }

    /**
     * 判断当前用户是否为游客
     * @return true 游客 ／ false 登录
     */
    public static boolean isGuest() {
        ActionContext context = ActionContext.getContext();
        return !context.getSession().containsKey(UserManager.LOGIN_USER_KEY);
    }

    /**
     * 使用用户名和密码登录
     * @param userName  用户名
     * @param password  密码
     * @param autoLogin 是否保持登录态
     * @return ['success'=>true/false, 'message'=>tips, 'data'=>cookie id(if autoLogin)]
     */
    public static BLMessage login(String userName, String password, boolean autoLogin) {
        BLMessage message = new BLMessage();
        UserDTO user = null;
        if (!UserManager.isGuest()) {
            message.setSuccess(false);
            message.setMessage("当前已登录。");
        } else if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
            message.setSuccess(false);
            message.setMessage("用户名和密码不能为空。");
        } else {
            user = (UserDTO) userBL.getByCondition(
                    UserDTO.User_Name+ "='"+StringUtil.mysqlEscapeStr(userName)+"'");
            if (user == null) {
                message.setSuccess(false);
                message.setMessage("用户名不存在。");
            } else {
                String passwordHash = "";
                try {
                    passwordHash = StringUtil.getMd5(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (passwordHash.equals(user.getPassword_hash())) {
                    message.setSuccess(true);
                    message.setMessage("登录成功。");
                } else {
                    message.setSuccess(false);
                    message.setMessage("密码错误。");
                }
            }
        }
        if (message.isSuccess() && autoLogin) {
            user.setAuth_key(NumberUtil.generateUUID());
            message.setData(user.getAuth_key());
            userBL.save(user);
        }
        if (message.isSuccess()) {
            ActionContext.getContext().getSession().
                    put(UserManager.LOGIN_USER_KEY, user);
        }
        return message;
    }

    /**
     * 使用authKey登录
     * @param authKey authKey
     * @return ['success'=>true/false, 'message'=>tips]
     */
    public static BLMessage login(String authKey) {
        BLMessage message = new BLMessage();
        UserDTO user = null;
        if (!UserManager.isGuest()) {
            message.setSuccess(false);
            message.setMessage("当前已登录。");
        } else if (StringUtil.isEmpty(authKey)) {
            message.setSuccess(false);
            message.setMessage("authKey为空。");
        } else {
            user = (UserDTO) userBL.getByCondition(
                    UserDTO.Auth_Key+ "='"+StringUtil.mysqlEscapeStr(authKey)+"'");
            if (user == null) {
                message.setSuccess(false);
                message.setMessage("校验authKey失败。");
            } else {
                message.setSuccess(true);
                message.setMessage("登录成功。");
            }
        }
        if (message.isSuccess()) {
            ActionContext.getContext().getSession().
                    put(UserManager.LOGIN_USER_KEY, user);
        }
        return message;
    }

    /**
     * 退出登录
     * @return
     */
    public static BLMessage loginout() {
        BLMessage message = new BLMessage();
        UserDTO user = UserManager.getUser();
        if (user == null) {
            message.setSuccess(false);
            message.setMessage("当前已处于游客状态。");
        } else {
            user = (UserDTO) userBL.get(user.getFid());
            if (user == null)
                throw new RuntimeException("用户在db中不存在");
            user.setAuth_key(null);
            userBL.save(user);
            ActionContext.getContext().getSession().
                    remove(UserManager.LOGIN_USER_KEY);
            message.setSuccess(true);
            message.setMessage("已退出登录。");
        }
        return message;
    }

    public static UserDTO getUserById(int userId) {
        return (UserDTO) userBL.get(userId);
    }
}
