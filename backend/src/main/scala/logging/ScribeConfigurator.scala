package logging

import scribe.Level
import scribe.file.FlushMode.AsynchronousFlush
import scribe.file._
import scribe.format.Formatter
import scribe.handler.{AsynchronousLogHandle, LogHandlerBuilder}
import scribe.output.format.{ANSIOutputFormat, ASCIIOutputFormat}
import scribe.writer.ConsoleWriter

trait ScribeConfigurator {

  scribe.Logger.root
    .clearModifiers()
    .clearHandlers()
    .withMinimumLevel(Level.Debug)
    .withHandler(
      LogHandlerBuilder(
        formatter = Formatter.strict,
        writer = FileWriter(
          flushMode = AsynchronousFlush()(concurrent.io.IOScheduler),
          pathBuilder = "logs" / (daily() % maxSize() % ".log")
        ),
        outputFormat = ASCIIOutputFormat,
        handle = AsynchronousLogHandle(1024),
        modifiers = Nil,
      )
    ).withHandler(
      LogHandlerBuilder(
        formatter = Formatter.enhanced,
        writer = ConsoleWriter,
        outputFormat = ANSIOutputFormat,
        handle = AsynchronousLogHandle(1024),
        modifiers = Nil,
      )
    ).replace()

}