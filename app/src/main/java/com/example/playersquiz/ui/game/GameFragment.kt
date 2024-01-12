package com.example.playersquiz.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playersquiz.R
import com.example.playersquiz.databinding.GameFragmentBinding
import com.example.playersquiz.databinding.OfflineCaseBinding
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.players.Players
import com.example.playersquiz.remote.models.transfer.MyData
import com.example.playersquiz.ui.game.adapters.Adapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer


class
GameFragment: Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: GameFragmentBinding
    private lateinit var customAdapter: Adapter
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
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        initializeGame()
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

        if (isNetworkAvailable()) {
            offlineLayout.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        } else {
            offlineLayout.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        }
    }

    private fun initializeGame() {
        if (isGameInitialized) {
            handleOfflineMode()
            apiCall()
            isGameInitialized = true
        }
    }

    private fun apiCall(){
        Log.d("GameFragment" ,  "apicall")
        aLoading = ALoading(this.activity)
        aLoading.startLoadingDialog()
        val page = generateRandomPage()
        if (wordsList.contains(page)) {
            apiCall()
        }else {
            wordsList.add(page)
            Log.d("GameFragment", "generateRandomPLayerId: $page")
            getPOIList(page, generateResponse())
        }
    }

    private fun createAll(){
        //qui fine caricamento
        aLoading.dismissDialog()
        updateNextImgOnScreen()
        updateNextWordOnScreen()
        updateScoreOnScreen()
    }

    private fun updateNextImgOnScreen() {
        customAdapter = Adapter(viewModel.uriList, viewModel.yearList, context = requireContext().applicationContext)
        binding.gridView.adapter = customAdapter
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
        Log.d("GameFragment", "Updating score: ${viewModel.score}")
        binding.score.text = getString(R.string.score, viewModel.score)
        binding.wordCount.text = getString(
            R.string.player_count, viewModel.currentWordCount, MAX_NO_OF_WORDS)
    }

    private fun generateRandomPage() : Int {
        return (1..45).random()
    }

    private fun generateResponse() : Int {
        return (0..19).random()
    }

    private fun getPOIList(page: Int, resp: Int) {
        MainScope().launch(Dispatchers.IO) {
            val metadata = RemoteApi.apiService.getPlayers(league, season, page)
            val result = metadata.enqueue(object : Callback<Players> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(call: Call<Players>, response: retrofit2.Response<Players>) {
                    if (response.isSuccessful) {
                        Log.d("GameFragment", "responseBody"+response.body())
                        val responseBody = response.body()!!.response[resp]
                        val name = responseBody.player.lastname //+ " " + responseBody.player.firstname
                        val meta = RemoteApi.apiService.getTransfer(responseBody.player.id.toLong())
                        val respo = meta.enqueue(object : Callback<MyData?> {
                            override fun onResponse(
                                call: Call<MyData?>,
                                response: Response<MyData?>
                            ) {
                                if(response.isSuccessful) {
                                    Log.d("GameFragment", "responseBody"+response.body())
                                    if (response.body()!!.response.isNotEmpty()) {
                                        viewModel.setting(response.body()!!.response[0], name)
                                        createAll()
                                        Log.d("GameFragment", "onResponse2")
                                        aLoading.dismissDialog()
                                    }else{
                                        aLoading.dismissDialog()
                                        apiCall()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                                Log.d("GameFragment", "onFailure: "+t.message)
                            }
                        })
                    }
                }

                override fun onFailure(call: Call<Players>, t: Throwable) {
                    Log.d("GameFragment", "onFailure: "+t.message)
                }
            })
        }
    }
}