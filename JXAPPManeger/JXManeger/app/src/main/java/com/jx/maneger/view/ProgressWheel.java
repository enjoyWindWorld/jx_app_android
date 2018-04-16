package com.jx.maneger.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.jx.maneger.R;


public class ProgressWheel extends View {
  private static final String TAG = ProgressWheel.class.getSimpleName();

  //Sizes (with defaults)
  private int circleRadius = 80;
  private boolean fillRadius = false;

  private final int barLength = 40;
  private final int barMaxLength = 270;
  private double timeStartGrowing = 0;
  private double barSpinCycleTime = 1000;
  private float barExtraLength = 0;
  private boolean barGrowingFromFront = true;

  private long pausedTimeWithoutGrowing = 0;
  private final long pauseGrowingTime = 300;
  private int barWidth = 5;
  private int rimWidth = 5;

  //Colors (with defaults)
  private int barColor = 0xAA000000;
  private int rimColor = 0x00FFFFFF;

  //Paints
  private Paint barPaint = new Paint();
  private Paint rimPaint = new Paint();

  //Rectangles
  private RectF circleBounds = new RectF();

  //Animation
  //The amount of degrees per second
  private float spinSpeed = 270.0f;
  // The last time the spinner was animated
  private long lastTimeAnimated = 0;

  private float mProgress = 0.0f;
  private float mTargetProgress = 0.0f;
  private boolean isSpinning = false;

  /**
   * The constructor for the ProgressWheel
   *
   * @param context
   * @param attrs
   */
  public ProgressWheel(Context context, AttributeSet attrs) {
    super(context, attrs);

    parseAttributes(context.obtainStyledAttributes(attrs,
            R.styleable.ProgressWheel));
  }

  /**
   * The constructor for the ProgressWheel
   *
   * @param context
   */
  public ProgressWheel(Context context) {
    super(context);
  }

  //----------------------------------
  //Setting up stuff
  //----------------------------------

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int viewWidth = circleRadius + this.getPaddingLeft() + this.getPaddingRight();
    int viewHeight = circleRadius + this.getPaddingTop() + this.getPaddingBottom();

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;

    //Measure Width
    if (widthMode == MeasureSpec.EXACTLY) {
      //Must be this size
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      //Can't be bigger than...
      width = Math.min(viewWidth, widthSize);
    } else {
      //Be whatever you want
      width = viewWidth;
    }

