package models
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
case class Post(
                 _id: Option[BSONObjectID],
                 name: String,
                 phone: Int,
                 email: String,
                 age: Int
               )
object Post {
  implicit val format: OFormat[Post] = Json.format[Post]
}