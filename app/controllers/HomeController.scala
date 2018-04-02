package controllers

import javax.inject.Inject

import play.api.i18n.I18nSupport
import play.api.mvc._

class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def logout: Action[AnyContent] = Action {
    Redirect(routes.SignInController.signIn()).withNewSession.flashing(
      "failure" -> "You've been logged out"
    )
  }

}
