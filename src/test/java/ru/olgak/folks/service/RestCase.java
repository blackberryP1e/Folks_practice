package ru.olgak.folks.service;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import ru.hflabs.util.test.ITestCaseExecutor;
import ru.hflabs.util.test.database.DataBaseTestDescriptor;
import ru.hflabs.util.test.definition.TestCaseDefinition;
import ru.hflabs.util.test.support.RestHelper;
import ru.olgak.folks.api.Folk;
import ru.olgak.folks.api.search.SearchService;

import javax.annotation.Resource;

/**
 * Класс <class>PartyRSFTCase</class> реализует тестовый запускатор REST-тестов веб-сервиса PartyWS
 *
 * @author ivanf
 */
public class RestCase extends FeatureCase implements ITestCaseExecutor<TestCaseDefinition> {

    private static final String REST_SEARCH_URL = "http://localhost:8088/rest/services/folks/v1/search";

    @Resource(name = "searchService")
    private SearchService<Folk> searchService;

    @Override
    protected String getTestCasesRootPath() {
        return TEST_CASES_ROOT_PATH + "rest";
    }

    @Test(
            description = "REST-запросы",
            dataProvider = "createTestCases"
    )
    public void testSearch(final DataBaseTestDescriptor descriptor) throws Exception {
        executeTest(this, descriptor);
    }

    @Override
    public void executeTestMethod(DataBaseTestDescriptor<TestCaseDefinition> descriptor) throws Exception {
            LOG.info("Rebuild search index...");
        // Перестраиваем
        searchService.rebuild();

        try {
            RestHelper.executeRestTestRequest(new RestTemplate(), getTestArtifactsDirectory(descriptor.getCaseName()), REST_SEARCH_URL, descriptor, null);

        } catch (HttpServerErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e);
        }
    }
}
