package model

import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class AssignmentInfoRepoTest extends Specification {

  val repo = new ModelTest[AssignmentInfoRepo]

  "assignment info repository" should {
    "add detail of an assignment" in {
      val assignment = Assignment(0, "Scala", "Functional Programming")
      val storeResult = Await.result(repo.repository.addAssignment(assignment), Duration.Inf)
      storeResult must equalTo(true)
    }
  }


//  "assignment info repository" should {
//    "delete detail of an assignment" in {
//      val result = Await.result(repo.repository.deleteAssignment(1), Duration.Inf)
//      result must equalTo(true)
//    }
//  }

  "assignment info repository" should {
    "view detail of all assignments" in {
      val result = Await.result(repo.repository.viewAssignments(), Duration.Inf)
      val list = List(Assignment(0, "Scala", "Functional Programming"))
      result must equalTo(list)
    }
  }

}
