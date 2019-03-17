package ru.pearx.cheapcsv

import org.jsoup.Jsoup
import java.io.File
import java.nio.file.Paths

fun main(vararg args: String) {
    val outputFileString = args.getOrNull(0) ?: readLine("Enter the output file path: ")
    if(outputFileString == null)
        printAndExit("Error: the output file path isn't specified")
    else {
        val outputFile = File(outputFileString)
        println("[1/3] Getting the TLD list from $TLD_LIST_URL...")
        val doc = Jsoup.connect(TLD_LIST_URL).get()

        println("[2/3] Parsing the HTML...")
        val tables = doc.getElementById("pricing").getElementsByTag("table")
        val tldList = mutableListOf<Tld>()
        for (table in tables) {
            val type = table.getElementsByTag("thead").first().child(0).child(0).text()
            for (row in table.getElementsByTag("tbody").first().children()) { // tbody/*
                val name = row.child(0).child(0).text() // th/a.text

                val whoIsGuard = row.child(1).child(0).text() == "yes"

                val amountTag = row.getElementsByClass("amount").first()
                val price = amountTag.child(1).text().toDouble()
                val fee = amountTag.children().size > 2

                val renewalPrice = row.getElementsByClass("renewal-price").first().child(0).text().toDouble()

                tldList.add(Tld(name, whoIsGuard, price, fee, renewalPrice, type))
            }
        }

        println("[3/3] Writing the CSV files...")
        outputFile.printWriter().use { writer ->
            with(writer) {
                appendCsvRow("Name", "WhoIs Guard", "Price ($)", "ICANN Fee", "Renewal Price ($), Type")
                for (tld in tldList) {
                    with(tld) {
                        appendCsvRow(tldName, if (whoIsGuard) "+" else "-", price.toString(), if (fee) "+" else "-", renewalPrice.toString(), type)
                    }
                }
            }
        }
    }
}

