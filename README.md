Add it in your root settings.gradle at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.zph666:android-utils:Tag'
	}

圆角布局
RCRelativeLayout

文字描边
StrokeTextView

SharedPreferences配置
继承 SPFDelegate
初始化 SPFContext.init(appliction)

音频管理
MusicPoolManager
raw短语音 showSound
raw长语音 showSoundLong
url语音 playSound

垃圾代码生成工具
RubbishCode
使用
class ExampleUnitTest {
@Test
fun addition_isCorrect() {
assertEquals(4, 2 + 2)
runCode()
}

    fun runCode(){
        val rubbishCode = RubbishCode()
        rubbishCode.setPathAndPage("lib_code", "com.sword.utils.code")
        rubbishCode.run()
    }
}


收藏jar
implementation("com.github.bumptech.glide:glide:4.16.0")
// Glide 图片变换库，提供图片变换效果
implementation("jp.wasabeef:glide-transformations:4.3.0")
implementation("com.google.code.gson:gson:2.13.1")
// （LiveData + ViewModel）做 MVVM
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
// 支持协程
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")
// 本地磁盘缓存实现
implementation("com.jakewharton:disklrucache:2.0.2")
// 列表adapter适配器
implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.16")
implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.3.4")
// 毛玻璃模糊库
implementation("com.github.Dimezis:BlurView:version-2.0.6")
// 沉浸式状态栏库，提供沉浸式状态栏支持
implementation("com.geyifeng.immersionbar:immersionbar:3.2.2")
// 沉浸式状态栏 Kotlin 扩展库
implementation("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")
// 音视频播放
implementation("androidx.media3:media3-exoplayer:1.8.0")
implementation("androidx.media3:media3-exoplayer-dash:1.8.0")
implementation("androidx.media3:media3-ui:1.8.0")
implementation("androidx.media3:media3-datasource:1.8.0")
// 进度条
implementation("com.github.FPhoenixCorneaE:SmartProgressBar:1.0.3")
// 星星评级
implementation("com.github.ome450901:SimpleRatingBar:1.5.1")
// 工具类
implementation("com.blankj:utilcodex:1.31.1")
// 我的工具
implementation("com.github.zph666:android-utils:v1.0.2")
// 用户引导
implementation("com.binioter:guideview:1.0.0")
implementation("com.github.huburt-Hu:NewbieGuide:v2.4.4")
// Android 权限请求
implementation("com.github.getActivity:XXPermissions:23.0")
// 冷启动优化启动加速
implementation("com.github.aiceking:AppStartFaster:2.3.0")
// AndroidX 下拉刷新库
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
// EventBus 库，用于组件间通信
implementation("org.greenrobot:eventbus:3.3.1")
// Lottie 库，用于渲染 After Effects 动画
implementation("com.airbnb.android:lottie:6.6.10")
// MMKV 库，腾讯的高性能键值存储库 MMKV 版本是 2.0+，只支持 64 位架构。
api("com.tencent:mmkv:1.3.9")
// okhttp
// Retrofit 的 Gson 转换器，用于将 JSON 转换为 Java/Kotlin 对象
implementation("com.squareup.retrofit2:converter-gson:3.0.0")
// Retrofit 库，用于网络请求
implementation("com.squareup.retrofit2:retrofit:3.0.0")
// OkHttp 日志拦截器，用于网络请求的日志记录
implementation("com.squareup.okhttp3:logging-interceptor:5.2.1")
// Retrofit 的标量转换器，用于处理简单的文本响应
implementation("com.squareup.retrofit2:converter-scalars:3.0.0")
// Retrofit 的 Moshi 转换器，用于 JSON 解析
implementation("com.squareup.retrofit2:converter-moshi:3.0.0")
// 腾讯 Bugly 库，用于崩溃报告
implementation("com.tencent.bugly:crashreport:latest.release")
// 文字自适应
implementation("com.github.wangfeng19930909:AutoFitColorTextView:1.0.0")
// 滚动积分板
implementation("com.github.Wiser-Wong:RollNumber:1.0.2")