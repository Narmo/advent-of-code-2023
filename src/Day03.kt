fun main() {
	abstract class Entry {
		var line: Int = -1
		var firstIndex: Int = -1
	}

	class Part : Entry() {
		private var partNumber = ""
		var lastIndex: Int = -1

		val realPartNumber: Int
			get() = partNumber.toInt()

		fun appendPartNumber(number: Int) {
			partNumber += number.toString()
		}
	}

	class Symbol(val symbol: Char) : Entry() {
		val isGear: Boolean
			get() = symbol == '*'

		val adjacentParts = mutableListOf<Part>()
	}

	fun mapEngine(input: List<String>): List<List<Entry>> = input.mapIndexed { lineIndex, line ->
		val entries = mutableListOf<Entry>()
		var currentPart: Part? = null

		line.forEachIndexed { index, c ->
			if (c.isDigit()) {
				if (currentPart == null) {
					currentPart = Part()
					currentPart?.line = lineIndex
					currentPart?.firstIndex = index
				}

				currentPart?.appendPartNumber(c.toString().toInt())
			}
			else {
				currentPart?.let {
					it.lastIndex = index - 1
					entries.add(it)
					currentPart = null
				}

				if (c != '.') {
					val symbol = Symbol(c)
					symbol.line = lineIndex
					symbol.firstIndex = index

					entries.add(symbol)
				}
			}

			if (index == line.lastIndex) {
				currentPart?.let {
					it.lastIndex = index - 1
					entries.add(it)
					currentPart = null
				}
			}
		}

		entries.toList()
	}

	fun part1(input: List<String>): Int {
		val engine = mapEngine(input)

		return engine.flatMapIndexed { lineIndex, entries ->
			entries.filter { entry ->
				if (entry !is Part) {
					return@filter false
				}

				val prevLine = engine.getOrNull(lineIndex - 1) ?: emptyList()
				val nextLine = engine.getOrNull(lineIndex + 1) ?: emptyList()

				val adjTopBottom = (prevLine + nextLine).filter {
					(it is Symbol) && it.firstIndex >= entry.firstIndex - 1 && it.firstIndex <= entry.lastIndex + 1
				}.size

				val adjLeftRight = entries.filter {
					(it is Symbol) && (it.firstIndex == entry.firstIndex - 1 || it.firstIndex == entry.lastIndex + 1)
				}.size

				adjTopBottom + adjLeftRight > 0
			}
		}.sumOf {
			if (it is Part) {
				it.realPartNumber
			}
			else {
				0
			}
		}
	}

	fun part2(input: List<String>): Int {
		val engine = mapEngine(input)

		return engine.flatMapIndexed { lineIndex, entries ->
			entries.filter { entry ->
				if ((entry as? Symbol)?.isGear != true) {
					return@filter false
				}

				val prevLine = engine.getOrNull(lineIndex - 1)?.filterIsInstance<Part>() ?: emptyList()
				val nextLine = engine.getOrNull(lineIndex + 1)?.filterIsInstance<Part>() ?: emptyList()

				entry.adjacentParts.addAll((prevLine + nextLine).filter {
					it.lastIndex >= entry.firstIndex - 1 && it.firstIndex <= entry.firstIndex + 1
				})

				entry.adjacentParts.addAll(entries.filterIsInstance<Part>().filter {
					it.lastIndex == entry.firstIndex - 1 || it.firstIndex == entry.firstIndex + 1
				})

				return@filter entry.adjacentParts.size == 2
			}
		}.sumOf {
			(it as? Symbol)?.adjacentParts?.fold(1) { acc, entry ->
				(acc * entry.realPartNumber)
			}?.toInt() ?: 0
		}
	}

	val testInput = readInput("Day03_test")
	check(part1(testInput) == 4361)
	check(part2(testInput) == 467835)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}
