package com.choucheng.dengdao2.tools.image;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.FileUtil;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.SystemUtil;
import com.choucheng.dengdao2.tools.viewtools.PicShowLayout;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BitmapUtils {
   	
	/**
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
       /* view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);*/
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
	}

	/**
	 * 从资源中获取Bitmap
	 * @param act
	 * @param resId
	 * @return
	 */
	public static Bitmap getBitmapFromResources(Activity act, int resId) {
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}


	/**
	 * 获得SD卡中的图片
	 * @param path
	 * @return
	 */
   public static Bitmap getBitmapFromPath(String path){
	  return  BitmapFactory.decodeFile(path);
   }

	/**
	 * 将Drawable转换成 Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap convertDrawable2BitmapSimple(Drawable drawable){
		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();
	}

	/**
	 * Bitmap转换成 Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	}


	/**
	 * 将 byte[]转换成Bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap convertBytes2Bimap(byte[] b) {
		if (b.length == 0) {
			return null;
		}
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	/**
	 * 将 Bitmap转换成byte[]
	 * @param bm
	 * @param flag 1 jpg; 2 png;
	 * @return
	 */
	public static byte[] convertBitmap2Bytes(Bitmap bm,int flag) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(flag==1) bm.compress(CompressFormat.JPEG, 100, baos);
		bm.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}




    /**
   	 * 裁剪图片方法实现
   	 * @param uri  进行裁剪的图片的uri
   	 */
   	public static void startPhotoZoom(Activity mActivity,Uri uri,int requestcode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
   	    intent.setDataAndType(uri, "image/*");
   	    // 设置裁剪
   	    intent.putExtra("crop", "true");
   	    // aspectX aspectY 是宽高的比例
   	    intent.putExtra("aspectX", 1);
   	    intent.putExtra("aspectY", 1);
   	    // outputX outputY 是裁剪图片宽高
   	    intent.putExtra("outputX", 100);
   	    intent.putExtra("outputY", 100);
   	    intent.putExtra("return-data", true);
   	    mActivity.startActivityForResult(intent, requestcode);
   	 }



    /**
     * 裁剪图片方法实现
     * @param fromuri  进行裁剪的图片的uri
     *                 返回的是大图，指定uri
     */
    public static void startPhotoZoomBig(Activity mActivity,Uri fromuri,Uri toUri,int requestcode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fromuri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, toUri);
        intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent, requestcode);
    }


   /**
    * 将base64string转换成bitmap
    * @param string
    * @return
    */
   @TargetApi(Build.VERSION_CODES.FROYO)
   public static Bitmap stringtoBitmap(String string) {
	       Bitmap bitmap = null;
	       try {
	               byte[] bitmapArray;
	               bitmapArray = Base64.decode(string, Base64.DEFAULT);
	               bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
	                               bitmapArray.length);
	       } catch (Exception e) {
               Logger.showLogfoException(e);
               return bitmap;
	       }
	       return bitmap;
	}

	/**
	 * 将Bitmap转换成BASE64字符串
	 * @param bitmap
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
    public static String bitmaptoBase64String(Bitmap bitmap) {
	       String string = null;
	       ByteArrayOutputStream bStream = new ByteArrayOutputStream();
	       bitmap.compress(CompressFormat.PNG, 100, bStream);
	       byte[] bytes = bStream.toByteArray();
	       string = Base64.encodeToString(bytes, Base64.DEFAULT);
	       return string;
	}

 /**
     * 获取裁剪后的圆形图片
     *
     * @param radius 半径
     */
    public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        //   int diameter = radius * 2;
        int diameter = radius;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth;
        int squareHeight;
        int x, y;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }
        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);
        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        return output;
    }


    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     */
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 500) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        newOpts.inPreferredConfig =Config.ARGB_4444;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 480f;//这里设置高度为800f
        float ww = 800f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
       // return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        return bitmap;
    }


    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 15) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;//每次都减少5
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


	public int judgeImagePosition(String imagepath,String imageName) {
		int position = 0;
		File file = new File(imagepath);
		if (!file.exists()) {
			return position;
		}
		File[] array = file.listFiles();
        if(array!=null){
            for (int i = 0; i < array.length; i++) {
                String name = array[i].getAbsolutePath();
                if (name.equals(imagepath + imageName)) {
                    position = i;
                }
            }
        }

		return position;
	}



	public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig =Config.ARGB_4444;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
       options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
       options.inJustDecodeBounds = false;

      return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放比值
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if(height>width){
            reqWidth=480;
            reqHeight=800;
        }else{
            reqHeight=480;
            reqWidth=800;
        }
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    // base64 --> bitmap

    @SuppressLint("NewApi")
    public static Bitmap Base64ToBitmap(String base64Str) {
        Bitmap bitmap = null;
        byte[] bitmapArray;
        try {
            bitmapArray = Base64.decode(base64Str, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public static final int UNCONSTRAINED = -1;

    // 获得设置信息
    public static BitmapFactory.Options getOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只描边，不读取数据
        options.inJustDecodeBounds = true;
        // 加载到内存
        BitmapFactory.decodeFile(path, options);
        return options;
    }



    private static Rect getScreenRegion(int width, int height) {
        return new Rect(0, 0, width, height);
    }
    public static Bitmap getBitmap(ContentResolver cr, Uri mImageCaptureUri) {

        Bitmap bitmap = null;
        Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));// 获取图片路
            String orientation = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.ORIENTATION));// 获取旋转的角度
            cursor.close();
            if (filePath != null) {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                int scaleFactor = 1;
                int max = 800;
                scaleFactor = Math.max(photoW / max, photoH / max);

                if (scaleFactor <= 0) {
                    scaleFactor = 1;
                }
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                bitmap = BitmapFactory.decodeFile(filePath, bmOptions);

                int angle = 0;
                if (orientation != null && !"".equals(orientation)) {
                    angle = Integer.parseInt(orientation);
                }
                if (angle != 0) {
                    // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                    Matrix m = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    m.setRotate(angle); // 旋转angle度
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                            m, true);// 从新生成图片

                }
            }
        }
        return bitmap;
    }


    /**
     * 将拍照后自动旋转的图片进行旋转回去
     * @param cr
     * @param mImageCaptureUri
     * @param bitmap
     * @return
     */
    public static Bitmap getBitmap_portrait(ContentResolver cr,
                                            Uri mImageCaptureUri, Bitmap bitmap) {

        Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
            String orientation = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.ORIENTATION));// 获取旋转的角度
            cursor.close();

            int angle = 0;
            if (orientation != null && !"".equals(orientation)) {
                angle = Integer.parseInt(orientation);
            }
            if (angle != 0) {
                // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                Matrix m = new Matrix();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                m.setRotate(angle); // 旋转angle度
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m,
                        true);// 从新生成图片

            }

        }
        return bitmap;
    }


    /**
     * 保存bitmap到sdcard上面
     * @param bitmap
     * @param newFilePath
     * @param newFileName
     * @return
     */
    public static File saveBitmapToFile(Bitmap bitmap, String newFilePath,
                                         String newFileName,CompressFormat fileformat) {
        boolean bCompressReturn = false;
        try {
            FileUtil fileUtil=new FileUtil();
            File folder = fileUtil.createSDDir(newFilePath);
            File newImgFile =fileUtil.createSDFile(newFilePath+newFileName);

            if (newImgFile.exists()) {
                newImgFile.delete();
            }

            FileOutputStream out = new FileOutputStream(newImgFile);
            bCompressReturn = bitmap.compress(fileformat, 90,
                    out);

            out.flush();
            out.close();
            if (bCompressReturn) {
                return newImgFile;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
    public static class ImageLoadingListener_ClickShowImg implements
            ImageLoadingListener {
        ImageView imageView;
        int cancelled_rid=0;
        int failed_rid=0;
        int started_rid=0;
        int def_rid=0;
        public ImageLoadingListener_ClickShowImg(ImageView imageView) {
            this(imageView,0,0,0,0);

        }
        public ImageLoadingListener_ClickShowImg(ImageView imageView,int def_rid) {
            this(imageView,def_rid,def_rid,def_rid,def_rid);
        }
        public ImageLoadingListener_ClickShowImg(ImageView imageView,int def_rid,int cancelled_rid,int failed_rid,int started_rid) {
            this.imageView=imageView;
            this.def_rid=def_rid;
            this.cancelled_rid=cancelled_rid;
            this.failed_rid=failed_rid;
            this.started_rid=started_rid;
        }
        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            Logger.d("ImageLoadingListener_ClickShowImg", "onLoadingComplete() loadedImage:" + loadedImage);
            if (imageView != null&&loadedImage!=null) {
                imageView.setOnClickListener(new OnClickListener_showImg(
                        loadedImage));
                imageView.setImageBitmap(loadedImage);
            }else if(loadedImage==null){
                if(def_rid!=0){
                    imageView.setImageResource(def_rid);
                }
            }
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {
            Logger.d("ImageLoadingListener_ClickShowImg","onLoadingCancelled() ");
            if(cancelled_rid!=0){
                imageView.setImageResource(cancelled_rid);
            }
        }

        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            Logger.d("ImageLoadingListener_ClickShowImg","onLoadingFailed() ");
            if(failed_rid!=0){
                imageView.setImageResource(failed_rid);
            }

        }

        @Override
        public void onLoadingStarted(String arg0, View arg1) {
            Logger.d("ImageLoadingListener_ClickShowImg","onLoadingStarted() ");
            if(started_rid!=0){
                imageView.setImageResource(started_rid);
            }

        }
    }

    public static class OnClickListener_showImg implements View.OnClickListener {
        Bitmap bitmap;

        public OnClickListener_showImg(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void onClick(View v) {

            if (bitmap != null) {

                Dialog dialog_showImg = new Dialog(v.getContext(),
                        R.style.CustomProgressDialog);
                View layout = new PicShowLayout(v.getContext(), bitmap);
                dialog_showImg.setContentView(layout);
                dialog_showImg.getWindow().setLayout(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                dialog_showImg.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

                dialog_showImg.show();
            }

        }

    }





    public static class OnTouchListener_view_transparency implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("onTouch", "onTouch OnTouchListener_view_transparency event.getAction():" + event.getAction());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.25f);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setAlpha(1);
            }
            if(v.getId()== R.id.ib_back||v.getId()== R.id.bar_left_button2){
                SystemUtil.hiddenKeyBord(v.getContext(),v);
            }
            return false;
        }

    }

    /**
     * 获得路径Media的路径，入图片等
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getMediaPath(Activity context,Uri uri) {
        String filePath=null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat&& DocumentsContract.isDocumentUri(context, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                    sel, new String[] { id }, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }else{
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
        }
        return filePath;

    }


    /**
     * 保存图片到本地并可以在系统图片中查看
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/" + fileName))));
    }

    /**
     * 图片添加文字水印
     */
    public static Bitmap addMark(Bitmap photo){
        int width = photo.getWidth(), hight = photo.getHeight();
        Bitmap icon = Bitmap.createBitmap(width, hight, Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(15.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.RED);//采用的颜色
        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
        String str="仅限xx展示用，其他用处无效";
        canvas.drawText(str, 1, hight/2, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return icon;
    }


    /**
     * 判断照片角度
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 旋转照片
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
}
