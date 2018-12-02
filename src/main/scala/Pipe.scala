import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.Pipe

object PipeExample {
  // connection between threads. Sink -> Source (unidirectional)
  def data(): Array[Byte] = s"Current time: ${System.nanoTime()}".getBytes()

  def example(): Unit = {
    val pipe = Pipe.open()

    // Sink : write
    val sinkChannel = pipe.sink()
    val buffer = ByteBuffer.allocate(48)
    buffer.clear()
    buffer.put(data())
    buffer.flip()
    while (buffer.hasRemaining) {
      sinkChannel.write(buffer)
    }

    // Source: read
    val sourceChannel = pipe.source()
    buffer.clear()
    sourceChannel.read(buffer)
  }
}
