package com.cm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.databinding.ActivityMainBinding
import com.cm.model.Point


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private var isRunning = false

    private val listPoint = ArrayList<Point>()

    private lateinit var adapter: ItemAdapter



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        setupRecyclerView()

        setupButtons()

    }



    private fun setupRecyclerView() {


        adapter = ItemAdapter(listPoint)


        binding.rvPoint.layoutManager =
            LinearLayoutManager(this)


        binding.rvPoint.adapter = adapter

    }



    private fun setupButtons() {


        // Tombol SIMPAN PENGATURAN

        binding.btnAddPoint.setOnClickListener {


            val min =
                binding.etMin.text.toString()
                    .toIntOrNull() ?: 0


            val max =
                binding.etMax.text.toString()
                    .toIntOrNull() ?: 0


            val jarak =
                binding.etJarak.text.toString()
                    .toIntOrNull() ?: 0



            if (min == 0 || max == 0) {


                Toast.makeText(
                    this,
                    "Isi Harga Minimal & Maksimal",
                    Toast.LENGTH_SHORT
                ).show()


                return@setOnClickListener

            }



            val point =
                Point(
                    min,
                    max,
                    jarak
                )


            listPoint.add(point)


            adapter.notifyItemInserted(
                listPoint.size - 1
            )



            binding.etMin.text.clear()

            binding.etMax.text.clear()

            binding.etJarak.text.clear()



            Toast.makeText(
                this,
                "Pengaturan Tersimpan!",
                Toast.LENGTH_SHORT
            ).show()

        }



        // Tombol START / STOP

        binding.btnStart.setOnClickListener {


            isRunning = !isRunning



            if (isRunning) {


                binding.tvStatus.text =
                    "RUNNING"


                binding.tvStatus.setTextColor(
                    getColor(android.R.color.holo_green_light)
                )


                binding.btnStart.text =
                    " STOP"



                binding.btnStart.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.ic_media_pause,
                    0,
                    0,
                    0
                )



            } else {


                binding.tvStatus.text =
                    "STOP"



                binding.tvStatus.setTextColor(
                    getColor(android.R.color.holo_red_light)
                )



                binding.btnStart.text =
                    " MULAI"



                binding.btnStart.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.ic_media_play,
                    0,
                    0,
                    0
                )

            }


        }


    }


}
