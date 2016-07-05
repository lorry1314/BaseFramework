package com.musejianglan.baseframework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Blur {

	private static final String TAG = "Blur";

	@SuppressLint("NewApi")
	public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
		if(context == null || sentBitmap == null){
			return null;
		}
		if (VERSION.SDK_INT > 16) {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

			final RenderScript rs = RenderScript.create(context);
			final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			final Allocation output = Allocation.createTyped(rs, input.getType());
			final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
			script.setRadius(radius /* e.g. 3.f */);
			script.setInput(input);
			script.forEach(output);
			output.copyTo(bitmap);
			return bitmap;
		}

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}

	public static void blur(Context context,Bitmap bkg, View view,int radius) {
		if(context == null || bkg == null){
			return ;
		}
		long startMs = System.currentTimeMillis();
		float scaleFactor = 8;

		Bitmap overlay = Bitmap.createBitmap((int) (bkg.getWidth() / scaleFactor), (int) (bkg.getHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);

		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = fastblur(context,overlay, (int)radius);
		view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), overlay));
	}

    public static void blur(Context context,Bitmap bkg, ImageView imageView,int radius) {
    	if(context == null || bkg == null){
			return ;
		}
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;

        Bitmap overlay = Bitmap.createBitmap((int) (bkg.getWidth() / scaleFactor), (int) (bkg.getHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-imageView.getLeft()/scaleFactor, -imageView.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);

        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = fastblur(context,overlay, (int)radius);
        imageView.setImageBitmap(overlay);
    }
    
    public static Bitmap blurImage(Context context,Bitmap bkg,int radius) {
    	if(context == null || bkg == null){
    		return null;
    	}
    	float scaleFactor = 8;
		
    	Bitmap overlay = Bitmap.createBitmap((int) (bkg.getWidth() / scaleFactor), (int) (bkg.getHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(overlay);
    	canvas.translate(0, 0);//目前不知道什么用处
    	canvas.scale(1 / scaleFactor, 1 / scaleFactor);
    	
    	Paint paint = new Paint();
    	paint.setFlags(Paint.FILTER_BITMAP_FLAG);
    	canvas.drawBitmap(bkg, 0, 0, paint);
    	
    	return fastblur(context,overlay, (int)radius);
    }

	public static Bitmap blurImage(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int newColor = 0;

		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				for (int m = 0; m < 9; m++)
				{
					int s = 0;
					int p = 0;
					switch(m)
					{
						case 0:
							s = i - 1;
							p = k - 1;
							break;
						case 1:
							s = i;
							p = k - 1;
							break;
						case 2:
							s = i + 1;
							p = k - 1;
							break;
						case 3:
							s = i + 1;
							p = k;
							break;
						case 4:
							s = i + 1;
							p = k + 1;
							break;
						case 5:
							s = i;
							p = k + 1;
							break;
						case 6:
							s = i - 1;
							p = k + 1;
							break;
						case 7:
							s = i - 1;
							p = k;
							break;
						case 8:
							s = i;
							p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}

				for (int m = 0; m < 9; m++)
				{
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}

				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		return bitmap;
	}

	/**
	 * 柔化效果(高斯模糊)(优化后比上面快三倍)
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				for (int m = -1; m <= 1; m++)
				{
					for (int n = -1; n <= 1; n++)
					{
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		//Log.d("may", "used time="+(end - start));
		return bitmap;
	}

}
