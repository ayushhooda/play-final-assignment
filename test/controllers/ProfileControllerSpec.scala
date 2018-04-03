package controllers

import model.{Assignment, AssignmentInfoRepo, UserForms, UserInfoRepo}
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.{stubControllerComponents, _}
import org.mockito.Mockito.when
import play.api.test.FakeRequest
import play.api.test.CSRFTokenHelper._

import scala.concurrent.Future



class ProfileControllerSpec extends PlaySpec with Mockito {



  "show all user assignment" in {
    val controller = getMockedObject
    val assignment1 = Assignment(0,"assignment1", "asdfg")
    val assignment2 = Assignment(1,"assignment1", "oijhg")
    val assignmentList = List(assignment1, assignment2)

    when(controller.assignmentRepo.viewAssignments()) thenReturn Future.successful(assignmentList)
    val result = controller.profileController.viewAssignments().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }


  "forgot password" in {

  }




  def getMockedObject: TestObjects = {
    val mockedUserFormRepo = mock[UserForms]
    val mockedAssignmentRepo = mock[AssignmentInfoRepo]
    val mockedUserRepo = mock[UserInfoRepo]

    val controller = new ProfileController(stubControllerComponents(), mockedUserFormRepo,
      mockedAssignmentRepo, mockedUserRepo)

    TestObjects(stubControllerComponents(), mockedUserFormRepo, mockedAssignmentRepo,
      mockedUserRepo, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         userForm: UserForms,
                         assignmentRepo: AssignmentInfoRepo,
                         userRepo: UserInfoRepo,
                         profileController: ProfileController)
}
