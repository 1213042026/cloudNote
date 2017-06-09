package cn.tedu.note.web;



import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;


import cn.tedu.note.entity.User;
import cn.tedu.note.service.UserService;
import cn.tedu.note.util.JsonResult;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	//返回值: {state:0,data:{id...}}
	//返回值: {state:1,message:"用户名..."}
	public JsonResult<User> login(String name, 
			String password, 
			HttpServletRequest req){
		User user=
			userService.login(name, password);
		//将用户信息保存到Session中
		req.getSession().setAttribute(
				"loginUser", user);
		return new JsonResult<User>(user); 
	}
	/**
	 * 注册控制器
	 */
	@RequestMapping("/regist.do")
	@ResponseBody
	public JsonResult<User> regist(
			String name, 
			String password, 
			String confirm, 
			String nick){
		User user=userService.regist(
			name, nick, password, confirm);
		return new JsonResult<User>(user);
	}
		
	//UserController
	@RequestMapping("/heartbeat.do")
	@ResponseBody
	//心跳检测控制器方法，目的保持session的新鲜
	public JsonResult<String> heartbeat(){
		//System.out.println("OK"); 
		return new JsonResult<String>("OK");
	}
}
