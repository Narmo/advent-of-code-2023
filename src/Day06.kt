fun main() {
	data class Race(val time: Long, val distance: Long)

	fun parseInput(input: List<String>, ignoreSpaces: Boolean = false): List<Race> {
		val times = input[0].split(":")[1].trim().split(Regex("\\s+")).map { it.trim().toLong() }
		val distances = input[1].split(":")[1].trim().split(Regex("\\s+")).map { it.trim().toLong() }

		return if (ignoreSpaces) {
			listOf(Race(times.joinToString(separator = "").toLong(), distances.joinToString(separator = "").toLong()))
		}
		else {
			times.zip(distances).map { Race(it.first, it.second) }
		}
	}

	fun calcWinWays(race: Race): List<Long> = (0..race.time).mapNotNull { hold ->
		(hold * (race.time - hold)).takeIf { it > race.distance }
	}

	fun part1(input: List<String>): Long {
		return parseInput(input).map {
			val wins = calcWinWays(it)
			wins.size.toLong()
		}.reduce { acc, i ->
			acc * i
		}
	}

	fun part2(input: List<String>): Long {
		return parseInput(input, ignoreSpaces = true).map {
			val wins = calcWinWays(it)
			wins.size.toLong()
		}.reduce { acc, i ->
			acc * i
		}
	}

	val testInput = readInput("Day06_test")
	check(part1(testInput) == 288L)
	check(part2(testInput) == 71503L)

	val input = readInput("Day06")
	println(benchmark { part1(input) })
	println(benchmark { part2(input) })
}
