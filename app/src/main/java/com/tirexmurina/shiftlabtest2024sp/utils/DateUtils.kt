package com.tirexmurina.shiftlabtest2024sp.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class DateUtils {
    fun isOver18YearsOld(dateString: String): Boolean {
        val today = Calendar.getInstance().time
        val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(dateString)
        // Создаем экземпляр Calendar для заданной даты
        val calendarBirth = Calendar.getInstance()
        if (date != null) {
            calendarBirth.time = date
        } else {
            throw DateException("date is corrupted")
        }
        // Добавляем 18 лет к дате рождения
        calendarBirth.add(Calendar.YEAR, 18)

        // Сравниваем полученную дату с текущей
        return calendarBirth.time.before(today)
    }
}