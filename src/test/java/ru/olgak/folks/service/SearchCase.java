package ru.olgak.folks.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;
import ru.hflabs.util.core.Pair;
import ru.hflabs.util.io.IOUtils;
import ru.hflabs.util.test.DataBaseContextTests;
import ru.olgak.folks.api.Folk;
import ru.olgak.folks.api.search.EntityQuery;
import ru.olgak.folks.api.search.SearchService;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/** Класс <class>SearchCase</class> */

public class SearchCase extends FeatureCase implements DataBaseContextTests.ExecuteTestCallback<SearchCase.ToSearchDescriptor> {


    @Resource(name = "searchService")
    private SearchService<Folk> searchService;

    @Override
    protected String getTestCasesRootPath() {
        return TEST_CASES_ROOT_PATH + "search";
    }

    @Override
    protected Pair<Class<ToSearchDescriptor>, Boolean> getTestSpecificProperties() {
        return Pair.valueOf(ToSearchDescriptor.class, true);
    }

    @Test(
            description = "Поиск контрагентов",
            dataProvider = "createTestCases"
    )
    public void testSearch(final DataBaseContextTests.TestParametersDescriptor descriptor) throws Exception {
        executeTest(this, descriptor);
    }

    @Override
    public void executeTestMethod(TestParametersDescriptor<ToSearchDescriptor> descriptor) throws Exception {
        LOG.info("Rebuild search index...");
        // Перестраиваем
        searchService.rebuild();

        ToSearchDescriptor toSearchDescriptor = descriptor.testSpecificDescriptor;
        // Выполняем поиск идентификаторов контрагентов
        List<Long> result = Lists.transform(searchService.findEntities(toSearchDescriptor.entityQuery), new Function<Folk, Long>() {
            @Override
            public Long apply(Folk input) {
                return input.getId();
            }
        });
        // Выполняем сравнение значений
        String assertMessage = assertResult(result, toSearchDescriptor.getExpectedResult());
        // Проверяем, что проверка прошла
        if (assertMessage.isEmpty()) {
            // Проверяем сортировку значений
            if (StringUtils.hasText(toSearchDescriptor.entityQuery.getSortOrderKey()) && !SortOrder.UNSORTED.equals(toSearchDescriptor.entityQuery.getSortOrderValue())) {
                String expectedOrder = StringUtils.collectionToDelimitedString(toSearchDescriptor.getExpectedResult(), ";");
                String actualOrder = StringUtils.collectionToDelimitedString(result, ";");
                assertEquals(actualOrder, expectedOrder, String.format("Case '%s' failed. expected order: <%s> but was: <%s>", descriptor.caseName, expectedOrder, actualOrder));
            }
        } else {
            fail(
                    String.format("Case '%s' failed. Different number of parties:", descriptor.caseName) + IOUtils.LINE_SEPARATOR + assertMessage
            );
        }
    }

    /** Класс <class>ToSearchDescriptor</class> описывает дескриптор поиска контрагентов */
    public static final class ToSearchDescriptor extends TestSpecificDescriptor {

        /** Запрос поиска */
        public EntityQuery entityQuery;
        /** Коллекция ожидаемых идентификаторов контрагентов */
        private Collection<Long> expectedResult = Collections.emptyList();

        public EntityQuery getEntityQuery() {
            return entityQuery;
        }

        public void setEntityQuery(EntityQuery entityQuery) {
            this.entityQuery = entityQuery;
        }

        public Collection<Long> getExpectedResult() {
            return expectedResult;
        }

        public void setExpectedResult(Collection<Long> expectedResult) {
            this.expectedResult = expectedResult;
        }
    }

}
