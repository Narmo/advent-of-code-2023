typealias Point = Pair<Int, Int>

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
	return this.first + other.first to this.second + other.second
}

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
	return this.first - other.first to this.second - other.second
}

fun main() {
	fun buildMap(input: List<String>): Pair<List<MutableList<Char>>, Point> {
		var start: Point? = null

		return input.mapIndexed { lineIndex, line ->
			line.mapIndexed { index, pipe ->
				if (pipe == 'S') {
					start = (lineIndex to index)
				}

				pipe
			}.toMutableList()
		} to (start ?: error("No start found"))
	}

	fun getAdjacentPoints(points: List<List<Char>>, row: Int, col: Int): List<Pair<Char?, Point?>> {        /*
		 -------------------
		 |     |     |     |
		 |     |-1,0 |     |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 | 0,-1| 0,0 | 0,1 |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 |     | 1,0 |     |
		 |     |     |     |
		 -------------------
		 */

		return listOf(Pair(0, -1), Pair(-1, 0), Pair(0, 1), Pair(1, 0)).map {
			runCatching {
				if ((row + it.first) in points.indices && (col + it.second) in points[row].indices) {
					if ((points[row + it.first][col + it.second]) == '.') {
						null
					}
					else {
						points[row + it.first][col + it.second] to (row + it.first to col + it.second)
					}
				}
				else {
					null to null
				}
			}.getOrNull() ?: (null to null)
		}
	}

	fun findLoop(input: List<String>) = run {
		val (map, start) = buildMap(input)

		/*
			| is a vertical pipe connecting north and south.
			- is a horizontal pipe connecting east and west.
			L is a 90-degree bend connecting north and east.
			J is a 90-degree bend connecting north and west.
			7 is a 90-degree bend connecting south and west.
			F is a 90-degree bend connecting south and east.
			. is ground; there is no pipe in this tile.
			S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
		 */

		val left = 0
		val top = 1
		val right = 2
		val bottom = 3

		val allowedDirections = mapOf(
				'S' to mapOf(
						left to listOf('-', 'L', 'F'),
						top to listOf('|', '7', 'F'),
						right to listOf('-', 'J', '7'),
						bottom to listOf('|', 'L', 'J'),
				),
				'|' to mapOf(
						top to listOf('|', '7', 'F'),
						bottom to listOf('|', 'L', 'J'),
				),
				'-' to mapOf(
						left to listOf('-', 'L', 'F'),
						right to listOf('-', 'J', '7'),
				),
				'L' to mapOf(
						right to listOf('-', '7', 'J'),
						top to listOf('|', '7', 'F'),
				),
				'J' to mapOf(
						left to listOf('-', 'L', 'F'),
						top to listOf('|', '7', 'F'),
				),
				'7' to mapOf(
						left to listOf('-', 'L', 'F'),
						bottom to listOf('|', 'L', 'J'),
				),
				'F' to mapOf(
						right to listOf('-', 'J', '7'),
						bottom to listOf('|', 'L', 'J'),
				),
		)

		var nextPoint = 'S' to start
		val path = mutableListOf(nextPoint)

		while (true) {
			val adj = getAdjacentPoints(map, nextPoint.second.first, nextPoint.second.second).mapIndexedNotNull { direction, pair ->
				val pipe = pair.first
				val location = pair.second

				if (pipe == null || location == null || pipe == 'S' || pipe == '.' || path.contains(pipe to location)) {
					return@mapIndexedNotNull null
				}

				val possibleDirections = allowedDirections[nextPoint.first]?.get(direction) ?: return@mapIndexedNotNull null

				if (pipe in possibleDirections) {
					return@mapIndexedNotNull pipe to location
				}

				null
			}

			if (adj.isEmpty()) {
				break
			}

			nextPoint = adj.firstOrNull {
				!path.contains(it)
			} ?: break

			if (nextPoint.second == start) {
				break
			}

			path.add(nextPoint)
		}

		map to path
	}

	fun part1(input: List<String>): Long {
		val (_, path) = findLoop(input)
		return path.size / 2L
	}

	fun part2(input: List<String>): Long {
		TODO()
	}

	val testInput1 = readInput("Day10_test1")
	val testInput2 = readInput("Day10_test2")
	check(part1(testInput1) == 4L)
	check(part1(testInput2) == 8L)

	// val testInput3 = readInput("Day10_test3")
	// val testInput4 = readInput("Day10_test4")
	// val testInput5 = readInput("Day10_test5")
	// check(part2(testInput3) == 4L)
	// check(part2(testInput4) == 8L)
	// check(part2(testInput5) == 10L)

	val input = readInput("Day10")
	println(benchmark { part1(input) })
	// println(benchmark { part2(input) })
}
