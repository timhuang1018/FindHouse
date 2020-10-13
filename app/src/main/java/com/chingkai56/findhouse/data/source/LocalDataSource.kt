package com.chingkai56.findhouse.data.source

import com.chingkai56.findhouse.Database
import com.chingkai56.findhouse.data.RentHouse

/**
 * Created by timhuang on 2020/10/13.
 **/

class LocalDataSource(db:Database) {
    private val query = db.houseQueries

    fun insertItems(list:List<RentHouse>){

    }
}