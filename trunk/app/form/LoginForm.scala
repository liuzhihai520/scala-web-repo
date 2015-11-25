package form

import play.api.data._
import play.api.data.Forms._

/**
  * 方法描述:TODO
  *
  * author 小刘
  * version v1.0
  * date 2015/11/25
  */
case class LoginForm(username:String,password:String,code:String)
object LoginForm {
    val loginForm = Form(
        mapping(
            "username" -> nonEmptyText(minLength = 5,maxLength = 20),
            "password" -> nonEmptyText(minLength = 6,maxLength = 20),
            "code" -> nonEmptyText(minLength = 4,maxLength = 4)
        )(LoginForm.apply)(LoginForm.unapply)
    )
}
