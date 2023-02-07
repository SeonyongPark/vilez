package kr.co.vilez.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kr.co.vilez.R
import kr.co.vilez.data.appointment.PointData
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("boardImageFromUrl")
    fun bindBoardImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("circleImageFromUrl")
    fun bindCircleImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(view)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @BindingAdapter("datetimeToDate")
    fun datetimeToDate(view: TextView, date: String?) {
        if(!date.isNullOrEmpty()) {
            view.text = date
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val d: Date = dateTimeFormat.parse(date) as Date
            view.text = dateFormat.format(d).toString()
        }
    }

    @JvmStatic
    @BindingAdapter("getPointReason")
    fun getPointReason(view:TextView, point: PointData) {
        if(point.pointVO.type == -1) { // 최초 가입 포인트는 내용 없애기
            view.visibility = View.GONE
            return
        }
        when(point.pointVO.reason) {
            0 -> { // 예약에 의해 삭감/증가된 내역
                if (point.pointVO.point > 0) {
                    view.text = "공유"
                    view.setTextColor(Color.parseColor("#66dd9c"))
                } else { // 대여해서 삭감
                    view.text=  "대여"
                    view.setTextColor(Color.parseColor("#8a8a8a"))
                }
            }
            1-> { // 취소에 의해 돌려받은 내역
                view.text = "예약 취소"
                view.setTextColor(Color.parseColor("#8a8a8a"))
            }
            2-> { // 패널티로 돌려받은 내역
                view.text = "패널티"
                view.setTextColor(Color.parseColor("#fc0101"))
            }
        }
    }

    @JvmStatic
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @BindingAdapter("dateToElapsedTime")
    fun elapsedTime(view: TextView, date:String?) {  // 시간을 경과시간으로 바꿔줌!
        if (!date.isNullOrEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val nowDate: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = nowDate.format(formatter)

            val endDate = dateFormat.parse(formatted.toString()).time
            val startDate = dateFormat.parse(date).time

            val resultTime = (endDate - startDate) / 1000

            val arr = arrayOf(60*60*24*365, 60*60*24*30, 60*60*24, 60*60, 60)
            val date = arrayOf("년", "개월", "일", "시간", "분")
            for(i in 0..4){
                val time = (resultTime / arr[i]).toInt()
                if(time > 0){
                    view.text = time.toString()+date[i]+"전"
                    return
                }
            }
            view.text = "방금 전"
        }
    }
}