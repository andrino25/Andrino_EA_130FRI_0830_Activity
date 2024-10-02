package com.capstone.bottomnavigation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.bottomnavigation.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Variables for calculator logic
    private val currentInput = StringBuilder()
    private var operand1: Double? = null
    private var operator: String? = null
    private var isInitialInput: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the calculator display and result
        binding.text2.text = "0"

        // Set up ImageButton listeners for calculator actions
        setupButtons()

        return root
    }

    private fun setupButtons() {
        val buttonActions = mapOf(
            binding.ACBtn to ::clear,  // Replace with your ImageButton IDs
            binding.moduloBtn to { appendText("%") },
            binding.BackBtn to ::deleteLastChar,
            binding.divideBtn to { setOperator("/") },
            binding.multiplyBtn to { setOperator("*") },
            binding.subtractBtn to { setOperator("-") },
            binding.addBtn to { setOperator("+") },
            binding.negativeBtn to ::toggleSign,
            binding.zeroBtn to { appendText("0") },
            binding.oneBtn to { appendText("1") },
            binding.twoBtn to { appendText("2") },
            binding.threeBtn to { appendText("3") },
            binding.fourBtn to { appendText("4") },
            binding.fiveBtn to { appendText("5") },
            binding.sixBtn to { appendText("6") },
            binding.sevenBtn to { appendText("7") },
            binding.eightBtn to { appendText("8") },
            binding.nineBtn to { appendText("9") },
            binding.pointBtn to { appendText(".") },
            binding.equalsBtn to ::calculateResult
        )

        // Set onClickListeners for all buttons
        buttonActions.forEach { (button, action) ->
            button.setOnClickListener { action() }
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            val text = currentInput.toString()
            if (text.toDoubleOrNull() != null) {
                if (text.startsWith("-")) {
                    currentInput.delete(0, 1)
                } else {
                    currentInput.insert(0, "-")
                }
                binding.text2.text = currentInput
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
        binding.text2.text = currentInput
        isInitialInput = false
    }

    private fun clear() {
        currentInput.clear()
        binding.text2.text = "0"
        binding.text1.text = ""
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
            binding.text2.text = currentInput
        }
    }

    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            operand1 = evaluatePercentage(currentInput.toString())
            if (operand1 != null) {
                operator = op
                currentInput.append(" $operator ")
                binding.text2.text = currentInput
                isInitialInput = true
            }
        }
    }

    private fun calculateResult() {
        val parts = currentInput.split(" ")
        if (parts.size == 3) {
            val operand2 = evaluatePercentage(parts[2])
            if (operand1 != null && operand2 != null && operator != null) {
                val resultValue = when (operator) {
                    "+" -> add(operand1!!, operand2)
                    "-" -> subtract(operand1!!, operand2)
                    "*" -> multiply(operand1!!, operand2)
                    "/" -> divide(operand1!!, operand2)
                    else -> return
                }
                binding.text1.text = binding.text2.text
                binding.text2.text = String.format("%.2f", resultValue)
                operand1 = resultValue
                operator = null
                currentInput.clear()
                currentInput.append(binding.text2.text)
                isInitialInput = true
            }
        }
    }

    private fun evaluatePercentage(value: String): Double? {
        val trimmedValue = value.trim()
        return if (trimmedValue.endsWith("%")) {
            val percentageValue = trimmedValue.removeSuffix("%").toDoubleOrNull()
            percentageValue?.div(100.0)
        } else {
            trimmedValue.toDoubleOrNull()
        }
    }

    private fun add(a: Double, b: Double) = a + b
    private fun subtract(a: Double, b: Double) = a - b
    private fun multiply(a: Double, b: Double) = a * b
    private fun divide(a: Double, b: Double) = if (b != 0.0) a / b else {
        binding.text2.text = ""
        0.00
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
