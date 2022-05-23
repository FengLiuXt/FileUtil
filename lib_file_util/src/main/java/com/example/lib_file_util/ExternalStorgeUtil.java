package com.example.lib_file_util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.lib_file_util.exception.CreateDirException;
import com.example.lib_file_util.exception.CreateFileExeption;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

final public class ExternalStorgeUtil {

   private  ExternalStorgeUtil(){
   }


   //data/data/<应用包名>/cache 文件相关处理
   public static File createCacheDir(Context context, String path) throws CreateDirException {
      String cacheDir  = context.getExternalCacheDir().getAbsolutePath();
      File dir = new File(cacheDir+"/"+path);
      if(!dir.exists()){
         if(!dir.mkdirs()){
            throw new CreateDirException("创建"+dir.getAbsolutePath()+"文件夹失败");
         }
      }
      return dir;
   }
   public static File createCacheFile(Context context, String path) throws CreateDirException, CreateFileExeption {
      String cacheDir  = context.getExternalCacheDir().getAbsolutePath();
      File file = new File(cacheDir+"/"+path);
      if(!file.getParentFile().exists() && !file.getParentFile().mkdirs()){
            throw new CreateDirException("创建"+file.getAbsolutePath()+"的父目录失败");
         }
      if(!file.exists()){
         try {
            if(!file.createNewFile()){
               throw new CreateFileExeption("创建"+file.getAbsolutePath()+"文件失败");
            }
         } catch (IOException e) {
            e.printStackTrace();
            throw new CreateFileExeption("创建"+file.getAbsolutePath()+"文件失败");
         }
      }
      return file;
   }

   public static void writeCacheFile(Context context, String filePath, String str) throws CreateDirException, CreateFileExeption, IOException {

         File file = createCacheFile(context, filePath);
         FileWriter fileWriter = new FileWriter(file);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         bufferedWriter.write(str);
         bufferedWriter.flush();
         bufferedWriter.close();
   }
   public static void writeCacheFile(Context context, String filePath, InputStream inputStream) throws CreateDirException, CreateFileExeption, IOException {
      File file = createCacheFile(context, filePath);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
      byte[] bytes = new byte[1024];
      int length;
      while ((length = bufferedInputStream.read(bytes)) != -1){
         bufferedOutputStream.write(bytes, 0, length);
      }
      bufferedOutputStream.flush();
      closeStream(bufferedInputStream);
      closeStream(bufferedOutputStream);
   }
   public static void writeCacheFile(Context context, String filePath, Bitmap bitmap) throws CreateDirException, CreateFileExeption, IOException {
      int bytes = bitmap.getByteCount();
      ByteBuffer buf = ByteBuffer.allocate(bytes);
      bitmap.copyPixelsToBuffer(buf);
      byte[] byteArray = buf.array();
      writeCacheFile(context, filePath, byteArray);
   }
   public static void writeCacheFile(Context context, String filePath, byte[] bytes) throws CreateDirException, CreateFileExeption, IOException {
      File file = createCacheFile(context, filePath);
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
      bufferedOutputStream.write(bytes, 0, bytes.length);
      bufferedOutputStream.flush();
      closeStream(bufferedOutputStream);
   }

   private static void closeStream(InputStream inputStream){
      try {
         inputStream.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   private static void closeStream(OutputStream outputStream){
      try {
         outputStream.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static InputStream getInputStreamFromCacheFile(Context context, String filePath) throws FileNotFoundException {
      File file = new File(context.getExternalCacheDir().getAbsolutePath()+"/"+filePath);
      InputStream inputStream = null;
      if(file.exists()){
         inputStream = new FileInputStream(file);
      }else{
         throw new FileNotFoundException(file.getAbsolutePath()+"文件不存在");
      }

      return inputStream;
   }
   public static byte[] getBytesFromCacheFile(Context context, String filePath) throws IOException {
      File file = new File(context.getExternalCacheDir()+"/"+filePath);
      if(file.exists()){
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
         byte[] bytes = new byte[1024];
         int length;
         while((length = bufferedInputStream.read(bytes)) != 0){
            byteArrayOutputStream.write(bytes, 0, length);
         }
         byte[] result = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         bufferedInputStream.close();
         return result;
      }else{
         throw new FileNotFoundException(file.getAbsolutePath()+"文件不存在");
      }
   }
   public static Bitmap getBitmapFromCacheFile(Context context, String filePath) throws IOException {

      File file = new File(context.getFilesDir()+filePath);
      if (file.exists()){
         FileInputStream fileInputStream = new FileInputStream(file);
         Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
         fileInputStream.close();
         return bitmap;
      }else{
         throw new FileNotFoundException(file.getAbsolutePath()+"文件不存在");
      }
   }
   public static String getStringFromCacheFile(Context context, String filePath) throws IOException {
      File file = new File(context.getExternalCacheDir()+filePath);
      if(file.exists()){
         StringBuilder sb = new StringBuilder();
         BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
         char[] chars = new char[1024];
         int length;
         while ((length  = bufferedReader.read(chars)) != 0){
            sb.append(chars, 0, length);
         }
         bufferedReader.close();
         return sb.toString();
      }else{
         throw new FileNotFoundException(file.getAbsolutePath()+"文件不存在");
      }
   }

}
