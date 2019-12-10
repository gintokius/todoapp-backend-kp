package models

case class Task(id: Option[Int], text: String, priority: Int, isDone: Boolean) extends Serializable
