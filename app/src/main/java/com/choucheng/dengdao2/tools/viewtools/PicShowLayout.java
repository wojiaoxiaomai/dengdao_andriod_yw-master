package com.choucheng.dengdao2.tools.viewtools;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.choucheng.dengdao2.R;


/**
 * 
 * @描述 图片放大缩小查看
 * 
 */
public class PicShowLayout extends LinearLayout {
	public PicShowLayout(Context context) {
		super(context);

		init(context);
	}
	public PicShowLayout(Context context, Bitmap bitmap) {
		super(context);
		this.bitmap=bitmap;
		init(context);
	}
	public PicShowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private String tag="PicShowLayout";
	public static final String IMAGE_CAPTURE_URI = "ImageCaptureUri"; // 本地路径
	public static final String STR_URL = "strUrl"; // url
	public static int sw;
	public static int sh;


	private static final int NONE = 0; // 表示什么都没做
	private static final int DRAG = 1; // 表示拖动中
	private static final int ZOOM = 2; // 表示缩放中

	private int mode = NONE;
	private float oldDist; // 缩放时两点距离
	private Matrix matrix = new Matrix(); // 坐标变换矩阵
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF(); // 坐标
	private PointF mid = new PointF();

	private float MIN_SCALE = 0.5f; // 最少缩放比例
	private float MAX_SCALE = 4f; // 最大缩放比例

	private int count = 0;
	private Bitmap bitmap;

	private float fristScale;

	private float bw;

	private ImageView imageView_img;
	private void init(Context context){
	    LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_pic_show, this);
        
        Log.d("PicShowActivity", "onCreate");
        
        WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Point p = new Point();
		wm.getDefaultDisplay().getSize(p);

		sw = p.x;
		sh = p.y;
		imageView_img = (ImageView) findViewById(R.id.imageView_img);
		if(bitmap!=null){
			imageView_img.setImageBitmap(bitmap);
		}
		
		
        
	}
	public void setIMG(Bitmap bitmap){
		this.bitmap=bitmap;
		imageView_img.setImageBitmap(bitmap);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:// 单点触摸
			if (count == 0) {
				if (bw > sw) {
					fristScale = sw / bw;
					matrix.setScale(fristScale, fristScale);
				}
				imageView_img.setScaleType(ScaleType.MATRIX);
				center(true, true);
			}
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_POINTER_UP: // 正常情况啥都不做
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // 多点触摸

			oldDist = (float) spacing(event); // 得到还没进行缩放时触摸两点间的距离
			if (oldDist > 10) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {// 处理拖动
				if (event.getX() < imageView_img.getWidth()
						&& event.getY() < imageView_img.getHeight()) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);

				}

			} else if (mode == ZOOM) {// 处理缩放
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		imageView_img.setImageMatrix(matrix);
		CheckScale();
		center(true, true);
		if (count == 0) {
			count++;
		}

		return true;
	}

	/**
	 * 居中显示
	 * 
	 * @param horizontal
	 * @param vertical
	 */
	protected void center(boolean horizontal, boolean vertical) {
		if (bitmap == null) {
			return;
		}
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int screenHeight = imageView_img.getHeight();
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = imageView_img.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int screenWidth = imageView_img.getWidth();
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 计算缩放时两点间的距离
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * 判断缩放比例
	 */
	protected void CheckScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {

			if (p[0] < MIN_SCALE) {

				matrix.setScale(MIN_SCALE, MIN_SCALE);
			}
			if (p[0] > MAX_SCALE) {
				matrix.set(savedMatrix);
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		Log.d(tag, "this.getWidth()" + this.getWidth());
		Log.d(tag, "this.getHeight()" + this.getHeight());
		Log.d(tag, "imageView_img.getWidth()" + imageView_img.getWidth());
		Log.d(tag, "imageView_img.getHeight()" + imageView_img.getHeight());
		super.onWindowFocusChanged(hasWindowFocus);
	}


}
