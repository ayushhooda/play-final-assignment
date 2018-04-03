package controllers

import model.{ForgotPassword, UserForms, UserInfoRepo}
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import org.mockito.Mockito.when
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers.{OK, status, stubControllerComponents}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._



class SignInControllerTest extends PlaySpec with Mockito {
  implicit val duration: Timeout = 20 seconds

  "go to User SignIn page" in {
    val controller = getMockedObject
    when(controller.userForm.signInForm) thenReturn { val form = new UserForms{}
    form.signInForm}
    val result = controller.signInController.signIn.apply(FakeRequest()
      .withCSRFToken)
    status(result) must equal(OK)
  }

  "user changed password" in {
    val controller = getMockedObject

    val password = ForgotPassword( "abc@gmail.com","qaz","qaz")

    val passwordForm = new UserForms {}.forgotPasswordForm.fill(password)

    when(controller.userForm.forgotPasswordForm) thenReturn passwordForm
    when(controller.userRepo.updatePassword(password.email, password.newPassword)) thenReturn Future.successful(true)

    val request = FakeRequest("GET", "/changePassword").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea","email"-> "abc@gmail.com",
      "newPassword"-> "qaz","confirmPassword"-> "qaz")
      .withCSRFToken

    val result = controller.signInController.changePassword().apply(request)
    status(result) must equal(400)

  }



  def getMockedObject: TestObjects = {
    val mockedUserFormRepo = mock[UserForms]
    val mockedUserRepo = mock[UserInfoRepo]

    val controller = new SignInController(stubControllerComponents(), mockedUserFormRepo, mockedUserRepo)

    TestObjects(stubControllerComponents(), mockedUserFormRepo,
      mockedUserRepo, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         userForm: UserForms,
                         userRepo: UserInfoRepo,
                         signInController: SignInController)
}
