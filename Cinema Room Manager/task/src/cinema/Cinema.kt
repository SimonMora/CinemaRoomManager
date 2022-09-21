package cinema

import java.lang.Exception

const val FIRST_HALF_PRICE: Int = 10
const val SECOND_HALF_PRICE: Int = 8
const val MAJOR_QUANTITY_SEATS: Int = 60
const val ZERO: Int = 0
const val MAXIMUM: Int = 9

fun main() {
    //List of variables to use in the code
    var rows: Int
    var seats: Int
    val cinemaRoom: MutableList<MutableList<String>> = mutableListOf()
    var userAction: Int = -1

    // Ask the number of rows and seats to build the cinema room
    do {
        try{
            println("Enter the number of rows:")
            rows = readln().toInt()

            println("Enter the number of seats in each row:")
            seats = readln().toInt()

            if(seats > MAXIMUM || rows > MAXIMUM){
                println("Number of seats or rows can't be greater than 9.")
            }
        } catch (e: NumberFormatException ) {
            println("Rows and seats should be numeric type")
            rows = -1
            seats = -1
        }
    } while(rows <= ZERO || seats <= ZERO || seats > MAXIMUM || rows > MAXIMUM)

    // Fill the cinema room with multi-dimensional list logic
    for(i in ZERO.rangeTo(rows)) {
        var list: MutableList<String>

        if (i == ZERO) {
            list = MutableList(seats +1) { it.toString() }
            list[0] = " "
        } else {
            list = MutableList(seats) { "S" }
            list.add(ZERO, i.toString())
        }

        cinemaRoom.add(list)
    }

    // Ask for user input into action to take
    while(userAction != 0) {
        try{
            println()
            println("1. Show the seats")
            println("2. Buy a ticket ")
            println("3. Statistics")
            println("0. Exit")
            userAction = readln().toInt()

            when (userAction) {
                1 -> printCinemaRoom(cinemaRoom)
                2 -> choseSeat(rows, seats, cinemaRoom)
                3 -> showStatistics(cinemaRoom)
                0 -> ""
                else -> print("Not right number for choice.")
            }

        } catch (e: NumberFormatException ) {
            println("Rows and seats should be numeric type")
            userAction = -1
        }
    }

}

fun showStatistics(cinemaRoom: MutableList<MutableList<String>>) {
    val purchasedTickets = cinemaRoom.map { list -> list.filter { it == "B" }.count() }.sum()
    val rows = cinemaRoom.size - 1
    val seats = cinemaRoom[0].size - 1

    println()
    println("Number of purchased tickets: $purchasedTickets")
    println("Percentage: ${"%.2f".format(calculateOccupationPercentage(rows, seats, purchasedTickets))}%")
    println("Current income: $${calculateCurrentIncome(rows, seats, cinemaRoom)}")
    println("Total income: $${calculateTotalIncome(seats, rows)}")
}

fun calculateCurrentIncome(rows: Int, seats: Int, cinemaRoom: MutableList<MutableList<String>>): Int {
    return cinemaRoom.sumOf { list ->
        list.sumOf {
            run {
                if (it == "B") calculateSeatPrice(
                    rows,
                    seats,
                    cinemaRoom.indexOf(list)
                ) else 0
            }
        }
    }
}

fun calculateOccupationPercentage(rows: Int, seats: Int, purchasedTickets: Int): Double {
    return purchasedTickets.toDouble() * 100 / (rows.toDouble() * seats.toDouble())
}

fun choseSeat(rows: Int, seats: Int, cinemaRoom: MutableList<MutableList<String>>) {
    var rowSelected: Int
    var seatSelected: Int

    // Ask the row and the seat to select by the user
    do {
        try{
            println()
            println("Enter a row number:")
            rowSelected = readln().toInt()

            println("Enter a seat number in that row:")
            seatSelected = readln().toInt()

            if (seatSelected > seats || rowSelected > rows) {
                throw Exception("Wrong input!")
            }

            if (cinemaRoom[rowSelected][seatSelected] == "B") {
                throw Exception("That ticket has already been purchased!")
            }
        } catch (e: NumberFormatException ) {
            println("Rows and seats should be numeric type")
            rowSelected = -1
            seatSelected = -1
        } catch (e: Exception) {
            println(e.message)
            rowSelected = -1
            seatSelected = -1
        }
    } while(rowSelected <= ZERO || seatSelected <= ZERO || seatSelected > seats || rowSelected > rows)

    // Select the seat in the cinema
    cinemaRoom[rowSelected][seatSelected] = "B"

    // Print the ticket price
    println("Ticket price: $${calculateSeatPrice(seats, rows, rowSelected)}")
}

fun printCinemaRoom(cinemaRoom: MutableList<MutableList<String>>): Unit {
    println()
    println("Cinema:")
    cinemaRoom.map {
            list -> println(list.joinToString(" "))
    }
}

fun calculateSeatPrice(seats: Int, rows: Int, rowSelected: Int): Int {
    return if (seats * rows <= MAJOR_QUANTITY_SEATS) {
        FIRST_HALF_PRICE
    } else {
        if ((rows - (rows/2)) > rowSelected) {
            FIRST_HALF_PRICE
        } else {
            SECOND_HALF_PRICE
        }
    }
}

fun calculateTotalIncome(rows: Int, seats: Int): Int {
    return if (rows * seats <= MAJOR_QUANTITY_SEATS) {
        rows * seats * FIRST_HALF_PRICE
    } else {
        seats * (rows / 2) * FIRST_HALF_PRICE + seats * (rows - rows / 2) * SECOND_HALF_PRICE
    }
}