import java.util.*

fun main() {

    fun mapToGates(input: List<String>): List<Gate> {
        return input.mapNotNull {
            val name = it
                .substring(0, it.indexOf("->"))
                .trim()
            val outputNames = it
                .substringAfter("->")
                .split(",")
                .map { it.trim() }

            when {
                it.startsWith('&') -> {
                    Gate.Conjunction(name = name.drop(1), outPuts = outputNames)
                }

                it.startsWith('%') -> {
                    Gate.FlipFlop(name = name.drop(1), outPuts = outputNames)
                }

                it.startsWith("broadcaster") -> {
                    Gate.Broadcaster(name = name, outPuts = outputNames)
                }

                else -> null
            }
        }
    }

    fun start2(allGates: List<Gate>): Long {
        val firstGate: Gate =
            (allGates.first { it is Gate.Broadcaster } as Gate.Broadcaster).copy(inputPulse = Pulse.LOW)

        val gateSet = allGates.associateBy { it.name }.toMutableMap()

        allGates
            .map {
                it to it.outPuts.mapNotNull { name ->
                    allGates.firstOrNull { it.name == name && it is Gate.Conjunction }
                }
            }
            .filter {
                it.second.isNotEmpty()
            }
            .forEach { pair ->
                pair.second.forEach {
                    (gateSet[it.name]!! as Gate.Conjunction).receivedInputs[pair.first.name] = Pulse.LOW
                }
            }

        val finishGates = mutableMapOf(
            "sj" to 0,
            "qq" to 0,
            "ls" to 0,
            "bg" to 0
        )

        var count = 0

        while(finishGates.values.any { it == 0 }) {
            count ++

            val queue: Queue<Gate> = LinkedList()
            queue.add(firstGate)

            while (queue.isNotEmpty()) {
                val gate = queue.poll()

                val inputPulse = gate.inputPulse ?: continue

                val foundGate = gateSet[gate.name] ?: continue

                val newOutput: Pulse?

                when (gate) {
                    is Gate.Broadcaster -> newOutput = gate.inputPulse

                    is Gate.Conjunction -> {
                        val fromGateName = gate.fromGateName ?: continue
                        foundGate as Gate.Conjunction
                        foundGate.receivedInputs[fromGateName] = inputPulse
                        val isAllHigh = foundGate.receivedInputs.values.all { it == Pulse.HIGH }

                        newOutput = if (isAllHigh) Pulse.LOW else Pulse.HIGH
                    }

                    is Gate.FlipFlop -> {
                        if (inputPulse == Pulse.HIGH) continue

                        foundGate as Gate.FlipFlop

                        gateSet[gate.name] = foundGate.copy(isOn = !gate.isOn)

                        newOutput = if (foundGate.isOn) Pulse.LOW else Pulse.HIGH
                    }

                    is Gate.Output -> continue
                }

                if(finishGates.keys.contains(gate.name) && finishGates[gate.name] == 0 && newOutput == Pulse.HIGH) {
                    finishGates[gate.name] = count
                }

                gate.outPuts
                    .map { name ->
                        gateSet[name]
                    }
                    .map {
                        when (it) {
                            is Gate.Broadcaster -> it.copy(inputPulse = newOutput, fromGateName = gate.name)
                            is Gate.Conjunction -> it.copy(inputPulse = newOutput, fromGateName = gate.name)
                            is Gate.FlipFlop -> it.copy(inputPulse = newOutput, fromGateName = gate.name)
                            else -> Gate.Output(inputPulse = newOutput, fromGateName = gate.name)
                        }
                    }.forEach(queue::add)
            }
        }

        return finishGates
            .values
            .map { it.toLong() }
            .fold(1L, ::calculateLCM)
    }

    fun start(allGates: List<Gate>, times: Int = 1): List<Pair<Pulse, Int>> {
        val firstGate: Gate =
            (allGates.first { it is Gate.Broadcaster } as Gate.Broadcaster).copy(inputPulse = Pulse.LOW)

        val gateSet = allGates.associateBy { it.name }.toMutableMap()
        val pulseCounts = HashMap<Pulse, Int>()

        allGates
            .map {
                it to it.outPuts.mapNotNull { name ->
                    allGates.firstOrNull { it.name == name && it is Gate.Conjunction }
                }
            }
            .filter {
                it.second.isNotEmpty()
            }
            .forEach { pair ->
                pair.second.forEach {
                    (gateSet[it.name]!! as Gate.Conjunction).receivedInputs[pair.first.name] = Pulse.LOW
                }
            }

        repeat(times) {

            val queue: Queue<Gate> = LinkedList()
            queue.add(firstGate)

            while (queue.isNotEmpty()) {
                val gate = queue.poll()

//                println("${gate.fromGateName} -> ${gate.inputPulse} -> ${gate.name}")

                val inputPulse = gate.inputPulse ?: continue

                pulseCounts[inputPulse] = (pulseCounts[inputPulse] ?: 0) + 1

                val foundGate = gateSet[gate.name] ?: continue

                val newOutput: Pulse?

                when (gate) {
                    is Gate.Broadcaster -> newOutput = gate.inputPulse

                    is Gate.Conjunction -> {
                        val fromGateName = gate.fromGateName ?: continue
                        foundGate as Gate.Conjunction
                        foundGate.receivedInputs[fromGateName] = inputPulse
                        val isAllHigh = foundGate.receivedInputs.values.all { it == Pulse.HIGH }

                        newOutput = if (isAllHigh) Pulse.LOW else Pulse.HIGH
                    }

                    is Gate.FlipFlop -> {
                        if (inputPulse == Pulse.HIGH) continue

                        foundGate as Gate.FlipFlop

                        gateSet[gate.name] = foundGate.copy(isOn = !gate.isOn)

                        newOutput = if (foundGate.isOn) Pulse.LOW else Pulse.HIGH
                    }

                    is Gate.Output -> continue
                }

                gate.outPuts
                    .map { name ->
                        gateSet[name]
                    }
                    .map {
                        when (it) {
                            is Gate.Broadcaster -> it.copy(inputPulse = newOutput, fromGateName = gate.name)
                            is Gate.Conjunction -> it.copy(inputPulse = newOutput, fromGateName = gate.name)
                            is Gate.FlipFlop -> it.copy(inputPulse = newOutput, fromGateName = gate.name)
                            else -> Gate.Output(inputPulse = newOutput, fromGateName = gate.name)
                        }
                    }.forEach(queue::add)
            }
        }

        return Pulse.entries.map { it to (pulseCounts[it] ?: 0) }
    }

    fun part1(input: List<String>): Long {
        val outputs = start(mapToGates(input), 1000)

        return outputs.map { it.second.toLong() }.fold(1L, Long::times)
    }

    fun part2(input: List<String>): Long {
        return start2(mapToGates(input))
    }

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}

private enum class Pulse {
    LOW, HIGH
}

private sealed interface Gate {
    val fromGateName: String?
    val inputPulse: Pulse?
    val name: String
    val outPuts: List<String>

    data class FlipFlop(
        override val fromGateName: String? = null,
        override val inputPulse: Pulse? = null,
        override val name: String,
        override val outPuts: List<String> = emptyList(),
        val isOn: Boolean = false
    ) : Gate

    data class Broadcaster(
        override val fromGateName: String? = null,
        override val inputPulse: Pulse? = null,
        override val name: String,
        override val outPuts: List<String> = emptyList()
    ) : Gate

    data class Conjunction(
        override val fromGateName: String? = null,
        override val inputPulse: Pulse? = null,
        override val name: String,
        override val outPuts: List<String> = emptyList(),
        val receivedInputs: HashMap<String, Pulse> = HashMap()
    ) : Gate

    data class Output(
        override val fromGateName: String? = null,
        override val inputPulse: Pulse?,
        override val name: String = "output",
        override val outPuts: List<String> = emptyList()
    ) : Gate
}