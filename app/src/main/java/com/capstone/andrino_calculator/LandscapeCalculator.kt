package com.capstone.andrino_calculator

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LandscapeCalculator : AppCompatActivity() {

    private lateinit var display: TextView
    private lateinit var result: TextView
    private val currentInput = StringBuilder()
    private var operand1: Double? = null
    private var operator: String? = null
    private var isInitialInput: Boolean = true  // Flag to check if it's the first input

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landscape_calculator)

        display = findViewById(R.id.text2)
        display.text = "0"
        result = findViewById(R.id.text1)

        // Initialize buttons and set click listeners
        setupButtons()

        // Restore state from Intent extras or savedInstanceState
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        } else {
            intent.extras?.let {
                restoreStateFromIntent(it)
            }
        }
    }

    private fun setupButtons() {
        val buttonActions = mapOf(
            R.id.ACBtn to ::clear,
            R.id.moduloBtn to { setOperator("%") },
            R.id.BackBtn to ::deleteLastChar,
            R.id.divideBtn to { setOperator("/") },
            R.id.multiplyBtn to { setOperator("*") },
            R.id.subtractBtn to { setOperator("-") },
            R.id.addBtn to { setOperator("+") },
            R.id.negativeBtn to ::toggleSign,
            R.id.zeroBtn to { appendText("0") },
            R.id.oneBtn to { appendText("1") },
            R.id.twoBtn to { appendText("2") },
            R.id.threeBtn to { appendText("3") },
            R.id.fourBtn to { appendText("4") },
            R.id.fiveBtn to { appendText("5") },
            R.id.sixBtn to { appendText("6") },
            R.id.sevenBtn to { appendText("7") },
            R.id.eightBtn to { appendText("8") },
            R.id.nineBtn to { appendText("9") },
            R.id.pointBtn to { appendText(".") },
            R.id.equalsBtn to ::calculateResult
        )

        buttonActions.forEach { (id, action) ->
            findViewById<Button>(id).setOnClickListener { action() }
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            // Check if the current input is a valid number
            val text = currentInput.toString()
            if (text.toDoubleOrNull() != null) {
                // Toggle the sign
                if (text.startsWith("-")) {
                    // If the number is negative, remove the leading '-'
                    currentInput.delete(0, 1)
                } else {
                    // If the number is positive, add a leading '-'
                    currentInput.insert(0, "-")
                }
                display.text = currentInput
            }
        }
    }

    private fun appendText(text: String) {
        if (text == "0" && (currentInput.isEmpty() || currentInput.toString() == "0")) {
            if (currentInput.isEmpty()) {
                currentInput.append(text)
            }
            return
        } else {
            if (currentInput.toString() == "0") {
                currentInput.clear()
            }
        }

        currentInput.append(text)
        display.text = currentInput
        isInitialInput = false
    }

    private fun clear() {
        currentInput.clear()
        display.text = "0"
        result.text = ""
        operand1 = null
        operator = null
        isInitialInput = true
    }

    private fun deleteLastChar() {
        if (currentInput.isNotEmpty()) {
            currentInput.deleteCharAt(currentInput.length - 1)
            if (currentInput.isEmpty()) {
                isInitialInput = true
            }
            display.text = currentInput
        }
    }

    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            operand1 = currentInput.toString().toDoubleOrNull()
            if (operand1 != null) {
                operator = op
                currentInput.append(" $operator ")
                display.text = currentInput
                isInitialInput = true
            }
        }
    }

    private fun calculateResult() {
        val parts = currentInput.split(" ")
        if (parts.size == 3) {
            val operand2 = parts[2].toDoubleOrNull()
            if (operand1 != null && operand2 != null && operator != null) {
                val resultValue = when (operator) {
                    "+" -> add(operand1!!, operand2)
                    "-" -> subtract(operand1!!, operand2)
                    "*" -> multiply(operand1!!, operand2)
                    "/" -> divide(operand1!!, operand2)
                    "%" -> modulo(operand1!!, operand2)
                    else -> return
                }
                result.text = display.text
                display.text = String.format("%.2f", resultValue)
                operand1 = resultValue
                operator = null
                currentInput.clear()
                currentInput.append(display.text)
                isInitialInput = true
            }
        }
    }

    private fun add(a: Double, b: Double) = a + b
    private fun subtract(a: Double, b: Double) = a - b
    private fun multiply(a: Double, b: Double) = a * b
    private fun divide(a: Double, b: Double) = if (b != 0.0) a / b else {
        display.text = ""
        0.00
    }

    private fun modulo(a: Double, b: Double) = if (b != 0.0) a % b else {
        display.text = ""
        0.00
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("display_text", display.text.toString())
        outState.putString("result_text", result.text.toString())
        outState.putString("current_input", currentInput.toString())
        outState.putDouble("operand1", operand1 ?: Double.NaN)
        outState.putString("operator", operator)
        outState.putBoolean("is_initial_input", isInitialInput)
    }

    private fun restoreState(savedInstanceState: Bundle) {
        display.text = savedInstanceState.getString("display_text", "0")
        result.text = savedInstanceState.getString("result_text", "")
        currentInput.clear()
        currentInput.append(savedInstanceState.getString("current_input", ""))
        operand1 = savedInstanceState.getDouble("operand1").takeIf { !it.isNaN() }
        operator = savedInstanceState.getString("operator")
        isInitialInput = savedInstanceState.getBoolean("is_initial_input", true)
    }

    private fun restoreStateFromIntent(extras: Bundle) {
        display.text = extras.getString("display_text", "0")
        result.text = extras.getString("result_text", "")
        currentInput.clear()
        currentInput.append(extras.getString("current_input", ""))
        operand1 = extras.getDouble("operand1").takeIf { !it.isNaN() }
        operator = extras.getString("operator")
        isInitialInput = extras.getBoolean("is_initial_input", true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Save state and switch to landscape activity
            val intent = Intent(this, LandscapeCalculator::class.java).apply {
                putExtra("display_text", display.text.toString())
                putExtra("result_text", result.text.toString())
                putExtra("current_input", currentInput.toString())
                putExtra("operand1", operand1 ?: Double.NaN)
                putExtra("operator", operator)
                putExtra("is_initial_input", isInitialInput)
            }
            startActivity(intent)
            finish() // Finish the portrait activity to prevent it from being in the background

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Save state and switch to portrait activity
            val intent = Intent(this, PortraitCalculator::class.java).apply {
                putExtra("display_text", display.text.toString())
                putExtra("result_text", result.text.toString())
                putExtra("current_input", currentInput.toString())
                putExtra("operand1", operand1 ?: Double.NaN)
                putExtra("operator", operator)
                putExtra("is_initial_input", isInitialInput)
            }
            startActivity(intent)
            finish() // Finish the landscape activity to prevent it from being in the background
        }
    }

}

