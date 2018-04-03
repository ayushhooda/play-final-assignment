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

class SignUpControllerTest extends PlaySpec with Mockito {
  implicit val duration: Timeout = 20 seconds

  "go to User Profile page" in {
    val controller = getMockedObject
    val result = controller.signUpController.profile.apply(FakeRequest()
      .withCSRFToken)
    status(result) must equal(OK)
  }

  "go to User SignUp page" in {
    val controller = getMockedObject
    when(controller.userForm.signUpForm) thenReturn { val form = new UserForms{}
      form.signUpForm}
    val result = controller.signUpController.signUp.apply(FakeRequest()
      .withCSRFToken)
    status(result) must equal(OK)
  }


  def getMockedObject: TestObjects = {
    val mockedUserFormRepo = mock[UserForms]
    val mockedUserRepo = mock[UserInfoRepo]

    val controller = new SignUpController(stubControllerComponents(), mockedUserFormRepo, mockedUserRepo)

    TestObjects(stubControllerComponents(), mockedUserFormRepo,
      mockedUserRepo, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         userForm: UserForms,
                         userRepo: UserInfoRepo,
                         signUpController: SignUpController)
}
