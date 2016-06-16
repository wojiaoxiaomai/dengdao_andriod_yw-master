package com.choucheng.dengdao2.tools;

/**
 * Created by zuoyan on 14-3-31.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.InitAppliction;
import com.choucheng.dengdao2.view.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler instance;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

   // private AndroidBugInfo androidBugInfo;
    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }
    /** 获取CrashHandler实例 ,单例模式 */
    public synchronized static CrashHandler getInstance() {
        if (instance == null)
            instance = new CrashHandler();
        return instance;
    }
    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //Logger.LOG_LEVEL=0; //0为发布阶段
    }
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Logger.e(TAG, "error : "+e);
            }
            // 重新启动程序
            Intent intent = new Intent();
            intent.setClass(mContext,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //mContext.startActivity(intent);
            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0,
                    intent, 0);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500,
                    restartIntent); // 1秒钟后重启应用
           /* System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());*/
        }
    }
    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * @param ex 抛出的异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, R.string.crash_exception, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        //保存日志文件
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				 saveCatchInfo2File(ex);
			}
		}).start();
       
        return true;
    }
    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            if(pm==null) return;
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Logger.i(TAG, "an error occured when collect package info:"+e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Logger.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.e(TAG, "an error occured when collect crash info:"+e);
            }
        }
    }
    private String getFilePath() {
       String file_dir ;
        // SD卡是否存在
        boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        // Environment.getExternalStorageDirectory()相当于File file=new File("/sdcard")
        boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
        if (isSDCardExist && isRootDirExist) {
            file_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + FinalVarible.Log_PATH;
        } else {
            file_dir = InitAppliction.getInstance().getFilesDir().getAbsolutePath() + FinalVarible.Log_PATH;
        }
        return file_dir;
    }
    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            String file_dir = getFilePath();
            File dir = new File(file_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(file_dir + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            sendCrashLog2PM(file_dir + fileName);
            fos.close();
            return fileName;
        } catch (Exception e) {
            Logger.e(TAG, "an error occured while writing file..."+ e);
        }
        return null;
    }
    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员
     * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     */
    private void sendCrashLog2PM(String fileName) {
        File file=new File(fileName);
        if (!file.exists()) {
        		Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
        		return;
        	}
       	FileInputStream fis = null;
       	BufferedReader reader = null;
        String s ;
        StringBuffer sb=new StringBuffer();
        try {
        	fis = new FileInputStream(fileName);
        	reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
        	while (true) {
        		s = reader.readLine();
            	if (s == null)
        			break;
                sb.append(s);
        	}
           /* boolean issend=sendHandler(sb.toString());
            if(issend){
                Logger.i(TAG,"send log suc");
                file.delete();
            }else{
                Logger.i(TAG,"send log fail");
            }*/
        } catch (FileNotFoundException e) {
            Logger.showLogfoException(e);
        } catch (Exception e) {
        	Logger.showLogfoException(e);
        } finally { // 关闭流
        	try {
        		reader.close();
        		fis.close();
        	} catch (Exception e) {
        		Logger.showLogfoException(e);
        	}
        }
    }


   

}