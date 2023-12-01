fun main() {
	val spelled = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)

	fun part1(input: List<String>): Int {
		return input.sumOf {
			val digits = it.filter { c -> c.isDigit() }
			(digits.first().toString() + digits.last().toString()).toInt()
		}
	}

	fun part2(input: List<String>): Int {
		return input.sumOf { line ->
			val digits = mutableListOf<Pair<Int, Int>>()

			line.forEachIndexed { index, c ->
				if (c.isDigit()) {
					digits.add(index to c.toString().toInt())
				}
			}

			spelled.mapNotNull { mapping ->
				var index = line.indexOf(mapping.key)

				while (index != -1) {
					digits.add(index to mapping.value)
					index = line.indexOf(mapping.key, index + 1)
				}
			}

			digits.sortBy { it.first }

			(digits.first().second.toString() + digits.last().second.toString()).toInt()
		}
	}

	val testInput1 = readInput("Day01_test1")
	check(part1(testInput1) == 142)

	val testInput2 = readInput("Day01_test2")
	check(part2(testInput2) == 281)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}
