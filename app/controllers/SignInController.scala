package controllers

import javax.inject.Inject

import model.UserForms
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class SignInController @Inject()(cc: ControllerComponents, form: UserForms) extends AbstractController(cc) with I18nSupport {

  val message = cc.messagesApi

  def signIn = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin(form.signInForm))
  }

}
