package commands

import exceptions.NoSuchFileOrDirectoryException
import streams.FileStream
import streams.Stream
import java.io.FileNotFoundException
import java.util.logging.Logger

class CatCommand(ins: Stream, out: Stream, err: Stream, args: List<String>, kwargs: Map<String, String>) :
    Command(ins, out, err, args, kwargs) {
    private val logger = Logger.getLogger(CatCommand::class.java.name)
    override fun execute(): Int {
        logger.finest("Running cat with arguments $args")
        if (args.isNotEmpty()) {
            for (filename in args) {
                try {
                    val stream = FileStream(filename)
                    printToOutput(stream)
                } catch (e: FileNotFoundException) {
                    logger.warning(e.message)
                    throw NoSuchFileOrDirectoryException("cat", filename)
                }
            }
        } else {
            printToOutput(inputStream)
        }
        return 0
    }

    private fun printToOutput(stream: Stream) {
        var v = stream.read()
        while (v != null) {
            outputStream.writeLine(v)
            v = stream.read()
        }
    }
}