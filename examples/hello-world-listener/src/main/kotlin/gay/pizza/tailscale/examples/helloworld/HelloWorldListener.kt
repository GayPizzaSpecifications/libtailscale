package gay.pizza.tailscale.examples.helloworld

import gay.pizza.tailscale.core.Tailscale
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException

object HelloWorldListener {
  @JvmStatic
  fun main(args: Array<String>) {
    val tailscale = Tailscale.configureAndUp {
      hostname = "kotlin-hello-world-listener"
    }

    val listener = tailscale.listen("tcp",":8989")
    listener.threadedAcceptLoop { conn ->
      val channel = conn.openReadWriteChannel()

      try {
        val buffer = ByteBuffer.allocate(2048)
        while (channel.isOpen) {
          val size = channel.read(buffer)
          if (size < 0) {
            break
          }
          channel.write(buffer.flip())
          buffer.clear()
        }
      } catch (_: ClosedChannelException) {
      } finally {
        channel.close()
        channel.close()
      }
    }
  }
}
