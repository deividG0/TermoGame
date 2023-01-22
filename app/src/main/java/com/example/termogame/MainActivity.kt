package com.example.termogame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.termogame.databinding.ActivityMainBinding
import com.example.termogame.databinding.InputLineBinding
import com.google.android.material.textfield.TextInputEditText
import java.text.Normalizer
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentLine: InputLineBinding
    private lateinit var currentGameWord: String
    private lateinit var raffledWord: String
    private val TAG = "Test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        currentLine = binding.line1
        //currentGameWord = "Termo"

        raffleWord()
        initConfig()

        binding.fab.setOnClickListener {
            verifyLine()
            Log.i(TAG, "FAB clicked")
        }

    }

    private fun raffleWord() {
        val wordListSize = WordList.wordList.size
        val randomIndex = (0..wordListSize).random()
        raffledWord = WordList.wordList[randomIndex]
        Log.i(TAG,"raffledWord $raffledWord")
        currentGameWord = withoutAccentuation(raffledWord)
    }

    private fun withoutAccentuation(str: String): String {
        val nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

    private fun verifyLine() {

        verifyEmptyInput()

        val letter1 = withoutAccentuation(currentLine.tf1.et.text.toString())
        val letter2 = withoutAccentuation(currentLine.tf2.et.text.toString())
        val letter3 = withoutAccentuation(currentLine.tf3.et.text.toString())
        val letter4 = withoutAccentuation(currentLine.tf4.et.text.toString())
        val letter5 = withoutAccentuation(currentLine.tf5.et.text.toString())

        val wordReceived = letter1 + letter2 + letter3 + letter4 + letter5

        Log.i(TAG, "wordReceived: $wordReceived")

        if (wordReceived.equals(currentGameWord, ignoreCase = true)) {
            paintBackground(currentLine.tf1.et, "green")
            paintBackground(currentLine.tf2.et, "green")
            paintBackground(currentLine.tf3.et, "green")
            paintBackground(currentLine.tf4.et, "green")
            paintBackground(currentLine.tf5.et, "green")

            finishGame(true)
            return
        }
        if (currentGameWord[0].equals(letter1[0], true)) {
            paintBackground(currentLine.tf1.et, "green")
        } else {
            if (currentGameWord.contains(letter1, true)) {
                paintBackground(currentLine.tf1.et, "yellow")
            }
        }
        if (currentGameWord[1].equals(letter2[0], true)) {
            paintBackground(currentLine.tf2.et, "green")
        } else {
            if (currentGameWord.contains(letter2, true)) {
                paintBackground(currentLine.tf2.et, "yellow")
            }
        }
        if (currentGameWord[2].equals(letter3[0], true)) {
            paintBackground(currentLine.tf3.et, "green")
        } else {
            if (currentGameWord.contains(letter3, true)) {
                paintBackground(currentLine.tf3.et, "yellow")
            }
        }
        if (currentGameWord[3].equals(letter4[0], true)) {
            paintBackground(currentLine.tf4.et, "green")
        } else {
            if (currentGameWord.contains(letter4, true)) {
                paintBackground(currentLine.tf4.et, "yellow")
            }
        }
        if (currentGameWord[4].equals(letter5[0], true)) {
            paintBackground(currentLine.tf5.et, "green")
        } else {
            if (currentGameWord.contains(letter5, true)) {
                paintBackground(currentLine.tf5.et, "yellow")
            }
        }

        nextLine()
    }

    private fun finishGame(winning: Boolean) {
        if (winning) {
            setLineClickable(currentLine, false)
            Toast.makeText(this, "Você ganhou, parabéns !", Toast.LENGTH_LONG).show()
        }
        return
    }

    private fun paintBackground(et: TextInputEditText, color: String) {
        if (color == "green") {
            et.setBackgroundResource(R.drawable.background_letter_field_right)
        } else if (color == "yellow") {
            et.setBackgroundResource(R.drawable.background_letter_field_wrong)
        }
    }

    private fun verifyEmptyInput() {

        Log.i(TAG, "Verification done")

        if (currentLine.tf1.et.text == null ||
            currentLine.tf2.et.text == null ||
            currentLine.tf3.et.text == null ||
            currentLine.tf4.et.text == null ||
            currentLine.tf5.et.text == null
        ) {

            Toast.makeText(this, "Informe todas as letras da palavra.", Toast.LENGTH_LONG).show()
            return

        }
    }

    private fun initConfig() {

        disableLine(binding.line2)
        disableLine(binding.line3)
        disableLine(binding.line4)
        disableLine(binding.line5)
        disableLine(binding.line6)

    }

    private fun disableLine(lineId: InputLineBinding) {

        //lineId = binding.line1 for example

        // SEPARAR CLICKABLE DE BACKGROUND

        setLineClickable(lineId, false)

        lineId.tf1.et.setBackgroundResource(R.drawable.background_letter_field_disabled)
        lineId.tf2.et.setBackgroundResource(R.drawable.background_letter_field_disabled)
        lineId.tf3.et.setBackgroundResource(R.drawable.background_letter_field_disabled)
        lineId.tf4.et.setBackgroundResource(R.drawable.background_letter_field_disabled)
        lineId.tf5.et.setBackgroundResource(R.drawable.background_letter_field_disabled)

    }

    private fun setLineClickable(lineId: InputLineBinding, choose: Boolean) {

        lineId.tf1.tf.isEnabled = choose
        lineId.tf2.tf.isEnabled = choose
        lineId.tf3.tf.isEnabled = choose
        lineId.tf4.tf.isEnabled = choose
        lineId.tf5.tf.isEnabled = choose

    }

    private fun enableLine(lineId: InputLineBinding) {

        //lineId = binding.line1 for example

        setLineClickable(lineId, true)

        lineId.tf1.et.setBackgroundResource(R.drawable.background_letter_field)
        lineId.tf2.et.setBackgroundResource(R.drawable.background_letter_field)
        lineId.tf3.et.setBackgroundResource(R.drawable.background_letter_field)
        lineId.tf4.et.setBackgroundResource(R.drawable.background_letter_field)
        lineId.tf5.et.setBackgroundResource(R.drawable.background_letter_field)

    }

    private fun nextLine() {
        when (currentLine) {
            binding.line1 -> {
                setLineClickable(currentLine, false)
                currentLine = binding.line2
                enableLine(currentLine)
            }
            binding.line2 -> {
                setLineClickable(currentLine, false)
                currentLine = binding.line3
                enableLine(currentLine)
            }
            binding.line3 -> {
                setLineClickable(currentLine, false)
                currentLine = binding.line4
                enableLine(currentLine)
            }
            binding.line4 -> {
                setLineClickable(currentLine, false)
                currentLine = binding.line5
                enableLine(currentLine)
            }
            binding.line5 -> {
                setLineClickable(currentLine, false)
                currentLine = binding.line6
                enableLine(currentLine)
            }
        }
    }
}


