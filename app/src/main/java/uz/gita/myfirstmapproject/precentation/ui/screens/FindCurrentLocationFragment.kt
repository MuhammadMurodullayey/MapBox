package uz.gita.myfirstmapproject.precentation.ui.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myfirstmapproject.R
import uz.gita.myfirstmapproject.databinding.FragmetFindCurrentLocationBinding
import uz.gita.myfirstmapproject.precentation.viewModels.FindCurrentLocationViewModel
import uz.gita.myfirstmapproject.precentation.viewModels.impl.FindCurrentLocationViewModelImpl

@AndroidEntryPoint
class FindCurrentLocationFragment: Fragment(R.layout.fragmet_find_current_location) , PermissionsListener, LocationEngineListener,
    OnMapReadyCallback {
    private val binding by viewBinding(FragmetFindCurrentLocationBinding::bind)
    private val viewModel: FindCurrentLocationViewModel by viewModels<FindCurrentLocationViewModelImpl>()
    private val REQUEST_CHECK_SETTINGS = 1
    var settingsClient: SettingsClient? = null

    private lateinit var map: MapboxMap
    lateinit var permissionManager: PermissionsManager
    var originLocation: Location? = null

    var locationEngine: LocationEngine? = null
    var locationComponent: LocationComponent? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapbox.onCreate(savedInstanceState)
        binding.mapbox.getMapAsync(this)
        settingsClient = LocationServices.getSettingsClient(requireActivity())


        binding.buttonFree.setOnClickListener {

        }
        binding.findMe.setOnClickListener {
            locationEngine?.requestLocationUpdates()
            locationEngine?.lastLocation?.let { it1 -> setCameraPosition(it1) }

        }
    }

    private fun enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            initializeLocationComponent()
            initializeLocationEngine()
        } else {
            permissionManager = PermissionsManager(this)
            permissionManager.requestLocationPermissions(requireActivity())
        }

    }
    //2
    @SuppressWarnings("MissingPermission")
    fun initializeLocationEngine() {
        locationEngine = LocationEngineProvider(requireContext()).obtainBestLocationEngineAvailable()
        locationEngine?.priority = LocationEnginePriority.BALANCED_POWER_ACCURACY
        locationEngine?.activate()
        locationEngine?.addLocationEngineListener(this)

        val lastLocation = locationEngine?.lastLocation
        if (lastLocation != null) {
            originLocation = lastLocation
            setCameraPosition(lastLocation)
        } else {
            locationEngine?.addLocationEngineListener(this)
        }


    }

    @SuppressWarnings("MissingPermission")
    fun initializeLocationComponent() {
        locationComponent = map.locationComponent
        locationComponent?.activateLocationComponent(requireContext())
        locationComponent?.isLocationComponentEnabled = true
        locationComponent?.cameraMode = CameraMode.NONE
        val options: LocationComponentOptions = LocationComponentOptions.builder(context)
            .foregroundDrawable(R.drawable.taxi)
            .build()
        locationComponent!!.applyStyle(options)


    }


    private fun setCameraPosition(location: Location) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude,
            location.longitude),  30.0))
        Log.d("ttt","$location")
          viewModel.setLocation(location)



    }

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            locationEngine?.requestLocationUpdates()
            locationComponent?.onStart()
        }

        binding.mapbox.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapbox.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapbox.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapbox.onStop()
        locationEngine?.removeLocationUpdates()
        locationComponent?.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapbox.onDestroy()
        locationEngine?.deactivate()

    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapbox.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapbox.onSaveInstanceState(outState)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(requireContext(), "This app needs location permission to be able to show your location on the map", Toast.LENGTH_LONG).show()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocation()
            Log.d("onPermissionResult","ttt")
        } else {
            Toast.makeText(requireContext(), "User location was not granted", Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
        }

    }

    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
        locationEngine?.lastLocation?.let { setCameraPosition(it) }
    }

    override fun onLocationChanged(location: Location?) {
       /* location?.run {
            originLocation = this
            setCameraPosition(this)
        }*/

    }

    override fun onMapReady(mapboxMap: MapboxMap?) {

        map = mapboxMap ?: return

        val locationRequestBuilder = LocationSettingsRequest.Builder().addLocationRequest(
            LocationRequest()
            .setPriority(LocationRequest.PRIORITY_NO_POWER))

        val locationRequest = locationRequestBuilder?.build()

        settingsClient?.checkLocationSettings(locationRequest)?.run {
            addOnSuccessListener {
                enableLocation()
                Log.d("onMapReady","ttt")
            }

            addOnFailureListener {
                val statusCode = (it as ApiException).statusCode

                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    val resolvableException = it as? ResolvableApiException
                    resolvableException?.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                enableLocation()
                Log.d("onActivityResult","ttt")
            } else if (resultCode == Activity.RESULT_CANCELED) {
                activity?.onBackPressed()
            }
        }
    }



}