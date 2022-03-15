package zioenv

import zio.kafka.producer.{Producer}
import zio.{Clock, ZIO}

object UserRegistration {
  def register(u: User): ZIO[Clock with Producer with DB, Throwable, User] = {
    for {
      _ <- UserModel.insert(u)
      _ <- UserNotifier.notify(u, "Welcome!")
      _ <- UserKafkaProducer.notify(u, "Welcome!")
    } yield u
  }
}
