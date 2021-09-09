package com.chingkai56.findhouse.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.chingkai56.findhouse.Database
import com.chingkai56.findhouse.data.RentHouse
import com.chingkai56.findhouse.data.domain.HouseUI
import com.chingkai56.findhouse.data.domain.OptionStorage
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import timber.log.Timber

/**
 * Created by timhuang on 2020/10/13.
 **/

class LocalDataSource(db:Database) {
    private val query = db.houseQueries

    private val seals = getSealedHousesFlow()
    private var sealedList :List<HouseUI> = listOf()
    private val scope = CoroutineScope(SupervisorJob())

    init {
        scope.launch(Dispatchers.IO){
            seals.collect {
                sealedList = it
            }
        }
    }

    /**
     * @return true means has at least one insert
     */
    fun insertItems(list:List<RentHouse>):Boolean{
        var hasNew = false
        query.transaction {
            list.forEach {

                if (query.findHouse(it.houseId).executeAsOneOrNull()!=null){
                    updateHouse(it)
                }else{
                    insertHouse(it)
                    hasNew = true
                }
                try {
                    if (!sealedList.any { seal-> seal.id == it.houseId } &&
                        sealedList.find { seal-> seal.allFloor == it.allFloor
                                && seal.floor == it.floor
                                && seal.area.compareTo(it.area)==0
                                && seal.price == priceLong(it.price)
                                && seal.fullAddress.startsWith(it.fullAddress.run { substring(0,indexOfFirst { it.isDigit() }) })}!=null){
                        sealOrNot(it.houseId,true)
                    }
                }catch (e:IndexOutOfBoundsException){
                    Timber.e("house:$it")
                }

            }
        }
        return hasNew
    }

    private fun insertHouse(house: RentHouse) {
        house.apply {
            if (shape!=2 && floor>2) return
            query.insertHouse(
                    houseId = houseId, userId = userId,
                    type = type,kind = kind,postId = postId,
                    regionId = regionId,regionName = regionName,
                    sectionId = sectionId,sectionName = sectionName,
                    streetId = streetId,streetName = streetName,
                    alleyName = alleyName,caseId = caseId,
                    caseName = caseName,layout = layout,area = area,
                    room = room,floor = floor,allFloor = allFloor,
                    updateTime = updateTime,condition = condition,cover = cover,
                    refreshTime = refreshTime,closed = closed,kindName = kindName,
                    iconClass = iconClass,fullAddress = fullAddress,shape = shape,
                    title = title,price = priceLong(price),createDate = Instant.now().epochSecond
            )
        }
    }

    private fun updateHouse(house: RentHouse) {
        house.apply {
            query.updateHouse(
                    houseId = houseId, userId = userId,
                    type = type,kind = kind,postId = postId,
                    regionId = regionId,regionName = regionName,
                    sectionId = sectionId,sectionName = sectionName,
                    streetId = streetId,streetName = streetName,
                    alleyName = alleyName,caseId = caseId,
                    caseName = caseName,layout = layout,area = area,
                    room = room,floor = floor,allFloor = allFloor,
                    updateTime = updateTime,condition = condition,cover = cover,
                    refreshTime = refreshTime,closed = closed,kindName = kindName,
                    iconClass = iconClass,fullAddress = fullAddress,shape = shape,
                    title = title,price = priceLong(price)
            )
        }
    }

    fun priceLong(price:String):Long{
        return try {
            val price = price.replace(",","")
            if (price.contains("~")){
                price.substring(price.indexOf("~")+1).toLong()
            }else{
                price.toLong()
            }
        }catch (e:NumberFormatException){
            Timber.e(e)
            //negative means fail
            -1
        }
    }

    fun findHouse(id:Int): HouseUI? {
        return dbHouseMapper(id).executeAsOneOrNull()
    }

