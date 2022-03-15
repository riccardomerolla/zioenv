package zioenv

import zio.kafka.producer.Producer
import zio.kafka.serde.Serde

object UserKafkaProducer {
  def notify(u: User, msg: String) = {
    Producer.produce[Any, Long, String](
      topic = "random",
      key = u.email.length.toLong,
      value = s"Sending $msg to ${u.email}",
      keySerializer = Serde.long,
      valueSerializer = Serde.string
    )
  }
}
