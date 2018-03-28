package model

import play.api.data.Form
import play.api.data.Forms.{email, mapping, nonEmptyText, number, text}
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import scala.util.matching.Regex

case class UserSignIn(email: String, password: String)

case class UserSignUp(fname: String,mname: String, lname: String, email: String,
                      password: String, confirmPwd: String, mobile: String, gender: String,
                      age: Int, hobbies: String)

case class Assignment (title:String, description:String)

case class ForgotPassword(email: String, newPassword: String, confirmPassword: String)

class UserForms {

  val nameRegex = "[a-zA-Z]"

  val signInForm: Form[UserSignIn] = Form(mapping(
    "email" -> text.verifying("Enter Email", _.nonEmpty),
    "password" -> text.verifying("Enter Password", _.nonEmpty)
  )(UserSignIn.apply)(UserSignIn.unapply))

  val signUpForm: Form[UserSignUp] = Form(mapping(
    "fname" -> nonEmptyText.verifying(allLettersConstraint),
    "mname" -> text,
    "lname" -> nonEmptyText.verifying(allLettersConstraint),
    "email" -> email,
    "password" -> text.verifying(passwordCheckConstraint()),
    "confirmPwd" -> text,
    "mobile" -> text.verifying(checkLengthConstraint),
    "gender" -> text,
    "age" -> number(min = 18, max = 75),
    "hobbies" -> text
  )(UserSignUp.apply)(UserSignUp.unapply)
    .verifying ("Password must match Confirm Password",verify =>verify match
    {case user => passwordCheck(user.password,user.confirmPwd)})
  )


  val assignmentForm = Form(mapping(
    "title" -> text.verifying("Enter title", _.nonEmpty),
    "description" -> text.verifying("Enter description", _.nonEmpty)
  )(Assignment.apply)(Assignment.unapply))

  val forgotPasswordForm = Form(mapping(
    "email" -> email,
    "newPassword" -> nonEmptyText.verifying(),
    "confirmPassword" -> nonEmptyText
  )(ForgotPassword.apply)(ForgotPassword.unapply)
    verifying("Password fields do not match", forget => forget.newPassword == forget.confirmPassword))

  val allNumbers: Regex = """\d*""".r
  val allLetters: Regex = """[A-Za-z]*""".r

  def passwordCheckConstraint(): Constraint[String] = {
    Constraint[String]("constraint.password") {
      plainText =>
        val errors = plainText match {
          case plaintext if plaintext.length < 8 => Seq(ValidationError("Password should be more than 8 characters"))
          case allNumbers() => Seq(ValidationError("Password is all numbers"))
          case allLetters() => Seq(ValidationError("Password is all letters"))
          case _ => Nil
        }
        if (errors.isEmpty) {
          Valid
        } else {
          Invalid(errors)
        }
    }
  }

  def passwordCheck(password:String,confirmPassword:String): Boolean = password == confirmPassword

  def allLettersConstraint: Constraint[String] = {
    Constraint("usernameCheck")(
      name => if (name matches """[A-Za-z]+""")
        Valid
      else
        Invalid(ValidationError("name can only contain letters"))
    )
  }

  def checkLengthConstraint: Constraint[String] = {
    Constraint("mobile")({
      mobile =>
        if (mobile.length == 10 && mobile.matches("""[0-9]+"""))
          Valid
        else
          Invalid(ValidationError("mobile number must be 10 digits"))
    })
  }

}