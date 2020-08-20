package com.flutterpluginx5.xwalk

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull;
import com.cjx.x5_webview.XWalkWebViewActivity

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** XwalkPlugin */
public class XwalkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  constructor(mContext: Context, mActivity: Activity){
    this.mActivity=mActivity
    this.mContext=mContext
  }
  constructor()

  var mContext: Context? = null
  var mActivity: Activity? = null
  var mFlutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null

  private lateinit var channel : MethodChannel
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "xwalk")
    channel.setMethodCallHandler(this)
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "xwalk")
      channel.setMethodCallHandler(XwalkPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }else if ( call.method == "openWebActivity"){
      val url = call.argument<String>("url")
      val title = call.argument<String>("title")
      val headers = call.argument<HashMap<String,String>>("headers")?:HashMap()
      val isUrlIntercept=call.argument<Boolean>("isUrlIntercept")
      val intent = Intent(mActivity, XWalkWebViewActivity::class.java)
      intent.putExtra("url", url)
      intent.putExtra("title", title)
      intent.putExtra("headers", headers)
      intent.putExtra("isUrlIntercept", isUrlIntercept)
      mActivity?.startActivity(intent)
      result.success(null)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onDetachedFromActivity() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    if (mFlutterPluginBinding == null) {
      this.mActivity = binding.activity
      return
    }
    this.mActivity = binding.activity
    this.mContext = binding.activity.applicationContext
  }

  override fun onDetachedFromActivityForConfigChanges() {
    }
}
