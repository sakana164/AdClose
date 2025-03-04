package com.close.hook.ads.data

import android.content.Context
import com.close.hook.ads.data.database.UrlDatabase
import com.close.hook.ads.data.model.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataSource(context: Context) {
    private val urlDao = UrlDatabase.getDatabase(context).urlDao

    fun getUrlList(): Flow<List<Url>> = urlDao.loadAllList()

    fun addUrl(url: Url) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!urlDao.isExist(url.type, url.url)) {
                urlDao.insert(url)
            }
        }
    }

    fun removeList(list: List<Url>) {
        CoroutineScope(Dispatchers.IO).launch {
            urlDao.deleteList(list)
        }
    }

    fun removeUrl(url: Url) {
        CoroutineScope(Dispatchers.IO).launch {
            urlDao.deleteUrl(url)
        }
    }

    fun removeAll() {
        CoroutineScope(Dispatchers.IO).launch {
            urlDao.deleteAll()
        }
    }

    fun addListUrl(list: List<Url>) {
        CoroutineScope(Dispatchers.IO).launch {
            urlDao.insertAll(list)
        }
    }

    fun updateUrl(url: Url) {
        CoroutineScope(Dispatchers.IO).launch {
            urlDao.update(url)
        }
    }

    fun removeUrlString(type: String, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            urlDao.deleteUrlString(type, url)
        }
    }

    fun search(searchText: String, offset: Int, limit: Int): Flow<List<Url>> = urlDao.searchUrls(searchText, offset, limit)

    suspend fun isExist(type: String, url: String): Boolean {
        return withContext(Dispatchers.IO) {
            urlDao.isExist(type, url)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataSource? = null

        fun getDataSource(context: Context): DataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataSource(context).also { INSTANCE = it }
            }
        }
    }
}
