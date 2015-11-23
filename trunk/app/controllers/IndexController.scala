package controllers

import core.mvc.{WAction, Action}
import core.utils.xutil.Validators
import play.api.mvc.Controller

/**
  * 方法描述:首页
  *
  * author 小刘
  * version v1.0
  * date 2015/11/23
  */
class IndexController extends Controller{

    //登录首页
    def index = Action{
        implicit request=>
        Ok(views.html.index())
    }

    //登陆
    def login = Action{
        implicit request =>
        //表单参数
        val map = request.body.asFormUrlEncoded
        //用户名
        val username = map.get.getOrElse("username",Seq(""))(0)
        //密码
        val password = map.get.getOrElse("password",Seq(""))(0)
        //验证码
        val code = map.get.getOrElse("code",Seq(""))(0)
        if(!code.equalsIgnoreCase(request.session.get("vcode").getOrElse("-1"))){
            //验证码错误
        }
        Ok(views.html.index())
    }
}
