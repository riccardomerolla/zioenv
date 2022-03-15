package zioenv

import zio._

object Main extends ZIOApp {
  override type Environment = DB

  override val tag: Tag[Environment] = Tag[Environment]

  override val layer: ZLayer[ZIOAppArgs, Any, DB] =
    ZLayer.make[DB](
      ConnectionPoolIntegration.live,
      DB.live,
      ZLayer.succeed(DBConfig("jdbc://localhost"))
    )

  override val run: ZIO[DB with Clock, Any, Any] = {
    val repeat = (1 to 5).map(i => UserRegistration.register(User(s"adam$i", s"adam$i@hello.world")).map { u => println(s"Registered user: $u (layers)") })
    ZIO.collectAllPar(repeat)
  }
}
