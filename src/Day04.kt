fun main() {
	data class Card(val id: Int, val winningNumbers: Set<Int>, val availableNumbers: List<Int>) {
		val wins: List<Int>
			get() = availableNumbers.filter { it in winningNumbers }

		val score: Int
			get() = wins.takeIf {
				it.isNotEmpty()
			}?.drop(1)?.fold(1) { acc, _ ->
				acc * 2
			} ?: 0

		var isCopy = false
	}

	fun parseInput(input: List<String>): List<Card> = input.map {
		val parts = it.trim().split(":")
		val id = parts[0].trim().split(" ").last().toInt()

		val (wins, available) = parts[1].trim().split("|").map { numbers ->
			numbers.trim().split(Regex("\\s+")).map { value ->
				value.trim().toInt()
			}
		}

		Card(id, wins.toSet(), available)
	}

	fun part1(input: List<String>): Int {
		return parseInput(input).sumOf { it.score }
	}

	fun part2(input: List<String>): Int {
		val originalCards = parseInput(input)
		val resultingPile = mutableListOf<Card>()

		fun collectWinningCards(cards: List<Card>) {
			for (card in cards) {
				val wins = card.wins

				if (!card.isCopy) {
					resultingPile.add(card)
				}

				if (wins.isNotEmpty()) {
					val awards = originalCards.subList(card.id, card.id + wins.size).map { it.copy().apply { isCopy = true } }
					resultingPile.addAll(awards)
					collectWinningCards(awards)
				}
			}
		}

		collectWinningCards(originalCards)

		return resultingPile.size
	}

	val testInput = readInput("Day04_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 30)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}
