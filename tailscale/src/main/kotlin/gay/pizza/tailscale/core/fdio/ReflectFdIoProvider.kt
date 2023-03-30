package gay.pizza.tailscale.core.fdio

import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

class ReflectFdIoProvider : FdIoProvider {
  override fun openInputStream(fd: Int): InputStream = FileInputStream(fileDescriptor(fd))
  override fun openOutputStream(fd: Int): OutputStream = FileOutputStream(fileDescriptor(fd))
  override fun openReadChannel(fd: Int): ReadableByteChannel = Channels.newChannel(openInputStream(fd))
  override fun openWriteChannel(fd: Int): WritableByteChannel = Channels.newChannel(openOutputStream(fd))

  fun supported(): Boolean {
    val fileDescriptorClass = FileDescriptor::class.java
    val constructor = fileDescriptorClass.getDeclaredConstructor(Int::class.java)
    return try {
      constructor.isAccessible = true
      true
    } catch (_: Exception) {
      false
    }
  }

  private fun fileDescriptor(fd: Int): FileDescriptor {
    val fileDescriptorClass = FileDescriptor::class.java
    val constructor = fileDescriptorClass.getDeclaredConstructor(Int::class.java)
    constructor.isAccessible = true
    return constructor.newInstance(fd)
  }
}
