package com.codecoy.fitnesskiodkapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.adapters.RecomendedEquipment
import com.codecoy.fitnesskiodkapp.callbacks.ReplaceFragment
import com.codecoy.fitnesskiodkapp.databinding.FragmentEquipmentDisplayBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.models.EquipmentResponse
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_equipment_display.*


@AndroidEntryPoint
class EquipmentDisplay : Fragment() , Player.Listener, ReplaceFragment {

    private var eqp_id: Int? = null
    private var eqpVideoPath: String? = null
    private var clasVideoPath: String? = null
    private var eqpName: String? = null
    private var eqpDesc: String? = null
    private lateinit var context: MainActivity
    private lateinit var binding: FragmentEquipmentDisplayBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var URL: String
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            eqp_id = it.getInt(Constants.ARG_PARAM1)
            clasVideoPath = it.getString(Constants.ARG_PARAM2)
            eqpVideoPath = it.getString(Constants.ARG_PARAM3)
            eqpName = it.getString(Constants.ARG_PARAM4)
            eqpDesc = it.getString(Constants.ARG_PARAM5)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEquipmentDisplayBinding.inflate(inflater, container, false)
        context.keepScreenOn()

        URL = Constants.BASE_URL + eqpVideoPath



        binding.run {
            textView6.text = eqpName
            textView7.text = eqpDesc
        }
        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)
        viewModel.getRecomendedEquipments(MyApp.userId!! , eqp_id!!)

        viewModel.recomendedEquipment.observe(context, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    binding.pbRecomend.visibility = View.VISIBLE
                }
                is APIResponse.Error -> {
                  //  MyApp.showToast(requireContext(), "$it.message")
                    Log.d("TAG", "${it.message}")

                    binding.pbRecomend.visibility = View.GONE

                }
                is APIResponse.Success<*> -> {
                    binding.pbRecomend.visibility = View.GONE
                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                    val response = it.data as EquipmentResponse


                    // added data from arraylist to adapter class.

                    // added data from arraylist to adapter class.
                    val adapter = RecomendedEquipment(response.data, requireContext(), this)

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.
                    val layoutManager = GridLayoutManager(requireContext(), 2)

                    // at last set adapter to recycler view.

                    // at last set adapter to recycler view.
                    binding.recyclerRecomended.layoutManager = layoutManager
                    binding.recyclerRecomended.adapter = adapter
                }
            }
        })


        // exo player code start
        player = ExoPlayer.Builder(requireContext()).build()

        // Add a listener to receive events from the player.
        player.addListener(this)
        player.repeatMode = Player.REPEAT_MODE_ALL

//        player.addListener(object : Listener {
//            override fun onTracksChanged(tracks: Tracks) {
//                // Update UI using current tracks.
//                Log.v("MyTAG", "$tracks")
//            }
//        })

        binding.playerView!!.player = player
        binding.playerView.apply {
            setShowFastForwardButton(false)
            setShowNextButton(false)
            setShowPreviousButton(false)
            setShowRewindButton(false)
        }
        //  binding.epVideoView!!.progress_bar.visibility = View.VISIBLE

        // Build the media item.
        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(URL)
// Set the media item to be played.
// Set the media item to be played.
        player.setMediaItem(mediaItem)
// Prepare the player.
// Prepare the player.
        player.prepare()
// Start the playback.
// Start the playback.
        player.play()


        return binding.root

    }

//
//    override fun onIsPlayingChanged(isPlaying: Boolean) {
//        if (isPlaying) {
//            Log.v("MyTAG", "playing")
//            // Active playback.
//        } else {
//            // Not playing because playback is paused, ended, suppressed, or the player
//            // is buffering, stopped or failed. Check player.getPlayWhenReady,
//            // player.getPlaybackState, player.getPlaybackSuppressionReason and
//            // player.getPlaybackError for details.
//
//            if (player.playWhenReady) {
//                Log.v("MyTAG", "playing Agian")
//            }
//
//            if (player.playbackState == ExoPlayer.STATE_BUFFERING) {
//                //   progressBar.setVisibility(View.VISIBLE);
//            } else {
//                //    progressBar.setVisibility(View.INVISIBLE);
//            }
//
////            when (player.playbackState) {
////                Player.STATE_IDLE -> {
////                    Log.v("MyTAG", "idle...")
////                }
////                Player.STATE_BUFFERING -> {
////                    Log.v("MyTAG", "buffering....")
////                }
////                Player.STATE_READY -> {
////                    Log.v("MyTAG", "ready....")
////                }
////                Player.STATE_ENDED -> {
////                    Log.v("MyTAG", "ended...")
////                }
//
//
//            //       }
//        }
//    }

    override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                Log.v("MyTAG", "idle")
            }
            Player.STATE_BUFFERING -> {
                Log.v("MyTAG","buffering")
                binding.progressBar.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                Log.v("MyTAG","ready")
                //   hideProgressBar();
                binding.progressBar.visibility = View.GONE
            }
            Player.STATE_ENDED -> {
                Log.v("MyTAG", "ended")
            }

        }

    }

