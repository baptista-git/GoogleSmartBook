package com.tech.baptista.bookstore.screens.detail

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tech.baptista.bookstore.databinding.FragmentDetailBinding

/**
 * This [Fragment] shows the detailed information about a selected piece of Mars real estate.
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val book = DetailFragmentArgs.fromBundle(requireArguments()).selectedBook
        val viewModelFactory = DetailViewModel.Factory(book, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        if(!book.buyLink.isNullOrEmpty())
            binding.buyButton.setOnClickListener{ openOnlineStore(book.buyLink)}
        else
            binding.buyButton.visibility = View.GONE

        return binding.root
    }

    fun openOnlineStore(url: String){
        Log.d("BUY", "store: ${url}")
        val packageManager = context?.packageManager ?: return
        // Try to generate a direct intent to the YouTube app
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if(intent.resolveActivity(packageManager) == null) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "No browser found",
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()
        }
        startActivity(intent)
    }

}