import java.io.RandomAccessFile
import java.nio.ByteBuffer

object ScatterGather {
  // Scatter example: header and body in separate buffers
  def example(fileName: String): Unit = {
    // Scatter reads: single channel -> multiple buffers
    val file = new RandomAccessFile(fileName, "rw")
    val channel = file.getChannel

    val header = ByteBuffer.allocate(128)
    val body = ByteBuffer.allocate(1024)
    val buffers = Array(header, body)

    // Writes 128 bytes in the first buffer and then move to the next
    channel.read(buffers)

    // Gather writes: multiple buffer into a single channel
    channel.write(buffers)
  }
}
