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

    def id: Rep[Int] = column[Int]("id")

    def fname: Rep[String] = column[String]("fname")

    def mname: Rep[String] = column[String]("mname")

    def lname: Rep[String] = column[String]("lname")

    def email: Rep[String] = column[String]("email")

    def password: Rep[String] = column[String]("password")

    def mobile: Rep[String] = column[String]("mobile")

    def gender: Rep[String] = column[String]("gender")

    def age: Rep[Int] = column[Int]("age")

    def hobbies: Rep[String] = column[String]("hobbies")

    def isEnable: Rep[Boolean] = column[Boolean]("isEnabled")

    def isAdmin: Rep[Boolean] = column[Boolean]("isAdmin")

    //scalastyle:off
    def * : ProvenShape[User] = (id,fname,mname,lname,email,password,mobile,
                                 gender,age,hobbies,isEnable,isAdmin)<>(User.tupled, User.unapply)

    //scalastyle:on
  }

  val userQuery: TableQuery[UserForm] = TableQuery[UserForm]
}

trait UserInfoRepoInterface {
  def store(userInformation: User): Future[Boolean]

  def isUserValid(email: String,password:String): Future[Boolean]

  def isUserEnabled(email: String): Future[Boolean]

  def checkUserExists(email: String): Future[Boolean]

  def updatePassword(email: String, password: String): Future[Boolean]

  def isAdmin(email: String): Future[Boolean]

  def enableOrDisableUser(email: String, value: Boolean): Future[Boolean]

}

trait UserInfoRepoImplementation extends UserInfoRepoInterface {
  self: UserInfoFirst =>

  import profile.api._

  def store(userInformation: User): Future[Boolean] =
    db.run(userQuery += userInformation) map (_ > 0)

  def isUserValid(email: String, password: String): Future[Boolean] = {
    db.run(userQuery
      .filter(data => data.email.toLowerCase === email.toLowerCase && data.password === password)
      .to[List].result).map(_.nonEmpty)
  }

  def isUserEnabled(email: String): Future[Boolean] = {
    db.run(userQuery.filter(user => user.email === email && user.isEnable).to[List].result).map(_.nonEmpty)
  }

  def checkUserExists(email: String): Future[Boolean] = {
    db.run(userQuery.filter(_.email === email).to[List].result).map(_.nonEmpty)
  }

  def updatePassword(email: String, password: String): Future[Boolean] = {
    db.run(userQuery.filter(_.email === email).map(_.password).update(password)).map(_ > 0)
  }

  def isAdmin(email: String): Future[Boolean] = {
    db.run(userQuery.filter(x => x.email === email && x.isAdmin).to[List].result).map(_.nonEmpty)
  }

  // Admin's view all users method
  def getAllUsers: Future[List[User]] = {
    db.run(userQuery.filter(_.isAdmin === false).to[List].result)
  }

  // User's update details method
  def updateUserDetails(email: String, updatedData: UserProfile): Future[Boolean] = {
    db.run(userQuery.filter(_.email === email).map(user => (user.fname, user.mname, user.lname,
      user.mobile, user.age, user.hobbies)).update(updatedData.fname, updatedData.mname,
      updatedData.lname, updatedData.mobile, updatedData.age, updatedData.hobbies)).map(_ > 0)
  }

  // User's view profile method
  def getUserDetails(email: String): Future[User] = {
    db.run(userQuery.filter(_.email === email)
      .to[List].result.head)
  }

  def enableOrDisableUser(email: String, value: Boolean): Future[Boolean] = {
    db.run(userQuery.filter(_.email === email).map(_.isEnable).update(value)).map(_ > 0)
  }

}
