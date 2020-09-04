class TestGroovy {
    public static void main(String[] args) {
        println "hello,groovy!"

        def test = new TestGroovy()
        println test.a("张三")
    }

    def a(String s){
        "hello,"+s+"!"
    }

}
