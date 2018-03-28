package model

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserInfoRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
UserInfoRepoImplementation with UserInfoFirst

trait UserInfoFirst extends HasDatabaseConfigProvider[JdbcProfile]
{
  import profile.api._

  class UserForm(tag: Tag) extends Table[User](tag, "user_details") {

    def id = column[Int]("id")

    def fname: Rep[String] = column[String]("fname")

    def mname: Rep[String] = column[String]("mname")

    def lname: Rep[String] = column[String]("lname")

    def email: Rep[String] = column[String]("email")

    def password: Rep[String] = column[String]("password")

    def mobile: Rep[String] = column[String]("mobile")

    def gender = column[String]("gender")

    def age = column[Int]("age")

    def hobbies = column[String]("hobbies")

    def isEnable = column[Boolean]("isEnabled")

    def isAdmin = column[Boolean]("isAdmin")

    def * : ProvenShape[User] = (id,fname,mname,lname,email,password,mobile,
                                 gender,age,hobbies,isEnable,isAdmin)<>(User.tupled, User.unapply)
  }

  val userQuery: TableQuery[UserForm] = TableQuery[UserForm]
}
trait UserInfoRepoInterface {
  def store(userInformation: User): Future[Boolean]

//  def findByEmailAndPassword(email: String,password:String): Future[Option[User]]

}
trait UserInfoRepoImplementation extends UserInfoRepoInterface {
  self: UserInfoFirst =>

  import profile.api._

  def store(userInformation: User): Future[Boolean] =
    db.run(userQuery += userInformation) map (_ > 0)
}
