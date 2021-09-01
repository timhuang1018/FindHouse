package com.chingkai56.findhouse

import com.chingkai56.findhouse.config.HouseException
import com.chingkai56.findhouse.data.SearchJson
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup

val ROOT_URL = "https://rent.591.com.tw"

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
