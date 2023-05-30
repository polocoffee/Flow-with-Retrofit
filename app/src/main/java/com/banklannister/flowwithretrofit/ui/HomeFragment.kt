package com.banklannister.flowwithretrofit.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.banklannister.flowwithretrofit.adapter.CryptoAdapter
import com.banklannister.flowwithretrofit.databinding.FragmentHomeBinding
import com.banklannister.flowwithretrofit.utils.DataStatus
import com.banklannister.flowwithretrofit.utils.initRecycler
import com.banklannister.flowwithretrofit.utils.isVisible
import com.banklannister.flowwithretrofit.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var cryptoAdapter: CryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        lifecycleScope.launch {
            binding.apply {
                viewModel.getCoinsList("eur")
                viewModel.coinsList.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DataStatus.Status.LOADING -> {
                            pBarLoading.isVisible(true, rvCrypto)
                        }

                        DataStatus.Status.SUCCESS -> {
                            pBarLoading.isVisible(false, rvCrypto)
                            cryptoAdapter.differ.submitList(it.data)
                            cryptoAdapter.setOnItemClickListener {
                                val direction =
                                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it.id)
                                findNavController().navigate(direction)
                            }
                        }

                        DataStatus.Status.ERROR -> {
                            pBarLoading.isVisible(true, rvCrypto)
                            Toast.makeText(
                                requireContext(),
                                "There is something wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvCrypto.initRecycler(LinearLayoutManager(requireContext()), cryptoAdapter)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}