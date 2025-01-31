package edu.bluejack23_1.nowlocate.views.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import edu.bluejack23_1.nowlocate.R
import edu.bluejack23_1.nowlocate.builder.FragmentBuilder
import edu.bluejack23_1.nowlocate.databinding.ActivityProfileBinding
import edu.bluejack23_1.nowlocate.handlers.BottomNavigationViewHandler
import edu.bluejack23_1.nowlocate.helpers.IntentHelper
import edu.bluejack23_1.nowlocate.interfaces.View
import edu.bluejack23_1.nowlocate.models.User
import edu.bluejack23_1.nowlocate.viewModels.ProfileViewModel
import edu.bluejack23_1.nowlocate.views.fragments.ProfileFragment

class ProfileActivity : AppCompatActivity(), View {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var editProfileBtn: ImageButton
    private lateinit var logoutBtn: ImageButton
    private lateinit var addReportFAB: FloatingActionButton
    private lateinit var emailTV: TextView
    private lateinit var usernameTV: TextView
    private lateinit var profileImageCIV: CircleImageView
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingHandler()
        elementHandler()
        eventHandler()

        val userExtra = intent.getParcelableExtra("user", User::class.java)

        viewModel.handleExtrasData(userExtra)

        setContentView(binding.root)
    }

    override fun bindingHandler() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.viewModel = viewModel
    }

    override fun elementHandler() {
        editProfileBtn = binding.btnEditProfile
        addReportFAB = binding.btnAddReport
        emailTV = binding.tvEmail
        usernameTV = binding.tvUsername
        logoutBtn = binding.btnSignOut
        profileImageCIV = binding.circleImageViewProfileImage

        profileFragment = ProfileFragment(viewModel)

        FragmentBuilder(supportFragmentManager).replace(R.id.fragment_profile, profileFragment)
            .commit()

        BottomNavigationViewHandler(this, binding.bottomNavigationView)
    }

    override fun eventHandler() {
        viewModel.isSelf.observe(this){
            if(it){
                editProfileBtn.visibility = android.view.View.VISIBLE
            } else {
                editProfileBtn.visibility = android.view.View.GONE
            }
        }
        logoutBtn.setOnClickListener {
            viewModel.handleLogout()
        }

        editProfileBtn.setOnClickListener {
            IntentHelper.moveTo(this, EditProfileActivity::class.java)
        }

        addReportFAB.setOnClickListener {
            IntentHelper.moveTo(this, CreateReportActivity::class.java)
        }

        viewModel.image.observe(this) {
            Picasso.get().load(it).placeholder(R.drawable.baseline_person_black_24)
                .into(profileImageCIV)
        }

        viewModel.activityToStart.observe(this) {
            IntentHelper.moveTo(this, it.java)
        }
    }


}