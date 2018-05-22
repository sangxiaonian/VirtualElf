package sang.com.minitools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import sang.com.minitools.R;
import sang.com.minitools.utlis.ConvertUtils;

/**
 * 作者： ${PING} on 2018/5/21.
 * 圆角ImageView 基于imageView的圆角图片控件，圆角ImageView 的所有属性
 *
 */

public class CircularImageView extends android.support.v7.widget.AppCompatImageView {

    /**
     * 是否显示边框
     */
    private boolean showBorder;

    /**
     * 边框颜色
     */
    private int borderColor;

    private int borderWidth;

    /**
     * 左上圆角半径
     */
    protected int radiusLeftTop;
    /**
     * 右上圆角半径
     */
    protected int radiusRightTop;
    /**
     * 右下圆角半径
     */
    protected int radiusRightBottom;
    /**
     * 左下圆角半径
     */
    protected int radiusLeftBottom;

    protected RectF rectLeftTop, rectRightTop, rectRightBottom, rectLeftBottom;
    protected RectF rectF;


    private Path borderPath;

    private Paint mPaint;

    private Xfermode xfermode;

    public CircularImageView(Context context) {
        this(context, null, 0);
    }

    public CircularImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
        setRadius(100);
        setBackgroundColor(Color.WHITE);
    }


    private void initView(Context context, AttributeSet attrs) {
        borderWidth = ConvertUtils.dip2px(1);
        borderColor = Color.WHITE;

        if (attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularView);
            borderColor = ta.getColor(R.styleable.CircularView_borderColor,Color.WHITE);
            showBorder = ta.getBoolean(R.styleable.CircularView_showBorder,false);
            borderWidth = (int) ta.getDimension(R.styleable.CircularView_borderWidth, ConvertUtils.dip2px(1));
            radiusLeftTop= (int) ta.getDimension(R.styleable.CircularView_radiusLeftTop,0);
            radiusRightTop=(int) ta.getDimension(R.styleable.CircularView_radiusRightTop,0);
            radiusRightBottom=(int) ta.getDimension(R.styleable.CircularView_radiusRightBottom,0);
            radiusLeftBottom=(int) ta.getDimension(R.styleable.CircularView_radiusLeftBottom,0);
            int radius = (int) ta.getDimension(R.styleable.CircularView_radius, -1);
            ta.recycle();
            if (radius>=0){
                setRadius(radius);
            }
        }




        //默认为白色
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(borderColor);
        borderPath = new Path();

        rectLeftTop = new RectF();
        rectRightTop = new RectF();
        rectRightBottom = new RectF();
        rectLeftBottom = new RectF();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    }

    /**
     * 设置圆角大小
     *
     * @param radius
     */
    public void setRadius(int radius) {
        if (getWidth() > 0) {
            int maxRadio = Math.min(getWidth(), getHeight()) / 2 - borderWidth;
            if (radius > maxRadio) {
                radius = maxRadio;
            }
        }
        setRadius(radius, radius, radius, radius);
    }

    /**
     * 设置圆角大小
     *
     * @param leftTop
     * @param rightTop
     * @param rightBottom
     * @param leftBottom
     */
    public void setRadius(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        int min = Math.min(getWidth(), getHeight());
        radiusLeftTop = leftTop>min?min:leftTop;
        radiusRightTop = rightTop>min?min:rightTop;
        radiusRightBottom = rightBottom>min?min:rightBottom;
        radiusLeftBottom = leftBottom>min?min:leftBottom;
        postInvalidate();
    }


    /**
     * 设置是否显示边框
     *
     * @param showBorder
     */
    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
        postInvalidate();
    }

    /**
     * 设置边框宽度
     *
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        setRadius(radiusLeftTop,radiusRightTop,radiusRightBottom,radiusLeftBottom);
    }

    /**
     * 设置边框颜色
     *
     * @param borderColor
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0, 0, w, h);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(rectF, mPaint, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        mPaint.setXfermode(xfermode);
        mPaint.setStrokeWidth(borderWidth);

        mPaint.setStyle(Paint.Style.FILL);
        initRadios(getWidth(), getHeight(), borderPath, borderWidth / 2);
        canvas.drawPath(borderPath, mPaint);
        mPaint.setXfermode(null);

        if (showBorder) {
            mPaint.setColor(borderColor);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(borderPath, mPaint);
        }

        canvas.restore();

    }

    private void initRadios(int w, int h, Path borderPath, int borderWidth) {
        borderPath.reset();
        int left = (int) Math.ceil(borderWidth);
        int top = (int) Math.ceil(borderWidth);

        int right = (int) Math.ceil(w - borderWidth);
        int bottom = (int) Math.ceil(h - borderWidth);
        //左上
        borderPath.moveTo(left, top + radiusLeftTop);
        rectLeftTop.left = left;
        rectLeftTop.top = top;
        rectLeftTop.right = radiusLeftTop * 2 + left;
        rectLeftTop.bottom = top + radiusLeftTop * 2;
        borderPath.arcTo(rectLeftTop, 180, 90);


        //右上
        borderPath.lineTo(right - radiusRightTop, top);
        rectRightTop.left = right - radiusRightTop * 2;
        rectRightTop.top = top;
        rectRightTop.right = right;
        rectRightTop.bottom = top + radiusRightTop * 2;
        borderPath.arcTo(rectRightTop, 270, 90);
//
//        //右下
        borderPath.lineTo(right, bottom - radiusRightBottom);
        rectRightBottom.left = right - radiusRightBottom * 2;
        rectRightBottom.top = bottom - radiusRightBottom * 2;
        rectRightBottom.right = right;
        rectRightBottom.bottom = bottom;
        borderPath.arcTo(rectRightBottom, 360, 90);
//
//        //左下
        borderPath.lineTo(left + radiusLeftBottom, bottom);
        rectLeftBottom.left = left;
        rectLeftBottom.top = bottom - radiusLeftBottom * 2;
        rectLeftBottom.right = radiusLeftBottom * 2 + left;
        rectLeftBottom.bottom = bottom;
        borderPath.arcTo(rectLeftBottom, 90, 90);
        borderPath.lineTo(left, top + radiusLeftTop);
        borderPath.close();
    }

}
