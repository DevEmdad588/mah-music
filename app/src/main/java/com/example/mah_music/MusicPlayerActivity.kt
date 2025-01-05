package com.example.mah_music

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.squareup.picasso.Picasso

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeTextView: TextView
    private lateinit var totalTimeTextView: TextView
    private lateinit var songTitleTextView: TextView
    private lateinit var artistTextView: TextView
    private lateinit var albumArtImageView: ImageView

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentSongIndex: Int = 0
    private lateinit var songList: List<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_screen)

        // Initialize views
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
        seekBar = findViewById(R.id.seekBar)
        currentTimeTextView = findViewById(R.id.currentTimeTextView)
        totalTimeTextView = findViewById(R.id.totalTimeTextView)
        songTitleTextView = findViewById(R.id.songTitleTextView)
        artistTextView = findViewById(R.id.artistTextView)
        albumArtImageView = findViewById(R.id.albumArtImageView)
        songList = intent.getParcelableArrayListExtra<Data>("songList") ?: listOf()

        handler = Handler() // Initialize handler here

        // Get song data from intent
        val title = intent.getStringExtra("title")
        val artist = intent.getStringExtra("artist")
        val albumArtUrl = intent.getStringExtra("albumArt")
        val previewUrl = intent.getStringExtra("previewUrl")

        // Set song data to views
        songTitleTextView.text = title
        artistTextView.text = artist
        Picasso.get().load(albumArtUrl).into(albumArtImageView)

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                seekBar.max = it.duration
                val totalTime = formatTime(it.duration)
                totalTimeTextView.text = totalTime
                it.start()
                playButton.isEnabled = false
                pauseButton.isEnabled = true
                stopButton.isEnabled = true
                updateSeekBar()
            }
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Error setting data source", e)
        }

        mediaPlayer.setOnCompletionListener {
            if (currentSongIndex < songList.size - 1) {
                playNextSong()
            } else {
                // Handle end of playlist (e.g., stop playback, reset UI)
                mediaPlayer.stop()
                mediaPlayer.reset()
                playButton.isEnabled = true
                pauseButton.isEnabled = false
                stopButton.isEnabled = false
                seekBar.progress = 0
                currentTimeTextView.text = "0:00"
            }
        }


        // Set up SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up Play/Pause button
        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                playButton.isEnabled = false
                pauseButton.isEnabled = true
                stopButton.isEnabled = true
                updateSeekBar()
            }
        }
        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playButton.isEnabled = true
                pauseButton.isEnabled = false
                stopButton.isEnabled = true
            }
        }

        // Set up Stop button
        stopButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.prepareAsync() // Prepare for playing again
            playButton.isEnabled = true
            pauseButton.isEnabled = false
            stopButton.isEnabled = false
            seekBar.progress = 0
            currentTimeTextView.text = "0:00"
        }

        // Set up Previous and Next buttons (Not functional in this example)
            // Implement previous song logic here
            previousButton.setOnClickListener {
                playPreviousSong()
            }

            // Implement next song logic here
            nextButton.setOnClickListener {
                playNextSong()
        }
    }

    private fun playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            playSong(currentSongIndex)
        }
    }

    private fun playNextSong() {
        if (currentSongIndex < songList.size - 1) {
            currentSongIndex++
            playSong(currentSongIndex)
        }
    }

    private fun playSong(index: Int) {
        val songData = songList[index]

        // Update UI elements with songData
        songTitleTextView.text = songData.title
        artistTextView.text = songData.artist.name
        Picasso.get().load(songData.album.cover).into(albumArtImageView)

        // Stop and reset MediaPlayer if it's playing
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }

        // Release existing resources and create a new MediaPlayer instance
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer.setDataSource(songData.preview)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                seekBar.max = it.duration
                val totalTime = formatTime(it.duration)
                totalTimeTextView.text = totalTime
                it.start()
                playButton.isEnabled = false
                pauseButton.isEnabled = true
                stopButton.isEnabled = true
                updateSeekBar()
            }
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Error setting data source", e)
            // Handle exceptions (e.g., show an error message to the user)
        }
    }

    // Update SeekBar progress and current time
    private fun updateSeekBar() {
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            val currentTime = formatTime(mediaPlayer.currentPosition)
            currentTimeTextView.text = currentTime
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    // Format time in milliseconds to mm:ss
    private fun formatTime(timeInMillis: Int): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // Release MediaPlayer when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized || mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }
}

// songTitleTextView.text = title
//        artistTextView.text = artist
//        Picasso.get().load(albumArtUrl).into(albumArtImageView)
//
//        // Initialize MediaPlayer
//        mediaPlayer = MediaPlayer.create(this, previewUrl?.toUri())
//        mediaPlayer.setOnPreparedListener {
//            seekBar.max = it.duration
//            val totalTime = formatTime(it.duration)
//            totalTimeTextView.text = totalTime
//            it.start()
//            playButton.isEnabled = false
//            pauseButton.isEnabled = true
//            stopButton.isEnabled = true
//            updateSeekBar() // Start updating seekbar
//        }



// private fun playSong(index: Int) {
//        val songData = songList[index]
//        // Update UI elements with songData (title, artist, album art)
//        // ...
//
//        // Reset and prepare MediaPlayer with new song
//        mediaPlayer.reset()
//        try {
//            mediaPlayer.setDataSource(songData.preview)
//            mediaPlayer.prepareAsync()
//            // ... (rest of your MediaPlayer setup)
//        }catch (e: Exception) {
//            // ... (handle exceptions)
//        }
//    }