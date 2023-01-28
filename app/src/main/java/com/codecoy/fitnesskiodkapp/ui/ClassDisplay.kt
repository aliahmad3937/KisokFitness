package com.codecoy.fitnesskiodkapp.ui


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.databinding.FragmentClassDisplayBinding
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.codecoy.fitnesskiodkapp.utils.Constants.BASE_URL
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Tracks
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_controler.view.*


@AndroidEntryPoint
class ClassDisplay : Fragment(), Player.Listener {
    // TODO: Rename and change types of parameters
    private lateinit var item: Data
    private var URL: String? = null
    private lateinit var context: MainActivity
    private lateinit var binding: FragmentClassDisplayBinding
    private lateinit var player: ExoPlayer

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
        binding =
            FragmentClassDisplayBinding.inflate(inflater, container, false)
        context.keepScreenOn()



        binding.layoutVideoInfo!!.visibility = View.VISIBLE
        binding.layoutVideoPlay!!.visibility = View.GONE

        Picasso.get()
            .load("${BASE_URL}${item.video_thumb_img}")
            .fit()
            .into(binding.videoThumb)

        Picasso.get()
            .load("${BASE_URL}${item.qr_img}")
            .fit()
            .into(binding.qrImage)






        binding.apply {
            traineeLevel!!.text = item.workoutLevel
            title!!.text = item.clasName
            name!!.text = item.trainerName
            desc!!.text = item.desc
        }


        binding.playNow!!.setOnClickListener{
            binding.layoutVideoInfo!!.visibility = View.GONE
            binding.layoutVideoPlay!!.visibility = View.VISIBLE
            player.playWhenReady = true
            player.play()

            if(handler.hasCallbacks(runnable)){
                handler.removeCallbacks(runnable)
            }
        }


        URL = Constants.BASE_URL + (item.eqpVideoPath ?: item.clasVideoPath)

        Log.v("TAG", "URL : $URL")


        // exo player code start
        player = ExoPlayer.Builder(requireContext()).build()

        // Add a listener to receive events from the player.
        player.addListener(this)


        player.addListener(object : Player.Listener {
            override fun onTracksChanged(tracks: Tracks) {
                // Update UI using current tracks.
                Log.v("MyTAG", "$tracks")
            }
        })

        binding.playerView!!.player = player
        binding.playerView!!.player!!.playWhenReady = true


        //  binding.epVideoView!!.progress_bar.visibility = View.VISIBLE

        // Build the media item.
        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(URL!!)
// Set the media item to be played.
// Set the media item to be played.
        player.setMediaItem(mediaItem)
// Prepare the player.
// Prepare the player.
        player.prepare()
        player.playWhenReady = false
// Start the playback.
// Start the playback.



        binding.playerView.exo_replay.setOnClickListener {
            player.seekTo(0)
            binding.playerView.exo_play_pause.visibility = View.VISIBLE
            binding.playerView.exo_replay.visibility = View.GONE
        }
        binding.playerView.end_video.setOnClickListener {
//            val intent = Intent(this, Review::class.java)
//            intent.putExtra("item", item)
//            startActivity(intent)
//            finish()

            findNavController().navigate(
                R.id.action_classDisplay_to_review,
                Bundle().apply {
                    putSerializable(Constants.ARG_PARAM1,item)
                }
            )
        }


        runnable = Runnable{
            MyApp.mainNavController.navigate(R.id.action_global_splash)
        }
        handler.postDelayed(runnable, Constants.DELAY_TIME)



        return binding.root

    }


    override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                Log.v("MyTAG", "idle")
            }
            Player.STATE_BUFFERING -> {
                Log.v("MyTAG", "buffering")
                binding.progressBar.visibility = View.VISIBLE
                binding.playerView.exo_play_pause.visibility = View.GONE
            }
            Player.STATE_READY -> {
                Log.v("MyTAG", "ready")
                //   hideProgressBar();
                binding.progressBar.visibility = View.GONE
                binding.playerView.exo_play_pause.visibility = View.VISIBLE
            }
            Player.STATE_ENDED -> {
                //        playBtn.setImageResource(R.drawable.ic_replay)
                updateUi()
//                Handler().postDelayed(Runnable {
//          showDialoge()
//            },500)

                Log.v("MyTAG", "ended")
            }

        }

    }

    private fun updateUi() {
        binding.playerView.apply {
            exo_play_pause.visibility = View.GONE
            exo_replay.visibility = View.VISIBLE
        }

    }

    // Release the media player resources when activity gets destroyed
    override fun onDestroy() {
        super.onDestroy()

        player.release()
    }

    override fun onPause() {
        super.onPause()
        Log.v("TAG", "pause")
        player.pause()

    }

    override fun onResume() {
        MyApp.bottomNav.visibility = View.GONE
        super.onResume()
    }

//    override fun onRestart() {
//        super.onRestart()
//        Log.v("TAG","restart")
//        player.play()
//    }


    override fun onAttach(contex: Context) {
        context = contex as MainActivity

        super.onAttach(context)
    }


}

