package uz.gita.myfirstmapproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [LocationData::class], version = 1)
abstract class MapDataBase: RoomDatabase() {
    abstract fun dao(): MapDao
}