import java.io.RandomAccessFile
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{DatagramChannel, ServerSocketChannel, SocketChannel}

object Channels {
  val EOF = -1

  def example(fileName: String): Unit = {
    val file = new RandomAccessFile(fileName, "rw")
    val inChannel = file.getChannel
    val buf = ByteBuffer.allocate(32)
    var bytesRead = inChannel.read(buf)
    while (bytesRead != EOF) {

      buf.flip // read mode

      var initial = ""
      while (buf.hasRemaining) {
        initial += buf.get().toChar
      }
      scala.Console.println(s"Initial: $initial")

      // Rewind
      var rewind = ""
      buf.rewind()
      while (buf.hasRemaining) {
        rewind += buf.get().toChar
      }
      scala.Console.println(s"Rewind: $rewind")

      // Mark / Set
      buf.rewind()
      while (buf.remaining() > (buf.limit() / 2)) {
        buf.get()
      }

      var first = ""
      buf.mark()
      while (buf.hasRemaining) {
        first += buf.get().toChar
      }
      scala.Console.println(s"Mark/Reset (first time): $first")

      buf.reset()
      var second = ""
      while (buf.hasRemaining) {
        second += buf.get().toChar
      }
      scala.Console.println(s"Mark/Reset (second time): $second")

      buf.clear // position back to 0
      bytesRead = inChannel.read(buf)
    }
    file.close()
  }
}

object SocketChannelExample {
  val newData = "New string to write to file.." + System.currentTimeMillis()
  val buffer = ByteBuffer.allocate(48)
  val socketChannel = SocketChannel.open()
  socketChannel.configureBlocking(false) /*Non-blocking*/
  socketChannel.connect(new InetSocketAddress("http://fail", 80))

  val bytesRead = socketChannel.read(buffer)

  buffer.clear()
  buffer.put(newData.getBytes())
  buffer.flip()

  while (buffer.hasRemaining) {
    socketChannel.write(buffer)
  }

  // Non blocking
  socketChannel.configureBlocking(false) /*Non-blocking*/
  // connect won't block (not connected yet)
  socketChannel.connect(new InetSocketAddress("http://fail", 80))
  while (!socketChannel.finishConnect()) {}

  // write() in non-blocking must be called in a loop
  // read() may return 0 bytes

  // Non-blocking works better with selector !

  socketChannel.close()
}

object ServerSocketChannelExample {
  // Channel that listen for incoming TCP connections
  val serverSocketChannel = ServerSocketChannel.open()
  serverSocketChannel.socket().bind(new InetSocketAddress(9000))
  serverSocketChannel.configureBlocking(false) /*non-blocking*/
  while (true) {
    val socketChannel = serverSocketChannel.accept() /*non-blocking: returns nulls*/
    if (socketChannel != null) {
      // do something with socketChannel
    }
  }
  serverSocketChannel.close()

}

object DatagramChannelExample {
  // UDP packets: don't require a connection

  val buf = ByteBuffer.allocate(48)
  val channel = DatagramChannel.open()
  channel.socket().bind(new InetSocketAddress(9000))

  buf.clear()
  channel.receive(buf)

  buf.clear()
  buf.put(s"Current time: ${System.currentTimeMillis()}".getBytes())
  buf.flip()
  val bytesSend = channel.send(buf, new InetSocketAddress("jenkov.com", 80))

  // Not a connection (UDP is connection-less) but restrict the address
  channel.connect(new InetSocketAddress("jenkov.com", 80))
  val bytesRead = channel.read(buf)
  val bytesWritten = channel.write(buf)

  channel.close()
}
