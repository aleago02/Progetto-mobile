package com.example.playersquiz.ui.game

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playersquiz.R
import com.example.playersquiz.databinding.NbaFragmentBinding
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.playernba.Data
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer

class NbaFragment: Fragment() {
    private val viewModel: GameNbaViewModel by viewModels()
    private lateinit var binding: NbaFragmentBinding
    private var wordsList: MutableList<Int> = mutableListOf()
    private lateinit var aLoading: ALoading
    private var isGameInitialized = false

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities((connectivityManager.activeNetwork))
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NbaFragmentBinding.inflate(inflater, container, false)
        aLoading = ALoading(this.activity)
        initializeGame()
        Log.d("GameFragment", "GameFragment created/re-created!")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
    }

    private fun handleOfflineMode() {
        val offlineLayout = binding.offlineLayout
        val scrollView = binding.onlineLayout
        val tryAgainButton = binding.tryAgainButton

        if (isNetworkAvailable()) {
            offlineLayout.visibility = LinearLayout.GONE
            scrollView.visibility = ScrollView.VISIBLE
        } else {
            offlineLayout.visibility = LinearLayout.VISIBLE
            scrollView.visibility = ScrollView.GONE
        }
    }

    fun onTryAgainClick(view: View) {
        if (isNetworkAvailable()) {
            handleOfflineMode()
            apiCall()
        }
    }

    private fun initializeGame() {
        if (!isGameInitialized) {
            if (isNetworkAvailable()) {
                handleOfflineMode()
                apiCall()
                isGameInitialized = true
            } else {
                handleOfflineMode()
            }

        }
    }

    private fun apiCall(){
        aLoading.startLoadingDialog()
        val id = generateRandomPage()
        if (wordsList.contains(id)) {
            apiCall()
        }else {
            wordsList.add(id)
            Log.d("GameFragment", "generateRandomPLayerId: $id")
            getPOIList(id)
        }
    }

    private fun createAll(){
        //qui fine caricamento
        aLoading.dismissDialog()
        updateNextStats()
        updateNextWordOnScreen()
        updateScoreOnScreen()

    }
    private fun updateNextStats() {
        binding.txtsquad.text = viewModel.squad
        binding.txtAlt.text = viewModel.altezza
        binding.txtPos.text = viewModel.position
    }

    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        Log.d("Debug", "User input: $playerWord")

        if (isNormalizedUsersWordCorrect(playerWord)) {
            setErrorTextField(false)
            viewModel.increaseScore()
            if (viewModel.nextWord()) {
                apiCall()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    private fun isNormalizedUsersWordCorrect(userWord: String): Boolean {
        val normalizedUserWord = normalizeString(userWord)
        val normalizedCurrentWord = normalizeString(viewModel.currentWord)

        // Stampa di debug
        Log.d("Debug", "Normalized user word: $normalizedUserWord")
        Log.d("Debug", "Normalized current word: $normalizedCurrentWord")

        return normalizedUserWord.equals(normalizedCurrentWord, ignoreCase = true)
    }

    private fun normalizeString(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

        Log.d("Debug", "After normalization: $normalized")

        return normalized
    }

    private fun onSkipWord() {
        showWord()
    }

    private fun showWord() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Player : "+ viewModel.currentWord)
            .setCancelable(false)
            .setPositiveButton("Go on") { _, _ ->
                if (viewModel.nextWord()) {
                    setErrorTextField(false)
                    apiCall()
                } else {
                    showFinalScoreDialog()
                }
            }
            .show()
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    private fun restartGame() {
        wordsList.clear()
        viewModel.reinitializeData()
        setErrorTextField(false)
        apiCall()
    }

    private fun exitGame() {
        activity?.finish()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }

    private fun updateScoreOnScreen(){
        binding.score.text = getString(R.string.score, viewModel.score)
        binding.wordCount.text = getString(
            R.string.player_count, viewModel.currentWordCount, MAX_NO_OF_WORDS)
    }

    private fun generateRandomPage() : Int {
        return (1..3092).random()
    }

    private fun getPOIList(id: Int) {
        MainScope().launch(Dispatchers.IO) {
            val metadata = RemoteApi.apiNbaService.getPlayers(id)
            val result = metadata.enqueue(object : Callback<Data?> {
                override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                    if (response.isSuccessful) {
                        Log.d("GameFragment", "responseBody"+response.body())
                        if(response.body()!!.height_feet == null || response.body()!!.height_inches == null || response.body()!!.position == ""){
                            aLoading.dismissDialog()
                            apiCall()
                        }else{
                            val responseBody = response.body()!!
                            val name = responseBody.last_name //+ " " + responseBody.first_name
                            viewModel.setting(responseBody, name)
                            Log.d("GameFragment", "onResponse")
                            createAll()
                        }
                    }
                }

                override fun onFailure(call: Call<Data?>, t: Throwable) {
                    Log.d("GameViewModel", "onFailure: "+t.message)
                }
            })
        }
    }
}