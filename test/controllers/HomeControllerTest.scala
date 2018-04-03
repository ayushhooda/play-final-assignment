package controllers

import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers.{stubControllerComponents, _}

class HomeControllerTest extends PlaySpec with Mockito {


  "go to User Profile page" in {
    val controller = getMockedObject
    val result = controller.homeController.index().apply(FakeRequest()
      .withCSRFToken)
    status(result) must equal(OK)
  }


  "user should logout" in {
    val controller = getMockedObject
    val result = controller.homeController.logout().apply(FakeRequest().withSession())
    status(result) must equal(303)
  }



  def getMockedObject: TestObjects = {

    val controller = new HomeController(stubControllerComponents())

    TestObjects(stubControllerComponents(), controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         homeController: HomeController)

}
