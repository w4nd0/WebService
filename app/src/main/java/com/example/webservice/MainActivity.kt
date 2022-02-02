package com.example.webservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var CEP: EditText
    lateinit var PesquisarCEP: Button

    lateinit var RUA: EditText
    lateinit var CIDADE: EditText
    lateinit var UF: EditText
    lateinit var PesquisarRCE: Button

    lateinit var barraProgresso: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CEP = findViewById(R.id.cep)
        PesquisarCEP = findViewById(R.id.pesquisaCEP)

        RUA = findViewById(R.id.rua)
        CIDADE = findViewById(R.id.cidade)
        UF = findViewById(R.id.uf)
        PesquisarRCE = findViewById(R.id.pesquisaRCE)

        barraProgresso = findViewById(R.id.progress_bar)

        PesquisarCEP.setOnClickListener {

            barraProgresso.visibility = View.VISIBLE

            val call = RetrofitFactory().retrofitService().getCEP(CEP.text.toString())

            call.enqueue(object : Callback<CEP> {

                override fun onResponse(call: Call<CEP>, response: Response<CEP>) {

                    response.body()?.let {
                        Log.i("CEP", it.toString())
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                        barraProgresso.visibility = View.INVISIBLE
                    } ?: Toast.makeText(this@MainActivity, "CEP não localizado", Toast.LENGTH_LONG)
                        .show()

                }

                override fun onFailure(call: Call<CEP>?, t: Throwable?) {
                    t?.message?.let { it1 -> Log.e("Error", it1) }
                    barraProgresso.visibility = View.INVISIBLE
                }
            })
        }

        //-- Ao clicar no botão número 2
        //-- Será pesquisado o logradouro com os dados:
        //-- RUA, CIDADE e ENDEREÇO
        PesquisarRCE.setOnClickListener {

            barraProgresso.visibility = View.VISIBLE

            val call = RetrofitFactory().retrofitService().getRCE(
                UF.text.toString(),
                CIDADE.text.toString(),
                RUA.text.toString()
            )

            call.enqueue(object : Callback<List<CEP>> {

                override fun onResponse(call: Call<List<CEP>>?, response: Response<List<CEP>>?) {

                    response?.body()?.let {
                        Log.i("CEP", it.toString())
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                        barraProgresso.visibility = View.INVISIBLE
                    } ?: Toast.makeText(
                        this@MainActivity,
                        "Endereço não localizado ",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onFailure(call: Call<List<CEP>>?, t: Throwable?) {
                    t?.message?.let { it1 -> Log.e("Erro", it1) }
                    barraProgresso.visibility = View.INVISIBLE
                }
            })
        }
    }
}