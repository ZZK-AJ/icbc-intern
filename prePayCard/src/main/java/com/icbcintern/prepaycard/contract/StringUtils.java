package com.icbcintern.prepaycard.contract;

import org.wasmer.Instance;
import org.wasmer.Memory;
import org.wasmer.exports.Function;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * description:wasm字符串操作工具类
 * @author: He Yihui
 * @create: 2022-07-29 14:56
 **/
public class StringUtils {
    Instance instance;

    public StringUtils(Instance instance) {
        this.instance = instance;
    }
    public void deallocate(int ptr,int length){
        //TODO:释放因传递参数锁申明的内存空间
    }

    public void drop(int ptr){
        //TODO:释放rust返回给java的string
    }

    public int initStrParam(String str) throws Exception {
        //转字节数组
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        if (instance==null){
            throw new Exception("instance未初始化");
        }
        Function allocate = instance.exports.getFunction("allocate");
        if (allocate==null){
            throw new Exception("方法allocate未定义");
        }
        int paramPtr = (int) allocate.apply(bytes.length+1)[0];
        return paramPtr;
    }

    public String getString(int ptr) throws Exception {
        if (instance==null){
            throw new Exception("instance未初始化");
        }
        Memory memory = instance.exports.getMemory("memory");
        ByteBuffer mbf = memory.buffer();
        return getString(ptr,mbf);
    }

    public void setString(int ptr,String str) throws Exception {
        if (instance==null){
            throw new Exception("instance未初始化");
        }
        Memory memory = instance.exports.getMemory("memory");
        ByteBuffer mbf = memory.buffer();
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        mbf.position(ptr);
        mbf.put(bytes);
    }
    public int addString(String str) throws Exception {
        if (instance==null){
            throw new Exception("instance未初始化");
        }
        int ptr = initStrParam(str);
        setString(ptr,str);
        return ptr;
    }
    
    public static String getString(int ptr, ByteBuffer mbf) {
        StringBuilder sb = new StringBuilder();
        for(int i = ptr, max = mbf.limit(); i < max; i++) {
            mbf.position(i);
            byte b = mbf.get();
            if (b == 0) {
                break;
            }
            sb.appendCodePoint(b);
        }
        String result = sb.toString();
        return result;
    }


}
