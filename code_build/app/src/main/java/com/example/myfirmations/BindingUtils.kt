package com.example.myfirmations

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
    @BindingAdapter("imageName")
    fun setImageName(view: ImageView, imageURL: String?) {
        var imgName:String = imageURL?:"birds"
        val resid =
            view.context.resources.getIdentifier(imgName, "drawable", view.context.packageName)
        if (!(resid==null))
             view.setImageResource(resid)
    }

/*
class BindingUtils {
    companion object {
        @JvmStatic @BindingAdapter("imageURL")
        fun loadImage(view: ImageView, imgName:String){
            val resid =
                view.context.resources.getIdentifier(imgName, "drawable", view.context.packageName)
            //if (!(resid==null))
                view.setImageResource(resid)
        }
    }
}
*/
