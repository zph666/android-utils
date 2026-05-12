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