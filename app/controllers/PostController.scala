package controllers
import javax.inject._
import models.Post
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import repositories.PostRepository

import scala.concurrent.{ExecutionContext, Future}
class PostController @Inject()(
                                implicit ec: ExecutionContext,
                                components: ControllerComponents,
                                postsRepo: PostRepository
                              ) extends AbstractController(components){
  def listPosts = Action.async {
    postsRepo.list().map {
      posts => Ok(Json.toJson(posts))
    }
  }
  def createPost = Action.async(parse.json) {
    _.body.validate[Post].map {
      post => postsRepo.create(post).map { _ => Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Format")))
  }
  def readPost(id: BSONObjectID) = Action.async {
    postsRepo.read(id).map {
      maybePost => maybePost.map {
        post => Ok(Json.toJson(post))
      }.getOrElse(NotFound)
    }
  }
  def updatePost(id: BSONObjectID) = Action.async(parse.json)
  {
    _.body.validate[Post].map{
      post => postsRepo.update(id, post).map {
        case Some(post) => Ok(Json.toJson(post))
        case _ => NotFound
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Format")))
  }
  def deletePost(id: BSONObjectID) = Action.async {
    postsRepo.delete(id).map {
      case Some(post) => Ok(Json.toJson(post))
      case _  => NotFound
    }
  }
}