package controllers

import model._
import org.mockito.Mockito.when
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{stubControllerComponents, _}

import scala.concurrent.Future


class AdminControllerSpec extends PlaySpec with Mockito {


  "should store a assignment" in {
    val controller = getMockedObject

    val assignment = UserAssignment("assignment1", "play application")

    val assignmentForm = new  UserForms{}.assignmentForm.fill(assignment)
    val payload = Assignment(0, "assignment1", "play application")


    when(controller.userForm.assignmentForm) thenReturn assignmentForm
    when(controller.assignmentInfoRep.addAssignment(payload)) thenReturn Future.successful(true)


    val request = FakeRequest(POST, "/signUp").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
      "title" -> "assignment1", "description" -> "play application")
      .withCSRFToken

    val result = controller.adminController.addAssignment().apply(request)
    status(result) must equal(303)
  }

  "unable to store a assignment" in {
    val controller = getMockedObject

    val assignment = UserAssignment("assignment1", "play application")

    val assignmentForm = new  UserForms{}.assignmentForm.fill(assignment)
    val payload = Assignment(0, "assignment1", "play application")


    when(controller.userForm.assignmentForm) thenReturn assignmentForm
    when(controller.assignmentInfoRep.addAssignment(payload)) thenReturn Future.successful(false)


    val request = FakeRequest(POST, "/signUp").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
      "title" -> "assignment1", "description" -> "play application")
      .withCSRFToken

    val result = controller.adminController.addAssignment().apply(request)
    status(result) must equal(500)
  }


  "enable disable user" in {
    val controller = getMockedObject
    val email = "ayushhooda14@gmail.com"
    when(controller.userInfoRepo.enableOrDisableUser(email, true)) thenReturn Future.successful(true)
    val result = controller.adminController.enableOrDisableUser(email, false).apply(FakeRequest().withCSRFToken)
    status(result) must equal(303)
  }

  "unable to enable disable user" in {
    val controller = getMockedObject
    val email = "ayushhooda14@gmail.com"
    when(controller.userInfoRepo.enableOrDisableUser(email, true)) thenReturn Future.successful(false)
    val result = controller.adminController.enableOrDisableUser(email, false).apply(FakeRequest().withCSRFToken)
    status(result) must equal(500)
  }


  "go to admin Profile page" in {
    val controller = getMockedObject
    val result = controller.adminController.profile().apply(FakeRequest()
      .withCSRFToken)
    status(result) must equal(OK)
  }

  "show all user" in {
    val controller = getMockedObject
    val user1 = User(0, "Ayush", "", "Hooda", "ayush@gmail.com",
      "88888888u", "9999999999", "Male", 23, "Cricket", true, true)
    val user2 = User(1, "Abc", "", "Def", "abc@gmail.com",
      "8d6c5597d", "8888888888", "Male", 23, "Football", false, true)
    val userList = List(user1, user2)

    when(controller.userInfoRepo.getAllUsers) thenReturn Future.successful(userList)
    val result = controller.adminController.showUsers().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }


  "delete not assignment" in {
    val controller = getMockedObject
    val id = 1
    when(controller.assignmentInfoRep.deleteAssignment(id)) thenReturn Future.successful(true)
    val result = controller.adminController.deleteAssignment(id).apply(FakeRequest().withCSRFToken)
    status(result) must equal(303)
  }

  "delete assignment" in {
    val controller = getMockedObject
    val id = 10
    when(controller.assignmentInfoRep.deleteAssignment(id)) thenReturn Future.successful(false)
    val result = controller.adminController.deleteAssignment(id).apply(FakeRequest().withCSRFToken)
    status(result) must equal(500)
  }


  "show all assignment" in {
    val controller = getMockedObject
    val assignment1 = Assignment(0, "assignment1", "make an app of play")
    val assignment2 = Assignment(1, "assignment1", "make an app of play")
    val assignmentList = List(assignment1, assignment2)

    when(controller.assignmentInfoRep.viewAssignments()) thenReturn Future.successful(assignmentList)
    val result = controller.adminController.showAssignments().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  def getMockedObject: TestObjects = {
    val mockedUserForm = mock[UserForms]
    val mockedAssignmentInfoRepo = mock[AssignmentInfoRepo]
    val mockedUserInfoRepo = mock[UserInfoRepo]

    val controller = new AdminController(stubControllerComponents(), mockedUserForm, mockedAssignmentInfoRepo, mockedUserInfoRepo)

    TestObjects(stubControllerComponents(), mockedUserForm, mockedAssignmentInfoRepo, mockedUserInfoRepo, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         userForm: UserForms,
                         assignmentInfoRep: AssignmentInfoRepo,
                         userInfoRepo: UserInfoRepo,
                         adminController: AdminController)

}
