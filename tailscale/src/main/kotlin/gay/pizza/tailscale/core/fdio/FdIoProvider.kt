package gay.pizza.tailscale.core.fdio

import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

interface FdIoProvider {
  fun openInputStream(fd: Int): InputStream
  fun openOutputStream(fd: Int): OutputStream

  fun openReadChannel(fd: Int): ReadableByteChannel
  fun openWriteChannel(fd: Int): WritableByteChannel

  companion object {
    fun auto(): FdIoProvider {
      val reflect = ReflectFdIoProvider()
      if (reflect.supported()) {
        return reflect
      }

      val devFd = Path("/dev/fd")
      if (devFd.exists() && devFd.isDirectory()) {
        return FsFdIoProvider(useProcSelfFd = false)
      }
      return FsFdIoProvider(useProcSelfFd = true)
    }
  }
}
