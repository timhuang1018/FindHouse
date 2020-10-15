package com.chingkai56.findhouse.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.chingkai56.findhouse.Database
import com.chingkai56.findhouse.data.RentHouse
import com.chingkai56.findhouse.data.domain.HouseUI
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import org.threeten.bp.Instant

/**
 * Created by timhuang on 2020/10/13.
 **/

class LocalDataSource(db:Database) {
    private val query = db.houseQueries

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
                    title = title,price = price,createDate = Instant.now().epochSecond
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
                    title = title,price = price
            )
        }
    }

    fun findHouse(id:Int): HouseUI? {
        return dbHouseMapper(id).executeAsOneOrNull()
    }

    fun dbHouseMapper(id:Int): Query<HouseUI> {
        return query.findHouse(houseId = id,mapper = { houseId, userId, type, kind, postId, regionId, regionName, sectionName, sectionId, streetId, streetName, alleyName, caseName, caseId, layout, area, room, floor, allFloor, updateTime, condition, cover, refreshTime, closed, kindName, iconClass, fullAddress, shape, createDate, isSealed,title,price->
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
                    title = title,price = price
            )
        })
    }

    fun getHouses(): LiveData<List<HouseUI>> {
        return query.getHouses { houseId, userId, type, kind, postId, regionId, regionName, sectionName, sectionId, streetId, streetName, alleyName, caseName, caseId, layout, area, room, floor, allFloor, updateTime, condition, cover, refreshTime, closed, kindName, iconClass, fullAddress, shape, createDate, isSealed, title, price ->
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
                    title = title,price = price
            )
        }.asFlow().mapToList().asLiveData()
    }

    fun sealHouse(id: Int) {
        query.updateSealed(true,id)
    }

}