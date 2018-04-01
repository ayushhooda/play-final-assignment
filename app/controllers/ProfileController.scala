package controllers

import javax.inject.Inject

import model._
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class ProfileController @Inject()(cc: ControllerComponents, userForm: UserForms, assignmentRepo: AssignmentInfoRepo,
                                  userRepo: UserInfoRepo)
  extends AbstractController(cc) with I18nSupport {

  def forgotPassword() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.forgotPassword(userForm.forgotPasswordForm))
  }

  def viewAssignments() = Action { implicit request: Request[AnyContent] =>
    val assignmentList = assignmentRepo.viewAssignments()
    val result = Await.result(assignmentList, Duration.Inf)
    Ok(views.html.viewUserAssignments(result))
  }

  def getProfileDetails: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val email = request.session.get("email").get
    val userDetails: Future[User] = userRepo.getUserDetails(email)
    userDetails.map {
      data =>
        val profile = UserProfile(data.fname, data.mname, data.lname,
          data.mobile, data.age, data.hobbies)
        Ok(views.html.updateProfile(userForm.userProfileForm.fill(profile)))
    }
  }

  def updateDetails = Action.async { implicit request: Request[AnyContent] =>
    userForm.userProfileForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.userProfile()))
      },
      profile => {
        val updatedDetails = UserProfile(profile.fname, profile.mname, profile.lname,
          profile.mobile, profile.age, profile.hobbies)
        val userUpdated = userRepo.updateUserDetails(request.session.get("email").get, updatedDetails)
        userUpdated.map {
          case true =>
            Redirect(routes.ProfileController.getProfileDetails())
          case false => InternalServerError("user details could not be updated")
        }
      })
  }

}
