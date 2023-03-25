package com.fitpass.libfitpass.base.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView
{
    private final Context context;
    private final int width, height;
    private final Paint paint;
    private final Paint paintBorder,imagePaint;
    private final Bitmap bitmap2;
    private final Paint paint3;
    private Bitmap bitmap;
    private BitmapShader shader;
    private float radius = 4.0f;
    float x = 0.0f;
    float y = 8.0f;
    private float stroke;
    private float strokeWidth = 0.0f;
    private Bitmap bitmap3;
    private int corner_radius=50;


    public CircularImageView(Context context, int width, int height, Bitmap bitmap)     {
        super(context);
        this.context = context;
        this.width = width;
        this.height = height;

        //here "bitmap" is the square shape(width* width) scaled bitmap ..

        this.bitmap = bitmap;


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);


        paint3=new Paint();
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setColor(Color.WHITE);
        paint3.setAntiAlias(true);

        paintBorder = new Paint();
        imagePaint= new Paint();

        paintBorder.setColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);


        this.bitmap2 = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() - 40), (bitmap.getHeight() - 40), true);


        imagePaint.setAntiAlias(true);




        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Shader b;
        if (bitmap3 != null)
            b = new BitmapShader(bitmap3, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        else
            b = new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        imagePaint.setShader(b);
        canvas.drawBitmap(maskedBitmap(), 20, 20, null);
    }

    private Bitmap maskedBitmap()
    {
        Bitmap l1 = Bitmap.createBitmap(width,width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(l1);
        paintBorder.setShadowLayer(radius, x, y, Color.parseColor("#454645"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final RectF rect = new RectF();
        rect.set(20, 20, bitmap2.getWidth(), bitmap2.getHeight());

        canvas.drawRoundRect(rect, corner_radius, corner_radius, paintBorder);

        canvas.drawRoundRect(rect, corner_radius, corner_radius, imagePaint);

        if (strokeWidth!=0.0f)
        {
            paint3.setStrokeWidth(strokeWidth);
            canvas.drawRoundRect(rect, corner_radius, corner_radius, paint3);
        }

        paint.setXfermode(null);
        return l1;
    }




    // use seekbar here, here you have to pass  "0 -- 250"  here corner radius will change

    public void setCornerRadius(int corner_radius)
    {
        this.corner_radius = corner_radius;
        invalidate();
    }





    public void setShadow(float radius)
    {
        this.radius = radius;
        invalidate();
    }

    // use seekbar here, here you have to pass  "0 -- 10.0f"  here stroke size  will change

    public void setStroke(float stroke)
    {
        this.strokeWidth = stroke;
        invalidate();
    }

    private Bitmap updateSat(Bitmap src, float settingSat)
    {

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(settingSat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvasResult.drawBitmap(src, 0, 0, paint);

        return bitmapResult;
    }




    // use seekbar here, here you have to pass  "0 -- 2.0f"  here saturation  will change

    public void setSaturation(float sat)
    {
        System.out.println("qqqqqqqqqq            "+sat);
        bitmap3=updateSat(bitmap2, sat);

        invalidate();
    }


}
