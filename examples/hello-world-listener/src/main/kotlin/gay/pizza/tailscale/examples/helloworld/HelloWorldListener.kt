package gay.pizza.tailscale.examples.helloworld

import gay.pizza.tailscale.core.TailscaleServer

object HelloWorldListener {
  @JvmStatic
  fun main(args: Array<String>) {
    val tailscale = TailscaleServer()
    tailscale.up()
    val listener = tailscale.listen("tcp",":8989")
    while (true) {
      val conn = listener.accept()
      val out = conn.outputStream().bufferedWriter()
      out.apply {
        appendLine("Hello World")
        close()
      }
      conn.close()
    }
  }
}
