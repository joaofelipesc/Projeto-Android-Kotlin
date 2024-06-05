package com.joao.projetofinal

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.joao.projetofinal.databinding.FragmentMapsBinding


class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(OnMapReadyCallback { googleMap ->
            map = googleMap


            requestLocationPermission()

            map.addMarker(
                MarkerOptions().position(LatLng(-22.905833, -47.060833)).title("Cotuca.")
            )
        })

        setHasOptionsMenu(true);

        binding.toolbar.setOnMenuItemClickListener { menu_item ->
            when (menu_item.itemId) {
                R.id.deslogar -> {
                    auth.signOut()
                    findNavController().navigate(R.id.action_mapsFragment_to_loginFragment2)
                    true
                }
                R.id.disciplinas -> {
                    findNavController().navigate(R.id.action_mapsFragment_to_disciplinasFragment)
                    Toast.makeText(requireContext(), "Disciplinas", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLastKnownLocation()
            } else {
                Toast.makeText(requireContext(), "É necessário permitir a localização aí", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getLastKnownLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(requireContext(), "A permissão de localização é necessária para exibir sua localização no mapa.", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                        map.addMarker(
                            MarkerOptions()
                                .position(currentLatLng)
                                .title("Sua Localização")
                        )

                    } else {
                        // A localização é nula, exibe uma mensagem para o usuário
                        Toast.makeText(
                            requireContext(),
                            "Não foi possível obter sua localização.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}