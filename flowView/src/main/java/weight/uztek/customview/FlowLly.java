package weight.uztek.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLly extends ViewGroup {

    private List<List<View>> allViews = new ArrayList<>();

    //每行的宽度
    private List<Integer> widthList = new ArrayList<>();

    //每行的高度
    private List<Integer> heightList = new ArrayList<>();


    public FlowLly(Context context) {
        super(context);
    }

    public FlowLly(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        allViews.clear();
        widthList.clear();
        heightList.clear();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        //行宽
        int lineWidth = 0;

        //行高
        int lineHeight = 0;

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();

        List<View> views = new ArrayList<>();//第一行的View

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            int childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

            int childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            //判断是否抢换行
            if (lineWidth + childWidth > widthSize) {
                //换行
                widthList.add(lineWidth);
                heightList.add(lineHeight);
                lineWidth = childWidth;
                lineHeight = childHeight;
                allViews.add(views);
                views = new ArrayList<>();
                views.add(childView);
            } else {
                //在同一行
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
                views.add(childView);
            }

            if(i==childCount-1){
                widthList.add(lineWidth);
                heightList.add(lineHeight);
                allViews.add(views);
            }

        }

        width = max(widthList);
        height = add(heightList);
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    private int max(List<Integer> datas) {
        int max = datas.get(0);
        for (int i : datas) {
            if (i > max)
                max = i;
        }
        return max;
    }

    private int add(List<Integer> datas) {
        int result = 0;
        for (int i : datas) {
            result += i;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height = 0;
        int width = 0;
        for (int i = 0; i < allViews.size(); i++) {

            for (View view : allViews.get(i)) {

                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

                int left, top, right, bottom;

                left =  width + layoutParams.leftMargin;

                top =  height + layoutParams.topMargin;

                right = left + view.getMeasuredWidth();

                bottom = top + view.getMeasuredHeight();

                width += view.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;

                view.layout(left,top,right,bottom);

            }

            width=0;
            height += heightList.get(i);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
