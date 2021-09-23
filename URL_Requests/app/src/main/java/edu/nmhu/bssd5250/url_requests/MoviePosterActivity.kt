package edu.nmhu.bssd5250.url_requests

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

class MoviePosterActivity : AppCompatActivity() {

    private var API_KEY = ""
    private var movieTitle: String = ""
    private var guessYear: String = ""
    private lateinit var posterImage: ImageView
    private val imageBasePath = "https://image.tmdb.org/t/p/w500"
    private val service = "https://api.themoviedb.org/3/search/movie"

    companion object{
        const val MOVIE_TITLE:String = "edu.nmhu.bssd5250.url_requests.COLOR_RESULT_01"
        const val GUESS_YEAR:String = "edu.nmhu.bssd5250.url_requests.COLOR_RESULT_02"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_poster)

        API_KEY = getString(R.string.api_key)

        val dataSent = intent

        // get bundled data
        movieTitle= dataSent.getStringExtra(MOVIE_TITLE)?.substring(1).toString()
        guessYear = dataSent.getStringExtra(GUESS_YEAR).toString()

        // verify reciept of data
        Log.i("Poster MOVIE_TITLE", movieTitle)
        Log.i("Poster GUESS_YEAR", guessYear)


        posterImage = ImageView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        thread(true) {
            //swapped for example requet from the tmdb api tab in your profile
            val query = movieTitle
            val requestURL = "$service?api_key=$API_KEY&query=$query"
            Log.i("URL_Requests - URL:", requestURL)
            Log.i("MACT", getRequest(requestURL).toString())
            Log.i("MACT", requestURL)
            parseJSON(getRequest(requestURL).toString())
            //Log.i("MACT", getRequest("https://api.themoviedb.org/3/movie/550?api_key=$API_KEY").toString())
        }

        setContentView(posterImage)
    }

    private fun parseJSON(jsonString:String){
        //tmdb always returns an object
        val jsonData = JSONObject(jsonString)
        val jsonArray = jsonData.getJSONArray("results")
        val film = jsonArray.getJSONObject(0)
        val posterPath = film.getString("poster_path")
        val fullPath = imageBasePath + posterPath

        var year = film.getString("release_date")
        year = year.substring(0,4) //only need first four digits for year
        var answer = "Incorrect"
        if(year == guessYear) {
            answer = "Correct"
        }
        this@MoviePosterActivity.runOnUiThread{
            Toast.makeText(applicationContext, answer, Toast.LENGTH_SHORT).show()
        }

        thread(start = true) {
            val bmp = loadBitmapData(fullPath)
            this@MoviePosterActivity.runOnUiThread{
                posterImage.setImageBitmap(bmp)
            }
            //posterImage.setImageBitmap(loadBitmapData(fullPath))
        }
        Log.i("MACPoster", fullPath)
    }

    private fun loadBitmapData(path:String): Bitmap?{
        val inputStream: InputStream
        var result: Bitmap? = null

        try {
            val url = URL(path)
            val conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            conn.connect()
            inputStream = conn.inputStream

            result = BitmapFactory.decodeStream(inputStream)

        }catch (err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
        }

        return result
    }

    private fun getRequest(sUrl: String): String? {
        val inputStream: InputStream
        var result: String? = null

        try {
            // Create URL
            val url = URL(sUrl)

            // Create HttpURLConnection
            val conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection

            // Launch GET request
            conn.connect()

            // Receive response as inputStream
            inputStream = conn.inputStream

            if (inputStream != null)
            // Convert input stream to string
                result = inputStream.bufferedReader().use(BufferedReader::readText)
            else
                result = "error: inputStream is null"
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }

        return result
    }

}