package yovi.putra.qrisapp.module

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

/**
 * author yovi.putra
 */
class PointsOverlayView : View {
    private var points: Array<out PointF>? = null
    private var paint: Paint = Paint()

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    private fun init() {
        paint.color = Color.YELLOW
        paint.style = Paint.Style.FILL
    }

    fun setPoints(points: Array<out PointF>?) {
        this.points = points
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        points?.let {
            for (point in it) {
                canvas.drawCircle(point.x, point.y, 10f, paint)
            }
        }
    }
}