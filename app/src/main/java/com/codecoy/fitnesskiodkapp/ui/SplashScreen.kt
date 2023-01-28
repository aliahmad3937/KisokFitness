package com.codecoy.fitnesskiodkapp.ui


import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest

import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.MyApp.Companion.mainNavController
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.databinding.FragmentSplashScreenBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.SplashResponse
import com.codecoy.fitnesskiodkapp.utils.Constants.DELAY_TIME
import com.codecoy.fitnesskiodkapp.utils.ToastUtils
import com.codecoy.fitnesskiodkapp.utils.ToastUtils.showToast
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : Fragment(), Player.Listener {
    private lateinit var binding: FragmentSplashScreenBinding

    private lateinit var viewModel: MainViewModel


    private lateinit var player: ExoPlayer
  //  lateinit var response: SplashResponse
  private  lateinit var context: MainActivity

    private lateinit var sp: SharedPreferences

     private var startTime: Int = 0
     private var currentTime:Int = 0
     private var timeofVip:Int =0



        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)

        sp = context.getSharedPreferences("vipexpire", Context.MODE_PRIVATE)
        startTime = sp.getInt("starttime", 0)
        currentTime = (System.currentTimeMillis() / 1000).toInt()
        timeofVip = currentTime - startTime //calculate the time of his VIP-being time



        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        mainNavController = navHostFragment.navController



        binding.layout1?.setOnClickListener{


            //  Log.v("TAG3","146 start timr :"+startTime   +" currenttime :"+currentTime)
            if(startTime != 0) {
                //   Log.v("TAG3","148")
                if (timeofVip >= 2592000) //2592000 is 30 days in seconds
                {
                    sp.edit().clear().apply()
                    //     Log.v("TAG3","152 vip :"+timeofVip)
                } else {
                    //  Log.v("TAG3","154")
                    MyApp.userId = sp.getInt("userid",0)
                    mainNavController.navigate(R.id.action_splashScreen_to_navigationScreen)
                }
            }else{
                //  Log.v("TAG3","157")
                mainNavController.navigate(R.id.action_splashScreen_to_login)
            }
        }




        // exo player code start
        player = ExoPlayer.Builder(context).build()
        // Add a listener to receive events from the player.
        player.addListener(this)
        player.repeatMode = Player.REPEAT_MODE_ALL
        binding.playerView!!.player = player

        viewModel.splashResponse.observe(context, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    //  MyApp.showToast(context, "loading")
                }
                is APIResponse.Error -> {
                      ToastUtils.showToast(context, it.message+"")
                }
                is APIResponse.Success<*> -> {
                 //   showToast(context, "success")
                   //  response = it.data as SplashResponse
                    updateUI()

                }
            }
        })

        if(MyApp.logoUrl != null){
            updateUI()
        }else{
            viewModel.splashData()
        }


        return binding.root

    }

    private fun updateUI() {
        val mediaItem: MediaItem =
            MediaItem.fromUri(MyApp.videoUrl.toString())
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()


//        Picasso.get().load(MyApp.logoUrl).fit().into(binding.imageView)
        binding.imageView.loadSvgOrOther(MyApp.logoUrl)

    }


    override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                Log.v("MyTAG", "idle")
            }
            Player.STATE_BUFFERING -> {
            }
            Player.STATE_READY -> {
                binding.playerView!!.visibility = View.VISIBLE

            }
            Player.STATE_ENDED -> {
              //  player.seekTo(0)
                 player.play()

                Log.v("MyTAG", "ended")
            }

        }

    }


    override fun onPause() {
        super.onPause()

        player.release()
    }

    // Release the media player resources when activity gets destroyed
    override fun onDestroy() {
        super.onDestroy()

       player.release()
    }


    override fun onAttach(contex: Context) {
        super.onAttach(contex)
        context = contex as MainActivity
    }

}


fun ImageView.loadSvgOrOther(myUrl: String?, cache: Boolean = true, errorImg: Int = R.drawable.logo ) {

    myUrl?.let {
        if (it.lowercase().endsWith("svg")) {
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry {
                    add(SvgDecoder(this@loadSvgOrOther.context))
                }.build()

            val request = ImageRequest.Builder(this.context).apply {
              //  error(errorImg)
             //   placeholder(errorImg)
                data(it).decoder(SvgDecoder(this@loadSvgOrOther.context))
            }.target(this).build()

            imageLoader.enqueue(request)
        } else {
            val imageLoader = ImageLoader(context)

            val request = ImageRequest.Builder(context).apply {
                if (cache) {
                    memoryCachePolicy(CachePolicy.ENABLED)
                } else {
                    memoryCachePolicy(CachePolicy.DISABLED)
                }
              //  error(errorImg)
              //  placeholder(errorImg)
                data("$it")
            }.target(this).build()

            imageLoader.enqueue(request)
        }
    }
} // loadSvgOrOther

