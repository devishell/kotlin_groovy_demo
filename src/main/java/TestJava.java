import java.util.Optional;

public class TestJava {
    public static void vid(){
        System.out.println("调用java void方法");
    }

    public static String null_test(){
        String str = null;
        return str;
    }

    public static TestJava Instance = new TestJava();

    //Optional
    public int parseAndInc(String number) {
        return Optional.ofNullable(number)
                .map(Integer::parseInt)
                .map(it -> it + 1)
                .orElse(0);
    }

}
