import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, CompletionHandler}
import java.nio.file.{Files, Paths, StandardOpenOption}

object AsyncFileChannel {
  def viaFuture(in: AsynchronousFileChannel,
                buf: ByteBuffer,
                out: AsynchronousFileChannel): Unit = {
    buf.clear()
    val readf = in.read(buf, 0)

    while (!readf.isDone) { /*dont*/ }

    buf.flip()

    // console
    val data = Array.ofDim[Byte](buf.limit())
    buf.get(data)
    scala.Console.println(new String(data))

    // file
    buf.rewind()
    val writef = out.write(buf, 0)
    while (!writef.isDone) { /*dont*/ }

    buf.clear()
    in.close()
  }

  def viaCallback(fileChannel: AsynchronousFileChannel,
                  buf: ByteBuffer): Unit = {
    fileChannel.read(
      buf,
      0,
      buf,
      new CompletionHandler[Integer, ByteBuffer] {
        override def completed(result: Integer,
                               attachment: ByteBuffer): Unit = {
          scala.Console.println("Read finished. Printing...")
          attachment.flip()
          val data = Array.ofDim[Byte](buf.limit())
          attachment.get(data)
          scala.Console.println(new String(data))
          attachment.clear()
          fileChannel.close()
        }
        override def failed(exc: Throwable, attachment: ByteBuffer): Unit = {
          scala.Console.println("Read failed.")
          attachment.clear()
          fileChannel.close()
        }
      }
    )

    fileChannel.write(buf, 0, buf, new CompletionHandler[Integer, ByteBuffer] {
      override def completed(result: Integer, attachment: ByteBuffer): Unit = ???
      override def failed(exc: Throwable, attachment: ByteBuffer): Unit = ???
    })
  }

  def main(args: Array[String]): Unit = {
    val inPath = Paths.get("data/lore.txt")
    val outPath = Paths.get("data/lore.copy.txt")

    val in =
      AsynchronousFileChannel.open(inPath, StandardOpenOption.READ)

    if (!Files.exists(outPath)) {
      Files.createFile(outPath)
    }

    val out =
      AsynchronousFileChannel.open(outPath, StandardOpenOption.WRITE)

    val buf = ByteBuffer.allocate(1024)

    viaFuture(in, buf, out)
//    viaCallback(in, buf)

    while (in.isOpen) { /*dont*/ }
  }
}
