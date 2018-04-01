package model

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AssignmentInfoRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
AssignmentInfoFirst with AssignmentInfoRepoImplementation

trait AssignmentInfoFirst extends HasDatabaseConfigProvider[JdbcProfile]
{
  import profile.api._

  class AssignmentForm(tag: Tag) extends Table[Assignment](tag, "assignment_details") {

    def id = column[Int]("id")

    def title: Rep[String] = column[String]("title")

    def description: Rep[String] = column[String]("description")

    def * : ProvenShape[Assignment] = (id,title,description)<>(Assignment.tupled, Assignment.unapply)
  }

  val assignmentQuery: TableQuery[AssignmentForm] = TableQuery[AssignmentForm]
}

trait AssignmentInfoRepoInterface {

  def addAssignment(assignment: Assignment): Future[Boolean]

  def viewAssignments(): Future[List[Assignment]]

}

trait AssignmentInfoRepoImplementation extends AssignmentInfoRepoInterface {
  self: AssignmentInfoFirst =>

  import profile.api._

  def addAssignment(assignment: Assignment): Future[Boolean] =
    db.run(assignmentQuery += assignment) map (_ > 0)

  def viewAssignments(): Future[List[Assignment]] = {
    db.run(assignmentQuery.to[List].result)
  }
  
}