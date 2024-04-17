package com.tirexmurina.shiftlabtest2024sp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

// Абстрактный базовый класс биндинга для фрагментов.
abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    // Приватное свойство для хранения биндинга к представлению.
    private var _binding: VB? = null

    // Публичное свойство для доступа к биндингу.
    val binding get() = _binding!!

    // Метод для создания представления фрагмента.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Создание биндинга к представлению.
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    // Абстрактный метод для инфлэйта представления биндинга.
    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    // Метод, вызываемый при уничтожении представления фрагмента.
    override fun onDestroyView() {
        super.onDestroyView()
        // Очистка биндинга.
        _binding = null
    }
}
