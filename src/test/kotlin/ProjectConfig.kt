import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.test.TestCaseOrder

class ProjectConfig : AbstractProjectConfig() {
    override val testCaseOrder = TestCaseOrder.Sequential
}
