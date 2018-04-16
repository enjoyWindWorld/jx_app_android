package com.jx.intelligent.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XBitmapUtils
{

    /**
     * 压缩图像文件
     * 
     * @param imageFile
     * @param size
     *            压缩目标大小
     * @return
     */
    public static Bitmap decodeFile(File imageFile, int size)
    {

        try
        {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(imageFile), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = size;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true)
            {
                if (width_tmp < REQUIRED_SIZE || height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(imageFile),
                    null, o2);
            return b;
        } catch (FileNotFoundException e)
        {
        }
        return null;
    }

    /**
     * 
     * @param path
     *            文件路径
     * @param size
     *            目标大小
     * @return
     */
    public static Bitmap decodeFile(String path, int size)
    {

        File file = new File(path);
        if (file.exists())
        {
            return decodeFile(file, size);
        }
        return null;
    }

    public static Bitmap decodeBitmap(Bitmap orgnalBitmap, int size)
    {

        int os = orgnalBitmap.getWidth() < orgnalBitmap.getHeight() ? orgnalBitmap
                .getWidth() : orgnalBitmap.getHeight();

        int minSize = os < size ? os : size;

        Bitmap bitmap = Bitmap.createBitmap(orgnalBitmap, 0, 0, minSize,
                minSize);
        return bitmap;
    }

    public static void SaveBitMap(Context context, String src, Bitmap bitmap)
    {

        File file = new File(src);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {

            FileOutputStream out = null;
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(file);
			intent.setData(uri);
			context.sendBroadcast(intent);
			
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
  
    /** 
     * Compress image by pixel, this will modify image width/height.  
     * Used to get thumbnail 
     *  
     * @param imgPath image path 
     * @param pixelW target pixel of width 
     * @param pixelH target pixel of height 
     * @return 
     */  
    public static Bitmap ratios(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容  
        newOpts.inJustDecodeBounds = true;  
        newOpts.inPreferredConfig = Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now    
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);
            
        newOpts.inJustDecodeBounds = false;    
        int w = newOpts.outWidth;    
        int h = newOpts.outHeight;    
        // 想要缩放的目标尺寸  
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了  
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了  
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (newOpts.outWidth / ww);    
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (newOpts.outHeight / hh);    
        }    
        if (be <= 0) be = 1;    
        newOpts.inSampleSize = be;//设置缩放比例  
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩  
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;  
    }  
          
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap){
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)   //API 19

	     {   
	          return bitmap.getAllocationByteCount();
	     }
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)  //API 12

	      {
	        return bitmap.getByteCount();
	     }
	    return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
	}
}
