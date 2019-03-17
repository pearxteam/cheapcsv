package ru.pearx.cheapcsv

import java.io.PrintWriter
import java.lang.Appendable
import java.nio.file.Path
import kotlin.system.exitProcess

const val TLD_LIST_URL = "https://www.namecheap.com/domains/new-tlds/explore"
const val FILENAME_PREFIX = "namecheap_"
const val FILENAME_SUFFIX = ".csv"

data class Tld(val tldName: String, val whoIsGuard: Boolean, val price: Double, val fee: Boolean, val renewalPrice: Double)

fun readLine(message: String): String? {
    print(message)
    return readLine()
}

fun printAndExit(message: String) {
    System.err.println(message)
    exitProcess(-1)
}

fun Appendable.appendCsvValue(text: String) {
    if (text.toCharArray().any { it == ',' || it == '"' || it == '\r' || it == '\n' }) {
        append('"')
        append(text.replace("\"", "\"\""))
        append('"')
    }
    else
        append(text)
}

fun Appendable.appendCsvRow(list: List<String>) {
    var start = true
    for (text in list) {
        if (start) start = false
        else append(',')
        appendCsvValue(text)
    }
    appendln()
}

fun Appendable.appendCsvRow(vararg list: String) {
    appendCsvRow(list.toList())
}

fun printTldsToFile(outputDir: Path, name: String, tlds: Iterator<Tld>) {
    outputDir.resolve(FILENAME_PREFIX + name + FILENAME_SUFFIX).toFile().printWriter().use { writer ->
        with(writer) {
            appendCsvRow("Name", "WhoIs Guard", "Price ($)", "ICANN Fee", "Renewal Price ($)")
            for (tld in tlds) {
                kotlin.with(tld) {
                    appendCsvRow(tldName, if (whoIsGuard) "+" else "-", price.toString(), if (fee) "+" else "-", renewalPrice.toString())
                }
            }
        }
    }
}