package controllers

import model.{AssignmentInfoRepo, UserForms, UserInfoRepo}
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers.stubControllerComponents

import scala.concurrent.Future

class AdminControllerSpec extends PlaySpec with Mockito {
//
//  "should store a user" in {
//    val controller = getMockedObject
//
//    val userInformation = UserInformation("ankit", "barthwal", "test@example.com")
//
//    val userForm = new UserForm {}.userInfoForm.fill(userInformation)
//    val payload = controller.userInfoRepo.UserInfo("ankit", "barthwal", "test@example.com")
//
//
//    when(controller.userForm.userInfoForm) thenReturn userForm
//    when(controller.userInfoRepo.getUser("test@example.com")) thenReturn Future.successful(None)
//    when(controller.userInfoRepo.store(payload)) thenReturn Future.successful(Done)
//
//
//    val request = FakeRequest(POST, "/store").withFormUrlEncodedBody("csrfToken"
//      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea", "fname" -> "ankit",
//      "lname" -> "barthwal", "email" -> "test@example.com")
//      .withCSRFToken
//
//    val result = controller.homeController.storeData(request)
//    status(result) must equal(OK)
//  }
//
//  def getMockedObject: TestObjects = {
//    val mockedUserForm = mock[UserForms]
//    val mockedAssignmentInfoRepo = mock[AssignmentInfoRepo]
//    val mockedUserInfoRepo= mock[UserInfoRepo]
//
//    val controller = new AdminController(stubControllerComponents(), mockedUserForm, mockedAssignmentInfoRepo, mockedUserInfoRepo)
//
//    TestObjects(stubControllerComponents(), mockedUserForm, mockedUserInfoRepo, controller)
//  }
//
//  case class TestObjects(controllerComponent: ControllerComponents,
//                         userForm: UserForms,
//                         assignmentInfoRep: AssignmentInfoRepo,
//                         userInfoRepo: UserInfoRepo,
//                         adminController: AdminController)



}
