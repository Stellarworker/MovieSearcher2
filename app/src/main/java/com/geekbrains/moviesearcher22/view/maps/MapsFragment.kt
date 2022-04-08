package com.geekbrains.moviesearcher22.view.maps

import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekbrains.moviesearcher22.R
import com.geekbrains.moviesearcher22.common.EMPTY_DOUBLE
import com.geekbrains.moviesearcher22.common.EMPTY_STRING
import com.geekbrains.moviesearcher22.common.ZERO_DOUBLE
import com.geekbrains.moviesearcher22.common.ZERO_INT
import com.geekbrains.moviesearcher22.databinding.FragmentMapsBinding
import com.geekbrains.moviesearcher22.utils.hideKeyboard
import com.geekbrains.moviesearcher22.utils.makeSnackbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

private const val COORDINATES_DELIMITER = ","
private const val INITIAL_MARKER_TITLE = "Marker in Moscow"
private const val INITIAL_LATITUDE = 55.753896
private const val INITIAL_LONGITUDE = 37.620628
private const val MAX_RESULTS = 1
private const val DEFAULT_MAP_ZOOM = 15f
private const val MAX_ARRAY_SIZE = 2
private const val FIRST = 0
private const val SECOND = 1

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        val moscow = LatLng(INITIAL_LATITUDE, INITIAL_LONGITUDE)
        googleMap.addMarker(MarkerOptions().position(moscow).title(INITIAL_MARKER_TITLE))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
        googleMap.setOnMapClickListener { latLng ->
            searchByCoordinates(latLng, binding.mapfMap, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapfMap)
                as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearch()
    }

    private fun initSearch() {
        binding.mapfSearchButton.setOnClickListener { view ->
            view.hideKeyboard()
            val query = binding.mapfSearchString.text.toString().lowercase().trim()
            when {
                isEmpty(query) -> view.makeSnackbar(
                    text = getString(R.string.emptyRequest),
                    anchor = activity?.findViewById(R.id.maBottomNavigation)
                )
                isCoordinates(query) -> searchByCoordinates(
                    convertStringToCoordinates(query),
                    view,
                    true
                )
                else -> searchByAddress(query, view)
            }
        }
    }

    private fun searchByCoordinates(location: LatLng, view: View, moveCamera: Boolean) {
        Thread {
            try {
                val geoCoder = Geocoder(view.context)
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    MAX_RESULTS
                )
                val geoTitle = if (addresses != null && addresses.size > ZERO_INT)
                    addresses[ZERO_INT].getAddressLine(ZERO_INT) else location.toString()
                view.post {
                    goToAddress(location, geoTitle, moveCamera)
                    view.makeSnackbar(
                        text = geoTitle,
                        anchor = activity?.findViewById(R.id.maBottomNavigation)
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun searchByAddress(query: String, view: View) {
        Thread {
            try {
                val geoCoder = Geocoder(view.context)
                val addresses = geoCoder.getFromLocationName(query, MAX_RESULTS)
                when {
                    addresses.size > ZERO_INT -> view.post {
                        goToAddress(
                            LatLng(
                                addresses[ZERO_INT].latitude,
                                addresses[ZERO_INT].longitude
                            ),
                            query
                        )
                    }
                    else -> view.post {
                        view.makeSnackbar(
                            text = getString(R.string.nothingFound),
                            anchor = activity?.findViewById(R.id.maBottomNavigation)
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun isEmpty(query: String) = (query == EMPTY_STRING)

    private fun isCoordinates(query: String): Boolean {
        val strings = query.split(COORDINATES_DELIMITER).toTypedArray()
        return (strings.size == MAX_ARRAY_SIZE)
                && (strings[FIRST].toDoubleOrNull() != null)
                && (strings[SECOND].toDoubleOrNull() != null)
    }

    private fun convertStringToCoordinates(query: String): LatLng {
        val strings = query.split(COORDINATES_DELIMITER).toTypedArray().toMutableList()
        strings.add(EMPTY_STRING)
        return LatLng(
            strings[FIRST].toDoubleOrNull() ?: ZERO_DOUBLE,
            strings[SECOND].toDoubleOrNull() ?: ZERO_DOUBLE
        )
    }

    private fun goToAddress(location: LatLng, query: String, moveCamera: Boolean = true) {
        map.clear()
        setMarker(location, query)
        if (moveCamera) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_MAP_ZOOM))
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int = R.drawable.ic_map_marker
    ) = map.addMarker(
        MarkerOptions()
            .position(location)
            .title(searchText)
            .icon(BitmapDescriptorFactory.fromResource(resourceId))
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAGMENT_TAG = "MAPS_FRAGMENT"

        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}