package gay.pizza.tailscale.channel

import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

class ChannelCopier(
  val source: ReadableByteChannel,
  val sink: WritableByteChannel,
  val bufferSize: Int = DEFAULT_BUFFER_SIZE
) {
  fun copyOneBuffer(): ChannelCopierState {
    try {
      val buffer = ByteBuffer.allocate(bufferSize)
      val size = source.read(buffer)
      if (size < 0) {
        return ChannelCopierState.ChannelClosed
      } else {
        buffer.flip()
        sink.write(buffer)
      }
      buffer.clear()
    } catch (e: ClosedChannelException) {
      return ChannelCopierState.ChannelClosed
    }
    return ChannelCopierState.CopiedBuffer
  }

  fun copyUntilClose(autoCloseSides: Boolean = true): ChannelCopierState {
    while (true) {
      val state = copyOneBuffer()
      if (state == ChannelCopierState.ChannelClosed) {
        if (autoCloseSides) {
          sink.close()
          source.close()
        }
        return state
      }
    }
  }

  fun spawnCopyThread(name: String = "Channel Copier"): Thread {
    val thread = Thread {
      copyUntilClose()
    }
    thread.name = name
    thread.start()
    return thread
  }
}
