package model

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

/**
  * 方法描述:TODO
  *
  * author 小刘
  * version v1.0
  * date 2015/11/25
  */

case class SysUser (id:Long,username:String,accountname:String,password:String,salt:String,satus:Int,createTime:String,description:String)
object SysUser {
    val sysUser = {
        get[Long]("id")~
        get[String]("username")~
        get[String]("accountname")~
        get[String]("password")~
        get[String]("salt")~
        get[Int]("status")~
        get[String]("createTime")~
        get[String]("description") map{
            case id~username~accountname~password~salt~status~createTime~description =>
                SysUser(id,username,accountname,password,salt,status,createTime,description)
        }
    }

    //登录
    def login(username:String,password:String) = DB.withConnection("default",true){
        implicit con =>
        val user = SQL(
            """
              |select * from t_sys_user where username = {username} and password = {password}
            """.stripMargin
        ).on('username -> username,'password -> password).as(sysUser.singleOpt)
        user
    }
}
