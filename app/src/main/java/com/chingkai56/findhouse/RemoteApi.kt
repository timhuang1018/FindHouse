package com.chingkai56.findhouse

import android.util.Log
import com.chingkai56.findhouse.data.RentHouse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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

suspend fun getHouses(): RentHouse {
    return httpClient.get<RentHouse>("$ROOT_URL/home/search/rsList"){
        parameter("is_new_list","1")
        parameter("type","1")
        parameter("kind","0")
        parameter("shape","2")
        parameter("searchtype","1")
        parameter("regionid",1)
        parameter("area","13,40")
        parameter("patternMore","1,2")
        parameter("rentprice","14000,25000")
        parameter("patternMore","2")
        parameter("option","cold")
        parameter("hasimg","1")
        parameter("not_cover", "1")
        parameter("firstRow", 0)
    }
}

fun test(){
    GlobalScope.launch {
        try {
            val result = getHouses()
            Log.e("data","$result")
        }catch (e:Exception){
            Log.e("error","$e")
        }

    }
}
