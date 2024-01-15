package org.sylvestre.projects.wallet.backend.utils

import org.sylvestre.projects.wallet.backend.utils.helper.DateHelper
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

object Utility {
    fun isLong(string: String): Boolean{
        try{ string.toLong() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotLong(string: String): Boolean{
        try{ string.toLong() }
        catch (e:Exception){ return true }
        return false
    }

    fun isDouble(string: String): Boolean{
        try{ string.toDouble() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotDouble(string: String): Boolean{
        try{ string.toDouble() }
        catch (e:Exception){ return true }
        return false
    }

    fun isInt(string: String): Boolean{
        try{ string.toInt() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotInt(string: String): Boolean{
        try{ string.toInt() }
        catch (e:Exception){ return true }
        return false
    }

    fun isFloat(string: String): Boolean{
        try{ string.toFloat() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotFloat(string: String): Boolean{
        try{ string.toFloat() }
        catch (e:Exception){ return true }
        return false
    }

    fun isBigDecimal(string: String): Boolean{
        try{ string.toBigDecimal() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotBigDecimal(string: String): Boolean{
        try{ string.toBigDecimal() }
        catch (e:Exception){ return true }
        return false
    }

    fun isBigInteger(string: String): Boolean{
        try{ string.toBigInteger() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotBigInteger(string: String): Boolean{
        try{ string.toBigInteger() }
        catch (e:Exception){ return true }
        return false
    }

    fun isShort(string: String): Boolean{
        try{ string.toShort() }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotShort(string: String): Boolean{
        try{ string.toShort() }
        catch (e:Exception){ return true }
        return false
    }

    fun isDigit(string: String): Boolean{
        if (isLong(string) || isBigDecimal(string) || isDouble(string) || isBigInteger(string) || isInt(string) || isFloat(string) || isShort(string)) return true
        return false
    }

    fun isNotDigit(string: String): Boolean{
        if (isNotLong(string) && isNotBigDecimal(string) && isNotDouble(string) && isNotBigInteger(string) && isNotInt(string) && isNotFloat(string) && isNotShort(string)) return true
        return false
    }

    fun isSplitedArray(string: String, regex: String): Boolean{
        if (regex.isNullOrEmpty()) return false
        else {
            try { string.split(regex.toRegex()) }
            catch (e:Exception){ return false }
            return true
        }
    }

    fun isNotSplitedArray(string: String, regex: String): Boolean{
        if (regex.isNullOrEmpty()) return true
        else {
            try { string.split(regex.toRegex()) }
            catch (e:Exception){ return true }
            return false
        }
    }

    fun isLocalDate(string: String): Boolean{
        try { LocalDate.parse(string) }
        catch (e:Exception){ return false }
        return true
    }

    fun isNotLocalDate(string: String): Boolean{
        try { LocalDate.parse(string) }
        catch (e:Exception){ return true }
        return false
    }

    fun isDate(string: String): Boolean{
        try { DateHelper.simpleDateFormat.parse(string) }
        catch (ex:Exception) { return false }
        return true
    }

    fun isNotDate(string: String): Boolean{
        try { DateHelper.simpleDateFormat.parse(string) }
        catch (ex:Exception) { return true }
        return false
    }

    fun isBoolean(string: String): Boolean{
        try { string.toBoolean() }
        catch (ex:Exception) { return false }
        return true
    }

    fun isNotBoolean(string: String): Boolean{
        try { string.toBoolean() }
        catch (ex:Exception) { return true }
        return false
    }

    fun toLong(string: String?): Long = if (string != null && isLong(string)) string.toLong() else -1L

    fun toDouble(string: String?): Double = if (string != null && isDouble(string)) string.toDouble() else 0.0

    fun toInt(string: String?): Int = if (string != null && isInt(string)) string.toInt() else 0

    fun toFloat(s: String?): Float = if (s != null && isFloat(s)) s.toFloat() else Float.MIN_VALUE

    fun toBigDecimal(s: String?): BigDecimal = if (s != null && isBigDecimal(s)) s.toBigDecimal() else BigDecimal.valueOf(0)

    fun toBigInteger(s: String?): BigInteger = if (s != null && isBigInteger(s)) s.toBigInteger() else BigInteger.valueOf(0)

    fun toShort(s: String?): Short = if (s != null && isShort(s)) s.toShort() else Short.MIN_VALUE

    fun split(s: String?, regex: String?): List<String> = if (s != null && regex != null && isSplitedArray(s, regex)) s.split(regex.toRegex()) else listOf()

    fun toLocalDate(s: String?): LocalDate = if (s != null && isLocalDate(s)) LocalDate.parse(s) else LocalDate.MIN

    fun toDate(s: String?): Date = if (s != null && isDate(s)) DateHelper().formatDateFromDB(s) else Date()

    fun isBeforeOrEquals(date_1: Date, date_2: Date): Boolean = date_1.before(date_2) || date_1 == date_2

    fun isAfterOrEquals(date_1: Date, date_2: Date): Boolean = date_1.after(date_2) || date_1 == date_2
}