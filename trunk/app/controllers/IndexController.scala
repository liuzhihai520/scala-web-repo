package controllers

import core.mvc.{WAction, Action}
import form.LoginForm
import model.SysUser
import play.api.i18n.{Messages}
import play.api.mvc.Controller
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

/**
  * 方法描述:首页
  *
  * author 小刘
  * version v1.0
  * date 2015/11/23
  */
class IndexController extends Controller {

    //登录首页
    def index = WAction{
        implicit request =>
        Ok(views.html.index(LoginForm.loginForm))
    }

    //登陆
    def login = WAction{
        implicit dataView =>
        LoginForm.loginForm.bindFromRequest.fold(
            error => {
                BadRequest(views.html.index(error))
            },
            data => {
                val code = dataView.request.session.get("vcode").getOrElse("-1")
                if(!data.code.equals(code)){
                    BadRequest(views.html.index(LoginForm.loginForm.withError("codeError","")))
                }else{
                    val user = SysUser.login(data.username,data.password)
                    if(user == None){
                        BadRequest(views.html.index(LoginForm.loginForm.withError("usrError",Messages("Login.error"))))
                    }else{
                        Ok()
                    }
                }
            }
        )
    }
}