//    override fun onPlayerError(error: PlaybackException) {
//        val cause = error.cause
//        if (cause is HttpDataSourceException) {
//            // An HTTP error occurred.
//            // It's possible to find out more about the error both by casting and by
//            // querying the cause.
//
//            Log.v("MyTAG", "error HttpDataSourceException")
//            if (cause is InvalidResponseCodeException) {
//                // Cast to InvalidResponseCodeException and retrieve the response code,
//                // message and headers.
//                Log.v("MyTAG", "error InvalidResponseCodeException")
//            } else {
//                // Try calling httpError.getCause() to retrieve the underlying cause,
//                // although note that it may be null.
//                Log.v("MyTAG", "error else InvalidResponseCodeException")
//            }
//        }
//    }
//
//    override fun onIsLoadingChanged(isLoading: Boolean) {
//        if (isLoading) {
//            Log.v("MyTAG", "Loading...")
//
//            //     progress_bar.visibility = View.VISIBLE
//
//        } else {
//            Log.v("MyTAG", "Loading finish")
//            //   progress_bar.visibility = View.GONE
//        }
//
//    }
//
//
//    override fun onPositionDiscontinuity(
//        oldPosition: PositionInfo,
//        newPosition: PositionInfo,
//        reason: Int
//    ) {
//
//        Log.v("MyTAG", "old position :$oldPosition")
//        Log.v("MyTAG", "New position :$newPosition")
//        Log.v("MyTAG", "Reason :$reason")
//
//    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (events.contains(EVENT_PLAYBACK_STATE_CHANGED)
            || events.contains(EVENT_PLAY_WHEN_READY_CHANGED)
        ) {
            Log.v("MyTAG", "onEvents :Update Ui")
            if (player.playbackState == STATE_ENDED) {
                Log.v("MyTAG", "onEvents :restart")
              //  player.seekTo(0)
            }
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
        MyApp.bottomNav.visibility = View.VISIBLE
        player?.play()
        super.onResume()
    }

    override fun onAttach(contex: Context) {
        context = contex as MainActivity

        super.onAttach(context)
    }

//    override fun onRestart() {
//        super.onRestart()
//        Log.v("TAG", "restart")
//        player.play()
//    }





    override fun showClassDisplayFragment(
      item: Data
    ) {
        findNavController().navigate(
            R.id.action_equipmentDisplay_to_classDisplay,
            Bundle().apply {
                putSerializable(Constants.ARG_PARAM1,item)
            }
        )
    }

    override fun showEquipmentDisplayFragment(
        id: Int,
        clasVideoPath: String?,
        eqpVideoPath: String?,
        name: String,
        desc: String
    ) {

    }



}


