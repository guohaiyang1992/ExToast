# ExToast
增强版Toast ，支持miui、emui、flyme、锤子os、oppo os、vivo os,支持android 7.0以及以上系统，支持突破通知权限仍可正常弹出

使用方法：<br/>
'''
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.guohaiyang1992:ExToast:V1.0'
	}
 '''

注意：</br>
  1.当前使用模拟器模拟的原生的 android7.1.1 以及以上系统，对于不开启悬浮窗权限时会崩溃，后续可以增加权限判断，目前没有更好的解决办法。</br>
  2.对于国产Rom android 7.1.1 以及以上系统，不开启悬浮窗权限会限制弹窗显示范围，只允许在当前应用中弹窗，开启后允许在全范围内弹出。</br>
  3.对于此库我想说还需要更多的人去用和反馈，因为此处涉及国产rom相关的修改以及android系统本身的修改，只有更多的反馈才能增强其兼容性。</br>
  
  

