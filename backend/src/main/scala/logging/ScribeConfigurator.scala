package logging

import scribe.Level
import scribe.file.FileWriter
import scribe.handler.{AsynchronousLogHandle, LogHandlerBuilder}

trait ScribeConfigurator {

  scribe.Logger.root
    .clearModifiers()
    .clearHandlers()
    .withMinimumLevel(Level.Debug)
    .withHandler(
      LogHandlerBuilder(
        writer = FileWriter()
      ).withLogHandle(
          AsynchronousLogHandle(1024)
      )
    ).replace()

}