import java.io.RandomAccessFile

object Transfer {
  // Data from one channel to another (one must be FileChannel)
  def example(from: String, to: String): Unit = {

    // FileChannel.transferFrom()
    val fromFile = new RandomAccessFile(from, "rw")
    val fromChannel = fromFile.getChannel

    val toFile = new RandomAccessFile(to, "rw")
    val toChannel = toFile.getChannel

    toChannel.transferFrom(fromChannel, 0, fromChannel.size())

    // force to flush into the disk
    toChannel.force(true)

    // FileChannel.transferTo()
//    fromChannel.transferTo(0, fromChannel.size(), toChannel)
  }
}
