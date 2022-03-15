package zioenv

import zio._
import zio.kafka.producer.{Producer, ProducerSettings}

object Main extends ZIOAppDefault {

  val dbLayer = ZLayer.make[DB](
    ConnectionPoolIntegration.live,
    DB.live,
    ZLayer.succeed(DBConfig("jdbc://localhost"))
  )

  val kafkaLayer = ZLayer.fromManaged(
    Producer.make(
      settings = ProducerSettings(List("localhost:29092"))
    )
  )

  val envLayer = dbLayer ++ kafkaLayer

  def run = {
    val repeat = (1 to 5).map(i =>
      UserRegistration.register(User(s"adam$i", s"adam$i@hello.world")).map { u => println(s"Registered user: $u (layers)") }
    )
    ZIO.collectAllPar(repeat).provideCustom(envLayer)
  }
}
