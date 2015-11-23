package controllers

import core.mvc.Action
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
}
