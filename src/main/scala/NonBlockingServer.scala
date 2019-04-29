import java.io.RandomAccessFile

// http://tutorials.jenkov.com/java-nio/non-blocking-server.html

object NonBlockingServer {

  /** Blocking IO Pipeline*/
  // Blocking IO servers requires each stream in a single thread (blocks the thread)
  // Each thread requires 1kb of memory (320k  bytes in 32bits JVM)
  // Solution: limit thread numbers to 100 and queue connections.
  //           the queue must be read pretty fast or the server becomes irresponsive
  //           if all threads are blocked... the server is dead

  /** Non-blocking IO Pipeline*/
  // 1 thread -> N connections
  // Channels in non blocking mode and use a Selector to check availables

  // Detecting if the message is partial or non is hard
  // Partial messages must be stored until the rest arrives

  // This task is done by the message reader (must be very fast).
  // One message reader per channel (don't mess up)

  // Storing partial messages:
  // 1.- copy meesages around as little as possible
  // 2.- messages stored in consecutive byte sequences

  // A buffer per message reader:
  //   won't work.. if the message is 1MB then we need at least 1MB buffer...
  //   1 Mb per connection x 1 milion connection = 1 TB of memory

  //Resizable buffers (impl)

  //    Resize by copy
  //       - start with a small buffer and if full double size.
  //       - you should find an appropiate initial size. Example:
  //           4 KB average messages
  //           128KB for files
  //           Max buffer size for non pattern files
  //        - this way we can have a 1 milion connection with only 1.000.000 x 4KB = 4GB

  //     Resize by append:
  //         - buffer consist of multiple arrays
  //         - when you need to resize buffer simply allocate anotehr byte array
  //         - you need a list of these byte arrays
  //         Disadvantage: not stored in consecutive array

  //     TLV (type, length, value) encodded messages:
  //     - length of message is in the header so you know beforehand the mem to allocate
  //     Disadvantage: you allocate memory before receiving the message
  //     Intenional DoS ...
  // Writing Partial Messages
  //   writing data is a challenge channel.write(byteBuffer) is no guarantee about how
  //   many of the bytes are written

  //   Message write: keep track of exactly how many bytes have been written
  //   Use a selector to check which channels are ready to write

  //   Checking all channels is a mess. You only want to check the channels which you want to write in
  //   Check only the channels we need:
  //   1. When a message is written in the Message Writer, register the channel
  //   2. If a meesage writer writes all its messages to a Channel, unregister it from the selector.

  // non-blocking server:
  // Three pipelines that must execute regularly:
  //   - The read pipeline which checks for new incoming data from the open connections.
  //   - The process pipeline which processes any full messages received.
  //   - The write pipeline which checks if it can write any outgoing messages to any of the open connections.

  // Example uses one thread for checking incoming new connections
  //              one thread to do the reading, procesing and writing work.

}
