package com.do_f.a1valet.database.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bumptech.glide.load.HttpException
import com.do_f.a1valet.database.entity.Device
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class DeviceRemoteMediator: RemoteMediator<Int, Device>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Device>): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {

                    // Get the latest cursor
                    // if it's the end return end of pagination reached
//                    if (nextPageKey == null) {
//                        return MediatorResult.Success(endOfPaginationReached = true)
//                    }

                    // Do the Api Call
                    // Save it on the DB

                    "" // return the latest key
                }
            }
            return MediatorResult.Success(endOfPaginationReached = true) // fake that we reached the last page
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}