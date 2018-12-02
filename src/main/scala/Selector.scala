import java.io.RandomAccessFile
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{SelectionKey, Selector, SocketChannel}

object SelectorExample {
  // Determine which NIO Channels are ready for data
  def example(): Unit = {

    val address = "http://httpbin.org"
    val socketChannel = SocketChannel.open()
    socketChannel.configureBlocking(false)
    socketChannel.connect(new InetSocketAddress("google.com", 80))
    while (!socketChannel.finishConnect()) {}
    val selector = Selector.open()
    socketChannel.configureBlocking(false)
    val key = socketChannel.register(
      selector,
      SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE)

    val buffer = ByteBuffer.allocate(58)
    socketChannel.read(buffer)

    key.isAcceptable

    // val channel2 = key.channel()
    // val selector2 = key.selector()

    key.attach(buffer)
    val retrievedBuffer = key.attachment()

    // select: blocks until at least one channel is ready
    // select(timeout: Long): it blocks for a maximum of timeout ms
    // selectNow: returns immediately with whateer channels are ready
    // return int tells one many channels are ready
    // calling it one will return the number of channels ready
    // calling it twice will only the return the ones ready since last select

    val readyKeys = selector.selectedKeys()
    val keyIterator = readyKeys.iterator()
    while (keyIterator.hasNext) {
      val key = keyIterator.next()
      key match {
        case k if k.isAcceptable  => ???
        case k if k.isConnectable => ???
        case k if k.isReadable    => ???
        case k if k.isWritable    => ???
        case _                    => ()
      }
      // to make the channel visible the next time is ready
      keyIterator.remove()
    }

    selector.select() // blocked
    selector.wakeup() // unblock
    // WTF: If a different thread calls wakeup() and no thread is currently blocked inside select(),
    //      the next thread that calls select() will "wake up" immediately.

    selector.close()
    socketChannel.close()

    /*
    Selector selector = Selector.open();

    channel.configureBlocking(false);

    SelectionKey key = channel.register(selector, SelectionKey.OP_READ);


    while(true) {

        int readyChannels = selector.select();

        if(readyChannels == 0) continue;


        Set<SelectionKey> selectedKeys = selector.selectedKeys();

        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while(keyIterator.hasNext()) {

        SelectionKey key = keyIterator.next();

        if(key.isAcceptable()) {
            // a connection was accepted by a ServerSocketChannel.

        } else if (key.isConnectable()) {
            // a connection was established with a remote server.

        } else if (key.isReadable()) {
            // a channel is ready for reading

        } else if (key.isWritable()) {
            // a channel is ready for writing
        }

        keyIterator.remove();
        }
    }
   * */

  }
}
