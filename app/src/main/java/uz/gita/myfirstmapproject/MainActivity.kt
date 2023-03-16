package uz.gita.myfirstmapproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoibW11aGFtbWFkMzgzNiIsImEiOiJjbGY0NzE1NzcwODg5M3FxaGZyM244NHg2In0.ITUMOBHcNlSanq4RPYzSbQ")
        setContentView(R.layout.activity_main)
    }
}