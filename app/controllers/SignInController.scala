package controllers

import javax.inject.Inject

import model.{UserForms, UserInfoRepo, UserSignIn}
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class SignInController @Inject()(cc: ControllerComponents, form: UserForms, userRepo: UserInfoRepo) extends AbstractController(cc) with I18nSupport {

  val message = cc.messagesApi

  /**
    * @return - open signin page
    */
  def signIn: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin(form.signInForm))
  }

  /**
    * @return - signin
    */
  def userPost: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    form.signInForm.bindFromRequest().fold(
      formsWithErrors => {
        Future.successful(BadRequest(views.html.signin(formsWithErrors)))
      },
      userData => {
        val data = UserSignIn(userData.email, userData.password)

        userRepo.isUserValid(data.email, data.password).map {
          case true =>
            if (Await.result(userRepo.isAdmin(data.email), Duration.Inf)) {
              Redirect(routes.AdminController.profile()).withSession("email" -> userData.email)
            }
            else if (!Await.result(userRepo.isUserEnabled(data.email), Duration.Inf)) {
              Redirect(routes.SignInController.signIn()).flashing("failure" -> "disabled by admin")
            }
            else {
              Redirect(routes.SignUpController.profile()).withSession("email" -> userData.email)
                .flashing("success" -> "Welcome back !!")
            }
          case false =>
            Redirect(routes.SignUpController.signUp()).flashing("failure" -> "Please register first !!!")

        }
      }
    )
  }

  /**
    * @return - change password
    */
  def changePassword: Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    form.forgotPasswordForm.bindFromRequest().fold(
      formsWithErrors => {
        Future.successful(BadRequest(views.html.forgotPassword(formsWithErrors)))
      },
      userData => {
        userRepo.checkUserExists(userData.email).map {
          case true =>
            userRepo.updatePassword(userData.email, userData.newPassword)
            Redirect(routes.SignInController.signIn()).flashing("failure" -> "Password successfully changed")
          case false => Redirect(routes.ProfileController.forgotPassword())
            .flashing("success" -> "Email entered donot exist !!!")
        }
      }
    )
  }

}
