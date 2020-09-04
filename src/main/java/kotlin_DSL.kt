fun main(args: Array<String>) {
    //DSL 领域特定语言 比如sql html语言只能用在特定领域

    //kotlin代码dsl化(利用扩展函数且带接受者)  - 让无技术人员简单培训也能懂
    //1.普通类的实现
    val person: Person =
            person {
                name = "张三"
                age = 23
                //address =
                address {
                    city = "湖北"
                }
            }
    person.age = 25;
    println(person)

    //2.html的实现
    val html = Tag("html")
    val head = Tag("head")
    val body = Tag("body")
    val div = Tag("div")
    html.addTag(head)
    html.addTag(body)
    body.addTag(div)
    println(html)
    val htmlstr = html {
        head {

        }
        body {
            div {
                div {

                }
            }
            div {

            }
        }
    }
    println(htmlstr)

    //3. 构建器模式 builder
    data class Person(val name: String?, val age: Int?, val address: Address?)
    class PersonBuilder {
        var name: String? = null;
        var age: Int? = null;
        var address: Address? = null;

        fun build(): Person {
            //Person的属性都为val 只能赋值一次
            return Person(name, age, address)
        }
    }

    fun person(block: PersonBuilder.() -> Unit): Person {
        val person = PersonBuilder()
        person.block()//执行函数
        return person.build()
    }

    fun PersonBuilder.address(block: Address.() -> Unit) {
        val address = Address()
        address.block()
        this.address = address
        //优化
        //this.address = Address().apply(block)
    }

    val person1: Person = person {
        name = "张三"
        age = 23
        name = "李思"
        address {
            city = "上海"
        }
    }
    //person1.age = 1;//error
    println(person1)

    //实现多个adress
    data class Person2(val name: String?, val age: Int?, val addresses: ArrayList<Address>?)

    @tag2
    class PersonBuilder2 {
        var name: String? = null;
        var age: Int? = null;
        var addresses: ArrayList<Address>? = null

        fun build(): Person2 {
            //Person的属性都为val 只能赋值一次
            return Person2(name, age, addresses)
        }
    }

    fun PersonBuilder2.addresses(b: ArrayList<Address>.() -> Unit) {
        val list = ArrayList<Address>()
        list.apply(b)
        addresses = list
    }

    fun ArrayList<Address>.address(b: Address.() -> Unit) {
        add(Address().apply(b))
    }

    fun person2(b: PersonBuilder2.() -> Unit): Person2 {
        return PersonBuilder2().apply(b).build()
    }

    val person2: Person2 = person2 {
        name = "张三"
        age = 28
        addresses {
            name = ""
            address {
                city = "黄冈"
                street = "长胜街"
                number = 22
            }
            address {
                city = "武汉"
                street = ""
                number = 33
                //name = ""
            }
            address {
                city = "武汉"
                number = 34
            }
        }
    }
    println(person2)

    //4.缩小作用域
    @tag2
    class MYLIST : ArrayList<Address>()

    fun PersonBuilder2.addresses(b: MYLIST.() -> Unit) {
        MYLIST().apply(b)
    }

    //5 .dsl作用
    //做cto，架构师等可以封装好后让小弟调用 免得小弟逻辑差不严谨，且减少出错机会
    //例子 Android anko库(已过期)


}

//1
@tag2
data class Address(var city: String? = null, var street: String? = null, var number: Int? = null)

@tag2
data class Person(var name: String? = null, var age: Int? = null, var address: Address? = null)

fun person(block: Person.() -> Unit): Person {
    val person = Person()
    person.block()//执行函数
    return person
    //优化
    //return Person().apply(block)
}

//fun address(block:Address.()->Unit):Address{
//    val address = Address()
//    address.block()
//    return address
//}
fun Person.address(block: Address.() -> Unit) {
    val address = Address()
    address.block()
    this.address = address
    //优化
    //this.address = Address().apply(block)
}

//2
@tag1
open class Tag(var name: String) {
    var list = ArrayList<Tag>()
    fun addTag(tag: Tag) {
        list.add(tag)
    }

    override fun toString(): String {
        var sb = StringBuffer()
        sb.append("<$name>")
        list.forEach { sb.append(it.toString()) }
        sb.append("</$name>")
        return sb.toString()
    }
}

class Html : Tag("html")
class Head : Tag("head")
class Body : Tag("body")
class Div : Tag("div")

fun html(b: Html.() -> Unit): Html {
    val html = Html()
    html.b()
    return html
}

fun Html.head(b: Head.() -> Unit) {
    val head = Head()
    head.b()
    this.addTag(head)
}

fun Html.body(b: Body.() -> Unit) {
    val body = Body()
    body.b()
    this.addTag(body)
}

fun Body.div(b: Div.() -> Unit) {
    val div = Div()
    div.b()
    this.addTag(div)
}

fun Div.div(b: Div.() -> Unit) {
    val div = Div()
    div.b()
    this.addTag(div)
}


@DslMarker //放在需要缩小作用域的类上面
annotation class tag1

@DslMarker //放在需要缩小作用域的类上面
annotation class tag2
