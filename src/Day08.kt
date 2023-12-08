fun main() {
	fun parseInput(input: List<String>): Pair<String, Map<String, Pair<String, String>>> = input.first().trim() to input.drop(1).mapNotNull { line ->
		if (line.trim().isEmpty()) {
			null
		}
		else {
			val entry = line.trim().split("=")
			val key = entry[0].trim()

			val points = entry[1].trim().split(",").map {
				it.trim('(', ')', ' ')
			}

			key to (points[0] to points[1])
		}
	}.toMap()

	fun findSteps(startingPoint: Pair<String, String>, instructions: String, map: Map<String, Pair<String, String>>, bySuffix: Boolean = false): Long {
		var currentPoint = startingPoint
		var steps = 1L
		var index = 0

		while (true) {
			val key = when (instructions[index]) {
				'R' -> currentPoint.second
				'L' -> currentPoint.first
				else -> throw IllegalStateException("Invalid instruction")
			}

			if (bySuffix) {
				if (key.last() == 'Z') {
					break
				}
			}
			else {
				if (key == "ZZZ") {
					break
				}
			}

			val nextPoint = map[key] ?: throw IllegalStateException("No next point")

			steps += 1
			index += 1

			if (index > instructions.lastIndex) {
				index = 0
			}

			currentPoint = nextPoint
		}

		return steps
	}

	fun part1(input: List<String>): Long {
		val (instructions, map) = parseInput(input)
		val currentPoint = map["AAA"] ?: throw IllegalStateException("No AAA")
		return findSteps(currentPoint, instructions, map)
	}

	fun lcm(a: Long, b: Long): Long {
		val larger = if (a > b) a else b
		val maxLcm = a * b
		var lcm = larger

		while (lcm <= maxLcm) {
			if (lcm % a == 0L && lcm % b == 0L) {
				return lcm
			}

			lcm += larger
		}

		return maxLcm
	}

	fun lcm(vararg numbers: Long): Long {
		return numbers.reduce(::lcm)
	}

	fun part2(input: List<String>): Long {
		val (instructions, map) = parseInput(input)
		val startingPoints = map.filter { it.key.endsWith("A") }.values.toList()
		val steps = startingPoints.map { findSteps(it, instructions, map, true) }
		return lcm(*steps.toLongArray())
	}

	val testInput1 = readInput("Day08_test1")
	val testInput2 = readInput("Day08_test2")
	val testInput3 = readInput("Day08_test3")
	check(part1(testInput1) == 2L)
	check(part1(testInput2) == 6L)
	check(part2(testInput3) == 6L)

	val input = readInput("Day08")
	println(benchmark { part1(input) })
	println(benchmark { part2(input) })
}
