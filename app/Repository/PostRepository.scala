package repositories
import javax.inject.Inject
import models.Post
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
class PostRepository @Inject() (
                                 implicit ec: ExecutionContext,
                                 reactiveApi: ReactiveMongoApi) {
  private def collection: Future[JSONCollection] =
    reactiveApi.database.map(_.collection("ask"))
  def list(limit: Int = 100): Future[Seq[Post]] =
    collection.flatMap(
      _.find(BSONDocument())
        .cursor[Post](ReadPreference.primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Post]]())
    )
  def create(post: Post): Future[WriteResult] =
    collection.flatMap(_.insert(post))
  def read(id: BSONObjectID): Future[Option[Post]] =
    collection.flatMap(_.find(BSONDocument("_id" -> id)).one[Post])
  def update(id: BSONObjectID, post: Post): Future[Option[Post]] =
    collection.flatMap(_.findAndUpdate(
      BSONDocument("_id" -> id), BSONDocument(
        f"$$set" -> BSONDocument(
          "name" -> post.name,
          "phone" -> post.phone,
          "email" -> post.email,
          "age"->post.age
        )
      ), true
    ).map(_.result[Post])
    )
  def delete(id: BSONObjectID): Future[Option[Post]] =
    collection.flatMap(_.findAndRemove
    (BSONDocument("_id" -> id )).map(_.result[Post]))
}

