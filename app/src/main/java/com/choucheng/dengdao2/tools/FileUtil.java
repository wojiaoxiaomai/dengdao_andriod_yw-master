package com.choucheng.dengdao2.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.choucheng.dengdao2.common.FinalVarible;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {
	public static File updateDir = null;
	public static File updateFile = null;
	
	private static String SDPATH;//用于存sd card 的文件的路径;
	private Bitmap returnBitmap=null;
	private String picpath= FinalVarible.PIC_PATH;





	 /**
	  ** 构造方法
	  * 获取sd卡路径
	  * ***/
      public FileUtil(){
			//获得当前外部存储设备的路径
			SDPATH=Environment.getExternalStorageDirectory()+"/";
	  }
	
      
	/**
	 * 创建文件夹
	 * @param name
	 * @return
	 */
	public static File createFile(String path,String name) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory()
					+ "/" + path);
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					Logger.showLogfoException(e);
				}
			}
		}
		return updateFile;
	}
	
	
		/**
		 * 在SD卡上面创建文件
		 * @throws IOException
		 * */
		public  File createSDFile(String fileName){
			System.out.println("filename:"+fileName);
			File file =new File(SDPATH+fileName);
			return file;
		}

		/**
		  * 在SD卡上创建目录
		  * 判断SD card是否存在
		  * 	String  Environment.getExternalStorageState();  //外部存储设备的状态
		  * 若结果是 Environment.MEDIA_MOUNTED则说明存在可读写
		  * 获得SD card目录
		  *      Environment.getExternalStorageDirectory()
		  */
		public  File createSDDir(String dirName){
			File dir=new File(SDPATH+dirName);
			System.out.println("storage device's state :"+Environment.getExternalStorageState());
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				System.out.println("the result of making directory:"+dir.mkdirs());
				}
			return dir;
		}

		/**
		 * 删除文件
		 * **/
		public boolean removeFile(String filename){
			File file=new File(SDPATH+filename);
			if(file.exists()){
				return file.delete();
			}else{
				return false;
			}


		}

		/**
		 *  判断SD卡上的文件夹是否存在
		 **/
		public static boolean isFileExist(String fileName){
			File file=new File(SDPATH+fileName);
			return file.exists();
		}

		 /**
	     * 保存图片
	     * @param bitmap
	     * @param fileName
	     * @throws IOException
	     */  
		public File writeBitmapToSD(String fileName,Bitmap bitmap) {
			Logger.i("FileUtil", "writeSDFromInput");
            if(bitmap==null) return null;
			File file=null;
			File tempf;
			try {
				//创建SD卡上的目录
				tempf = createSDDir(picpath);
				synchronized (tempf) {
					System.out.println("directory in the sd card:"+tempf.exists());
					String nameString=picpath+fileName;
				//	removeFile(nameString);
					RenameFile(fileName); //添加标记
					file= createSDFile(nameString);
					System.out.println("file in the sd card:"+file.exists());
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
					String name=fileName.substring(fileName.lastIndexOf("."));
					if(name.equals(".jpg")){
						bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
					}else if(name.equals(".png")){
						bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos); //PNG格式是无损的，因此质量的设置将被忽略，无法进行压缩
					}
					bos.flush();
					bos.close();
					DeletemarkFile();//删除掉标记
				}
				
			//	removeFile(path+marks);
			} catch (FileNotFoundException e) {
				Logger.showLogfoException(e);
			} catch (IOException e) {
				Logger.showLogfoException(e);
			}
			return file;
		}




	/**
	 * 获取指定文件类型的文件路径
	 */
	public List<String> getFileList(String path,String filenameType) {
		List<String> filespath=new ArrayList<>();
		File f = new File(SDPATH+picpath);
		if (f.exists()) {
			if (f.isDirectory()) {
				File[] files = f.listFiles();
				if(files==null) return filespath;
				for (File file : files) {
					String oldname = file.getName();
					if (oldname.endsWith(filenameType)) {//只能锁定相关图片，不相关的则不需要lock
                        filespath.add(file.getPath());
					}
				}
			}
		}
       return filespath;
	}



		/**
		 * 修改以前的图片为标记文件
		 */
		public void RenameFile(String filename) {
			File f = new File(SDPATH+picpath);
			if (f.exists()) {
				if (f.isDirectory()) {
					File[] files = f.listFiles();
                    if(files==null) return;
                    for (File file : files) {
                        String oldname = file.getName();
                        if (oldname.contains(filename)) {//只能锁定相关图片，不相关的则不需要lock
                            String newname = oldname.substring(0, oldname.length());
                            newname = newname + ".lock";
                            if (!oldname.equals(newname)) {
                                String path = file.getParent();
                                File newfile = new File(path + "/" + newname);
                                if (!newfile.exists()) {
                                    file.renameTo(newfile);
                                }
                            }
                        }

                    }
				}
			}
		
		}
		/**
		 * 删除标记文件
		 */
		public void DeletemarkFile() {
			File f = new File(SDPATH+picpath);
			if (f.exists()) {
				if (f.isDirectory()) {
					File[] files = f.listFiles();
					File file;
                    if(files==null) return;
                    for (File file1 : files) {
                        file = file1;
                        String oldname = file.getName();
                        if (oldname.contains(".lock")) {
                            file.delete();
                        }
                    }
				}
			} 
		
		}
		/**
		 * 判断是否存在标记文件
		 */
		public boolean IsmarkFile() {
			File f = new File(SDPATH+picpath);
			if (f.exists()) {
				if (f.isDirectory()) {
					File[] files = f.listFiles();
                    if(files==null) return false;
					File file;
                    for (File file1 : files) {
                        file = file1;
                        String oldname = file.getName();
                        if (oldname.contains(".lock")) {
                            return true;
                        }
                    }
				}
			} 
			return false;
		
		}


		/**
		 * 保存信息到文件中
		 * @param filename
		 * @param content
		 */
		public static void writeFile(String filename, String content) {
			try {
	            FileUtil fileUtil=new FileUtil();
	            filename=FinalVarible.PROJECT_PATH+filename+".txt";
	            File file=fileUtil.createSDFile(filename);
				FileOutputStream outStream = new FileOutputStream(file);
				outStream.write(content.getBytes());
				outStream.close();
			} catch (FileNotFoundException e) {
				Logger.showLogfoException(e);
			} catch (IOException e) {
				Logger.showLogfoException(e);
			}
		}


		/**
		 * 读取文件内容
		 *            文件名称
		 * @return 文件内容
		 */
		public String read(Context context,String filename) {
			try {
				File temp = context.getFileStreamPath(filename);
				if (temp.exists()) {
					FileInputStream inStream = context.openFileInput(filename);
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					byte[] data = outStream.toByteArray();
					outStream.close();
					inStream.close();
					return new String(data);
				} else {
					return "";
				}

			} catch (Exception e) {
				Logger.showLogfoException(e);
				return "";
			}
		}

		/*
		 * public String readFromRaw() { String result = ""; try { InputStream in =
		 * context.getResources().openRawResource(R.raw.init); int length =
		 * in.available(); byte [] buffer = new byte[length]; in.read(buffer);
		 * result = EncodingUtils.getString(buffer, "UTF-8"); } catch (IOException
		 * e) { e.printStackTrace(); return result; } return result; }
		 */

		/**
		 * 读取文件内容
		 * 
		 * @param filename
		 *            文件名称
		 * @return 文件内容
		 */
		public byte[] readFromSD(String filename) {
			try {
				File file = new File(filename);
				FileInputStream inStream = new FileInputStream(file);
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len ;
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				byte[] data = outStream.toByteArray();
				return data;
			} catch (Exception e) {
				Logger.showLogfoException(e);
				return null;
			}
		}


    public static boolean deleteLocalFile(String imgPath) {
        boolean flag = false;
        File file = new File(imgPath);
        if (file.exists()) {
            flag = file.delete();
        }

        return flag;

    }
		
    
	
	  
	/**
	 * 读取文件
	 * **/
	public Bitmap getBitmap(String filename) {
		String path=picpath;
		returnBitmap=null;
        if(filename!=null){
            final String name=path+filename;
            Thread pictureThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    String filename1=name;
                    File imageFile = new File(SDPATH+filename1);
                    FileInputStream inputStream=null;
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = Bitmap.Config.RGB_565;
                    opt.inPurgeable = true;
                    opt.inInputShareable = true;
                    if (imageFile.exists()) {
                        try {
                            inputStream=new FileInputStream(imageFile);
                            returnBitmap = BitmapFactory.decodeStream(inputStream,null,opt);
                            if(inputStream!=null){
                                inputStream.close();
                            }
                        } catch (FileNotFoundException e) {
                            Logger.showLogfoException(e);
                            returnBitmap=null;
                        } catch (IOException e) {
                        	Logger.showLogfoException(e);
                            returnBitmap=null;
                        }catch (OutOfMemoryError e) {
                            Logger.e("OutofMemoryError",e.toString());
                            returnBitmap=null;
                        }
                    }
                }
            });
            pictureThread.start();
            try {
                pictureThread.join();
            } catch (InterruptedException e) {
            	Logger.showLogfoException(e);
            }
        }
		return returnBitmap;
	}



	public static String  getTakepicPath(){
		FileUtil fileUtil=new FileUtil();
		File file=fileUtil.createSDDir(FinalVarible.TAKE_PIC_PATH);
		return file.getPath();
	}



	/**
	 * 递归删除 文件/文件夹
	 *
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}
		
}