    //Measure Height
    if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY) {
      //Must be this size
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      //Can't be bigger than...
      height = Math.min(viewHeight, heightSize);
    } else {
      //Be whatever you want
      height = viewHeight;
    }

    setMeasuredDimension(width, height);
  }

  /**
   * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
   * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
   * Use this dimensions to setup the bounds and paints.
   */
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    setupBounds(w, h);
    setupPaints();
    invalidate();
  }

  /**
   * 设置进度轮的的属性
   */
  private void setupPaints() {
    barPaint.setColor(barColor);
    barPaint.setAntiAlias(true);
    barPaint.setStyle(Style.STROKE);
    barPaint.setStrokeWidth(barWidth);

    rimPaint.setColor(rimColor);
    rimPaint.setAntiAlias(true);
    rimPaint.setStyle(Style.STROKE);
    rimPaint.setStrokeWidth(rimWidth);
  }

  /**
   * 设置组件的边界
   */
  private void setupBounds(int layout_width, int layout_height) {
    int paddingTop = getPaddingTop();
    int paddingBottom = getPaddingBottom();
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();

    if (!fillRadius) {
      // Width should equal to Height, find the min value to setup the circle
      int minValue = Math.min(layout_width - paddingLeft - paddingRight,
              layout_height - paddingBottom - paddingTop);

      int circleDiameter = Math.min(minValue, circleRadius * 2 - barWidth * 2);

      // Calc the Offset if needed for centering the wheel in the available space
      int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft;
      int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop;

      circleBounds = new RectF(xOffset + barWidth,
              yOffset + barWidth,
              xOffset + circleDiameter - barWidth,
              yOffset + circleDiameter - barWidth);
    } else {
      circleBounds = new RectF(paddingLeft + barWidth,
              paddingTop + barWidth,
              layout_width - paddingRight - barWidth,
              layout_height - paddingBottom - barWidth);
    }
  }

  /**
   * 解析从XML传递给视图的属性
   *
   * @param a the attributes to parse
   */
  private void parseAttributes(TypedArray a) {
    DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
    barWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth, metrics);
    rimWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth, metrics);

    circleRadius = (int) a.getDimension(R.styleable.ProgressWheel_matProg_circleRadius, circleRadius);

    fillRadius = a.getBoolean(R.styleable.ProgressWheel_matProg_fillRadius, false);

    barWidth = (int) a.getDimension(R.styleable.ProgressWheel_matProg_barWidth, barWidth);

    rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_matProg_rimWidth, rimWidth);

    float baseSpinSpeed = a.getFloat(R.styleable.ProgressWheel_matProg_spinSpeed, spinSpeed / 360.0f);
    spinSpeed = baseSpinSpeed * 360;

    barSpinCycleTime = a.getInt(R.styleable.ProgressWheel_matProg_barSpinCycleTime, (int) barSpinCycleTime);

    barColor = a.getColor(R.styleable.ProgressWheel_matProg_barColor, barColor);

    rimColor = a.getColor(R.styleable.ProgressWheel_matProg_rimColor, rimColor);

    if (a.getBoolean(R.styleable.ProgressWheel_matProg_progressIndeterminate, false)) {
      spin();
    }

    // Recycle
    a.recycle();
  }

  //----------------------------------
  //Animation stuff
  //----------------------------------

  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

    boolean mustInvalidate = false;

    if (isSpinning) {
      //Draw the spinning bar
      mustInvalidate = true;

      long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
      float deltaNormalized = deltaTime * spinSpeed / 1000.0f;

      updateBarLength(deltaTime);

      mProgress += deltaNormalized;
      if (mProgress > 360) {
        mProgress -= 360f;
      }
      lastTimeAnimated = SystemClock.uptimeMillis();

      float from = mProgress - 90;
      float length = barLength + barExtraLength;

      canvas.drawArc(circleBounds, from, length, false,
              barPaint);
    } else {
      if (mProgress != mTargetProgress) {
        //We smoothly increase the progress bar
        mustInvalidate = true;

        float deltaTime = (float) (SystemClock.uptimeMillis() - lastTimeAnimated) / 1000;
        float deltaNormalized = deltaTime * spinSpeed;

        mProgress = Math.min(mProgress + deltaNormalized, mTargetProgress);
        lastTimeAnimated = SystemClock.uptimeMillis();
      }

      canvas.drawArc(circleBounds, -90, mProgress, false, barPaint);
    }

    if (mustInvalidate) {
      invalidate();
    }
  }

  private void updateBarLength(long deltaTimeInMilliSeconds) {
    if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
      timeStartGrowing += deltaTimeInMilliSeconds;

      if (timeStartGrowing > barSpinCycleTime) {
        // We completed a size change cycle
        // (growing or shrinking)
        timeStartGrowing -= barSpinCycleTime;
        timeStartGrowing = 0;
        if(!barGrowingFromFront) {
          pausedTimeWithoutGrowing = 0;
        }
        barGrowingFromFront = !barGrowingFromFront;
      }

      float distance = (float) Math.cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI) / 2 + 0.5f;
      float destLength = (barMaxLength - barLength);

      if (barGrowingFromFront) {
        barExtraLength = distance * destLength;
      } else {
        float newLength = destLength * (1 - distance);
        mProgress += (barExtraLength - newLength);
        barExtraLength = newLength;
      }
    } else {
      pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
    }
  }

  /**
   *检查是否正在旋转
   */

  public boolean isSpinning() {
    return isSpinning;
  }

  /**
   * 重置计数
   */
  public void resetCount() {
    mProgress = 0.0f;
    mTargetProgress = 0.0f;
    invalidate();
  }

  /**
   * 关闭自旋模式
   */
  public void stopSpinning() {
    isSpinning = false;
    mProgress = 0.0f;
    mTargetProgress = 0.0f;
    invalidate();
  }

  /**
   * 自旋模式
   */
  public void spin() {
    lastTimeAnimated = SystemClock.uptimeMillis();
    isSpinning = true;
    invalidate();
  }

  /**
   * 将进度设置为一个特定的值，该栏将顺利动画，直到该值
   * @param progress between 0 and 1
   */
  public void setProgress(float progress) {
    if (isSpinning) {
      mProgress = 0.0f;
      isSpinning = false;
    }

    if(progress > 1.0f) {
      progress -= 1.0f;
    } else if(progress < 0) {
      progress = 0;
    }

    if (progress == mTargetProgress) {
      return;
    }

    // If we are currently in the right position
    // we set again the last time animated so the
    // animation starts smooth from here
    if (mProgress == mTargetProgress) {
      lastTimeAnimated = SystemClock.uptimeMillis();
    }

    mTargetProgress = Math.min(progress * 360.0f, 360.0f);

    invalidate();
  }

  /**
   * 将进度设置为一个特定的值，将立即设置为该值
   * @param progress  between 0 and 1
   */
  public void setInstantProgress(float progress) {
    if (isSpinning) {
      mProgress = 0.0f;
      isSpinning = false;
    }

    if(progress > 1.0f) {
      progress -= 1.0f;
    } else if(progress < 0) {
      progress = 0;
    }

    if (progress == mTargetProgress) {
      return;
    }

    mTargetProgress = Math.min(progress * 360.0f, 360.0f);
    mProgress = mTargetProgress;
    lastTimeAnimated = SystemClock.uptimeMillis();
    invalidate();
  }

  @Override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();

    WheelSavedState ss = new WheelSavedState(superState);

    // We save everything that can be changed at runtime
    ss.mProgress = this.mProgress;
    ss.mTargetProgress = this.mTargetProgress;
    ss.isSpinning = this.isSpinning;
    ss.spinSpeed = this.spinSpeed;
    ss.barWidth = this.barWidth;
    ss.barColor = this.barColor;
    ss.rimWidth = this.rimWidth;
    ss.rimColor = this.rimColor;
    ss.circleRadius = this.circleRadius;

    return ss;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state) {
    if(!(state instanceof WheelSavedState)) {
      super.onRestoreInstanceState(state);
      return;
    }

    WheelSavedState ss = (WheelSavedState)state;
    super.onRestoreInstanceState(ss.getSuperState());

    this.mProgress = ss.mProgress;
    this.mTargetProgress = ss.mTargetProgress;
    this.isSpinning = ss.isSpinning;
    this.spinSpeed = ss.spinSpeed;
    this.barWidth = ss.barWidth;
    this.barColor = ss.barColor;
    this.rimWidth = ss.rimWidth;
    this.rimColor = ss.rimColor;
    this.circleRadius = ss.circleRadius;
  }


  /**
   * @return between 0.0 and 1.0,
   */
  public float getProgress() {
    return isSpinning ? -1 : mProgress / 360.0f;
  }

  /**
   * @return 圆环的半径
   */
  public int getCircleRadius() {
    return circleRadius;
  }

  /**
   * Sets the radius of the wheel
   * @param circleRadius the expected radius, in pixels
   */
  public void setCircleRadius(int circleRadius) {
    this.circleRadius = circleRadius;
    if (!isSpinning) {
      invalidate();
    }
  }

  /**
   * @return 圆环的宽度
   */
  public int getBarWidth() {
    return barWidth;
  }

  /**
   * 设置圆环的宽度
   * @param barWidth
   */
  public void setBarWidth(int barWidth) {
    this.barWidth = barWidth;
    if (!isSpinning) {
      invalidate();
    }
  }

  /**
   * 获得当前圆环的颜色
   */
  public int getBarColor() {
    return barColor;
  }

  /**
   *设置圆环的颜色
   */
  public void setBarColor(int barColor) {
    this.barColor = barColor;
    setupPaints();
    if (!isSpinning) {
      invalidate();
    }
  }

  /**
   *获取轮廓的颜色
   */
  public int getRimColor() {
    return rimColor;
  }

  /**
   *设置轮廓的颜色
   */
  public void setRimColor(int rimColor) {
    this.rimColor = rimColor;
    setupPaints();
    if (!isSpinning) {
      invalidate();
    }
  }

  /**
   *转速
   */
  public float getSpinSpeed() {
    return spinSpeed / 360.0f;
  }

  /**
   *设置圆环转动的速度，默认是1.0f
   */
  public void setSpinSpeed(float spinSpeed) {
    this.spinSpeed = spinSpeed * 360.0f;
  }

  /**
   *获得轮廓的宽度
   */
  public int getRimWidth() {
    return rimWidth;
  }

  /**
   *设置轮廓的宽度
   */
  public void setRimWidth(int rimWidth) {
    this.rimWidth = rimWidth;
    if (!isSpinning) {
      invalidate();
    }
  }

  static class WheelSavedState extends BaseSavedState {
    float mProgress;
    float mTargetProgress;
    boolean isSpinning;
    float spinSpeed;
    int barWidth;
    int barColor;
    int rimWidth;
    int rimColor;
    int circleRadius;

    WheelSavedState(Parcelable superState) {
      super(superState);
    }

    private WheelSavedState(Parcel in) {
      super(in);
      this.mProgress = in.readFloat();
      this.mTargetProgress = in.readFloat();
      this.isSpinning = in.readByte() != 0;
      this.spinSpeed = in.readFloat();
      this.barWidth = in.readInt();
      this.barColor = in.readInt();
      this.rimWidth = in.readInt();
      this.rimColor = in.readInt();
      this.circleRadius = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeFloat(this.mProgress);
      out.writeFloat(this.mTargetProgress);
      out.writeByte((byte) (isSpinning ? 1 : 0));
      out.writeFloat(this.spinSpeed);
      out.writeInt(this.barWidth);
      out.writeInt(this.barColor);
      out.writeInt(this.rimWidth);
      out.writeInt(this.rimColor);
      out.writeInt(this.circleRadius);
    }

    //required field that makes Parcelables from a Parcel
    public static final Creator<WheelSavedState> CREATOR =
            new Creator<WheelSavedState>() {
              public WheelSavedState createFromParcel(Parcel in) {
                return new WheelSavedState(in);
              }
              public WheelSavedState[] newArray(int size) {
                return new WheelSavedState[size];
              }
            };
  }
}
