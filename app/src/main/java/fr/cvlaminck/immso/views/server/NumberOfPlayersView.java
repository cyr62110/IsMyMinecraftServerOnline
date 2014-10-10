package fr.cvlaminck.immso.views.server;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import fr.cvlaminck.immso.R;

/**
 * Custom view displaying the number of player playing on the server and
 * the maximum number of simultaneous players.
 */
public class NumberOfPlayersView
        extends View {
    private static final float SQUARE_ROOT_2 = (float) Math.sqrt(2.0d);

    private static final int NORMAL = 0;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;

    private int numberOfPlayers = 20;

    private int maxNumberOfPlayers = 20;

    private int numberOfPlayersTextSize = 20;

    private int numberOfPlayersTextColor = Color.BLACK;

    private int maxNumberOfPlayersTextSize = 20;

    private int numberOfPlayersTypeface = 0;

    private String numberOfPlayersFontFamily = null;

    private int numberOfPlayersTextStyle = 0;

    private int maxNumberOfPlayersTextColor = Color.BLACK;

    private int maxNumberOfPlayersTypeface = 0;

    private String maxNumberOfPlayersFontFamily = null;

    private int maxNumberOfPlayersTextStyle = 0;

    /**
     * Paint that will be used to draw the number of player.
     */
    private Paint numberOfPlayersPaint = null;
    private Paint.FontMetricsInt numberOfPlayersFontMetrics = null;
    private int numberOfPlayersHeight = 0;

    /**
     * Paint that will be used to draw the max number of player.
     */
    private Paint maxNumberOfPlayersPaint = null;
    private Paint.FontMetricsInt maxNumberOfPlayersFontMetrics = null;
    private int maxNumberOfPlayersHeight = 0;

    /**
     * Separator width in pixels
     */
    private int separatorWidth = 1;

    /**
     * Length of the separator in pixels
     */
    private int separatorLength = 20;

    /**
     * Space between the separation bar and the numbers.
     */
    private int separatorSpacing = 0;

    private int separatorColor = Color.BLACK;

    /**
     * Paint that will be used to draw the separator between the numbers.
     */
    private Paint separatorPaint = null;

    public NumberOfPlayersView(Context context) {
        super(context);

        //We init all paints required to draw this view with values coming from the attributes
        refreshNumberOfPlayersPaint();
        refreshMaxNumberOfPlayersPaint();
        refreshSeparatorPaint();
    }

    public NumberOfPlayersView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberOfPlayersView);
        parseCustomAttributes(a);
        a.recycle();

        //We init all paints required to draw this view with values coming from the attributes
        refreshNumberOfPlayersPaint();
        refreshMaxNumberOfPlayersPaint();
        refreshSeparatorPaint();
    }

    public NumberOfPlayersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberOfPlayersView, defStyleAttr, 0);
        parseCustomAttributes(a);
        a.recycle();

        //We init all paints required to draw this view with values coming from the attributes
        refreshNumberOfPlayersPaint();
        refreshMaxNumberOfPlayersPaint();
        refreshSeparatorPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            final int measuredWidth = measureWidth();
            if (widthMode == MeasureSpec.UNSPECIFIED || measuredWidth < width)
                width = measuredWidth;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            final int measuredHeight = measureHeight();
            if (heightMode == MeasureSpec.UNSPECIFIED || measuredHeight < height)
                height = measuredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final String sMaxNumberOfPlayers = Integer.toString(maxNumberOfPlayers);
        //Space between the two text in pixels. This space is a square.
        final float spaceBetweenTexts = ((2f / SQUARE_ROOT_2) * separatorSpacing) + (SQUARE_ROOT_2 * separatorWidth);

        float baselineX, baselineY, startX, startY, stopX, stopY;

        //NUMBER OF PLAYERS
        // /!\ Coordinates given to the drawText function are text baseline ones.
        baselineX = getPaddingLeft() + measureNumberOfPlayersMaxWidth();
        baselineY = getPaddingTop() + numberOfPlayersHeight;
        canvas.drawText(Integer.toString(numberOfPlayers), baselineX, baselineY, numberOfPlayersPaint);

        //SEPARATOR
        startX = baselineX + (spaceBetweenTexts / 2f) + (separatorLength / (2f * SQUARE_ROOT_2));
        startY = baselineY + (spaceBetweenTexts / 2f) - (separatorLength / (2f * SQUARE_ROOT_2));
        stopX = baselineX + (spaceBetweenTexts / 2f) - (separatorLength / (2f * SQUARE_ROOT_2));
        stopY = baselineY + (spaceBetweenTexts / 2f) + (separatorLength / (2f * SQUARE_ROOT_2));
        canvas.drawLine(startX, startY, stopX, stopY, separatorPaint);

        //MAX NUMBER OF PLAYERS
        baselineX += spaceBetweenTexts;
        baselineY += spaceBetweenTexts + maxNumberOfPlayersHeight;
        canvas.drawText(sMaxNumberOfPlayers, baselineX, baselineY, maxNumberOfPlayersPaint);
    }

    private void parseCustomAttributes(TypedArray attrs) {
        final int n = attrs.getIndexCount();
        for(int i = 0; i < n; i++) {
            int attr = attrs.getIndex(i);
            switch (attr) {
                case R.styleable.NumberOfPlayersView_numberOfPlayers:
                    numberOfPlayers = attrs.getInt(attr, numberOfPlayers);
                    break;

                case R.styleable.NumberOfPlayersView_numberOfPlayersTextSize:
                    numberOfPlayersTextSize = attrs.getDimensionPixelSize(attr, numberOfPlayersTextSize);
                    break;

                case R.styleable.NumberOfPlayersView_numberOfPlayersTextColor:
                    numberOfPlayersTextColor = attrs.getColor(attr, numberOfPlayersTextColor);
                    break;

                case R.styleable.NumberOfPlayersView_numberOfPlayersTextStyle:
                    numberOfPlayersTextStyle = attrs.getInt(attr, maxNumberOfPlayersTextStyle);
                    break;

                case R.styleable.NumberOfPlayersView_numberOfPlayersTypeface:
                    numberOfPlayersTypeface = attrs.getInt(attr, maxNumberOfPlayersTypeface);
                    break;

                case R.styleable.NumberOfPlayersView_numberOfPlayersFontFamily:
                    numberOfPlayersFontFamily = attrs.getString(attr);
                    break;

                case R.styleable.NumberOfPlayersView_maxNumberOfPlayers:
                    maxNumberOfPlayers = attrs.getInt(attr, maxNumberOfPlayers);
                    break;

                case R.styleable.NumberOfPlayersView_maxNumberOfPlayersTextSize:
                    maxNumberOfPlayersTextSize = attrs.getDimensionPixelSize(attr, maxNumberOfPlayersTextSize);
                    break;

                case R.styleable.NumberOfPlayersView_maxNumberOfPlayersTextColor:
                    maxNumberOfPlayersTextColor = attrs.getColor(attr, maxNumberOfPlayersTextColor);
                    break;

                case R.styleable.NumberOfPlayersView_maxNumberOfPlayersTextStyle:
                    maxNumberOfPlayersTextStyle = attrs.getInt(attr, maxNumberOfPlayersTextStyle);
                    break;

                case R.styleable.NumberOfPlayersView_maxNumberOfPlayersTypeface:
                    maxNumberOfPlayersTypeface = attrs.getInt(attr, maxNumberOfPlayersTypeface);
                    break;

                case R.styleable.NumberOfPlayersView_maxNumberOfPlayersFontFamily:
                    maxNumberOfPlayersFontFamily = attrs.getString(attr);
                    break;

                case R.styleable.NumberOfPlayersView_separatorLength:
                    separatorLength = attrs.getDimensionPixelSize(attr, separatorLength);
                    break;

                case R.styleable.NumberOfPlayersView_separatorWidth:
                    separatorWidth = attrs.getDimensionPixelOffset(attr, separatorWidth);
                    break;

                case R.styleable.NumberOfPlayersView_separatorSpacing:
                    separatorSpacing = attrs.getDimensionPixelSize(attr, separatorSpacing);
                    break;

                case R.styleable.NumberOfPlayersView_separatorColor:
                    separatorColor = attrs.getColor(attr, separatorColor);
                    break;
            }
        }
    }

    private int measureHeight() {
        final Paint.FontMetricsInt maxNumberOfPlayersFontMetric = maxNumberOfPlayersPaint.getFontMetricsInt();

        //Numbers do not have any descent so we just ignore the value
        final int spaceBetweenTextHeight = (int) Math.ceil(((2f / SQUARE_ROOT_2) * separatorSpacing) + (SQUARE_ROOT_2 * separatorWidth));

        //Do not forget to add the padding managed by the View impl.
        return numberOfPlayersHeight + maxNumberOfPlayersHeight + spaceBetweenTextHeight
                + getPaddingTop() + getPaddingBottom();
    }

    private int measureWidth() {
        final String text = Integer.toString(maxNumberOfPlayers);
        final float numberOfPlayersMaxWidth = measureNumberOfPlayersMaxWidth();
        final float maxNumberOfPlayersWidth = maxNumberOfPlayersPaint.measureText(text);
        final float spaceBetweenTextWidth = (int) (((2f / SQUARE_ROOT_2) * separatorSpacing) + (SQUARE_ROOT_2 * separatorWidth));
        return (int) (Math.ceil(numberOfPlayersMaxWidth + maxNumberOfPlayersWidth + spaceBetweenTextWidth) +
                +getPaddingLeft() + getPaddingRight());
    }

    /**
     * Return the maximum width that can be used to display the number of player actually
     * playing on the server
     */
    private float measureNumberOfPlayersMaxWidth() {
        final String text = Integer.toString(maxNumberOfPlayers);
        return numberOfPlayersPaint.measureText(text);
    }

    private int measureNumberOfPlayersHeight() {
        final Rect textBounds = new Rect();
        numberOfPlayersPaint.getTextBounds("8", 0, 1, textBounds);
        return -textBounds.top;
    }

    private int measureMaxNumberOfPlayersHeight() {
        final Rect textBounds = new Rect();
        maxNumberOfPlayersPaint.getTextBounds("8", 0, 1, textBounds);
        return -textBounds.top;
    }

    private void refreshNumberOfPlayersPaint() {
        numberOfPlayersPaint = new Paint();
        numberOfPlayersPaint.setAntiAlias(true);
        numberOfPlayersPaint.setColor(numberOfPlayersTextColor);
        numberOfPlayersPaint.setTextAlign(Paint.Align.RIGHT);
        numberOfPlayersPaint.setTextSize(numberOfPlayersTextSize);

        //Now, we take care of the typeface.
        refreshTypefaceForPaint(numberOfPlayersPaint, numberOfPlayersFontFamily, numberOfPlayersTextStyle,
                numberOfPlayersTypeface);

        //We do not forget to refresh the font metrics too
        numberOfPlayersFontMetrics = numberOfPlayersPaint.getFontMetricsInt();
        numberOfPlayersHeight = measureNumberOfPlayersHeight();
    }

    private void refreshMaxNumberOfPlayersPaint() {
        maxNumberOfPlayersPaint = new Paint();
        maxNumberOfPlayersPaint.setAntiAlias(true);
        maxNumberOfPlayersPaint.setColor(maxNumberOfPlayersTextColor);
        maxNumberOfPlayersPaint.setTextAlign(Paint.Align.LEFT);
        maxNumberOfPlayersPaint.setTextSize(maxNumberOfPlayersTextSize);

        //Now, we take care of the typeface.
        refreshTypefaceForPaint(maxNumberOfPlayersPaint, maxNumberOfPlayersFontFamily, maxNumberOfPlayersTextStyle,
                maxNumberOfPlayersTypeface);

        //We do not forget to refresh the font metrics too
        maxNumberOfPlayersFontMetrics = maxNumberOfPlayersPaint.getFontMetricsInt();
        maxNumberOfPlayersHeight = measureMaxNumberOfPlayersHeight();
    }

    private void refreshTypefaceForPaint(Paint paint, String fontFamily, int textStyle, int typeface) {
        //Now, we take care of the typeface. This is a bit tricky
        Typeface tf = null;
        if(fontFamily != null)
            tf = Typeface.create(fontFamily, textStyle);
        //If we have found a font matching the family and the style, we have finished here
        //Otherwise, we use the typeface and the style to find the right font to use
        if(tf == null) {
            switch (typeface) {
                case SANS:
                    tf = Typeface.SANS_SERIF;
                    break;
                case SERIF:
                    tf = Typeface.SERIF;
                    break;
                case MONOSPACE:
                    tf = Typeface.MONOSPACE;
                    break;
            }
            //If we have a style to apply, we apply it
            if(textStyle > 0) {
                if(tf == null)
                    tf = Typeface.defaultFromStyle(textStyle);
                else
                    tf = Typeface.create(tf, textStyle);
                //Here Android TextView has some computation to allow emulation of inexisting style in the font. Not supported in this View.
            }
        }
        //Finally, we set the typeface on the
        paint.setTypeface(tf);
    }

    private void refreshSeparatorPaint() {
        separatorPaint = new Paint();
        separatorPaint.setColor(separatorColor);
        separatorPaint.setStrokeWidth(separatorWidth);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        //Invalidate the view, the layout does not need to be recalculated.
        invalidate();
    }

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        final int oldMaxNumberOfPlayers = this.maxNumberOfPlayers;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        //If the size of the number has changed, we request a relayout.
        if(Integer.toString(oldMaxNumberOfPlayers).length() != Integer.toString(maxNumberOfPlayers).length())
            requestLayout();
        invalidate();
    }
}
