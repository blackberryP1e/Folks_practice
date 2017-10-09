package ru.olgak.folks.service;

import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;
import ru.hflabs.util.core.Pair;
import ru.hflabs.util.io.IOUtils;
import ru.hflabs.util.test.DataBaseContextTests;
import ru.hflabs.util.test.definition.TestCaseDefinition;
import ru.hflabs.util.test.support.TestHelper;

import java.util.*;

/**
 * Класс <class>FeatureCase</class>
 *
 * @author nikolaig
 */
@ContextConfiguration(
        locations = {
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "services-*.xml",
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "**/case-*.xml"
        }
)
public abstract class FeatureCase extends DataBaseContextTests {

    /** Базовая директория тестов */
    public static final String TEST_CASES_ROOT_PATH = "cases/";

    @Override
    protected IDataTypeFactory getBaseDataTypeFactory() throws Exception {
        return new HsqldbDataTypeFactory();
    }

    @Override
    protected int getTimestampColumnsTolerance() {
        return 5 * 60 * 1000; // 5 минут
    }

    @Override
    protected Set<String> getTolerantTimestampColumns() {
        return Collections.emptySet();
    }

    /**
     * Возвращает параметры специфики теста
     *
     * @return Возвращает пару: класс, описывающий специфику теста + обязательность наличия для каждого теста
     */
    protected abstract <TCD extends TestCaseDefinition> Pair<Class<TCD>, Boolean> getTestSpecificProperties();

    /**
     * Возвращает корневой путь к данным
     *
     * @return Возвращает корневой путь к данным
     */
    protected abstract String getTestCasesRootPath();

    @DataProvider
    protected Iterator<Object[]> createTestCases() throws Exception {
        return createTestCases(
                getTestCasesRootPath(),
                TestHelper.buildTestCasesFilter(),
                TestHelper.buildDataSetStartFilter(),
                TestHelper.buildDataSetEndFilter(),
                TestHelper.VALIDATE_QUERY,
                getTestSpecificProperties()
        );
    }

    /**
     * Строит разницу между двумя коллекциями.
     *
     * @param from исходная коллекция
     * @param to проверяемая коллекция
     * @return Возвращает элементы, которые содержатся в коллекции #from, но не содержатся в коллекции #to
     */
    private static <T> Collection<T> createDifference(Collection<T> from, Collection<T> to) {
        Collection<T> fromCopy = new ArrayList<T>(from);
        for (T toOne : to) {
            fromCopy.remove(toOne);
        }
        return fromCopy;
    }

    /**
     * Проверяет на равнство ожидаемую и актуальную коллекцию объектов
     *
     * @param actualResult текущий результат
     * @param expectedResult ожидаемый результат
     * @return Возвращает сформированную строку ожидаемого результата
     */
    protected static <T> String assertResult(Collection<T> actualResult, Collection<T> expectedResult) {
        StringBuilder assertionMessage = new StringBuilder();
        // actual NOT NULL, expected NULL
        String actualMessage = StringUtils.collectionToDelimitedString(createDifference(actualResult, expectedResult), ";");
        if (!actualMessage.isEmpty()) {
            assertionMessage.append(String.format("unexpected elements: <%s>", actualMessage)).append(IOUtils.LINE_SEPARATOR);
        }
        // actual NULL, expected NOT NULL
        String expectedMessage = StringUtils.collectionToDelimitedString(createDifference(expectedResult, actualResult), ";");
        if (!expectedMessage.isEmpty()) {
            assertionMessage.append(String.format("missing elements: <%s>", expectedMessage)).append(IOUtils.LINE_SEPARATOR);
        }
        return assertionMessage.toString();
    }
}
