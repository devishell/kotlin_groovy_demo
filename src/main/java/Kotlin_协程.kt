import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main(args: Array<String>) = coroutineScope {
    println("main start")
    //launch协程有点像开启了个线程和main并行执行
    //launch函数的前三个参数都有默认值 最后是lambda表达式 返回Job就是协程的任务
    //第一个参数是上下文context即是CommonPoll,由ForkJoinPoll线程池实现
    launch {
        for (i in 1..5) {
            println("launch $i "+Thread.currentThread().name)
            delay(500)
        }
    }
    //.join() //协程join到主线程
    var job = launch {
        for (i in 1..5) {
            println("launch2 $i "+Thread.currentThread().name)
            delay(500)
        }
    }

    //ForkJoinPoll原理
    var service = ForkJoinPool(3)
    service.execute(Runnable{//ForkJoinPoll里面是守护线程 主线程结束它也结束了
        for (i in 1..5) {
            println("ForkJoinPool "+i+ " "+Thread.currentThread().name)
            Thread.sleep(500)
        }
    })
    service.execute(Runnable{//ForkJoinPoll里面是守护线程 主线程结束它也结束了
        for (i in 1..5) {
            println("ForkJoinPool2 "+i+ " "+Thread.currentThread().name)
            Thread.sleep(500)
        }
    })
    //协程为了解决这个问题可以把1.主线程sleep等待 2.线程join到主线程 主线程就会等待守护线程结束才结束[错误]
    //[新版本和以前实验版本表现不同]
    //launch 1 DefaultDispatcher-worker-1
    //launch2 1 DefaultDispatcher-worker-2
    //launch2 3 DefaultDispatcher-worker-1
    //launch 3 DefaultDispatcher-worker-2
    //协程先把耗时任务挂起(必须用delay不能用sleep),执行的线程可能改变 而线程池执行的代码块的线程不会改变
    //delay:挂起当前线程,但不一定会挂起,且必须在suspend函数或协程中使用 sleep:当前线程休眠阻塞
    //协程优点:1.效率比线程高
    //2.可取消
    val job1 = launch {
        //定时取消
        withTimeout(3000){
            delay(1000)
            println("withTimeout 1000")
            delay(2100)
            println("withTimeout 3100")
        }
        //解决sleep 取消失效的问题
        if (!job.isActive)return@launch
    }
    job.cancel()//直接取消只能取消挂起的,不能取消阻塞的 但是可以通过判断job.isActive

    //async启动
    val deferred1 = async {
        suspend fun test(): String {
            delay(3500)
            println("async1")
            return "async1"
        }
        test()
    }
//    val deferred1Value = deferred1.await();//可以获取返回值但会阻塞主线程
//    println("deferred1Value = $deferred1Value")

    //自行指定上下文
    launch (coroutineContext,CoroutineStart.DEFAULT){
        //coroutineContext 会运行在主线程
        println("coroutineContext "+ Thread.currentThread().name)

        //CoroutineStart 执行开始方式
    }
    launch (newFixedThreadPoolContext(2,"fixedpoll")){
        println("fixedpoll "+ Thread.currentThread().name)
    }

    println("main end")
}