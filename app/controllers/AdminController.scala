package controllers

import javax.inject.Inject

import model.{Assignment, AssignmentInfoRepo, UserForms}
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class AdminController @Inject()(cc: ControllerComponents, form: UserForms, assignmentRepo: AssignmentInfoRepo) extends AbstractController(cc) with I18nSupport {

  def addAssignment() = Action.async { implicit request: Request[AnyContent] =>
    form.assignmentForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.addAssignment(formWithErrors)))
      },
      assignment => {
        assignmentRepo.addAssignment(Assignment(0, assignment.title, assignment.description)).map {
          case false => InternalServerError("Failed to add assignment")
          case true => Redirect(routes.AdminController.showAssignments()).flashing("success" ->
            "Assignment added successfully.")
        }
      })
  }

  def showAssignments() = Action { implicit request: Request[AnyContent] =>
    val assignmentList = assignmentRepo.viewAssignments()
    val result = Await.result(assignmentList, Duration.Inf)
    Ok(views.html.viewAssignments(result))
  }

}
