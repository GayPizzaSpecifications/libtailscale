package gay.pizza.tailscale.examples.helloworld

import gay.pizza.tailscale.channel.ChannelCopier
import gay.pizza.tailscale.channel.ChannelCopierState
import gay.pizza.tailscale.core.Tailscale

object HelloWorldListener {
  @JvmStatic
  fun main(args: Array<String>) {
    val tailscale = Tailscale.configureAndUp {
      hostname = "kotlin-hello-world-listener"
    }

    val listener = tailscale.listen("tcp",":8989")
    listener.threadedAcceptLoop { conn ->
      val readChannel = conn.openReadChannel()
      val writeChannel = conn.openWriteChannel()
      val copier = ChannelCopier(readChannel, writeChannel)
      if (copier.copyUntilClose() == ChannelCopierState.AllClosed) {
        conn.close()
      }
    }
  }
}
