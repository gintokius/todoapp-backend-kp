package controllers

import dao.TaskDAO
import models.Task
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.db.slick.DatabaseConfigProvider
import akka.stream.Materializer
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.Injector
import play.api.Mode
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json._

import scala.concurrent.ExecutionContext.Implicits.global

class APIControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val materializer: Materializer = app.materializer

  lazy val appBuilder: GuiceApplicationBuilder = new GuiceApplicationBuilder().in(Mode.Test)
  lazy val injector: Injector = appBuilder.injector()

  lazy val dbConfigProvider: DatabaseConfigProvider = injector.instanceOf[DatabaseConfigProvider]

  val taskDAO: TaskDAO = new TaskDAO(dbConfigProvider)

  val controller = new APIController(taskDAO, stubControllerComponents())

  "APIController" should {

    "output tasks on getTasks/ GET request" in {
      val showTasks = controller.showTasks().apply(FakeRequest(GET, "/"))
      status(showTasks) mustBe OK
      contentType(showTasks) mustBe Some("application/json")
      val response = contentAsJson(showTasks)
      (response \ "status").as[String] mustBe "success"
    }

    "output task on getTask/1 GET request" in {
      val showTask = controller.showTask(1).apply(FakeRequest(GET, "/"))
      status(showTask) mustBe OK
      contentType(showTask) mustBe Some("application/json")
    }

    "fail inserting a non valid json" in {
      val response = controller
        .createTask()
        .apply(FakeRequest(POST, "/")
        .withJsonBody(obj("id" -> 98, "text" -> "some text"))
        .withHeaders(CONTENT_TYPE -> JSON))
      status(response) mustBe BAD_REQUEST
    }

    "fail updating with a non valid json" in {
      val response = controller
        .updateTask(1)
        .apply(FakeRequest(PATCH, "/")
          .withJsonBody(obj("id" -> false, "text" -> "some text"))
          .withHeaders(CONTENT_TYPE -> JSON))
      status(response) mustBe BAD_REQUEST
    }

    "output `no content` on delete" in {
      val response = controller
        .removeTask(1)
        .apply(FakeRequest(DELETE, "/"))
      status(response) mustBe NO_CONTENT
    }

  }
}
