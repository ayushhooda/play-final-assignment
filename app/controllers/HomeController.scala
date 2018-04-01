package controllers

import javax.inject.Inject

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def logout = Action {
    Redirect(routes.SignInController.signIn()).withNewSession.flashing(
      "failure" -> "You've been logged out"
    )
  }

}
