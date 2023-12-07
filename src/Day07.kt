enum class Type(val power: Int) {
	FIVE_OF_A_KIND(6), FOUR_OF_A_KIND(5), FULL_HOUSE(4), THREE_OF_A_KIND(3), TWO_PAIR(2), ONE_PAIR(1), HIGH_CARD(0)
}

fun main() {
	val cards = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
	val jCards = cards.toMutableList().apply { remove('J'); add('J') }.toTypedArray()

	data class Hand(val hand: String, val bid: Long, private val useJoker: Boolean = false) : Comparable<Hand> {
		val type = run {
			val counts = hand.groupingBy { it }.eachCount().toMutableMap().apply {
				if (useJoker) {
					this.filter { it.key != 'J' }.maxByOrNull { it.value }?.let {
						this[it.key] = (this[it.key] ?: 0) + (this['J'] ?: 0)
					}

					this.remove('J')
				}
			}.toMap()

			when {
				counts.values.all { it == 5 } -> Type.FIVE_OF_A_KIND
				counts.values.any { it == 4 } -> Type.FOUR_OF_A_KIND
				counts.values.any { it == 3 } && counts.values.any { it == 2 } -> Type.FULL_HOUSE
				counts.values.any { it == 3 } -> Type.THREE_OF_A_KIND
				counts.values.count { it == 2 } == 2 -> Type.TWO_PAIR
				counts.values.count { it == 2 } == 1 -> Type.ONE_PAIR
				else -> Type.HIGH_CARD
			}
		}

		override fun compareTo(other: Hand): Int {
			if (type != other.type) {
				return type.power.compareTo(other.type.power)
			}

			hand.zip(other.hand).forEach {
				val (a, b) = it

				if (a != b) {
					val source = if (useJoker) jCards else cards
					return source.indexOf(b).compareTo(source.indexOf(a))
				}
			}

			return 0
		}
	}

	fun process(input: List<String>, useJoker: Boolean) = input.map {
		val (hand, bid) = it.split(" ")
		Hand(hand, bid.toLong(), useJoker)
	}.sortedDescending().let {
		it.foldIndexed(0L) { index, acc, hand ->
			acc + hand.bid * (it.size - index)
		}
	}

	fun part1(input: List<String>) = process(input, useJoker = false)

	fun part2(input: List<String>) = process(input, useJoker = true)

	val testInput = readInput("Day07_test")
	check(part1(testInput) == 6440L)
	check(part2(testInput) == 5905L)

	val input = readInput("Day07")
	println(benchmark { part1(input) })
	println(benchmark { part2(input) })
}
