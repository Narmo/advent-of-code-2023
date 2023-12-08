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

			source.forEachIndexed { index, _ ->
				source[index] = abs(source[index])
			}
		}

		return source.minOrNull() ?: throw Exception("No min value found")
	}

	fun part2(input: String): Long {
		val blocks = input.split(Regex("\n\n"))

		val sources = blocks.first().split(":")[1].trim().split(" ").map { it.toLong() }.windowed(size = 2, step = 2).map {
			LongRange(it[0], it[0] + it[1] - 1)
		}.toMutableList()

		blocks.drop(1).forEach { block ->
			val targets = mutableListOf<LongRange>()

			val ranges = block.split("\n").drop(1).map { line ->
				val (dstStart, srcStart, length) = line.split(" ").map { it.toLong() }
				LongRange(srcStart, srcStart + length - 1) to LongRange(dstStart, dstStart + length - 1)
			}

			while (sources.isNotEmpty()) {
				val nextSource = sources.removeFirst()
				var skip = false

				ranges.forEach { (src, dst) ->
					if (nextSource.first in src && nextSource.last in src) {
						val intersection = dst.first + (nextSource.first - src.first)..dst.last - (src.last - nextSource.last)
						targets.add(intersection)
						skip = true
					}
					else if (nextSource.first in src || nextSource.last in src) {
						val intersectionStart = maxOf(nextSource.first, src.first)
						val intersectionEnd = minOf(nextSource.last, src.last)
						val lowerSubRange = if (nextSource.first < src.first) nextSource.first until intersectionStart else null
						val upperSubRange = if (nextSource.last > src.last) (intersectionEnd + 1)..nextSource.last else null
						val intersection = dst.first - (src.first - intersectionStart)..dst.last - (src.last - intersectionEnd)

						targets.add(intersection)

						lowerSubRange?.let {
							sources.add(it)
						}

						upperSubRange?.let {
							sources.add(it)
						}

						skip = true
					}
				}

				if (!skip) {
					targets.add(nextSource)
				}
			}

			targets.removeDuplicates()

			sources.addAll(targets)
		}

		return sources.minOf { it.first }
	}

	val testInput = readInputPlain("Day05_test")
	check(part1(testInput) == 35L)
	check(part2(testInput) == 46L)

	val input = readInputPlain("Day05")
	println(benchmark { part1(input) })
	println(benchmark { part2(input) })
}
