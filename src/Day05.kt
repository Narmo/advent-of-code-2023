import kotlin.math.abs

fun main() {
	fun part1(input: String): Long {
		val blocks = input.split(Regex("\n\n"))
		val source = blocks.first().split(":")[1].trim().split(" ").map { it.toLong() }.toTypedArray()

		blocks.drop(1).forEach { block ->
			block.split("\n").drop(1).forEach { line ->
				val (dstStart, srcStart, length) = line.split(" ").map { it.toLong() }

				for (src in source) {
					if (src < 0L) {
						continue
					}

					if (src in srcStart..(srcStart + length)) {
						val diff = src - srcStart
						val index = source.indexOf(src)
						source[index] = -(dstStart + diff)
					}
				}
			}

			source.forEachIndexed { index, l ->
				source[index] = abs(source[index])
			}
		}

		return source.minOrNull() ?: throw Exception("No min value found")
	}

	fun part2(input: String): Long {
		TODO()
	}

	val testInput = readInputPlain("Day05_test")
	check(part1(testInput) == 35L)
	// check(part2(testInput) == 46L)

	val input = readInputPlain("Day05")
	println(benchmark { part1(input) })
	// println(part2(input))
}
