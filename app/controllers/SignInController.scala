package controllers

import javax.inject.Inject

import model.{User, UserForms, UserInfoRepo, UserSignIn}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SignInController @Inject()(cc: ControllerComponents, form: UserForms, userRepo: UserInfoRepo) extends AbstractController(cc) with I18nSupport {

  val message = cc.messagesApi

  def signIn = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin(form.signInForm))
  }

  def userPost = Action async { implicit request: Request[AnyContent] =>
    form.signInForm.bindFromRequest().fold(
      formsWithErrors => {
        Future.successful(BadRequest(views.html.signin(formsWithErrors)))
      },
      userData => {
        val data = UserSignIn(userData.email, userData.password)
        userRepo.findUser(data.email, data.password).map {
          case true => Redirect(routes.SignUpController.profile()).withSession("email" -> userData.email)
              .flashing("success" -> "Welcome back !!")
          case false => Redirect(routes.HomeController.index())
        }
      }
    )
  }

}
