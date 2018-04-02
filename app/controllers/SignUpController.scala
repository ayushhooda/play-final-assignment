package controllers

import javax.inject.Inject

import model.{User, UserForms, UserInfoRepo}
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SignUpController @Inject()(cc: ControllerComponents, form: UserForms, userRepo: UserInfoRepo) extends AbstractController(cc) with I18nSupport {

  val message = cc.messagesApi

  def signUp: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(form.signUpForm))
  }

  def profile: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.userProfile())
  }

  def userPost: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    form.signUpForm.bindFromRequest().fold(
      formsWithErrors => {
        Future.successful(BadRequest(views.html.signup(formsWithErrors)))
      },
      userData => {
        userRepo.checkUserExists(userData.email).map {
          case true => Redirect(routes.SignInController.signIn()).flashing("failure" -> "You are already registered")
          case false =>
            val data = User(0, userData.fname, userData.mname, userData.lname,
              userData.email, userData.password, userData.mobile, userData.gender,
              userData.age, userData.hobbies, true, false)
            userRepo.store(data)
            Redirect(routes.SignUpController.profile()).withSession("email" -> userData.email)
              .flashing("success" -> "You are successfully registered")
        }
      }
    )
  }

}
