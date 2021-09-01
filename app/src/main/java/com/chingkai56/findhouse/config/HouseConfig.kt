package com.chingkai56.findhouse.config

import com.chingkai56.findhouse.BuildConfig

object HouseKeyWord {
    const val Type = "type"
    const val Kind = "kind" //住家類型
    const val Shape = "shape" //房屋型態
    const val RegionId = "region" //縣市 regionid也可以
    const val SectionId = "section" //行政區 sectionid也可以
    const val Area = "area" //坪
    const val AreaMin = "area min"
    const val AreaMax = "area max"
    const val PatternMore = "patternMore" //幾房 pattern也可以
    const val Rentprice = "rentprice"
    const val PriceMin = "price min"
    const val PriceMax = "price max"
    const val Option = "option"

    const val PriceSelectIndex = "PriceSelectIndex"
    const val TypeSelectIndex = "TypeSelectIndex"

    const val ApplicationName = BuildConfig.APPLICATION_ID
}