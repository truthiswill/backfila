package com.squareup.backfila.service

import com.squareup.backfila.dashboard.DashboardModule
import com.squareup.skim.SkimModule
import misk.MiskCaller
import misk.config.ConfigModule
import misk.environment.Environment
import misk.environment.EnvironmentModule
import misk.inject.KAbstractModule
import misk.security.authz.AccessAnnotationEntry
import misk.security.authz.DevelopmentOnly
import misk.web.metadata.AdminDashboardAccess

class BackfilaServiceModule(
  private val environment: Environment,
  private val config: BackfilaConfig
) : KAbstractModule() {
  override fun configure() {
    install(ConfigModule.create("backfila", config))
    install(EnvironmentModule(environment))
    install(SkimModule(environment, config.skim))
    install(DashboardModule())
    multibind<AccessAnnotationEntry>().toInstance(
        AccessAnnotationEntry<AdminDashboardAccess>(roles = listOf("eng")))
    bind<MiskCaller>().annotatedWith<DevelopmentOnly>()
        .toInstance(MiskCaller(user = "development", roles = setOf("eng")))
  }
}
