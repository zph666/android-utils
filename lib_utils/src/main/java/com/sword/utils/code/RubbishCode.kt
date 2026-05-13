package com.sword.utils.code

import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.Random

// 垃圾代码生成器，包含 drawable、layout、strings、manifest、java
class RubbishCode {
    fun setPathAndPage(path: String, page: String) {
        libraryPath = path
        packageBase = page
    }

    @Throws(IOException::class)
    fun run() {
        initpath()
        activityList.clear()
        stringList.clear()

        generateClasses()
        generateStringsFile()
    }

    companion object {
        var libraryPath = "lib_code"
        var packageBase = "com.sword.utils.code" // 生成 java 类根包名

        var packageCount = randomInRange(20, 30) // 生成包数量
        var activityCountPerPackage = randomInRange(20, 30) // 每个包下生成 Activity 类数量

        private val random = Random()
        private const val NAME_LENGTH = 5
        private val abc = "abcdefghijklmnopqrstuvwxyz".toCharArray()
        private val colorHex = "0123456789abcdef".toCharArray()
        private val bigValueChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKMNLOPQRSTUVWZYZ0123456789".toCharArray()

        // 布局里统一生成可安全声明的控件，避免随机属性与控件类型不匹配导致 aapt 失败。
        private val layoutWidgets = arrayOf(
            "TextView", "EditText", "Button", "ImageView", "ProgressBar", "SeekBar", "CheckBox", "RadioButton",
            "ToggleButton", "Spinner", "ListView", "GridView", "ScrollView", "DatePicker", "TimePicker",
            "RatingBar", "Switch", "TableLayout", "FrameLayout", "LinearLayout", "RelativeLayout", "ViewFlipper"
        )

        private val textWidgets = setOf("TextView", "EditText", "Button", "CheckBox", "RadioButton", "ToggleButton", "Switch")
        private val imageWidgets = setOf("ImageView")

        private val activityList = mutableListOf<String>()
        private val stringList = mutableListOf<String>()

        private var rootPath: String = ""

        private fun randomInRange(startInclusive: Int, endExclusive: Int): Int {
            require(endExclusive > startInclusive) { "Invalid range: [$startInclusive, $endExclusive)" }
            return Random().nextInt(endExclusive - startInclusive) + startInclusive
        }

        fun initpath() {
            rootPath = "$libraryPath/java/${packageBase.replace('.', '/')}"
            val outputDir = File(rootPath)
            if (outputDir.exists()) {
                outputDir.deleteRecursively()
            }
        }

        @Throws(IOException::class)
        fun generateClasses() {
            repeat(packageCount) {
                val page = generateName()
                val packageDir = "$rootPath/$page"

                repeat(activityCountPerPackage) {
                    val activityPreName = generateName()
                    generateActivity(packageDir, page, activityPreName)
                }
            }
            generateManifest()
        }

        @Throws(IOException::class)
        fun generateClass(
            packageName: String,
            page: String,
            className: String
        ): List<String> {
            val fields = mutableListOf<String>()
            val fieldCount = random.nextInt(20)

            val content = buildString {
                appendLine("package $packageBase.$page;")
                appendLine()
                appendLine("public class $className {")

                repeat(fieldCount) {
                    val name = generateName()
                    fields.add(name)
                    appendLine("    public String $name;")
                }

                appendLine()
                appendLine("    public $className() {")
                appendLine("    }")
                appendLine("}")
            }

            writeStringToFile(File("$packageName/$className.java"), content)
            return fields
        }

        @Throws(IOException::class)
        fun generateActivity(
            packageName: String,
            page: String,
            activityPreName: String
        ) {
            val className = "${abc[random.nextInt(abc.size)].uppercaseChar()}${activityPreName}Activity"
            val layoutName = "layout_$activityPreName"
            val textIds = generateLayout(layoutName)

            val stringsXmlName = generateName().lowercase(Locale.getDefault())
            stringList.add(stringsXmlName)

            val otherClassName = "${abc[random.nextInt(abc.size)].uppercaseChar()}${generateName()}"
            val fieldList = generateClass(packageName, page, otherClassName)
            val widget = layoutWidgets[random.nextInt(layoutWidgets.size)]

            var methodName = generateName()
            val otherClassField = generateName()

            val content = buildString {
                appendLine("package $packageBase.$page;")
                appendLine()
                appendLine("import android.app.Activity;")
                appendLine("import android.os.Bundle;")
                appendLine("import android.view.View;")
                appendLine("import android.widget.Toast;")
                appendLine("import android.widget.$widget;")
                appendLine("import $packageBase.R;")
                appendLine()
                appendLine("public class $className extends Activity {")
                appendLine("    @Override")
                appendLine("    protected void onCreate(Bundle savedInstanceState) {")
                appendLine("        super.onCreate(savedInstanceState);")
                appendLine("        setContentView(R.layout.$layoutName);")

                textIds.forEach {
                    val name = generateName()
                    appendLine("        View $name = findViewById(R.id.$it);")
                    appendLine("        $name.setOnClickListener(new View.OnClickListener() {")
                    appendLine("            @Override")
                    appendLine("            public void onClick(View v) {")
                    appendLine("                $name.setVisibility(View.INVISIBLE);")
                    appendLine("            }")
                    appendLine("        });")
                }

                appendLine()
                appendLine("        $methodName();")
                appendLine("        $otherClassName $otherClassField = new $otherClassName();")
                fieldList.forEach {
                    appendLine("        $otherClassField.$it = \"${generateBigValue()}\";")
                }
                appendLine("        Toast.makeText($className.this, getString(R.string.$stringsXmlName), Toast.LENGTH_SHORT).show();")
                appendLine("    }")

                val methodCount = random.nextInt(20) + 3
                repeat(methodCount) { index ->
                    val nextMethodName = generateName()
                    if (index != methodCount - 1) {
                        appendLine("    void $methodName() {")
                        appendLine("        $nextMethodName();")
                        appendLine("    }")
                    } else {
                        val name = generateName()
                        appendLine("    void $methodName() {")
                        appendLine("        $widget $name = new $widget($className.this);")
                        appendLine("        $name.setVisibility(View.VISIBLE);")
                        appendLine("    }")
                    }
                    methodName = nextMethodName
                }

                appendLine("}")
            }

            writeStringToFile(File("$packageName/$className.java"), content)
            activityList.add("$packageBase.$page.$className")
        }

        @Throws(IOException::class)
        fun generateLayout(layoutName: String): List<String> {
            val textIds = mutableListOf<String>()
            val drawableName = generateName().lowercase(Locale.getDefault())
            generateDrawable(drawableName)

            val content = buildString {
                appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                appendLine("<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"")
                appendLine("    android:layout_width=\"match_parent\"")
                appendLine("    android:layout_height=\"wrap_content\"")
                appendLine("    android:orientation=\"vertical\">")

                val viewCount = random.nextInt(20)
                repeat(viewCount) {
                    val id = generateName()
                    textIds.add(id)
                    val widget = layoutWidgets[random.nextInt(layoutWidgets.size)]

                    appendLine("    <$widget")
                    appendLine("        android:id=\"@+id/$id\"")
                    appendLine("        android:layout_width=\"match_parent\"")
                    appendLine("        android:layout_height=\"${random.nextInt(600).coerceAtLeast(1)}dp\"")

                    if (widget in textWidgets) {
                        appendLine("        android:text=\"${generateName()}\"")
                    }
                    if (widget in imageWidgets) {
                        appendLine("        android:src=\"@drawable/$drawableName\"")
                    }
                    if (widget == "LinearLayout") {
                        val orientation = if (random.nextBoolean()) "horizontal" else "vertical"
                        appendLine("        android:orientation=\"$orientation\"")
                    }

                    appendLine("        />")
                }

                appendLine("</LinearLayout>")
            }

            writeStringToFile(File("$libraryPath/res/layout/$layoutName.xml"), content)
            return textIds
        }

        @Throws(IOException::class)
        fun writeStringToFile(file: File, data: String) {
            val parent = file.parentFile
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            file.bufferedWriter().use { writer ->
                writer.write(data)
            }
        }

        @Throws(IOException::class)
        fun generateManifest() {
            val content = buildString {
                appendLine("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">")
                appendLine("    <application>")
                activityList.forEach {
                    appendLine("        <activity android:name=\"$it\"/>")
                }
                appendLine("    </application>")
                appendLine("</manifest>")
            }

            writeStringToFile(File("$libraryPath/AndroidManifest.xml"), content)
        }

        fun generateName(): String {
            val sb = StringBuilder(NAME_LENGTH)
            repeat(NAME_LENGTH) {
                sb.append(abc[random.nextInt(abc.size)])
            }
            return sb.toString()
        }

        fun generateBigValue(): String {
            val length = random.nextInt(1000)
            val sb = StringBuilder(length)
            repeat(length) {
                sb.append(bigValueChars[random.nextInt(bigValueChars.size)])
            }
            return sb.toString()
        }

        @Throws(IOException::class)
        fun generateStringsFile() {
            val content = buildString {
                appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                appendLine("<resources>")
                stringList.forEach {
                    appendLine("    <string name=\"$it\">${generateBigValue()}</string>")
                }
                appendLine("</resources>")
            }

            writeStringToFile(File("$libraryPath/res/values/strings.xml"), content)
        }

        @Throws(IOException::class)
        fun generateDrawable(drawableName: String) {
            val width = random.nextInt(100).coerceAtLeast(1)
            val height = random.nextInt(100).coerceAtLeast(1)
            val viewportWidth = random.nextInt(100).coerceAtLeast(1)
            val viewportHeight = random.nextInt(100).coerceAtLeast(1)

            val pointCount = random.nextInt(40).coerceAtLeast(2)
            val pathData = buildString {
                append('M')
                repeat(pointCount) { index ->
                    if (index > 0) append(',')
                    append(random.nextInt(100))
                }
                append('z')
            }

            val content = buildString {
                appendLine("<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"")
                appendLine("    android:width=\"${width}dp\"")
                appendLine("    android:height=\"${height}dp\"")
                appendLine("    android:viewportWidth=\"$viewportWidth\"")
                appendLine("    android:viewportHeight=\"$viewportHeight\">")
                appendLine("    <path")
                appendLine("        android:fillColor=\"${generateColor()}\"")
                appendLine("        android:pathData=\"$pathData\" />")
                appendLine("</vector>")
            }

            writeStringToFile(File("$libraryPath/res/drawable/$drawableName.xml"), content)
        }

        fun generateColor(): String {
            val sb = StringBuilder(7)
            sb.append('#')
            repeat(6) {
                sb.append(colorHex[random.nextInt(colorHex.size)])
            }
            return sb.toString()
        }
    }
}
