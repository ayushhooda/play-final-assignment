package controllers

import javax.inject.Inject

import model.{User, UserForms, UserInfoRepo}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SignUpController @Inject()(cc: ControllerComponents, form: UserForms, userRepo: UserInfoRepo) extends AbstractController(cc) with I18nSupport {

  val message = cc.messagesApi

  def signUp = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(form.signUpForm))
  }

  def signIn = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin(form.signInForm))
  }

  def profile = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.profile())
  }


  def userPost = Action.async { implicit request: Request[AnyContent] =>
    form.signUpForm.bindFromRequest().fold(
      formsWithErrors => {
        Future.successful(BadRequest(views.html.signup(formsWithErrors)))
      },
      userData => {
        val data = User(0, userData.fname, userData.mname, userData.lname,
          userData.email, userData.password, userData.mobile, userData.gender,
          userData.age, userData.hobbies, true, false)
        userRepo.store(data).map {
          case true => Redirect(routes.SignUpController.profile())
          case false => Redirect(routes.HomeController.index())
        }
      }
    )
  }

}
