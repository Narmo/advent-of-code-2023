enum class Color {
	RED, GREEN, BLUE
}

data class Cube(val count: Int, val color: Color)
data class Game(val id: Int, val sets: List<List<Cube>>)

fun main() {
	fun maxCounts(sets: List<List<Cube>>): Map<Color, Int> {
		val counts = mutableMapOf<Color, Int>()

		sets.forEach { set ->
			set.forEach { cube ->
				counts[cube.color] = maxOf(counts[cube.color] ?: 0, cube.count)
			}
		}

		return counts.toMap()
	}

	fun inputToGames(input: List<String>): List<Game> = input.map {
		val (gameParams, cubeParams) = it.split(":")

		val game = gameParams.trim().split(" ").last().toInt()

		val cubes = cubeParams.split(";").map { set ->
			set.trim().split(",").map { cube ->
				val (count, color) = cube.trim().split(" ")
				Cube(count.toInt(), Color.valueOf(color.uppercase()))
			}
		}

		Game(game, cubes)
	}

	fun part1(input: List<String>): Int {
		val targetCounts = mapOf(Color.RED to 12, Color.GREEN to 13, Color.BLUE to 14)

		return inputToGames(input).filter { game ->
			maxCounts(game.sets).all { (color, count) ->
				count <= (targetCounts[color] ?: throw IllegalStateException("Unknown color $color"))
			}
		}.sumOf {
			it.id
		}
	}

	fun part2(input: List<String>): Int {
		return inputToGames(input).map {
			maxCounts(it.sets)
		}.sumOf {
			it.values.fold(1) { acc, count ->
				acc * count
			}.toInt()
		}
	}

	val testInput = readInput("Day02_test")
	check(part1(testInput) == 8)
	check(part2(testInput) == 2286)

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}
