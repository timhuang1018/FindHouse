package com.chingkai56.findhouse

import android.util.Log
import com.chingkai56.findhouse.config.HouseException
import com.chingkai56.findhouse.data.RentHouse
import com.chingkai56.findhouse.data.SearchJson
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.jsoup.Jsoup

val httpClient = HttpClient(OkHttp){
    install(JsonFeature){
        serializer = KotlinxSerializer()
//        accept(ContentType.Application.HalJson)
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Ktor", message)
            }
        }
        level = LogLevel.ALL
    }

    defaultRequest {
        header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
        header("accept-encoding","gzip, deflate, br")
        header("accept-language","en-US,en;q=0.9")
        header("connection","keep-alive")
        header("dnt","1")
        header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
    }
}

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = false
}

val ROOT_URL = "https://rent.591.com.tw"

suspend fun getHouses(token:String?=null): List<RentHouse> {
    return httpClient.get<List<RentHouse>>("$ROOT_URL/home/search/rsList"){
        token?.let { header("X-CSRF-TOKEN",it) }
        parameter("is_new_list","1")
        parameter("type","1")
        parameter("kind","0")
        parameter("shape","2")
        parameter("searchtype","1")
        parameter("regionid",1)
        parameter("area","13,40")
        parameter("patternMore","1,2")
        parameter("rentprice","14000,25000")
        parameter("option","cold")
        parameter("hasimg","1")
        parameter("not_cover", "1")
        parameter("firstRow", 0)
    }
}

suspend fun fetchData(params:Map<String,String>,firstRow:Int) = withContext(Dispatchers.IO){
    val doc2 = Jsoup.connect(ROOT_URL).run {
        this.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
        execute()
    }
    val elements = doc2.parse().getElementsByTag("meta")
    elements.forEach {
        val has = it.attr("name")=="csrf-token"
//        Log.e("csrf","element:$it,has csrf token:$has")
        if(has){
            val result = Jsoup.connect("$ROOT_URL/home/search/rsList")
                    .ignoreContentType(true)
                    .run {
                        this.cookies(doc2.cookies())
                        header("X-CSRF-TOKEN",it.attr("content"))
                        params.toList().forEach { param->
                            //key, value
                            data(param.first,param.second)
                        }
//                        data("is_new_list","1")
//                        data("type","1")
//                        data("kind","1")
////                        data("shape","2")
//                        data("searchtype","1")
//                        data("regionid","1")
//                        data("area","13,40")
//                        data("patternMore","1,2")
//                        data("rentprice","14000,27000")
//                        data("option","cold")
//                        data("hasimg","1")
//                        data("not_cover", "1")
                        data("firstRow", firstRow.toString())
                        execute()
                    }
            val serial = Json{
                isLenient = true
                ignoreUnknownKeys = true
            }.decodeFromString<SearchJson>(result.body())
//            Log.e("serial","serial size:${serial.data.data.size}")
            return@withContext serial
        }
    }
    throw HouseException("no data")
}
