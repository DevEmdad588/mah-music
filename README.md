# Mah Music -Android Music Player App

This is an Android music player app that fetches music data from the Deezer API and allows users to play songs.

## Features

* Fetches music data using Retrofit from the Deezer API.
* Displays a list of songs in a RecyclerView.
* Plays selected songs using MediaPlayer.
* Provides basic playback controls (play, pause, stop, previous, next).
* Displays song title, artist, and album art.
* Shows current and total playback time.
* SeekBar for seeking within a song.

## Architecture

* MainActivity: Fetches music data, sets up the RecyclerView, and handles user interactions.
* MusicPlayerActivity: Plays the selected song, manages playback controls, and updates the UI.
* MyAdapter: Adapts the music data to the RecyclerView for display.
* Data classes: `Data`, `Artist`, `Album` represent the musicdata fetched from the API.
* ApiInterface: Defines the API endpoints for Retrofit.

## Libraries Used

* Retrofit: For making API calls.
* GsonConverterFactory: For converting JSON responses to data classes.
* Picasso: For loading and displaying album art images.
* MediaPlayer: For playing audio files.
* RecyclerView: For displaying the list of songs.
* CardView: For styling list items.
* Parcelable: For passing data between activities.
* CoroutineScope: For managing asynchronous operations.

## Usage

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app.
4. Search for music by artist name.
5. Select a song to play.
6. Use the playback controls to manage playback.

## API Key

You'll need to obtain an API key from Deezer and add it to the `ApiInterface` file.

## Future Enhancements

* Implement background playback and notifications.
* Add a search feature to find specific songs.
* Allow users to create and manage playlists.
* Improve the UI/UX.

## Contributing

Contributions are welcome! Feel free to open issuesor pull requests.
