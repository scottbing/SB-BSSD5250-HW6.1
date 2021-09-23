package edu.nmhu.bssd5250.url_requests

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.LinearLayoutCompat

class MainActivity : AppCompatActivity() {

    private var API_KEY = ""
    private var movieTitle: EditText? = null
    private var guessYear: EditText? = null

    companion object{
        const val MOVIE_TITLE:String = "edu.nmhu.bssd5250.url_requests.COLOR_RESULT_01"
        const val GUESS_YEAR:String = "edu.nmhu.bssd5250.url_requests.COLOR_RESULT_02"
    }


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                movieTitle?.setText(intent?.getStringExtra(MOVIE_TITLE).toString())
                guessYear?.setText(intent?.getStringExtra(GUESS_YEAR).toString())
                // Handle the Intent
                Log.i("MACTResult1", intent?.getStringExtra(MOVIE_TITLE).toString())
                Log.i("MACTResult2", intent?.getStringExtra(GUESS_YEAR).toString())
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieTitle = EditText(this).apply {
            hint = "Movie Title: Enter desired movei title"
        }

        guessYear = EditText(this).apply {
            hint = "Guess Year: Guess the year it was made"
        }

        val submitButton = Button(this).apply {
            "Submit".also { text = it }
            setOnClickListener {
                val passableData = Intent(applicationContext, MoviePosterActivity::class.java).apply {
                    putExtra(MoviePosterActivity.MOVIE_TITLE, "#"+ movieTitle!!.text.toString())
                    putExtra(MoviePosterActivity.GUESS_YEAR, "#"+ guessYear!!.text.toString())
                }
                startForResult.launch(passableData)
                startForResult.launch(passableData)

            }
        }

        val linearLayout = LinearLayoutCompat(this).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT)
            orientation = LinearLayoutCompat.VERTICAL
            addView(movieTitle)
            addView(guessYear)
            addView(submitButton)
        }

        //setContentView(R.layout.activity_main)
        setContentView(linearLayout)

    }


}