//@RequiresApi(Build.VERSION_CODES.O)
//class EquipmentDisplay : AppCompatActivity(), SurfaceHolder.Callback,
//    SeekBar.OnSeekBarChangeListener,
//    MediaPlayer.OnPreparedListener  {
//    lateinit var binding: ActivityEquipmentDisplayBinding
//
//    //  var list: ArrayList<Equipment> = ArrayList()
//    lateinit var item: Data
//    val mediaPlayer:MediaPlayer = MediaPlayer()
//    private var runnable: Runnable? = null
//    private var handler = Handler(Looper.getMainLooper())
//
//    private lateinit var selectedVideoUri: Uri
//
//
//    var sh: SurfaceHolder? = null
//
//    var list: ArrayList<Data> = ArrayList()
//    lateinit var viewModel: MainViewModel
//
//
//    companion object {
//        const val GET_VIDEO = 123
//        const val SECOND = 1000
//        lateinit var URL: String
//
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityEquipmentDisplayBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//
//        item = intent.getSerializableExtra("item") as Data
//
//        URL = "https://wh717090.ispot.cc/fitness/public/storage/" + item.eqpVideoPath
//
//        Log.v("TAG", "Path : ${URL}")
//
//        sh = surfaceview.holder
//
//        mediaPlayer.setDataSource(URL)
//        mediaPlayer.prepareAsync()
//
//        mediaPlayer.setOnPreparedListener(this)
//
//        sh!!.addCallback(this)
//
//
//
//
//
//            // gor buffering
////        mediaPlayer.setOnInfoListener(object : MediaPlayer.OnInfoListener {
////            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
////                when (what) {
////                    MediaPlayer.MEDIA_INFO_BUFFERING_START ->   progress_bar.visibility = View.VISIBLE
////                    MediaPlayer.MEDIA_INFO_BUFFERING_END ->   progress_bar.visibility = View.GONE
////                }
////                return false
////            }
////        })
//
//
//
//        surfaceview.setOnTouchListener(object : OnTouchListener{
//            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
//                when (motionEvent.action) {
//                    MotionEvent.ACTION_DOWN -> if (mediaPlayer.isPlaying) {
//                        mediaPlayer.pause()
//                        MyApp.showToast(this@EquipmentDisplay,"pause")
//                    } else {
//                        mediaPlayer.start()
//                        MyApp.showToast(this@EquipmentDisplay,"resume")
//                    }
//                }
//                return true
//            }
//        })
//
//
//        //   seek_bar.setOnSeekBarChangeListener(this)
//        //   play_button.isEnabled = false
//
////        play_button.setOnClickListener {
////            if (mediaPlayer.isPlaying) {
////                mediaPlayer.pause()
////                play_button.setImageResource(android.R.drawable.ic_media_play)
////            } else {
////
////                play_button.setImageResource(android.R.drawable.ic_media_pause)
////            }
////        }
//        binding.run {
//            textView6.text = item.eqpName
//            textView7.text = item.eqpDesc
//        }
//
//
//        viewModel = ViewModelProvider(
//            this, MyViewModelFactory(
//                MainRepository(
//                    RetrofitAPI.getInstance()
//                )
//            )
//        ).get(
//            MainViewModel::class.java
//        )
//        viewModel.getRecomendedEquipments(item.id!!)
//
//        viewModel.recomendedEquipment.observe(this, Observer {
//            when (it) {
//                is APIResponse.Loading -> {
//                    pb_recomend.visibility = View.VISIBLE
//                }
//                is APIResponse.Error -> {
//                    MyApp.showToast(this, "$it.message")
//                    Log.d("TAG", "${it.message}")
//
//                    pb_recomend.visibility = View.GONE
//
//                }
//                is APIResponse.Success<*> -> {
//                    pb_recomend.visibility = View.GONE
//                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
//                    val response = it.data as EquipmentResponse
//
//
//                    // added data from arraylist to adapter class.
//
//                    // added data from arraylist to adapter class.
//                    val adapter = RecomendedEquipment(response.data, this)
//
//                    // setting grid layout manager to implement grid view.
//                    // in this method '2' represents number of columns to be displayed in grid view.
//
//                    // setting grid layout manager to implement grid view.
//                    // in this method '2' represents number of columns to be displayed in grid view.
//                    val layoutManager = GridLayoutManager(this, 2)
//
//                    // at last set adapter to recycler view.
//
//                    // at last set adapter to recycler view.
//                    binding.recyclerRecomended.layoutManager = layoutManager
//                    binding.recyclerRecomended.adapter = adapter
//                }
//            }
//        })
//
//
//    }
//
//
//    // Converting seconds to mm:ss format to display on screen
//    private fun timeInString(seconds: Int): String {
//        return String.format(
//            "%02d:%02d",
//            (seconds / 3600 * 60 + ((seconds % 3600) / 60)),
//            (seconds % 60)
//        )
//    }
//
//    // Initialize seekBar
//    private fun initializeSeekBar() {
//        seek_bar.max = mediaPlayer.seconds
//        text_progress.text = getString(R.string.default_value)
//        text_total_time.text = timeInString(mediaPlayer.seconds)
//        progress_bar.visibility = View.GONE
//   //    mediaPlayer.start()
//        mediaPlayer.isLooping = true
//        onCompleted()
//    }
//
//    // Update seek bar after every 1 second
//    private fun updateSeekBar() {
//        runnable = Runnable {
//            text_progress.text = timeInString(mediaPlayer.currentSeconds)
//            seek_bar.progress = mediaPlayer.currentSeconds
//            handler.postDelayed(runnable!!, SECOND.toLong())
//        }
//        handler.postDelayed(runnable!!, SECOND.toLong())
//    }
//
//    // SurfaceHolder is ready
//    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
//
//        mediaPlayer.apply {
//            // setDataSource(applicationContext, Uri.parse("android.resource://$packageName/raw/test_video"))
//            //setDataSource(applicationContext, selectedVideoUri)
//
//            //For DRM protected video
//            //   setOnDrmInfoListener(this@EquipmentDisplay) //This method will invoke onDrmInfo() function
//            //
//           setDisplay(surfaceHolder)
//        }
//    }
//
//    // Ignore
//    override fun surfaceChanged(surfaceHolder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
//        Log.v("TAG","surface change")
//    }
//
//    // Ignore
//    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
//        Log.v("TAG","surface Destroy")
//
//    }
//
//    // This function gets called if the file is DRM protected
////    override fun onDrmInfo(mediaPlayer: MediaPlayer?, drmInfo: MediaPlayer.DrmInfo?) {
////        mediaPlayer?.apply {
////            val key = drmInfo?.supportedSchemes?.get(0)
////            key?.let {
////                prepareDrm(key)
////                val keyRequest = getKeyRequest(
////                    null, null, null,
////                    MediaDrm.KEY_TYPE_STREAMING, null
////                )
////                provideKeyResponse(null, keyRequest.data)
////            }
////        }
////    }
//
//    // This function gets called when the media player gets ready
//    override fun onPrepared(mediaPlayer: MediaPlayer?) {
//
//
//
//        mediaPlayer!!.setOnBufferingUpdateListener { mediaPlayer1: MediaPlayer?, percent: Int ->
//            if (percent <= 99)
//              progress_bar.visibility = View.VISIBLE
//            else  progress_bar.visibility = View.GONE
//
//
////            val ratio: Double = percent / 100.0
////            val bufferingLevel = (mediaPlayer!!.getDuration() * ratio).toInt()
////            seek_bar.secondaryProgress = bufferingLevel
//        }
//      //  view.setTrackDuration(mediaPlayer!!.duration)
//       mediaPlayer.start()
////
////        changeTrackBarProgress()
//
//        initializeSeekBar()
//        updateSeekBar()
//    }
//
//    // Update media player when user changes seekBar
//    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//        if (fromUser)
//            mediaPlayer.seekTo(progress * SECOND)
//    }
//
//    // Ignore
//    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//    }
//
//    // Ignore
//    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//    }
//
//    // Create option menu in toolbar
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.app_menu, menu)
//        return true
//    }
//
//    // Invoked when an option is selected in menu
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.select_file -> {
//                val intent = Intent()
//                intent.type = "video/*"
//                intent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(
//                    Intent.createChooser(
//                        intent,
//                        getString(R.string.select_file)
//                    ), GET_VIDEO
//                )
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    // Invoked when a video is selected from the gallery
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == GET_VIDEO) {
//                selectedVideoUri = data?.data!!
//               // video_view.holder.addCallback(this)
//            }
//        }
//    }
//
//    // Release the media player resources when activity gets destroyed
//    override fun onDestroy() {
//        super.onDestroy()
//        runnable?.let {
//            handler.removeCallbacks(it)
//        }
//        mediaPlayer.release()
////        mediaPlayer.releaseDrm()
//    }
//
//    // on completing video
//    private fun onCompleted(){
//
//        mediaPlayer.setOnErrorListener { mp, what, extra -> true }
//
//        mediaPlayer.setOnCompletionListener {
//            Log.v("TAG","completed ")
//          it.start()
//        }
//    }
//
//
//
//    override fun onPause() {
//        super.onPause()
//        Log.v("TAG","pause")
//       mediaPlayer.pause()
//
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Log.v("TAG","restart")
//       mediaPlayer.start()
//        }
//
//
//
//    // Creating an extension properties to get the media player total time and current duration in seconds
//    private val MediaPlayer.seconds: Int
//        get() {
//            return this.duration / SECOND
//        }
//
//    private val MediaPlayer.currentSeconds: Int
//        get() {
//            return this.currentPosition / SECOND
//        }
//
//
//}