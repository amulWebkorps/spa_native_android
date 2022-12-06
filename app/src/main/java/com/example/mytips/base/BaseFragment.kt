package com.example.mytips.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.fragment.app.Fragment
import com.example.mytips.R
import com.example.mytips.base.listener.IsolatedListener
import com.example.mytips.base.listener.Listener
import com.example.mytips.utilities.core.Session
import com.example.mytips.utilities.picker.ImagePickerDialog
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.validation.Validator
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment(), OnTouchListener, View.OnClickListener{

    var listener: Listener? = null
    var isolatedListener: IsolatedListener? = null

    private val displayMetrics = DisplayMetrics()
//    var fragmentNavigator: FragmentNavigationListener? = null
//
//    protected set
    protected var density = 0f
    @Inject
    lateinit var validator: Validator
    @Inject
    lateinit var session: Session

    override fun onAttach(context: Context) {
        super.onAttach(context)

            if (context is Listener) {
                listener = context }
            else if (context is IsolatedListener){
                isolatedListener = context
            }
            else {
                //throw RuntimeException("$context must implement LoginListener")
            }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        density = resources.displayMetrics.density
        validator=Validator()
        session = Session(requireContext())
    }

    override fun onStart() {
        super.onStart()
        if (view != null) {
            view?.setOnTouchListener(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        userVisibleHint = false
    }

    override fun onResume() {
        super.onResume()
        userVisibleHint = true
        onFragmentResume()
    }

    override fun onStop() {
        super.onStop()
        Log.e(javaClass.simpleName,  javaClass.simpleName + ".onStop(" + name + ")" )
        super.setUserVisibleHint(false)
    }

    override fun onDetach() {
//        fragmentNavigator!!.onFragmentDetached(javaClass)
        super.onDetach()
    }
    override fun getContext(): Context? {
        return activity
    }

    /* public void resetHeaderBtn() {
        if (fragmentNavigator instanceof HeaderedFragmentNavigator) {
            ((HeaderedFragmentNavigator) fragmentNavigator).setRightBtnEnabled(true);
        }
    }*/
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        // consume the event to prevent other fragment beneath it
        // to capture the touch events:
        return true
    }

    open fun onFragmentResume() {
        Log.e(javaClass.simpleName, javaClass.simpleName + ".onFragmentResume(" + name + ")")
    }

    fun onFragmentResumed() {}
    val name: String
        get() = javaClass.hashCode().toString() + ""

    fun onFragmentRefresh() {}
    fun onUpdate(bundle: Bundle?) {}
    protected fun back() {
//        (fragmentNavigator as SlidingFragmentNavigator?)!!.processBackPressed()
//        (fragmentNavigator as SlidingFragmentNavigator?)!!.toggleSlidingMenu()
    }
    override fun onClick(v: View) {
        when (v.id) {
//            R.id.header_back -> (fragmentNavigator as SlidingFragmentNavigator?)!!.processBackPressed()
//            R.id.header_home -> (fragmentNavigator as SlidingFragmentNavigator?)!!.toggleSlidingMenu()
        }
    }

    protected fun switchFragmentTo(fragmentTo: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.hide(this)
        transaction.show(fragmentTo).commit()
        fragmentTo.onResume()
    }

    fun goBack() {
      //  (fragmentNavigator as SlidingFragmentNavigator?)!!.processBackPressed()
    }

    fun goBackIfPossible() {
      //  (fragmentNavigator as SlidingFragmentNavigator?)!!.processBackPressedSilently()
    }

    interface OnBackPressed {
        fun onBackPressed()
    }

    interface RefreshUIInterface {
        fun onRefreshCountBubble()
        fun onScreenRefresh()
    }

    interface OnActionSelectedListener {
     //   fun onActionSelected(action: Action?, anchor: View?)
        fun onStandardActionSelected(action: String?, anchor: View?)
    }

    companion object {
        fun printStackTrace() {
            Log.e("BaseFragment", Log.getStackTraceString(Exception()))
        }

        fun getEventTitle(time: Calendar): String? {
            return String.format(Locale.US, "Event of %02d:%02d %s/%d", time[Calendar.HOUR_OF_DAY], time[Calendar.MINUTE], time[Calendar.MONTH] + 1, time[Calendar.DAY_OF_MONTH])
        }
    }

    fun imagePicker(
        isVideo: Boolean = false,
        isOnlyVideo: Boolean = false,
        onCallBack: (Uri) -> Unit
    ) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BaseBottomSheetDialog)

        when {
            isOnlyVideo -> {
//                VideoPickerDialog.showDialog(childFragmentManager, { uri, _ ->
//                    val thumb: Bitmap = ThumbnailUtils.createVideoThumbnail(
//                        uri.path.toString(),
//                        MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
//                    )!!
//                    onCallBack.invoke(uri)
//                }, { })
            }
            else -> {
                if (isVideo) {
//                    showSettingsBottomDialog(
//                        dialog,
//                        mediaOption()
//                    ) { it ->
//                        when (it) {
//                            //Image
//                            0 -> {
//                                ImagePickerDialog.showDialog(childFragmentManager, { uri ->
//                                    onCallBack.invoke(uri)
//                                }, { showMessage(it) })
//                            }
//                            //Video
//                            1 -> {
//                                VideoPickerDialog.showDialog(childFragmentManager, { uri, _ ->
//                                    val thumb: Bitmap = ThumbnailUtils.createVideoThumbnail(
//                                        uri.path.toString(),
//                                        MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
//                                    )!!
//                                    onCallBack.invoke(uri)
//                                }, { showMessage(it) })
//                            }
//                        }
//                        dialog.dismiss()
//                    }
                } else {
                    ImagePickerDialog.showDialog(childFragmentManager, { uri ->
                        onCallBack.invoke(uri)
                    }, {

                    })
                }
            }
        }
    }




}