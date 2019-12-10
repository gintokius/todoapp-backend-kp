package controllers

import dao.TaskDAO
import javax.inject._
import models.Task
import play.api.libs.json.{Format, JsError, JsPath, JsSuccess, JsValue, Json, Reads}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class APIController @Inject()(taskDAO: TaskDAO, controllerComponents: ControllerComponents)
                             (implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

  implicit val taskFormat: Format[Task] = Json.format[Task]

  implicit val taskReads: Reads[Task] = Json.reads[Task]

  def showTasks(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    taskDAO.all().map { tasks =>
      Ok(Json.obj("status" -> "success", "data" -> tasks))
    }
  }

  def showTask(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    taskDAO.one(id.toInt).map { tasks =>
      Ok(Json.obj("status" -> "success", "data" -> tasks))
    }
  }

  def createTask(): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue] =>
    Json.fromJson[Task](request.body) match {
      case JsSuccess(task: Task, _: JsPath) =>
        taskDAO.insert(task).map { _ =>
          Ok(Json.obj("status" -> "success"))
        }
      case e@JsError(_) =>
        Future {
          BadRequest(Json.obj("status" -> "error"))
        }
    }
  }

  def updateTask(id: Long): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue] =>
    Json.fromJson[Task](request.body) match {
      case JsSuccess(task: Task, _: JsPath) =>
        taskDAO.update(id.toInt, task).map { _ =>
          Ok(Json.obj("status" -> "success"))
        }
      case e@JsError(_) =>
        Future {
          BadRequest(Json.obj("status" -> "error"))
        }
    }
  }

  def removeTask(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    taskDAO.remove(id.toInt).map { _ => NoContent }
  }
}