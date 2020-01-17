import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

class ExamplePipelineTest extends BasePipelineTest {
    @Override
    @Before
    void setUp() throws Exception {
        super.setUp()
    }

    @Test
    void should_execute_without_errors() throws Exception {
        def script = loadScript("src/test/jenkins/ExamplePipelineFile.groovy")
        script.run()
        printCallStack()
    }
}
