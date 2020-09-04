import java.math.BigInteger
import java.util.*
import kotlin.Exception
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    //Kotlin类测试
    val testKotlin = TestKotlin()
    val d = (Math.random() * 100).toBigDecimal().toInt()
    testKotlin.test(testKotlin, d)

    //调用java库
    val date = Date()
    println("date:$date")

    //java的返回void值 在kotlin里面是kotlin.Unit
    val vid = TestJava.vid()
    println("vid:$vid")
    val vid2 = kotlin.Unit

    //可在里面定义class在之后使用 方法返回不为空时不能省略方法后面 ":类型" （:Any）
    class test {
        fun a(): Any {
            return test_fun()
        }
    }
    println("test:" + test().a())

    //集合测试 lambda
    val names = arrayOf("Amy", "Alex", "Bob", "Cindy", "Jeff", "Jack", "Sunny", "Sara", "Steven")
    println("names:$names  name[0]:${names[0]}") //Array打印不会显示具体数据
    //筛选S开头的人名
    val sName = names.filter { it.startsWith("S") }.toList()
    println("sName:$sName sName[0]:${sName[0]}") //list
    //按首字母分组并排序
    val nameGroup = names.groupBy { it[0] }.toSortedMap(Comparator { key1, key2 -> key1.compareTo(key2) })
    println("nameGroup:$nameGroup nameGroup:${nameGroup['W']}") //Map

    val list = listOf("Saab", "Volvo")//不可变集合
    val map = mapOf("firstName" to "John", "lastName" to "Doe")

    val mutableList = mutableListOf("Saab", "Volvo")//可变集合 和oc有点像 本质也是ArrayList
    mutableList.add("test")
    val arrayList = arrayListOf("Saab", "Volvo")//java的java.util.ArrayList
    arrayList.add("test")
    val mutableSet = mutableSetOf("test", "test");
    println(mutableSet)//set元素不重复 无序
    val treeSet = TreeSet<String>()
    treeSet.add("z");treeSet.add("b");treeSet.add("f");treeSet.add("a");treeSet.add("a")
    println(treeSet)//TreeSet里的元素必须实现comparable接口来排序

    //闭包 返回的内部函数使用了外部函数的变量
    fun te(): () -> Unit {
        var a = 3;
        return {
            println(a)
            a--
        }
    }
    //高阶函数
    var h = fun(a: Int, block: (Int) -> Int): Int {
        return block(a)
    }
    val hresult = h(12) {
        println("it = $it")
        it + 1 //lambda使用的时候只有一个参数可以省略默认为it 定义的时候不能省略 返回值是最后一行
    }
    println("hresult=$hresult")
    /*-------------------------- 自定义注释 --------------------------*/ //live template +group'
    //四大函数 apply let run（三个扩展函数）  with（任意地方都可以调用）
    mutableList.apply {
        println(list[0])
        this.add("123");add("456")
    }.add("789")//返回本身
    mutableList.let {
        it.add("345")
        it
    }.add("345")//返回let参数的返回值
    mutableList.run {
        add("345")
        this
    }.add("345")//返回run参数的返回值
    with(mutableList) {
        add("123")
        "dddd"
    }.length //返回第二个参数的返回值

    //函数回调代替java的接口回调
    test_callback {
        it.sayTo("hehe")
    }


    //null安全
    val nullval: String? = getStringNull();
    println("nullval:$nullval")
    val nullval2: String? = getStringNonnull()
    println("nullval2:$nullval2")

    //处理java的null 可使用safe call会返回null否则会报空指针异常
    val javanull: String? = TestJava.null_test();
    println("TestJava.null_test().length:${javanull?.length}")

    //static member
    TestJava.Instance //java ok
    //TestJava().Instance //error
    TestKotlin.Instance //伴生类 实现类似于java的静态成员
    TestKotlin.Companion.Instance//Companion可省略
    TestKotlin.Instance2
    val i1 = TestKotlin.Companion.static_method("fja")
    println("i1:$i1")
    val i2 = TestKotlin.static_method("3355")
    println("i2:$i2")
    //TestKotlin().Instance//error

    //Kotlin 中没有 java的Optional(Maybe) 的等价物 只能用可空类型代替
    val testJava = TestJava()
    testJava.parseAndInc(null)
    testKotlin.parseAndInc(null)

    //var是一个可变变量 val是一个只读变量，相当于java中的final变量。一个val初始化后不能被改变
    //var -> variables，val -> values
    var var0: String = "var0"
    val val0: String = "val0"
    //val0 = "val1"
    var0 = "var1"
    println("var0 = $var0")

    //数据类 参数为var时才可修改属性
    data class User(var name: String, var age: Int)

    val user = User("Amy", 40)
    user.age = 1;
    println(user)
    //Kotlin 对 equals()、hashCode()、toString() 以及 copy() 有很好的实现。在实现简单的DTO 时它非常有用。
    //但无法扩展数据类或者将其抽象化。

    //Kotlin 类默认为 final。如果你想扩展一个类，必须添加 open 修饰符。
    //class Customer : User() //error
    open class ClassA {
        constructor(a: String)
    }

    class ClassB(a: String) : ClassA(a) {

    }
    //使用all open的依赖： classpath group: 'org.jetbrains.kotlin', name: 'kotlin-allopen', version: "$versions.kotlin"

    //基本类型
    var b = false;
    b = b.not()
    var i = 1;
    i = i.dec()
    var f = 3.14;
    f = f.dec()
    println("$b $i $f")

    //运算符重载有点类似cpp
    val testKotlin1 = TestKotlin() + TestKotlin()
    val testKotlin2 = TestKotlin() - TestKotlin()
    println("lowercase12 ".plus(testKotlin).plus(testJava).capitalize().substring(0))

    //when操作符
    var score = 59.9
    when (score) {
        in 0..60 -> println("$score <= 60")
        else -> println("score = $score")
    }
    fun getWeek(week: Int): String {
        return when (week) {
            1 -> return "星期一"
            2 -> return "星期二"
            else -> "星期$week"
        }
    }
    println(getWeek(23))
    while (score > 0) {
        score -= 30
    }
    do {
        score += 25
    } while (score > 60)

    //loop range
    var result = 0;
    for (num in 1..100 step 1) {
        result += num
    }
    println("result = $result")
    val num1: IntRange = 1 until 100//[1,100)
    var list1 = listOf("1", "teta", "sun", "boy")
    for ((a, b) in list1.withIndex()) {
        println("index=$a value=$b")
    }
    //tag1@for break@tag1可跳出指定tag处for循环

    //函数式声明
    fun add(x: Int, y: Int): Int = x + y
    var add1 = { x: Int, y: Int -> x + y }
    add1 = { s1: Int, s2: Int -> s1.plus(s2) }
    var add2: (Int, Int) -> Int = { x, y -> x + y }

    //默认参数和具名参数
    fun 获取圆周长(PI: Float = 3.1415926f, 直径: Float): Float {//默认参数
        return PI * 直径
    }
    println(获取圆周长(直径 = 3f))//具名参数

    //递归
    fun fact(num: BigInteger): BigInteger {
        if (num == BigInteger.ONE) {
            return BigInteger.ONE
        } else {
            return num * fact(num - BigInteger.ONE)
        }
    }

    val bi = BigInteger("100")
    println("fact(bi)=${fact(bi)}")//数太大的话不能使用Int接收
    //尾递归优化
    fun oldAdd(num: Int): Int {
        //println("num = $num")
        if (num == 1) {
            return 1
        } else {
            return oldAdd(num - 1) + num
        }
    }
    println("oldAdd(100) = ${oldAdd(100)}")//println("${oldAdd(100000)}")//satck溢出
    tailrec fun oldAdd2(num: Int, res: Int): Int { //tailrec：尾递归优化 java没有这种机制
        //println("num=$num, res=$res")
        return if (num == 0) {
            res
        } else {
            oldAdd2(num - 1, res + num)//尾递归要求：函数返回值还是本身
        }
    }
    println("oldAdd2(10000,0) = ${oldAdd2(10000, 0)}")

    //面向对象
    class Person(var age: Int, var sex: String, var voice: String) {
        fun smile() {
            println("妹子笑了一下")
        }

        fun cry() {
            println("妹子哭了一下")
        }
    }

    val person = Person(12, "女", "甜美")
    println("${person.age}");person.smile();person.cry();

    //抽象类 接口
    abstract class Human(var name: String) {
        abstract fun laugh()
        open fun talk() {}
    }

    class Man(name: String) : Human(name), IWalk {
        constructor() : this("")

        override fun laugh() {
            println("${name}哈哈大笑")
        }

        override fun talk() {
            println(this.name + " can talk")
        }

        override fun walk() {
            println("walk")
        }
    }

    //接口代理/委托 by  、属性委托
    class Things() : IWalk by Man() {
        //IWalk的实现用的Man的
        fun fly() {}
    }

    class Things1(val iWalk: IWalk) : IWalk by iWalk {
        override fun walk() {
            println("呼叫iWalk")
            iWalk.walk()
        }
    }

    //单例 object
    class Things2(name: String) : IWalk by Man2 {
//        override fun walk() {
//            Man2.walk()
//        }
    }

    val thingsObj1: IWalk = Things()
    //thingsObj1.fly()//error
    if (thingsObj1 is Things) thingsObj1.fly() //kotlin不需要强转

    //枚举 Enum name,ordinal
    println("" + Color.BLUE.ordinal + " " + Color.RED.r)

    //印章类 sealed Class 只能有指定个数的子类类型
    //val sealedClass = SealedClass()//error
    val aS = SealedClass.a("a")
    val bS = SealedClass.b()
    val cS = SealedClass.c

    //中缀表达式 infix
    testKotlin.sayTo("java")
    testKotlin sayTo "kotlin"//限制：成员函数或扩展函数，只能有一个参数，不能为默认参数和可变参数

    //懒加载 延迟初始化
    val lazy1: String by lazy { "1" }
    lateinit var lateinit1: String //直接用会报错

    //扩展类的函数 即是扩展函数
    fun String?.myIsEmpty(): Boolean {
        return this == null || this.length == 0
    }
    println("123".myIsEmpty())

    //人机交互和异常处理
    println("请输入数字：")
    var str1 = readLine() //键盘输入的都是字符串
    try {
        println("input is ${str1?.toInt()}")
    } catch (e: Exception) {
        println("出错了：${e.message}")
    }

}

interface IWalk {
    fun walk()
    fun test() {

    }
}

//单例类没有构造方法
object Man2 : IWalk {
    override fun walk() {
        println("单例类的walk方法被调用了")
    }

}

enum class Color(var r: Int, var g: Int, var b: Int) {
    RED(255, 0, 0), GREEN(0, 255, 0), BLUE(0, 0, 255)
}

sealed class SealedClass {
    fun hello() {
        println("hello")
    }

    class a(var name: String) : SealedClass()
    class b : SealedClass()
    object c : SealedClass()
}

//类似java的单例
class Utils private constructor() {
    var age = 20 //非静态

    companion object {
        var name = "amy" //静态
        val Instance by lazy { Utils() }//惰性加载 只会加载一次 线程安全
    }
}

fun getStringNull(): String? {
    return null
}

fun getStringNonnull(): String? {
    return "nonnull"
}

fun test_fun(): Any {
    return "hello"
}

fun test_callback(block: ((TestKotlin) -> Unit)?) {
    thread {
        Thread.sleep(3000)
        block?.invoke(TestKotlin()) //if (block!=null)block(TestKotlin()) //java方式
    }.start()
}

