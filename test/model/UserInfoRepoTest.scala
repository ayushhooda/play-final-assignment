package model

import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class UserInfoRepoTest extends Specification {

  val repo = new ModelTest[UserInfoRepo]

  "user info repository" should {
    "add detail of an user" in {
      val user = User(0, "Ayush", "kumar", "Hooda", "ayushhooda14@gmail.com", "abc",
        "9991", "Male", 23, "xyz", true, true)
      val storeResult = Await.result(repo.repository.store(user), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "check validity of an user" in {
      val storeResult = Await.result(repo.repository.isUserValid("ayushhooda14@gmail.com", "abc"), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "check if user is enabled" in {
      val storeResult = Await.result(repo.repository.isUserEnabled("ayushhooda14@gmail.com"), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "check if user exists" in {
      val storeResult = Await.result(repo.repository.checkUserExists("ayushhooda14@gmail.com"), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "should update password" in {
      val storeResult = Await.result(repo.repository.updatePassword("ayushhooda14@gmail.com", "bcd"), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "check if user is Admin" in {
      val storeResult = Await.result(repo.repository.isAdmin("ayushhooda14@gmail.com"), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "view all users" in {
      val storeResult = Await.result(repo.repository.getAllUsers, Duration.Inf)
      storeResult must equalTo(List())
    }
  }

  "user info repository" should {
    "update user details" in {
      val newDetails = UserProfile("Ayush", "", "Hooda", "9991", 23, "xyz")
      val storeResult = Await.result(repo.repository.updateUserDetails("ayushhooda14@gmail.com", newDetails), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

  "user info repository" should {
    "view user details" in {
      val record = User(0, "Ayush", "", "Hooda", "ayushhooda14@gmail.com", "bcd",
        "9991", "Male", 23, "xyz", true, true)
      val storeResult = Await.result(repo.repository.getUserDetails("ayushhooda14@gmail.com"), Duration.Inf)
      storeResult must equalTo(record)
    }
  }

  "user info repository" should {
    "should enable/ disable user" in {
      val storeResult = Await.result(repo.repository.enableOrDisableUser("ayushhooda14@gmail.com", false), Duration.Inf)
      storeResult must equalTo(true)
    }
  }

}
