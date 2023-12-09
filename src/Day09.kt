fun main() {
	fun part1(input: List<String>) = input.sumOf { line ->
		line.split("\\s+".toRegex()).map { it.toLong() }.let { sequence ->
			val sequences = mutableListOf<List<Long>>()
			sequences.add(sequence)

			var currentSequence = sequence

			while (!currentSequence.all { it == 0L }) {
				currentSequence = currentSequence.windowed(size = 2, step = 1, partialWindows = false).map {
					it[1] - it[0]
				}

				sequences.add(currentSequence)
			}

			var last = sequences.last().last()

			for (s in sequences.reversed()) {
				last += s.last()
			}

			last
		}
	}

	fun part2(input: List<String>) = input.sumOf { line ->
		line.split("\\s+".toRegex()).map { it.toLong() }.let { sequence ->
			val sequences = mutableListOf<List<Long>>()
			sequences.add(sequence)

			var currentSequence = sequence

			while (!currentSequence.all { it == 0L }) {
				currentSequence = currentSequence.windowed(size = 2, step = 1, partialWindows = false).map {
					it[1] - it[0]
				}

				sequences.add(currentSequence)
			}

			var first = sequences.last().first()

			for (s in sequences.reversed()) {
				first = s.first() - first
			}

			first
		}
	}

	val testInput = readInput("Day09_test")
	check(part1(testInput) == 114L)
	check(part2(testInput) == 2L)

	val input = readInput("Day09")
	println(benchmark { part1(input) })
	println(benchmark { part2(input) })
}
