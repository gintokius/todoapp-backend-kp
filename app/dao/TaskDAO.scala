package dao

import javax.inject.Inject
import models.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class TaskDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Tasks = TableQuery[TasksTable]

  def all(): Future[Seq[Task]] = db.run(Tasks.result).map { task => task }

  def one(id: Int): Future[Seq[Task]] = db.run(Tasks.filter(_.id === id).result)

  def insert(task: Task): Future[Unit] = db.run(Tasks += task).map { _ => () }

  def update(id: Int, task: Task): Future[Unit] = {
    db.run(Tasks.filter(_.id === id).map(t =>
      (t.text, t.priority, t.isDone)
    ).update(
      (task.text, task.priority, task.isDone)
    ).map { _ => () })
  }

  def remove(id: Int): Future[Unit] = {
    db.run(Tasks.filter(_.id === id).delete.map { _ => () })
  }

  private class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {

    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def text = column[String]("text")
    def isDone = column[Boolean]("is_done")
    def priority = column[Int]("priority")

    def * =
      (id, text, priority, isDone) <> (Task.tupled, Task.unapply)
  }
}