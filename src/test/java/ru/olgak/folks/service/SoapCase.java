package ru.olgak.folks.service;

import org.apache.cxf.jaxws.EndpointImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.hflabs.util.test.ITestCaseExecutor;
import ru.hflabs.util.test.database.DataBaseTestDescriptor;
import ru.hflabs.util.test.definition.TestCaseDefinition;
import ru.olgak.folks.api.Folk;
import ru.olgak.folks.api.search.SearchService;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.net.URL;

import static org.testng.Assert.assertNotNull;

/**
 * Класс <class>SoapFeatureTestCase</class> реализует основу для тестирования SOAP-сервисов
 *
 * @see <a href="http://confluence.hflabs.ru/x/EId1AQ">Тесты на SOAP-сервисы</a>
 */
public class SoapCase extends FeatureCase implements ITestCaseExecutor<TestCaseDefinition> {

    @Resource(name = "searchService")
    private SearchService<Folk> searchService;
    /** Endpoint тестируемого сервиса */
    @Resource(name = "folksEndpoint")
    protected EndpointImpl endpoint;
    /** Динамический сервис */
    protected Dispatch<Source> dispatch;

    @Override
    protected String getTestCasesRootPath() {
        return TEST_CASES_ROOT_PATH + "soap";
    }

    @Test(
            description = "SOAP-запросы",
            dataProvider = "createTestCases"
    )
    public void testSearch(final DataBaseTestDescriptor descriptor) throws Exception {
        executeTest(this, descriptor);
    }

    @BeforeClass
    public void createDispatch() throws Exception {
        assertNotNull(endpoint);
        Service dynamicService = Service.create(new URL(endpoint.getAddress() + "?wsdl"), endpoint.getServiceName());
        dispatch = dynamicService.createDispatch(endpoint.getEndpointName(), Source.class, Service.Mode.PAYLOAD);
    }

    @AfterClass
    public void releaseDispatch() throws Exception {
        endpoint.stop();
    }

    @Override
    public void executeTestMethod(DataBaseTestDescriptor<TestCaseDefinition> descriptor) throws Exception {
        LOG.info("Rebuild search index...");
        // Перестраиваем
        searchService.rebuild();

        executeMultipleSoapTestRequests(dispatch, descriptor);
    }
}
