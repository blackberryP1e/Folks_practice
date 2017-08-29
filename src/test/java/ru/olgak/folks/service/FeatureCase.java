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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    protected IDataTypeFactory getDataTypeFactory() throws Exception {
        return new HsqldbDataTypeFactory();
    }

    /**
     * Возвращает параметры специфики теста
     *
     * @return Возвращает пару: класс, описывающий специфику теста + обязательность наличия для каждого теста
     */
    protected abstract <TSD extends TestSpecificDescriptor> Pair<Class<SearchCase.ToSearchDescriptor>, Boolean> getTestSpecificProperties();


    /**
     * Возвращает корневой путь к данным
     *
     * @return Возвращает корневой путь к данным
     */
    protected abstract String getTestCasesRootPath();

    @DataProvider
    protected Iterator<Object[]> createTestCases() throws Exception {
        return createTestCases(
                getTestCasesRootPath(), TEST_CASES_FILTER,
                new DataBaseContextTests.PatternDataSetFilter("dbStart.*\\.*"),
                new DataBaseContextTests.PatternDataSetFilter("dbEnd.*\\.*"),
                VALIDATE_QUERY_FILE_NAME,
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
