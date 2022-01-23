package com.example.videowithfirebase.ui.gallery

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.videowithfirebase.R
import com.example.videowithfirebase.UpdateProfile
import com.example.videowithfirebase.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val name = binding.nameFrag
        val username = binding.usernameFrag
        val phone = binding.phoneFrag
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseData = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseAuth.currentUser!!.uid)

        username.text = "ok"

        //for showing profile info
        firebaseData.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                // this if condition is not executing, i.e., snapshot doesn't exists.
                if (snapshot.exists()) {
                    name.text = "sgdjcs"
                    name.text = snapshot.child("Name").value as String
                    username.text = snapshot.child("Username").value as String
                    phone.text = snapshot.child("Phone Number").value as String
                } else {
                    phone.text="sckjsbc"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        val updateText = binding.tvUpdateProfile
        updateText.setOnClickListener {
            startActivity(Intent(activity, UpdateProfile::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}