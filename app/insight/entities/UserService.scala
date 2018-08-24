package insight.entities

import insight.services.Users
import insight.entities.User
import scala.concurrent.Future

object UserService {

  def addUser(user: User): Future[String] = {
    Users.add(user)
  }

  def deleteUser(id: Long): Future[Int] = {
    Users.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    Users.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }
}
