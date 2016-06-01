import com.typesafe.config.ConfigFactory
import play.api.{ApplicationLoader, Configuration, Logger, Play}
import play.api.Mode._
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceApplicationLoader}

class CustomApplicationLoader extends GuiceApplicationLoader {
  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
    val builder = initialBuilder.in(context.environment).overrides(overrides(context): _*)

    context.environment.mode match {
      case Prod =>
        // start mode
        Logger.info("Starting Prod mode")
        val prodConf = Configuration(ConfigFactory.load("application.prod.conf"))
        builder.loadConfig(prodConf)
      case Dev =>
        // run mode
        Logger.info("Starting Dev mode")
//        val devConf = Configuration(ConfigFactory.load("application.dev.conf"))
//        builder.loadConfig(devConf)
        builder
    }
  }
}