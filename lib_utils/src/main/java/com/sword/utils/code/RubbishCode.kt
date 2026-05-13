package com.sword.utils.code

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Locale
import java.util.Random
import kotlin.Array
import kotlin.CharArray
import kotlin.Exception
import kotlin.Int
import kotlin.Throws
import kotlin.arrayOf
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.indices
import kotlin.collections.plus
import kotlin.plus
import kotlin.sequences.plus
import kotlin.text.StringBuilder
import kotlin.text.lowercase
import kotlin.text.plus
import kotlin.text.replace
import kotlin.text.toCharArray
import kotlin.text.uppercase

//垃圾代码生成器，包含drawable,layout,string,manifests,java
//使用方法：选中这个文件，右键编译，编译好后通过project方式打开就可以找到生成的代码，不是通过android目录结构
@RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
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

        //生成成类
        generateClasses()
        //生成资源
        generateStringsFile()
    }

    companion object {
        var libraryPath = "lib_code"
        var packageBase = "com.sword.utils.code" //生成java类根包名

        private fun randomInRange(startInclusive: Int, endExclusive: Int): Int {
            return Random().nextInt(endExclusive - startInclusive) + startInclusive
        }

        var packageCount = randomInRange(20, 30) //生成包数量 30
        var activityCountPerPackage = randomInRange(20, 30) //每个包下生成Activity类数量  30

        var views = arrayOf<String>(
            "TextView",
            "EditText",
            "Button",
            "ImageView",
            "ProgressBar",
            "SeekBar",
            "CheckBox",
            "RadioButton",
            "ToggleButton",
            "Spinner",
            "ListView",
            "GridView",
            "ScrollView",
            "DatePicker",
            "TimePicker",
            "RatingBar",
            "Switch",
            "TableLayout",
            "FrameLayout",
            "LinearLayout",
            "RelativeLayout",
            "ViewFlipper"
        )
        var random: Random = Random()
        var rootpath: String? = null
        var abc: CharArray = "abcdefghijklmnopqrstuvwxyz".toCharArray()
        var color: CharArray = "0123456789abcdef".toCharArray()
        var activityList: MutableList<String?> = ArrayList<String?>()
        var stringList: MutableList<String?> = ArrayList<String?>()

        fun initpath() {
            val basepath = packageBase
            rootpath = libraryPath + "/java/" + basepath.replace(".", "/")
            val file = File(basepath)
            if (file.exists()) {
                file.delete()
            }
        }


        @Throws(IOException::class)
        fun generateClasses() {
            for (i in 0..<packageCount) {
                val page: kotlin.String = generateName()
                val packageName: kotlin.String = rootpath + "/" + page
                //生成Activity
                for (j in 0..<activityCountPerPackage) {
                    val activityPreName: kotlin.String = generateName()
                    generateActivity(packageName, page, activityPreName)
                }
            }
            //所有Activity生成完了
            generateManifest()
        }


        //普通类
        @Throws(IOException::class)
        fun generateClass(
            packageName: kotlin.String?,
            page: kotlin.String?,
            className: kotlin.String?
        ): MutableList<kotlin.String?> {
            val fields: MutableList<kotlin.String?> = ArrayList<kotlin.String?>()
            var content =
                "package  " + packageBase + "." + page + ";\n" + "\n" + "import java.lang.Exception;\n" + "import java" + ".lang.RuntimeException;\n" + "import java.lang.String;\n" + "import java.lang.System;\n" + "import " + "java.util.Date;\n" + "\n" + "public class " + className + "  {\n"

            val t: Int = random.nextInt(20)
            for (i in 0..<t) {
                val name: kotlin.String = generateName()
                fields.add(name)
                content = content + "\npublic String " + name + ";"
            }


            content = content + "\n      public " + className + "() {\n" + "        }\n}\n"

            val drawableFile = File(packageName + "/" + className + ".java")
            writeStringToFile(drawableFile, content)
            return fields
        }

        @Throws(IOException::class)
        fun generateActivity(
            packageName: kotlin.String,
            page: kotlin.String?,
            activityPreName: kotlin.String
        ) {
            val className = abc[random.nextInt(abc.size)].toString()
                .uppercase(Locale.getDefault()) + activityPreName + "Activity"
            val layoutName = "layout_" + activityPreName
            val textIds: MutableList<kotlin.String?> = generateLayout(layoutName) //生成layout

            val stringsxml: kotlin.String =
                generateName().lowercase(Locale.getDefault()) //生成strings字符串
            stringList.add(stringsxml)

            val otherclassName = abc[random.nextInt(abc.size)].toString()
                .uppercase(Locale.getDefault()) + generateName()
            val fieldlist: MutableList<kotlin.String?> =
                generateClass(packageName, page, otherclassName)
            val widget: kotlin.String? = views[random.nextInt(views.size)]
            var content =
                "package  " + packageBase + "." + page + ";\n" + "\n" + "import android.app" + ".Activity;\n" + "import android.os.Bundle;\n" + "import " + packageBase + ".R;\n" + "import java" + ".lang.Exception;\n" + "import java.lang.Override;\n" + "import java.lang.RuntimeException;\n" + "import java.lang.String;\n"
            content = content + "import android.widget." + widget + ";\n"
            content =
                content + "import android.view.View;\n" + "import android.widget.TextView;\n" + "import java.lang" + ".System;\n" + "import android.widget.Toast;\n" + "import java.util.Date;\n" + "\n" + "public class " + className + " extends Activity {\n" + "    @Override\n" + "    protected void onCreate(Bundle savedInstanceState) {\n" + "        super.onCreate(savedInstanceState);\n" + "        setContentView(R.layout." + layoutName + ");\n"

            for (i in textIds.indices) {
                val name: kotlin.String = generateName()
                content =
                    content + "     View  " + name + " = findViewById(R.id." + textIds.get(i) + ");\n" + "         " + name + ".setOnClickListener(new View.OnClickListener() {\n" + "            @Override\n" + "    " + "        public void onClick(View v) {\n" + name + ".setVisibility(View.INVISIBLE);\n" + "       " + "     }\n" + "        });\n"
            }

            val otherclassNamefeild: kotlin.String = generateName()
            var methodName: kotlin.String = generateName()

            content = (content + "\n" + methodName + "();\n" //本类方法
                    + otherclassName + "   " + otherclassNamefeild + " =     new " + otherclassName + "();\n")
            for (i in fieldlist.indices) {
                content =
                    content + otherclassNamefeild + "." + fieldlist.get(i) + " =\"" + generateBigValue() + "\";\n"
            }
            content =
                content + " Toast.makeText(" + className + ".this,getString(R.string." + stringsxml + "),Toast" + ".LENGTH_SHORT).show();\n" + "    }"

            //其它方法
            val bwe: Int = random.nextInt(20) + 3
            for (j in 0..<bwe) { //生成方法
                val methodNamenext: kotlin.String = generateName()

                if (j != bwe - 1) {
                    content =
                        (content + "\n" + " void " + methodName + "() {" + "\n         " + methodNamenext + "();\n" //调用下一个方法
                                + "}") //方法末尾
                } else {
                    val name: kotlin.String = generateName()
                    content =
                        content + "\n" + " void " + methodName + "() {\n" + widget + " " + name + "   = new " + widget + "(" + className + ".this);\n" + name + ".setVisibility(View.VISIBLE);\n" + "}" //方法末尾
                }
                methodName = methodNamenext
            }
            content = content + "}" //类末尾

            val drawableFile = File(packageName + "/" + className + ".java")
            println(packageName)
            println(className)
            writeStringToFile(drawableFile, content)
            val actpath =
                packageName.replace(packageBase + "/", "").replace("/", ".") + "." + className
            activityList.add(actpath)
        }


        /**
         * 生成layout
         */
        @Throws(IOException::class)
        fun generateLayout(layoutName: kotlin.String?): MutableList<kotlin.String?> {
            val textids: MutableList<kotlin.String?> = ArrayList<kotlin.String?>()
            val drawableName: kotlin.String = generateName().lowercase(Locale.getDefault())
            generateDrawable(drawableName)

            var content =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<LinearLayout " + "xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" + "    android:layout_width=\"match_parent\"\n" + "    android:layout_height=\"wrap_content\"\n" + "    android:orientation=\"vertical\">\n"
            val t: Int = random.nextInt(20)

            for (i in 0..<t) {
                val id: kotlin.String = generateName()
                textids.add(id)
                val widget: kotlin.String = views[random.nextInt(views.size)]
                content =
                    content + "   <" + widget + "\n" + "        android:id=\"@+id/" + id + "\"\n" + "        " + "android:layout_width=\"match_parent\"\n" + "        android:layout_height=\"" + random.nextInt(
                        600
                    ) + "dp\"\n" + "        android:text=\"" + generateName() + "\" \n" + "        android:src=\"@drawable/" + drawableName + "\" \n"
                if (widget == "LinearLayout") {
                    if (random.nextInt(10) % 2 == 0) {
                        content = content + "android:orientation=\"horizontal\""
                    } else {
                        content = content + "android:orientation=\"horizontal\""
                    }
                }

                content = content + "/>\n"
            }

            content = content + "   </LinearLayout>\n"

            val layoutFile = File(libraryPath + "/res/layout/" + layoutName + ".xml")
            writeStringToFile(layoutFile, content)

            return textids
        }

        @Throws(IOException::class)
        fun writeStringToFile(file: File, data: kotlin.String?) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs()
            }
            var writer: FileWriter? = null
            try {
                writer = FileWriter(file)
                writer.write(data)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                writer?.close()
            }
        }


        /**
         * 生成Manifest
         */
        @Throws(IOException::class)
        fun generateManifest() {
            val manifestFile: File = File("$libraryPath/AndroidManifest.xml")
            val sb = StringBuilder()
            sb.append("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n")
            sb.append("    <application>\n")
            for (i in activityList.indices) {
                sb.append(
                    "        <activity android:name=\"" + activityList.get(i)!!.replace(
                        "$libraryPath.java.", ""
                    ) + "\"/>\n"
                )
            }
            sb.append("    </application>\n")
            sb.append("</manifest>\n")
            writeStringToFile(manifestFile, sb.toString())
        }


        //生成名字
        fun generateName(): kotlin.String {
            val sb = StringBuilder()
            for (i in 0..4) {
                sb.append(abc[random.nextInt(abc.size)])
            }
            println(sb.toString())
            return sb.toString()
        }

        fun generateBigValue(): kotlin.String {
            val abc1 =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKMNLOPQRSTUVWZYZ0123456789".toCharArray()
            val sb = StringBuilder()
            for (i in 0..<random.nextInt(1000)) {
                sb.append(abc1[random.nextInt(abc1.size)])
            }
            println(sb.toString())
            return sb.toString()
        }


        //    生成strings.xml
        @Throws(IOException::class)
        fun generateStringsFile() {
            val sb = StringBuilder()
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
            sb.append("<resources>\n")
            for (i in stringList.indices) {
                sb.append("    <string name=\"" + stringList.get(i) + "\">" + generateBigValue() + "</string>\n")
            }
            sb.append("</resources>\n")
            val stringFile: File = File(libraryPath + "/res/values/strings.xml")
            writeStringToFile(stringFile, sb.toString())
        }

        //    生成Drawable
        @Throws(IOException::class)
        fun generateDrawable(drawableName: kotlin.String?) {
            var content =
                "<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"\n  " + " android:width" + "=\"" + random.nextInt(
                    100
                ) + "dp\"\n" + " android:height=\"" + random.nextInt(100) + "dp\"\n" + " " + "android:viewportWidth=\"" + random.nextInt(
                    100
                ) + "\"\n" + " android:viewportHeight=\"" + random.nextInt(100) + "\"\n" + ">\n" + "     <path\n" + "  android:fillColor=\"" + generateColor() + "\"\n" + "   android:pathData=\"M"
            val t: Int = random.nextInt(40)
            for (i in 0..<t) {
                if (i != t - 1) {
                    content = content + random.nextInt(100) + ","
                } else {
                    content = content + random.nextInt(100)
                }
            }
            content = content + "z\" />\n" + "</vector>\n" + "\n"

            val drawableFile = File(libraryPath + "/res/drawable/" + drawableName + ".xml")
            writeStringToFile(drawableFile, content)
        }

        //    生成颜色代码
        fun generateColor(): kotlin.String {
            val sb = StringBuilder()
            sb.append("#")
            for (i in 0..5) {
                sb.append(color[random.nextInt(color.size)])
            }
            return sb.toString()
        }
    }
}
