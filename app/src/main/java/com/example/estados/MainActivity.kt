package com.example.estados

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.estados.databinding.ActivityMainBinding
import java.io.Serializable
import java.lang.Double.parseDouble
import kotlin.math.round

enum class Ops : Serializable {
    MAS, MENOS, MUL, DIV, AC
}

class MainActivity : AppCompatActivity(), OnClickListener {

    private var res: Double = 0.0
    private var mod: Double = 0.0
    private var op = Ops.AC
    private var modIsSetToZeroByUser: Boolean = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        op = when (savedInstanceState?.getString("op")) {
            Ops.MAS.toString() -> Ops.MAS
            Ops.MENOS.toString() -> Ops.MENOS
            Ops.MUL.toString() -> Ops.MUL
            Ops.DIV.toString() -> Ops.DIV
            else -> Ops.AC
        }
        res = savedInstanceState?.getDouble("res") ?: 0.0
        mod = savedInstanceState?.getDouble("mod") ?: 0.0
        modIsSetToZeroByUser = savedInstanceState?.getBoolean("modIsSetToZeroByUser") == true

        binding.txtNumero.text = if (op == Ops.AC || (mod == 0.0 && !modIsSetToZeroByUser))
            res.toString() else mod.toString()

        binding.boton00.setOnClickListener(this)
        binding.boton01.setOnClickListener(this)
        binding.boton02.setOnClickListener(this)
        binding.boton03.setOnClickListener(this)
        binding.boton04.setOnClickListener(this)
        binding.boton05.setOnClickListener(this)
        binding.boton06.setOnClickListener(this)
        binding.boton07.setOnClickListener(this)
        binding.boton08.setOnClickListener(this)
        binding.boton09.setOnClickListener(this)
        binding.botonAC.setOnClickListener(this)
        binding.botonEq.setOnClickListener(this)
        binding.botonMas.setOnClickListener(this)
        binding.botonMenos.setOnClickListener(this)
        binding.botonMul.setOnClickListener(this)
        binding.botonDiv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.boton00.id -> appendN(0.0)
            binding.boton01.id -> appendN(1.0)
            binding.boton02.id -> appendN(2.0)
            binding.boton03.id -> appendN(3.0)
            binding.boton04.id -> appendN(4.0)
            binding.boton05.id -> appendN(5.0)
            binding.boton06.id -> appendN(6.0)
            binding.boton07.id -> appendN(7.0)
            binding.boton08.id -> appendN(8.0)
            binding.boton09.id -> appendN(9.0)
            binding.botonMas.id -> chooseOp(Ops.MAS)
            binding.botonMenos.id -> chooseOp(Ops.MENOS)
            binding.botonMul.id -> chooseOp(Ops.MUL)
            binding.botonDiv.id -> chooseOp(Ops.DIV)
            binding.botonEq.id -> applyState()
            binding.botonAC.id -> {
                op = Ops.AC
                res = 0.0
                mod = 0.0
                modIsSetToZeroByUser = false
                binding.txtNumero.text = "0.0"
            }
            else -> throw IllegalStateException()
        }
    }

    private fun applyState() {
        when (op) {
            Ops.MAS -> res += mod
            Ops.MENOS -> res -= mod
            Ops.MUL -> res *= mod
            Ops.DIV -> {
                if (mod == 0.0) {
                    res = 0.0
                } else {
                    res /= mod
                }
            }
            else -> res
        }
        op = Ops.AC
        mod = 0.0
        modIsSetToZeroByUser = false
        binding.txtNumero.text = res.toString()
    }

    private fun appendN(n: Double) {
        if (op == Ops.AC) {
            if (round(res) == res) {
                res = res * 10 + if (res >= 0) n else -n
            } else {
                res = parseDouble(res.toString() + n.toString().substringBefore('.'))
                // BUG: con números decimales muy grandes, n se añade al exponente de la notación científica
            }
            binding.txtNumero.text = res.toString()
        } else {
            mod = mod * 10 + n // mod nunca puede ser negativo o decimal
            modIsSetToZeroByUser = mod == 0.0
            binding.txtNumero.text = mod.toString()
        }
    }

    private fun chooseOp(newOp: Ops) {
        if (!(op == Ops.AC || (mod == 0.0 && !modIsSetToZeroByUser))) {
            applyState()
        }
        op = newOp
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("res", res)
        outState.putDouble("mod", mod)
        outState.putString("op", op.toString())
        outState.putBoolean("modIsSetToZeroByUser", modIsSetToZeroByUser)
    }
}