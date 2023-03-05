package gay.pizza.tailscale.core

object TestHello {
  @JvmStatic
  fun main(args: Array<String>) {
    val tailscale = Tailscale()
    tailscale.up()
    val conn = tailscale.dial("tcp","100.81.142.128:8989")
    println(conn.handle)
  }
}
