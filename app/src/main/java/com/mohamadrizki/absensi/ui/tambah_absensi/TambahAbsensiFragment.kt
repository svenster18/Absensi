package com.mohamadrizki.absensi.ui.tambah_absensi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.mohamadrizki.absensi.App
import com.mohamadrizki.absensi.R
import com.mohamadrizki.absensi.UserPreference
import com.mohamadrizki.absensi.databinding.FragmentTambahAbsensiBinding
import com.mohamadrizki.absensi.ui.lihat_absensi.DetailAbsensiActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class TambahAbsensiFragment : Fragment(), OnMapReadyCallback {

    private val userPreference = UserPreference(App.applicationContext())

    private var _binding: FragmentTambahAbsensiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var tambahAbsensiViewModel: TambahAbsensiViewModel

    private lateinit var currentPhotoPath: String
    private var locationPermissionGranted = false
    private var map: GoogleMap? = null

    private var photoFile: File? = null

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var lastKnownLocation: Location? = null

    private var lastLocation: LatLng? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahAbsensiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tambahAbsensiViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TambahAbsensiViewModel::class.java)

        if (tambahAbsensiViewModel.jam in 16..22) {
            binding.btnKirim.text = "Absen Keluar"
        }

        else if (tambahAbsensiViewModel.jam in 7..16){
            binding.btnKirim.text = "Absen Masuk"
        }

        lastLocation = tambahAbsensiViewModel.getLatLng()

        currentPhotoPath = tambahAbsensiViewModel.getCurrentPhotoPath()

        //if (!currentPhotoPath.equals("")) {
        //    setPic()
        //}
        photoFile = tambahAbsensiViewModel.getPhoto()


        // Construct a PlacesClient
        Places.initialize(App.applicationContext(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(App.applicationContext())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.applicationContext())

        binding.btnAmbil.setOnClickListener {
            when (PackageManager.PERMISSION_DENIED) {
                ContextCompat.checkSelfPermission(App.applicationContext(), Manifest.permission.CAMERA)
                -> ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA),
                    REQUEST_IMAGE_CAPTURE
                )
                else -> {
                    dispatchTakePictureIntent()
                }
            }

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

            mapFragment?.getMapAsync(this)

            binding.btnKirim.isEnabled = true
        }

        tambahAbsensiViewModel.toastString.observe(requireActivity()) { toastString ->
            Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show()
        }

        binding.btnKirim.setOnClickListener {
            if (tambahAbsensiViewModel.jam in 17..21) {
                tambahAbsensiViewModel.absenKeluar()
            }
            else if (tambahAbsensiViewModel.jam in 6..16) {
                tambahAbsensiViewModel.absen()
            }
            tambahAbsensiViewModel.absensi.observe(requireActivity()) { absensi ->
                val intent = Intent(context, DetailAbsensiActivity::class.java)
                intent.putExtra(DetailAbsensiActivity.EXTRA_ABSENSI, absensi)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            photoFile = tambahAbsensiViewModel.getPhoto()
            setPic()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }

        updateLocationUI()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${activity?.packageName}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    context?.grantUriPermission(activity?.packageName, photoURI, flags)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
                tambahAbsensiViewModel.setPhoto(photoFile!!)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val user = userPreference.getUser()
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val name = user.displayName?.replace(" ", "")?.lowercase()
        return File.createTempFile(
            "${name}_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            tambahAbsensiViewModel.setCurrentPhotoPath(currentPhotoPath)
        }
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = binding.photocapture.width
        val targetH: Int = binding.photocapture.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = (photoW / targetW).coerceAtMost(photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            binding.photocapture.setImageBitmap(rotateBitmap(bitmap, -90f))
        }
    }

    private fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(App.applicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        }
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            tambahAbsensiViewModel.setLatLng(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude))
                            lastLocation = tambahAbsensiViewModel.getLatLng()
                            map?.addMarker(
                                MarkerOptions().position(lastLocation!!).title("Lokasi Pegawai")
                            )
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                lastLocation!!, DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE=1
        const val LOCATION_REQUEST_CODE=2
        private const val DEFAULT_ZOOM = 15
        private const val TAG = "MainActivity"
    }
}