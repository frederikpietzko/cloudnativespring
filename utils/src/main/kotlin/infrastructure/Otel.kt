package com.github.frederikpietzko.cloudnativespring.infrastructure

import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmClassLoadingMeterConventions
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmCpuMeterConventions
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmMemoryMeterConventions
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmThreadMeterConventions
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.tracing.TraceContext
import io.micrometer.tracing.Tracer
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.support.ContextPropagatingTaskDecorator
import org.springframework.http.server.observation.OpenTelemetryServerRequestObservationConvention
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Configuration(proxyBeanMethods = false)
class OpenTelemetryConfiguration {
    @Bean
    fun openTelemetryServerRequestObservationConvention(): OpenTelemetryServerRequestObservationConvention =
        OpenTelemetryServerRequestObservationConvention()

    @Bean
    fun openTelemetryJvmCpuMeterConventions(): OpenTelemetryJvmCpuMeterConventions =
        OpenTelemetryJvmCpuMeterConventions(Tags.empty())

    @Bean
    fun processorMetrics(): ProcessorMetrics =
        ProcessorMetrics(mutableListOf(), OpenTelemetryJvmCpuMeterConventions(Tags.empty()))

    @Bean
    fun jvmMemoryMetrics(): JvmMemoryMetrics =
        JvmMemoryMetrics(mutableListOf(), OpenTelemetryJvmMemoryMeterConventions(Tags.empty()))

    @Bean
    fun jvmThreadMetrics(): JvmThreadMetrics =
        JvmThreadMetrics(mutableListOf(), OpenTelemetryJvmThreadMeterConventions(Tags.empty()))

    @Bean
    fun classLoaderMetrics(): ClassLoaderMetrics =
        ClassLoaderMetrics(OpenTelemetryJvmClassLoadingMeterConventions())

}

@Component
internal class InstallOpenTelemetryAppender(private val openTelemetry: OpenTelemetry?) : InitializingBean {
    override fun afterPropertiesSet() {
        OpenTelemetryAppender.install(this.openTelemetry)
    }
}

@Component
class TraceIdFilter(private val tracer: Tracer) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = this.traceId
        if (traceId != null) {
            response.setHeader("X-Trace-Id", traceId)
        }
        filterChain.doFilter(request, response)
    }

    private val traceId: String?
        get() {
            val context: TraceContext? = this.tracer.currentTraceContext().context()
            return context?.traceId()
        }
}

@Configuration(proxyBeanMethods = false)
class ContextPropagationConfiguration {

    @Bean
    fun contextPropagatingTaskDecorator(): ContextPropagatingTaskDecorator =
        ContextPropagatingTaskDecorator()

}