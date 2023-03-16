package uz.gita.myfirstmapproject.domein.repository.impl

import android.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.myfirstmapproject.data.local.LocationData
import uz.gita.myfirstmapproject.data.local.MapDataBase
import uz.gita.myfirstmapproject.domein.repository.MapRepo
import javax.inject.Inject

class MapRepoImpl @Inject constructor(
    private val dataBase: MapDataBase
) : MapRepo {
    override fun setLocation(location: Location) = flow<Unit> {
        dataBase.dao().insertLocation(LocationData(1, location.latitude,location.longitude))
    }.flowOn(Dispatchers.IO)


}