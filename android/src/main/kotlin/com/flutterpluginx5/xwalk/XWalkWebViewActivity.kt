package com.cjx.x5_webview

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebSettings
import org.xwalk.core.*

class XWalkWebViewActivity : XWalkActivity() {
    val TAG = "XWalkWebViewActivity"
    var xwalkview: XWalkView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        Log.d(TAG, "onCreate")
        xwalkview = XWalkView(this)
        setContentView(xwalkview)
    }

    override fun onXWalkReady() {

        Log.d(TAG, "onXWalkReady")
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true) //开启默认动画
        var setting = xwalkview?.settings
        setting?.loadWithOverviewMode = false
        setting?.javaScriptEnabled = true                        //支持js
        setting?.javaScriptCanOpenWindowsAutomatically = true    //支持通过JS打开新窗口
        setting?.useWideViewPort = true                          //将图片调整到合适webview的大小
        setting?.loadWithOverviewMode = true                     //缩放至屏幕的大小
        setting?.loadsImagesAutomatically = true                 //支持自动加载图片
        setting?.supportMultipleWindows()                        //支持多窗口
        setting?.setSupportZoom(true)
        setting?.allowFileAccess = true
        setting?.setDomStorageEnabled(true)
        setting?.allowContentAccess = true
        setting?.domStorageEnabled = true
        xwalkview?.requestFocus()
        setting?.cacheMode = WebSettings.LOAD_NO_CACHE
        xwalkview?.setResourceClient(object : XWalkResourceClient(xwalkview) {
            override fun onLoadStarted(view: XWalkView?, url: String?) {
                super.onLoadStarted(view, url)
                Log.d(TAG, "onLoadStarted url ${url}")
            }

            override fun onLoadFinished(view: XWalkView?, url: String?) {
                super.onLoadFinished(view, url)
                Log.d(TAG, "onLoadFinished url ${url}")
            }

            override fun shouldOverrideUrlLoading(view: XWalkView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: XWalkView?, callback: ValueCallback<Boolean>?, error: SslError?) {
                callback?.onReceiveValue(true)
//                super.onReceivedSslError(view, callback, error)
                Log.d(TAG,"onReceivedSslError error ${error.toString()}")
            }


            override fun onReceivedLoadError(view: XWalkView?, errorCode: Int,
                                             description: String?, failingUrl: String?) {
                super.onReceivedLoadError(view, errorCode, description, failingUrl)
                Log.d(TAG,"onReceivedLoadError description ${description} errorCode ${errorCode} failingUrl ${failingUrl}")
            }

            override fun onProgressChanged(view: XWalkView?, process: Int) {
                super.onProgressChanged(view, process)
                Log.d(TAG,"onProgressChanged  process${process}")
            }
        })
        xwalkview?.setUIClient(object : XWalkUIClient(xwalkview) {

            override fun onJsAlert(view: XWalkView?, url: String?,
                                   message: String?, result: XWalkJavascriptResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onReceivedTitle(view: XWalkView?, title: String?) {
                super.onReceivedTitle(view, title)
            }

            override fun openFileChooser(view: XWalkView?, uploadFile: ValueCallback<Uri>?, acceptType: String?, capture: String?) {
                super.openFileChooser(view, uploadFile, acceptType, capture)
            }
        })
//        var url = "https://10.155.0.134:31311"
        var url =  "https://10.155.0.134:31311/static/testvideo/video.html?sid=60cbd910-d555-11ea-af73-45d5ffed74da&userName=cross&token=97DD4550E28511EA9A1E5DA467561DB4&serverAddress=wss://10.155.0.135:30670/hari&accountId=AZ019121"
//        var url = "https://www.baidu.com"
        xwalkview?.loadUrl(url)
        Log.d(TAG,"loadUrl ${url}")
    }

    override fun onResume() {
        super.onResume()
//        xwalkview?.let {
//            xwalkview?.pauseTimers()
//            xwalkview?.onHide()
//        }

    }

    override fun onPause() {
        super.onPause()
        xwalkview?.let {
            xwalkview?.pauseTimers()
            xwalkview?.onHide()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (xwalkview?.navigationHistory!!.canGoBack()) {
            xwalkview?.getNavigationHistory()!!.navigate(
                    XWalkNavigationHistory.Direction.BACKWARD, 1)
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (xwalkview != null) {
            xwalkview?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (xwalkview != null) {
            xwalkview?.onNewIntent(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }

}