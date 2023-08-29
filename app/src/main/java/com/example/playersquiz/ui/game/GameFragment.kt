package com.example.playersquiz.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playersquiz.R
import com.example.playersquiz.databinding.GameFragmentBinding
import com.example.playersquiz.remote.models.RootMetadataSupportResponseRemoteModel
import com.example.playersquiz.ui.game.Adapters.Adapter
import com.example.playersquiz.viewmodels.HomePageViewModel
import com.example.playersquiz.viewmodels.HomePageViewModelListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class GameFragment: Fragment(), HomePageViewModelListener {
    private val viewModel: HomePageViewModel by viewModels()
    private val gameViewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding
    //private var recyclerView: RecyclerView = binding.recyclerView
    private lateinit var customAdapter: Adapter
    private lateinit var gridView: GridView

    private lateinit var transfersList: List<RootMetadataSupportResponseRemoteModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        // Inflate the layout XML file and return a binding object instance
        val player = generateRandomPLayerId()
        viewModel.getPlayer(player)
        Log.d("callAPI", "$viewModel")
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")

        return binding.root
    }

    private fun generateRandomPLayerId() : Long {
        return (1..200015).random().toLong()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
        Log.d("updateNextImgOnScreen", "yearList : ${gameViewModel.yearList} uriList : ${gameViewModel.uriList}")
        updateNextImgOnScreen()
        updateNextWordOnScreen()
        updateScoreOnScreen()

    }

    private fun updateNextImgOnScreen() {
        Log.d("updateNextImgOnScreen", "yearList : ${gameViewModel.yearList} uriList : ${gameViewModel.uriList}")
        customAdapter = Adapter(transfersList, requireContext())
        gridView = binding.gridView.findViewById(R.id.gridView)
        gridView.adapter = customAdapter
    }

    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (gameViewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (gameViewModel.nextWord()) {
                updateNextWordOnScreen()
                updateScoreOnScreen()
                updateNextImgOnScreen()
            } else {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    private fun onSkipWord() {
        if (gameViewModel.nextWord()) {
            setErrorTextField(false)
            updateNextWordOnScreen()
            updateScoreOnScreen()
            updateNextImgOnScreen()
        } else {
            showFinalScoreDialog()
        }
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, gameViewModel.score))
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
        gameViewModel.reinitializeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

    private fun exitGame() {
        activity?.finish()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    private fun updateNextWordOnScreen() {
        Log.d("updateNextWordOnScreen", " score: ${gameViewModel.currentScrambledWord}")
        binding.textViewUnscrambledWord.text = gameViewModel.currentScrambledWord
    }

    private fun updateScoreOnScreen(){
        Log.d("updateScoreOnScreen", " score: ${gameViewModel.score}")
        binding.score.text = getString(R.string.score, gameViewModel.score)
        binding.wordCount.text = getString(
            R.string.word_count, gameViewModel.currentWordCount, MAX_NO_OF_WORDS)
    }

    override fun onTransfersList(list: List<RootMetadataSupportResponseRemoteModel>) {
        customAdapter = Adapter(list, requireContext().applicationContext)
        gridView.adapter = customAdapter
    }
}
