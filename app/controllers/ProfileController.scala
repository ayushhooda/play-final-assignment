package controllers

import javax.inject.Inject

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import model.UserForms
import play.api.i18n.I18nSupport
import play.api.mvc._

class ProfileController @Inject()(cc: ControllerComponents, userForm: UserForms) extends AbstractController(cc) with I18nSupport{

//  def assignments = Action {
//
//  }

  def forgotPassword() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.forgotPassword(userForm.forgotPasswordForm))
  }

//  def adminProfile = Action { implicit request: Request[AnyContent] =>
//
//  }

}
