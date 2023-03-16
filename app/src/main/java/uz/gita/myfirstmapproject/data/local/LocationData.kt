package uz.gita.myfirstmapproject.data.local

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude : Double,
    val longitude : Double
)