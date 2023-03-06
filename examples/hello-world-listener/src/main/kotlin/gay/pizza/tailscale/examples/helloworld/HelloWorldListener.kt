package gay.pizza.tailscale.examples.helloworld

import gay.pizza.tailscale.core.Tailscale

object HelloWorldListener {
  @JvmStatic
  fun main(args: Array<String>) {
    val tailscale = Tailscale()
    tailscale.up()
    val listener = tailscale.listen("tcp",":8989")
    while (true) {
      val conn = listener.accept()
      val out = conn.outputStream()
      val writer = out.bufferedWriter()
      writer.write("Hello World")
      writer.newLine()
      writer.close()
      conn.close()
    }
  }
}
