package com.example.playersquiz.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playersquiz.MainActivity
import com.example.playersquiz.R
import com.example.playersquiz.databinding.GameFragmentBinding
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.MyData
import com.example.playersquiz.ui.game.adapters.Adapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback


class GameFragment: Fragment() {
    private val viewModel: GameViewModel by viewModels()
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding
    private lateinit var customAdapter: Adapter
    private lateinit var gridView: GridView
    private var wordsList: MutableList<Int> = mutableListOf()
    //loding
    private lateinit var aLoding: ALoading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        apiCall()

    }

    private fun apiCall(){
        //qui da inserire inizio caricamento
        aLoding = ALoading(this.activity)
        aLoding.startLoadingDialog()
        val id = generateRandomPLayerId()
        if (wordsList.contains(id.toInt())) {
            apiCall()
        }else {
            wordsList.add(id.toInt())
            Log.d("GameFragment", "generateRandomPLayerId: $id")
            getPOIList(id)
        }
    }

    private fun createAll(){
        //qui fine caricamento
        aLoding.dismissDialog()
        updateNextImgOnScreen()
        updateNextWordOnScreen()
        updateScoreOnScreen()

    }

    private fun updateNextImgOnScreen() {
        customAdapter = Adapter(viewModel.uriList, viewModel.yearList, context = requireContext().applicationContext)
        gridView = binding.gridView.findViewById(R.id.gridView)
        gridView.adapter = customAdapter
    }

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
        if (viewModel.nextWord()) {
            setErrorTextField(false)
            apiCall()
        } else {
            showFinalScoreDialog()
        }
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
            R.string.word_count, viewModel.currentWordCount, MAX_NO_OF_WORDS)
    }

    private fun generateRandomPLayerId() : Long {
        return (1..200015).random().toLong()
    }

    private fun getPOIList(id: Long) {
        MainScope().launch(Dispatchers.IO) {
            val metadata = RemoteApi.apiService.getMetadata(id)
            val result = metadata.enqueue(object : Callback<MyData> {
                override fun onResponse(call: Call<MyData>, response: retrofit2.Response<MyData>) {
                    if (response.isSuccessful) {
                        Log.d("GameFragment", "responseBody"+response.body())
                        val responseBody = response.body()!!.response[0]
                        viewModel.setting(responseBody)
                        Log.d("GameFragment", "onResponse")
                        createAll()
                    }
                }

                override fun onFailure(call: Call<MyData>, t: Throwable) {
                    Log.d("GameViewModel", "onFailure: "+t.message)
                }

            })
        }

    }

}