    fun dbHouseMapper(id:Int): Query<HouseUI> {
        return query.findHouse(houseId = id,mapper = { houseId, userId, type, kind, postId, regionId, regionName, sectionName, sectionId, streetId, streetName, alleyName, caseName, caseId, layout, area, room, floor, allFloor, updateTime, condition, cover, refreshTime, closed, kindName, iconClass, fullAddress, shape, createDate, isSealed,sealDate,title,price->
            HouseUI(
                 id = houseId, userId = userId,
                    type = type,kind = kind,postId = postId,
                    regionId = regionId,regionName = regionName,
                    sectionId = sectionId,sectionName = sectionName,
                    streetId = streetId,streetName = streetName,
                    alleyName = alleyName,caseId = caseId,
                    caseName = caseName,layout = layout,area = area,
                    room = room,floor = floor,allFloor = allFloor,
                    updateTime = updateTime,condition = condition,cover = cover,
                    refreshTime = refreshTime,closed = closed,kindName = kindName,
                    iconClass = iconClass,fullAddress = fullAddress,shape = shape,
                    title = title,price = price,isSealed = isSealed
            )
        })
    }

    fun getHouses(option:OptionStorage): LiveData<List<HouseUI>> {
        Timber.e("get local data by option:$option")
        return query.getHousesByOptions(regionId = option.regionId,priceMin = option.priceMin.toLong(),priceMax = option.priceMax.toLong())
        { houseId, userId, type, kind, postId, regionId, regionName, sectionName, sectionId, streetId, streetName, alleyName, caseName, caseId, layout, area, room, floor, allFloor, updateTime, condition, cover, refreshTime, closed, kindName, iconClass, fullAddress, shape, createDate, isSealed, sealDate, title, price ->
            HouseUI(
                    id = houseId, userId = userId,
                    type = type, kind = kind, postId = postId,
                    regionId = regionId, regionName = regionName,
                    sectionId = sectionId, sectionName = sectionName,
                    streetId = streetId, streetName = streetName,
                    alleyName = alleyName, caseId = caseId,
                    caseName = caseName, layout = layout, area = area,
                    room = room, floor = floor, allFloor = allFloor,
                    updateTime = updateTime, condition = condition, cover = cover,
                    refreshTime = refreshTime, closed = closed, kindName = kindName,
                    iconClass = iconClass, fullAddress = fullAddress, shape = shape,
                    title = title, price = price, isSealed = isSealed
            )
        }.asFlow().mapToList().asLiveData()
    }

    fun getSealedHouses():LiveData<List<HouseUI>>{
        return getSealedHousesFlow().asLiveData()
    }

    fun getSealedHousesFlow(): Flow<List<HouseUI>> {
        return query.getSealed { houseId, userId, type, kind, postId, regionId, regionName, sectionName, sectionId, streetId, streetName, alleyName, caseName, caseId, layout, area, room, floor, allFloor, updateTime, condition, cover, refreshTime, closed, kindName, iconClass, fullAddress, shape, createDate, isSealed, sealDate, title, price ->
            HouseUI(
                id = houseId, userId = userId,
                type = type,kind = kind,postId = postId,
                regionId = regionId,regionName = regionName,
                sectionId = sectionId,sectionName = sectionName,
                streetId = streetId,streetName = streetName,
                alleyName = alleyName,caseId = caseId,
                caseName = caseName,layout = layout,area = area,
                room = room,floor = floor,allFloor = allFloor,
                updateTime = updateTime,condition = condition,cover = cover,
                refreshTime = refreshTime,closed = closed,kindName = kindName,
                iconClass = iconClass,fullAddress = fullAddress,shape = shape,
                title = title,price = price,isSealed = isSealed
            )
        }.asFlow().mapToList()
    }

    fun sealOrNot(id: Int,toSeal:Boolean) {
        query.updateSealed(
                houseId = id,
                isSealed = toSeal,
                sealDate = if (toSeal) Instant.now().epochSecond else null
        )
    }

}