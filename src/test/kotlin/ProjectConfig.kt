import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.TestCaseOrder

/**
 * Project-wide Kotest configuration to disable parallelism
 */
class ProjectConfig : AbstractProjectConfig() {
    // Disable parallel execution of tests
    override val parallelism = 4
    
    // Optional: Set isolation mode for specs
   // override val isolationMode = IsolationMode.InstancePerLeaf
    
    // Optional: Set test case order
    override val testCaseOrder = TestCaseOrder.Sequential
    
    // Optional: Set global timeout for tests
    // override val timeout = 30000 // 30 seconds
}
