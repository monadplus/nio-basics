import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

object ChannelsExample extends App {
    def runServer(): Unit = {
        val server = AsynchronousServerSocketChannel.open()
        try {
            server.bind(new InetSocketAddress("127.0.0.1", 1234));
            val acceptCon = server.accept();
            val client = acceptCon.get(10, TimeUnit.SECONDS);
            if ((client ne null) && (client.isOpen())) {
                val buffer = ByteBuffer.allocate(1024);
                val readval = client.read(buffer);
                println("Received from client: " + new String(buffer.array()).trim())
                readval.get()
                buffer.flip()
                val str = "I'm fine. Thank you!"
                val writeVal = client.write(ByteBuffer.wrap(str.getBytes()))
                println("Writing back to client: " + str)
                writeVal.get()
                buffer.clear()
            }
            client.close()
        } catch {
            case e: Exception =>
            e.printStackTrace()
        } finally {
            server.close()  
        }
    }

    def runClient(): Unit = {
        val client = AsynchronousSocketChannel.open()
        try {
            val result = client.connect(new InetSocketAddress("127.0.0.1", 1234))
            result.get();
            val str = "Hello! How are you?";
            val buffer = ByteBuffer.wrap(str.getBytes());
            val  writeval = client.write(buffer);
            println("Writing to server: "+str)
            writeval.get()
            buffer.flip()
            val readval = client.read(buffer);
            println("Received from server: " + new String(buffer.array()).trim())
            readval.get()
            buffer.clear()
        } catch {
            case e: InterruptedException =>
              println("Disconnected")
            case e: Exception => 
              e.printStackTrace()
        } finally {
            client.close()
        }
    }
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global

    Future(runServer())
    Future(runClient())
}
