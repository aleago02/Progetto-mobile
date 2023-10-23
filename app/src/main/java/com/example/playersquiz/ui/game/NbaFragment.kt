package com.example.playersquiz.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playersquiz.R
import com.example.playersquiz.databinding.NbaFragmentBinding
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.playernba.Data
import com.example.playersquiz.ui.game.adapters.Adapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NbaFragment: Fragment() {
    private val viewModel: GameNbaViewModel by viewModels()
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: NbaFragmentBinding
    private lateinit var customAdapter: Adapter
    private var wordsList: MutableList<Int> = mutableListOf()
    //loding
    private lateinit var aLoding: ALoading



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = NbaFragmentBinding.inflate(inflater, container, false)
        aLoding = ALoading(this.activity)
        apiCall()
        Log.d("GameFragment", "GameFragment created/re-created!")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

    }

    private fun apiCall(){
        //qui da inserire inizio caricamento
        aLoding.startLoadingDialog()
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
        aLoding.dismissDialog()
        updateNextStats()
        updateNextWordOnScreen()
        updateScoreOnScreen()

    }

    /*Se vuoi -> binding.txtsquad.setText(viewModel.squad)*/
    private fun updateNextStats() {
        binding.txtsquad.text = viewModel.squad
        binding.txtAlt.text = viewModel.altezza
        binding.txtPos.text = viewModel.position
    }

    /*
        Qua usate this.binding.textInputEditText.addTextChangedListener(new TextWatcher() {})
        Fate @Override dei metodi è fatto apposta per i campi in cui va controllato il codice
        Così potete gestire vari aspetti in momenti diversi della scrittura,
        come che quando uno sbaglia sarebbe bello gli svuotaste l'inputText
    */
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                apiCall()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
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
                            aLoding.dismissDialog()
                            apiCall()
                        }else{
                            val responseBody = response.body()!!
                            val name = responseBody.first_name + " " + responseBody.last_name
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