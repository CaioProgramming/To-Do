package com.myself.todo.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utilities {
    //Convert Date to Calendar
    private fun dateToCalendar(date: Date?): Calendar? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }





    companion object {
        val RC_SIGN_IN = 1;


        @Throws(IOException::class)
        fun handleSamplingAndRotationBitmap(context: Context?, selectedImage: Uri?): Bitmap? {
            val MAX_HEIGHT = 1024
            val MAX_WIDTH = 1024
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var imageStream = context.getContentResolver().openInputStream(selectedImage)
            BitmapFactory.decodeStream(imageStream, null, options)
            imageStream.close()
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            imageStream = context.getContentResolver().openInputStream(selectedImage)
            var img = BitmapFactory.decodeStream(imageStream, null, options)
            img = rotateImageIfRequired(context, img, selectedImage)
            return img
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options?,
                                          reqWidth: Int, reqHeight: Int): Int { // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) { // Calculate ratios of height and width to requested height and width
                val heightRatio = Math.round(height as Float / reqHeight as Float)
                val widthRatio = Math.round(width as Float / reqWidth as Float)
                // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
// with both dimensions larger than or equal to the requested height and width.
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
                // This offers some additional logic in case the image has a strange
// aspect ratio. For example, a panorama may have a much larger
// width than height. In these cases the total pixels might still
// end up being too large to fit comfortably in memory, so we should
// be more aggressive with sample down the image (=larger inSampleSize).
                val totalPixels = width * height.toFloat()
                // Anything more than 2x the requested pixels we'll sample down further
                val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
                while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                    inSampleSize++
                }
            }
            return inSampleSize
        }

        @Throws(IOException::class)
        private fun rotateImageIfRequired(context: Context?, img: Bitmap?, selectedImage: Uri?): Bitmap? {
            val input = context.getContentResolver().openInputStream(selectedImage)
            val ei: ExifInterface
            ei = if (Build.VERSION.SDK_INT > 23) ExifInterface(input) else ExifInterface(selectedImage.getPath())
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
                else -> img
            }
        }

        private fun rotateImage(img: Bitmap?, degree: Int): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true)
            img.recycle()
            return rotatedImg
        }

        fun convertDate(dia: String?): String? { //2. Test - Convert Date to Calendar
//3. Test - Convert Calendar to Date
            var dia = dia
            val df: DateFormat = SimpleDateFormat("yyy-MM-dd")
            try {
                val result = df.parse(dia)
                val format = SimpleDateFormat("dd MMM yyyy EE")
                dia = format.format(result)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            println(dia)
            return dia
        }

    }
}