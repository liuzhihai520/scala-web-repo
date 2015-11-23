package core.auth

import play.api.libs.json.Json

/**
  * 用户登录标示类
  * @param username 用户名称
  * @param nickname 用户昵称
  */
case class Identity(
                     id: Int,
                     username: String = "",
                     nickname: String = ""
                   )

object Identity {
  implicit val fmt = Json.format[Identity]
}
