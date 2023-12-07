fun main() {
    val labels = charArrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val labels2 = charArrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')


    fun getCardMoneys(input: List<String>): List<CardMoney> {
        return input
            .map {
                val (card, money) = it.split(" ")

                CardMoney(card, money.toInt())
            }
    }

    fun getStrength(card: String): Int {
        val groups = card
            .groupBy { it }
            .values
            .sortedBy(List<Char>::size)

        return when {
            groups.size == 1 -> 7
            groups.size == 2 && groups.first().size == 1 -> 6
            groups.size == 2 && groups.first().size == 2 -> 5
            groups.size == 3 && groups.first().size == 1 && groups[1].size == 1 -> 4
            groups.size == 3 && groups.first().size == 1 && groups[1].size == 2 -> 3
            groups.size == 4 && groups.first().size == 1 && groups.last().size == 2 -> 2
            else -> 1
        }
    }

    fun part1(input: List<String>): Int {
        return getCardMoneys(input)
            .sortedWith { c1, c2 ->
                val strength1 = getStrength(c1.card)
                val strength2 = getStrength(c2.card)

                if (strength1 != strength2) return@sortedWith strength1.compareTo(strength2)

                c1.card
                    .zip(c2.card)
                    .forEach {
                        val strength1 = labels.indexOf(it.first)
                        val strength2 = labels.indexOf(it.second)

                        if (strength1 != strength2) return@sortedWith strength2.compareTo(strength1)
                    }

                0
            }.mapIndexed { index, card ->
                card.money * index.plus(1)
            }.sum()
    }

    fun makeStronger(card: String): String {
        val toMakeStronger = card
            .filter { it != 'J' }
            .groupBy { it }
            .maxByOrNull { it.value.size }
            ?.key

        return toMakeStronger?.let { card.replace('J', it) } ?: card
    }

    fun part2(input: List<String>): Int {
        return getCardMoneys(input)
            .sortedWith { c1, c2 ->
                val actualCard1 = c1.card
                val actualCard2 = c2.card

                val newCard1 = makeStronger(actualCard1)
                val newCard2 = makeStronger(actualCard2)

                val strength1 = getStrength(newCard1)
                val strength2 = getStrength(newCard2)

                if (strength1 != strength2) return@sortedWith strength1.compareTo(strength2)

                actualCard1
                    .zip(actualCard2)
                    .forEach {
                        val strength1 = labels2.indexOf(it.first)
                        val strength2 = labels2.indexOf(it.second)

                        if (strength1 != strength2) return@sortedWith strength2.compareTo(strength1)
                    }

                0
            }.mapIndexed { index, card ->
                card.money * index.plus(1)
            }.sum()
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class CardMoney(
    val card: String,
    val money: Int
)