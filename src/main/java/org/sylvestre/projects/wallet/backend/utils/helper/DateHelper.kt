package org.sylvestre.projects.wallet.backend.utils.helper

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class DateHelper (date: Date = Date()) {

    companion object {
        val simpleDateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").apply { timeZone = TimeZone.getTimeZone(ZoneOffset.UTC) }

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy").apply { timeZone = TimeZone.getTimeZone(ZoneOffset.UTC) }

        val englishSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd").apply { timeZone = TimeZone.getTimeZone(ZoneOffset.UTC) }
    }

    var dayOfMonth: Int = 0

    var monthOfYear: Int = 0

    var year: Int = 0

    var dayOfWeek: Int = 0

    var hour: Int = 0

    var minute: Int = 0

    var second: Int = 0

    var monthName: String = ""

    var dateFormat: String = ""

    var weekOfMonth: Int = 0

    init {
        val localDateTime = Timestamp(date.time).toLocalDateTime().atZone(ZoneId.from(ZoneOffset.UTC))

        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(localDateTime.year, localDateTime.monthValue - 1, localDateTime.dayOfMonth)

        dayOfMonth = localDateTime.dayOfMonth
        monthOfYear = localDateTime.monthValue
        year = localDateTime.year
        dayOfWeek = localDateTime.dayOfWeek.value
        weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
        hour = localDateTime.hour
        minute = localDateTime.minute
        second = localDateTime.second
        monthName = localDateTime.month.name
        dateFormat = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    fun format(date: LocalDate?) = (if (date != null) simpleDateTimeFormat.format(date) else "-")!!

    fun format(date: Date?) = (if (date != null) simpleDateTimeFormat.format(date) else "-")!!

    fun formatWithoutTime(date: Date?) = (if (date != null) simpleDateFormat.format(date) else "-")!!

    fun dayOfWeekFrench(day:Int):String{
        when (day) {
            1 -> {
                return "Lundi"
            }
            2 -> {
                return  "Mardi"
            }
            3 -> {
                return "Mercedi"
            }
            4 -> {
                return "Jeudi"
            }
            5 -> {
                return "Vendredi"
            }
            6 -> {
                return "Samedi"
            }
            7 -> {
                return "Dimanche"
            }
            else -> {
                return ""
            }
        }
    }

    fun monthFrench(month:Int):String{
        return when(month){
            1-> "Janvier"
            2-> "Fevrier"
            3-> "Mars"
            4-> "Avril"
            5-> "Mai"
            6-> "Juin"
            7-> "Juillet"
            8-> "Août"
            9-> "Septembre"
            10-> "Octobre"
            11-> "Novembre"
            12-> "Décembre"
            else-> ""
        }

    }

    fun formatDateToYyyyMmDd( date : String ) : Date {
        val format = SimpleDateFormat( "yyyy-MM-dd" )

        return format.parse( date )
    }

    fun formatDateV2(date: String?) : Date {
//        val format = SimpleDateFormat( "dd-MM-yyyy" )
        val format = SimpleDateFormat( "MM/dd/yyyy" )

        return format.parse( date )
    }

    fun formatDateFromDB(date: String?) : Date {
        val format = SimpleDateFormat( "yyyy-MM-dd" )
        return format.parse( date )
    }

    fun determineDayInGivenMonth(currentDate: Date, month: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, month)

        return calendar.time
    }

    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getCurrentTimestamp(): String {
        val format = SimpleDateFormat( "HHmmssZ" )
        var currentTimestamp = format.format(Calendar.getInstance().time)
        currentTimestamp = currentTimestamp.replace("-", "")
        currentTimestamp = currentTimestamp.replace("+", "")

        return currentTimestamp
    }

    fun getDaysBetweenTwoDates(date1: Date, date2: Date): Long {
        val d1 = convertToLocalDateViaInstant(date1)
        val d2 = convertToLocalDateViaInstant(date2)

        return ChronoUnit.DAYS.between(d1, d2)
    }

    fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate? {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    fun listOfMounthFrench(): MutableList<String>{
        val list: MutableList<String> = mutableListOf()
        var i = 0
        while (i < 12){
            i++
            list.add(this.monthFrench(i).toUpperCase(Locale.FRENCH))
        }

        return list
    }

    fun listOfYears(): MutableList<Int> {
        val lesAnnees: MutableList<Int> = mutableListOf()
        val beginYear = year
        var i = 20
        var j = 0

        while (i > 0){
            lesAnnees.add(beginYear - i)
            i--
        }

        while (j <= 10){
            lesAnnees.add(beginYear + j)
            j++
        }

        return lesAnnees
    }
}