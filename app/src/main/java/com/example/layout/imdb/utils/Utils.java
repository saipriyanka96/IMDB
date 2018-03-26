package com.example.layout.imdb.utils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Pri on 10/21/2017.
 */

class Utils {
    /*
    The Util class cannot be instantiated and stores short static convenience methods that are often quite useful.
    CopyStream:Copies the contents of an InputStream to an OutputStream using a copy buffer of a given size and notifies the provided CopyStreamListener of the progress of the copy operation by calling
    its bytesTransferred(long, int) method after each write to the destination.
    bufferSize - The number of bytes to buffer during the copy
     */
    static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;

        try
        {
/*creating the bytes object for buffersize

 */
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                //Read byte from input stream

                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;

                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ignored){}
    }
}