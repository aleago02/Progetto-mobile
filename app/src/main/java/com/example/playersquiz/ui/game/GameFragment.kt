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
import com.example.playersquiz.ui.game.adapters.Adapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay


class GameFragment: Fragment() {
    private val viewModel: GameViewModel by viewModels()
    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding
    private lateinit var customAdapter: Adapter
    private lateinit var gridView: GridView

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
        // Update the UI
        updateNextImgOnScreen()
//        updateNextWordOnScreen()
        updateScoreOnScreen()

    }

    private fun updateNextImgOnScreen() {
        Log.d("updateNextImgOnScreen", "yearList : ${viewModel.yearList} uriList : ${viewModel.uriList}")
        customAdapter = Adapter(viewModel.uriList, viewModel.yearList, context = requireContext().applicationContext)
        gridView = binding.gridView.findViewById(R.id.gridView)
        gridView.adapter = customAdapter
    }

    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
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
        if (viewModel.nextWord()) {
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
        viewModel.reinitializeData()
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
        Log.d("updateNextWordOnScreen", " score: ${viewModel.currentScrambledWord}")
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }

    private fun updateScoreOnScreen(){
        Log.d("updateScoreOnScreen", " score: ${viewModel.score}")
        binding.score.text = getString(R.string.score, viewModel.score)
        binding.wordCount.text = getString(
            R.string.word_count, viewModel.currentWordCount, MAX_NO_OF_WORDS)
    }

}
