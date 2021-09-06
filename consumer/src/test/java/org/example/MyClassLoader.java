package org.example;

import com.whb.mq.MqDepotService;

import java.io.*;

public class MyClassLoader extends  ClassLoader{
    private final String path;
    public MyClassLoader(String path){
        super(null);
        this.path = path;
    }
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(name);
        byte [] bytes = new byte[0];
        bytes = readBytes(name);
        return defineClass(name,bytes,0,bytes.length);
    }
    private byte[] readBytes(String name){
        name = name.replace("\\.","\\\\");
        String classPath = path+name+".class";
        File classFile = new File(classPath);
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        byte[]dataBytes = new byte[1024];
        byte[]readBytes = null;
        try{
            bos = new ByteArrayOutputStream();
            is = new FileInputStream(classFile);
            int length;
            while((length=is.read(dataBytes)) != -1){
                bos.write(dataBytes,0,length);
            }
            readBytes = bos.toByteArray();
        }catch (Exception e){

        }finally {
            try {
                if(bos != null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return readBytes;
    }
}
