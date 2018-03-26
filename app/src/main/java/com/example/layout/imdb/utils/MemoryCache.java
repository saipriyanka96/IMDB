package com.example.layout.imdb.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Pri on 10/21/2017.
 */

class MemoryCache {

    private static final String TAG = "MemoryCache";

    //Last argument true for LRU ordering
    private Map<String, Bitmap> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(2, 1.5f, true));

    //current allocated size
    private long size=0;

    //max memory cache folder used to download images in bytes
    private long limit=1000000;

    MemoryCache(){

        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory()/4);
    }

    private void setLimit(long new_limit){
//we give the limit for memory cache
        limit=new_limit;
        Log.i(TAG, "MemoryCache will use up to "+limit/1024./1024.+"MB");
    }

   /*this is to get the id if the id is not available then return the null otherwise it catcjes the exception

    */
    public Bitmap get(String id){

        try{
            if(!cache.containsKey(id))
                return null;
            return cache.get(id);
        }catch(NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }

   /*
   if cacje contains the id then it need to send the size in bytes and check the size every time otherwise send the exception
    */
    void put(String id, Bitmap bitmap){
        try{
            if(cache.containsKey(id))
                size-=getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size+=getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

   /*
   checkSize of the memory
   if size is greater than limit then we use iterator- It is a Java Cursor used to iterate a collection of objects.
    */
    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + cache.size());
        if(size>limit){
            Iterator<Map.Entry<String, Bitmap>> iter=cache.entrySet().iterator();
            //least recently accessed item will be the first one iterated
            while(iter.hasNext()){
                Map.Entry<String, Bitmap> entry=iter.next();
                size-=getSizeInBytes(entry.getValue());
                iter.remove();
                if(size<=limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size "+cache.size());
        }
    }
/*once it iterator completes it work the it wii clear the cache until size is 0

 */
    void clear() {
        try{
            cache.clear();
            size=0;
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }
/* then it again get size in form of bytes with help of bitmap. once values is null then it returns bytes and height of bitmap

 */
    private long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
