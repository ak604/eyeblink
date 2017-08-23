package eyeblink.com.eyeblink


fun operation( op : ()-> Unit){
    println("before")
    op()
    println("after")

}

class Response(var status : String, var desc:String)

