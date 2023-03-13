package gay.pizza.tailscale.examples.helloworld

import gay.pizza.tailscale.core.Tailscale

object HelloWorldListener {
  @JvmStatic
  fun main(args: Array<String>) {
    val tailscale = Tailscale.configureAndUp {
      hostname = "kotlin-hello-world-listener"
    }

    val listener = tailscale.listen("tcp",":8989")
    listener.threadedAcceptLoop { conn ->
      val input = conn.inputStream()
      val output = conn.outputStream()

      try {
        input.copyTo(output)
      } finally {
        input.close()
        output.close()
        conn.close()
      }
    }
  }
}
