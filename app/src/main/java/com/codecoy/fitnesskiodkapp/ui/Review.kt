package com.codecoy.fitnesskiodkapp.ui

import android.app.Dialog
import android.content.Context

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.databinding.FragmentReviewBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.models.LoginResponse
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.codecoy.fitnesskiodkapp.utils.Constants.DELAY_TIME
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_review.*


@AndroidEntryPoint
class Review : Fragment() {
    private var instrRating: Int? = null
    private var diffiRating: Int? = null
    private var reviews: String? = null
    private lateinit var binding: FragmentReviewBinding
    private lateinit var context: MainActivity
    private lateinit var item: Data
    private lateinit var viewModel: MainViewModel
    private var rate: Boolean = false
    private  val handler = Handler(Looper.getMainLooper())
    private lateinit  var runnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getSerializable(Constants.ARG_PARAM1) as Data
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)
//        Picasso.get()
//            .load(MyApp.logoUrl)
//            .fit().
//            into(binding.imageView)
        binding.imageView.loadSvgOrOther(MyApp.logoUrl)
        binding.logo.loadSvgOrOther(MyApp.logoUrl)




//            try {
//                if (!reviews!!.isEmpty()) {
//                    if (reviews!!.equals(Constants.likeReview)) {
//                        binding.like.setImageResource(R.drawable.like_fill)
//                        binding.dislike.setImageResource(R.drawable.dislike)
////                binding.like.isEnabled = false
////                binding.dislike.isEnabled = true
//                        reviews = Constants.likeReview
//                    } else {
//                        binding.dislike.setImageResource(R.drawable.dislike_fill)
//                        binding.like.setImageResource(R.drawable.like)
////                binding.dislike.isEnabled = false
////                binding.like.isEnabled = true
//                        reviews = Constants.dislikeReview
//                    }
//                }
//            }catch (e:Exception){
//
//            }


        binding.no.setOnClickListener {
            binding.layoutReview.visibility = View.GONE
            binding.layoutInstructor.visibility = View.GONE
            binding.layoutDifficulty.visibility = View.GONE
            title.text = "Thank you for your time."
            binding.btnGroup.visibility = View.GONE
            ok.visibility = View.VISIBLE
        }

        binding.yes.setOnClickListener {
            try {
                findNavController().navigate(
                    R.id.action_review_to_bookedTraining,
                    Bundle().apply {
                        putSerializable(Constants.ARG_PARAM1,item)
                    }
                )
            }catch (e:Exception){

            }
        }

        binding.ok.setOnClickListener {
            findNavController().navigate(R.id.action_global_home2)
        }


        binding.like.setOnClickListener {
            binding.like.setImageResource(R.drawable.like_fill)
            binding.dislike.setImageResource(R.drawable.dislike)
            binding.like.isEnabled = false
            binding.dislike.isEnabled = true
            reviews = Constants.likeReview

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                binding.layoutDifficulty.visibility = View.VISIBLE
                binding.layoutReview.visibility = View.GONE
                binding.layoutInstructor.visibility = View.GONE
             //   binding.difficultyRating.rating = (diffiRating!!).toFloat()
            }, 500)


        }

        binding.dislike.setOnClickListener {
            binding.dislike.setImageResource(R.drawable.dislike_fill)
            binding.like.setImageResource(R.drawable.like)
            binding.dislike.isEnabled = false
            binding.like.isEnabled = true
            reviews = Constants.dislikeReview

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                binding.layoutDifficulty.visibility = View.VISIBLE
                binding.layoutReview.visibility = View.GONE
                binding.layoutInstructor.visibility = View.GONE
             //   binding.difficultyRating.rating = (diffiRating!!).toFloat()
            }, 500)

            //  binding.difficultyRating.setRating =

        }

        binding.difficultyRating.setOnRatingChangeListener { ratingBar, rating ->

            ratingBar.rating = rating
            diffiRating = rating.toInt()
        //    if (rate) {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    binding.layoutReview.visibility = View.GONE
                    binding.layoutInstructor.visibility = View.VISIBLE
                    binding.layoutDifficulty.visibility = View.GONE
                }, 500)
        //    }
       //     rate = true

            //  MyApp.showToast(this,diffiRating)
        }

        binding.rating2.setOnRatingChangeListener { ratingBar, rating ->

            ratingBar.rating = rating
            instrRating = rating.toInt()


            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                binding.layoutReview.visibility = View.GONE
                binding.layoutInstructor.visibility = View.GONE
                binding.layoutDifficulty.visibility = View.GONE
                binding.logo.visibility = View.GONE
                binding.layoutBookedTraining.visibility = View.VISIBLE
              //    binding.rating2.rating = (diffiRating!!).toFloat()
            }, 500)

//            showDialoge(0)

            try {
                viewModel.reviewClass(
                    MyApp.userId!!,
                    item.id!!,
                    reviews!!,
                    diffiRating!!,
                    instrRating!!
                )
            } catch (e: Exception) {

            }



            reviews = null
            diffiRating = null
            instrRating = null

        }



        runnable = Runnable{ MyApp.mainNavController.navigate(R.id.action_global_splash) }
        handler.postDelayed(runnable, Constants.DELAY_TIME)


        return binding.root
    }

    override fun onResume() {
        if(!handler.hasCallbacks(runnable)){
            handler.postDelayed(runnable,DELAY_TIME)
        }
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        Log.v("TAG", "return from review")
        if(handler.hasCallbacks(runnable)){
            handler.removeCallbacks(runnable)
        }
    }

    fun showDialoge(id: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.bookin_dialog)

        val yes: Button = dialog.findViewById(R.id.yes) as Button
        val no: Button = dialog.findViewById(R.id.no) as Button
        val ok: Button = dialog.findViewById(R.id.ok) as Button
        val group: Group = dialog.findViewById(R.id.btn_group) as Group
        val title: TextView = dialog.findViewById(R.id.title) as TextView


        if (id == 1) {
            title.text = "Thank you for your time."
            group.visibility = View.GONE
            ok.visibility = View.VISIBLE
        }


        no.setOnClickListener {
            title.text = "Thank you for your time."
            group.visibility = View.GONE
            ok.visibility = View.VISIBLE
        }
//
//        yes.setOnClickListener {
//            try {
//                dialog.dismiss()
//                findNavController().navigate(
//                    R.id.action_review_to_bookedTraining,
//                    Bundle().apply {
//                        putInt(Constants.ARG_PARAM1, classId!!)
//                    }
//                )
//            }catch (e:Exception){
//
//            }
//
//        }

        ok.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_global_home2)
        }
        dialog.show()
    }

//    override fun onBackPressed() {
//        if (binding.layoutThanku.visibility == View.VISIBLE) {
//            finish()
//        } else {
//            super.onBackPressed()
//        }
//    }


    override fun onAttach(contex: Context) {
        context = contex as MainActivity

        super.onAttach(context)
    }
}