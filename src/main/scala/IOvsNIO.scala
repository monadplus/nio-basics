object IOvsNIO {
  /*
        IO              NIO
   ----------------------------
   Stream oriented  | Buffer oriented
   Blocking IO      | Non blocking IO
                    | Selectors

   IO Streams:
     -  Once data is read is not cached anywhere
     - You cant move forth and back
   Buffer NIO:
     - Data is stored in buffer
     - You cna move forth and back
     - need to check if all data is available and wait for it

   Blocking: blocks thread until data is read
             1 thread -> 1 channel
   Nonblocking: thread read/write is non-blocking so thread can check other channels in the meanwhile
                1 thread -> N channels
                Selectors makes the task easier

    Application Design: NIO vs IO apps

       The processing of Data:

         IO:

          Read data, block until ready
          Read data, block until ready
          Read data, block until ready
          Read data, block until ready

          NIO:

          Determining the end of a message is hard.
          You must read the buffer and check if all data is loaded.

    Summary:

       NIO: multiple channels one thread but the cost of parsing data is high (you dont know when its end)

       NIO: For thousands of open conn. each only send a little data is really good
            (thread cost is low, parsing cost is high)

       IO: For fewer connection with high bandwith
 */
}
