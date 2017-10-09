package ru.olgak.folks.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;
import ru.hflabs.util.core.Pair;
import ru.hflabs.util.io.IOUtils;
import ru.hflabs.util.spring.Assert;
import ru.hflabs.util.test.ITestCaseExecutor;
import ru.hflabs.util.test.database.DataBaseTestDescriptor;
import ru.hflabs.util.test.definition.TestCaseDefinition;
import ru.olgak.folks.api.Folk;
import ru.olgak.folks.api.search.EntityFieldFilter;
import ru.olgak.folks.api.search.EntityQuery;
import ru.olgak.folks.api.search.SearchService;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.File;
import java.util.*;

import static java.lang.String.format;
import static org.testng.Assert.fail;
import static ru.hflabs.util.test.support.DataSetHelper.createDataSet;

/**
 * Класс <class>ComplexSearchFTCase</class> реализует комплексный тест для поиска, фильтрации и сортировки контрагентов
 *
 * @author nikolaig
 */
public class ComplexSearchCase extends FeatureCase implements ITestCaseExecutor<ComplexSearchCase.ToComplexSearchDescriptor> {

    @Resource(name = "searchService")
    private SearchService<Folk> searchService;

    @Override
    protected String getTestCasesRootPath() {
        return TEST_CASES_ROOT_PATH + "complex_search";
    }

    @Override
    protected Pair<Class<ToComplexSearchDescriptor>, Boolean> getTestSpecificProperties() {
        return Pair.valueOf(ToComplexSearchDescriptor.class, false);
    }

    /** Названия групп поисковых кейсов */
    private static final String SEARCH = "Search";
    private static final String ADVANCED_SEARCH = "Extended search";
    private static final String FILTERING = "Filtering";
    private static final String COLUMN_FILTERING = "By";
    private static final String SORTING = "Sorting";

    /** Константы */
    private static final int PERCENTAGE_DOUBLE = 80;
    private static final int PERCENTAGE_TRIPLE = 50;

    /** Маппинг группы кейсов и применителей поискового фильтра */
    private final Map<String, SearchEntryApplier> SEARCH_ENTRY_APPLIERS = ImmutableMap.<String, SearchEntryApplier>builder()
            .put(SEARCH, new SearchEntryApplier() {
                @Override
                public void applyToFilter(SearchGroup searchGroup, SearchEntry searchEntry, EntityQuery entityQuery) {
                    entityQuery.setSearch(searchEntry.query);
                }
            })
            .put(ADVANCED_SEARCH, new SearchEntryApplier() {
                @Override
                public void applyToFilter(SearchGroup searchGroup, SearchEntry searchEntry, EntityQuery entityQuery) {
                    entityQuery.setSearch(searchEntry.query);
                }
            })
            .put(SORTING, new SearchEntryApplier() {
                @Override
                public void applyToFilter(SearchGroup searchGroup, SearchEntry searchEntry, EntityQuery entityQuery) {
                    String[] split = searchEntry.query.split(" ");
                    Assert.isTrue(split.length == 2, format("Sorting entry '%s' must have two parts delimited by whitespace: %s", searchEntry.name, searchEntry.query));
                    Assert.isTrue("ASC".equals(split[1]) || "DESC".equals(split[1]), format("Sorting entry '%s' has wrong sort order '%s' (ASC or DESC available)", searchEntry.name, searchEntry.query));
                    entityQuery.setSortOrderValue(split[0], "ASC".equals(split[1]) ? SortOrder.ASCENDING : SortOrder.DESCENDING);
                }
            })
            .put(COLUMN_FILTERING, new SearchEntryApplier() {
                @Override
                public void applyToFilter(SearchGroup searchGroup, SearchEntry searchEntry, EntityQuery entityQuery) {
                    String columnName = StringUtils.uncapitalize(searchGroup.groupName.substring(2));
                    entityQuery.getFilters().put(columnName, createColumnFilter(columnName, searchEntry.query));
                }
            })
            .build();

    @Test(
            description = "Поиск контрагентов. Комплексный тест",
            dataProvider = "createTestCases"
    )
    public void testSearch(final DataBaseTestDescriptor<ToComplexSearchDescriptor> descriptor) throws Exception {
        executeTest(this, descriptor);
    }

    @Override
    public void executeTestMethod(DataBaseTestDescriptor<ToComplexSearchDescriptor> descriptor) throws Exception {
        LOG.info("Rebuild search index...");
        // Перестраиваем
        searchService.rebuild();
        // Загружаем эталонные данные
        IDataSet dataSet = createDataSet(new File[]{new File(descriptor.parametersDirectory, "etalon.xls")});
        // Простой поиск
        SearchGroup search = extractSearchGroup(dataSet, SEARCH);
        // Продвинутый поиск
        SearchGroup advancedSearch = extractSearchGroup(dataSet, ADVANCED_SEARCH);
        // Сортировка
        SearchGroup sorting = extractSearchGroup(dataSet, SORTING);
        // Фильтрация по колонкам
        List<SearchGroup> filteringByColumns = extractSearchGroupsByPattern(dataSet, COLUMN_FILTERING);
        // Конфликтующие группы
        List<SearchGroup> conflictingGroups = ImmutableList.<SearchGroup>builder().add(search).add(advancedSearch).build();
        // Все группы
        List<SearchGroup> allGroups = new ArrayList<SearchGroup>(filteringByColumns);
        allGroups.add(search);
        allGroups.add(advancedSearch);
        allGroups.add(sorting);
        // Если задан конкретный кейс, то проверяем его
        if (descriptor.testCaseDefinition != null && descriptor.testCaseDefinition.searchCase != null) {
            checkConcreteSearchCase(allGroups, descriptor.testCaseDefinition.searchCase, descriptor.getCaseName());
        } else {
            // Проверяем атомарные кейсы
            LOG.info("=================== Check atomic cases");
            StringBuilder atomicAssertStringBuilder = new StringBuilder();
            checkAtomicEntries(search, atomicAssertStringBuilder);
            checkAtomicEntries(advancedSearch, atomicAssertStringBuilder);
            checkAtomicEntries(sorting, atomicAssertStringBuilder);
            for (SearchGroup filteringByColumn : filteringByColumns) {
                checkAtomicEntries(filteringByColumn, atomicAssertStringBuilder);
            }
            // Если есть ошибки, то сложные кейсы не прогоняем
            if (atomicAssertStringBuilder.toString().contains("failed")) {
                fail(format("Case '%s' failed.\n%s", descriptor.getCaseName(), atomicAssertStringBuilder.toString()));
            }
            // Проверяем сложные комбинаторные кейсы
            // Перемешиваем атомарные кейсы во всех группах
            for (SearchGroup group : allGroups) {
                if (group.searchEntries != null) {
                    Collections.shuffle(group.searchEntries);
                }
            }
            // Проверяем
            StringBuilder combinedAssertStringBuilder = new StringBuilder();
            LOG.info("=================== Check double combinations");
            checkDoubleCombinations(allGroups, conflictingGroups, combinedAssertStringBuilder);
            LOG.info("=================== Check triple combinations");
            checkTripleCombinations(allGroups, conflictingGroups, combinedAssertStringBuilder);
            if (!combinedAssertStringBuilder.toString().isEmpty()) {
                fail(format("Case '%s' failed.\n%s", descriptor.getCaseName(), combinedAssertStringBuilder.toString()));
            }
        }
    }

    private void checkConcreteSearchCase(List<SearchGroup> allGroups, String searchCase, String caseName) throws Exception {
        String[] atomicCases = searchCase.split(" \\+ ");
        List<Pair<SearchGroup, Integer>> atomicCasesList = new ArrayList<Pair<SearchGroup, Integer>>();
        for (String atomicCase : atomicCases) {
            String[] groupAndEntry = atomicCase.split(":");
            Assert.isTrue(groupAndEntry.length == 2, "Wrong search case format. ':' is delimiter for group and entry name.");
            boolean found = false;
            for (SearchGroup searchGroup : allGroups) {
                if (searchGroup.groupName.equals(groupAndEntry[0])) {
                    for (SearchEntry searchEntry : searchGroup.searchEntries) {
                        if (searchEntry.name.equals(groupAndEntry[1])) {
                            atomicCasesList.add(Pair.valueOf(searchGroup, searchGroup.searchEntries.indexOf(searchEntry)));
                            found = true;
                        }
                    }
                }
            }
            Assert.isTrue(found, format("Atomic case '%s' not found", atomicCase));
        }
        StringBuilder assertStringBuilder = new StringBuilder();
        checkCombinedSearch(atomicCasesList, assertStringBuilder);
        if (!assertStringBuilder.toString().isEmpty()) {
            fail(format("Case '%s' failed.\n%s", caseName, assertStringBuilder.toString()));
        }
    }

    private void checkTripleCombinations(List<SearchGroup> allGroups, List<SearchGroup> conflictingGroups, StringBuilder assertStringBuilder) throws Exception {
        for (int i = 0; i < allGroups.size(); i++) {
            for (int j = i + 1; j < allGroups.size(); j++) {
                for (int k = j + 1; k < allGroups.size(); k++) {
                    SearchGroup firstGroup = allGroups.get(i);
                    SearchGroup secondGroup = allGroups.get(j);
                    SearchGroup thirdGroup = allGroups.get(k);
                    if ((conflictingGroups.contains(firstGroup) ? 1 : 0) + (conflictingGroups.contains(secondGroup) ? 1 : 0) + (conflictingGroups.contains(thirdGroup) ? 1 : 0) <= 1) {
                        for (int e1 = 0; e1 < firstGroup.searchEntries.size() * PERCENTAGE_TRIPLE / 100; e1++) {
                            for (int e2 = 0; e2 < secondGroup.searchEntries.size() * PERCENTAGE_TRIPLE / 100; e2++) {
                                for (int e3 = 0; e3 < thirdGroup.searchEntries.size() * PERCENTAGE_TRIPLE / 100; e3++) {
                                    List<Pair<SearchGroup, Integer>> combinedSearchList = ImmutableList.<Pair<SearchGroup, Integer>>builder()
                                            .add(Pair.valueOf(firstGroup, e1)).add(Pair.valueOf(secondGroup, e2)).add(Pair.valueOf(thirdGroup, e3)).build();
                                    checkCombinedSearch(combinedSearchList, assertStringBuilder);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkDoubleCombinations(List<SearchGroup> allGroups, List<SearchGroup> conflictingGroups, StringBuilder assertStringBuilder) throws Exception {
        for (int i = 0; i < allGroups.size(); i++) {
            for (int j = i + 1; j < allGroups.size(); j++) {
                SearchGroup firstGroup = allGroups.get(i);
                SearchGroup secondGroup = allGroups.get(j);
                if (!(conflictingGroups.contains(firstGroup) && conflictingGroups.contains(secondGroup))) {
                    for (int e1 = 0; e1 < firstGroup.searchEntries.size() * PERCENTAGE_DOUBLE / 100; e1++) {
                        for (int e2 = 0; e2 < secondGroup.searchEntries.size() * PERCENTAGE_DOUBLE / 100; e2++) {
                            List<Pair<SearchGroup, Integer>> combinedSearchList = ImmutableList.<Pair<SearchGroup, Integer>>builder()
                                    .add(Pair.valueOf(firstGroup, e1)).add(Pair.valueOf(secondGroup, e2)).build();
                            checkCombinedSearch(combinedSearchList, assertStringBuilder);
                        }
                    }
                }
            }
        }
    }

    private List<SearchGroup> extractSearchGroupsByPattern(IDataSet dataSet, String startWith) throws DataSetException {
        List<SearchGroup> result = new ArrayList<SearchGroup>();
        for (String tableName : dataSet.getTableNames()) {
            if (tableName.startsWith(startWith)) {
                SearchGroup searchGroup = extractSearchGroup(dataSet, tableName, SEARCH_ENTRY_APPLIERS.get(COLUMN_FILTERING));
                result.add(searchGroup);
            }
        }
        return result;
    }

    private SearchGroup extractSearchGroup(IDataSet dataSet, String groupName) throws DataSetException {
        return extractSearchGroup(dataSet, groupName, null);
    }

    private SearchGroup extractSearchGroup(IDataSet dataSet, String groupName, SearchEntryApplier searchEntryApplier) throws DataSetException {
        ITable table = null;
        try {
            table = dataSet.getTable(groupName);
        } catch (DataSetException e) {
            return null;
        }
        Assert.isTrue(table.getRowCount() > 2, "Row count <= 2");
        boolean skipColumn = true;
        List<Long> ids = new ArrayList<Long>();
        List<SearchEntry> etalonEntries = new ArrayList<SearchEntry>();
        for (Column column : table.getTableMetaData().getColumns()) {
            // Пропускаем все данные до колонки с идентификатором
            if (!skipColumn) {
                String name = column.getColumnName();
                Object value = table.getValue(0, name);
                Assert.notNull(value, format("First row for column '%s' is empty", name));
                String query = value.toString();
                // Идем начиная с 3 строки и собираем проставленные номера в связке с идентификатором
                List<Pair<Long, Long>> numbers = new ArrayList<Pair<Long, Long>>();
                for (int i = 1; i < table.getRowCount(); i++) {
                    Long number = getLong(table, i, column);
                    if (number != null) {
                        numbers.add(Pair.valueOf(ids.get(i - 1), number));
                    }
                }
                // Сортируем по номеру (нужно в случае сортировки)
                Collections.sort(numbers, new Comparator<Pair<Long, Long>>() {
                    @Override
                    public int compare(Pair<Long, Long> o1, Pair<Long, Long> o2) {
                        return o1.getSecond().compareTo(o2.getSecond());
                    }
                });
                List<Long> resultedIds = new ArrayList<Long>();
                for (Pair<Long, Long> number : numbers) {
                    resultedIds.add(number.first);
                }
                // Добавляем в результат
                etalonEntries.add(new SearchEntry(name, query, resultedIds));
            }
            // В колонке ID собираем все идентификаторы
            if ("ID".equals(column.getColumnName())) {
                skipColumn = false;
                for (int i = 1; i < table.getRowCount(); i++) {
                    Long id = getLong(table, i, column);
                    Assert.notNull(id, format("ID in row '%d' is empty", i));
                    ids.add(id);
                }
            }
        }
        return new SearchGroup(groupName, etalonEntries, searchEntryApplier == null ? SEARCH_ENTRY_APPLIERS.get(groupName) : searchEntryApplier);
    }

    private void checkAtomicEntries(SearchGroup searchGroup, StringBuilder assertStringBuilder) throws Exception {
        if (searchGroup == null) {
            return;
        }
        assertStringBuilder.append(IOUtils.LINE_SEPARATOR).append("============= ").append(searchGroup.groupName);
        for (SearchEntry searchEntry : searchGroup.searchEntries) {
            EntityQuery partyFilter = new EntityQuery();
            searchGroup.searchEntryApplier.applyToFilter(searchGroup, searchEntry, partyFilter);
            checkEntityQuery(searchEntry.name, partyFilter, searchEntry.resultedPartyIds, assertStringBuilder);
        }
    }

    private void checkEntityQuery(String caseName, EntityQuery entityQuery, List<Long> expectedResult, StringBuilder assertStringBuilder) throws Exception {
        List<Long> result = Lists.transform(searchService.findEntities(entityQuery), new Function<Folk, Long>() {
            @Override
            public Long apply(Folk input) {
                return input.getId();
            }
        });
        LOG.info("Check '{}'. Expected: {} Actual: {}.", new Object[]{caseName.toString(), expectedResult.size(), result.size()});
        // Выполняем сравнение значений
        String assertMessage = assertResult(result, expectedResult);
        // Проверяем, что проверка прошла
        if (assertMessage.isEmpty()) {
            // Проверяем сортировку значений
            if (entityQuery.getSortOrderValue() != null && !SortOrder.UNSORTED.equals(entityQuery.getSortOrderValue())) {
                String expectedOrder = StringUtils.collectionToDelimitedString(expectedResult, ";");
                String actualOrder = StringUtils.collectionToDelimitedString(result, ";");
                if (!actualOrder.equals(expectedOrder)) {
                    assertStringBuilder.append(IOUtils.LINE_SEPARATOR)
                            .append(format("Search case '%s' failed. expected order: <%s> but was: <%s>", caseName, expectedOrder, actualOrder));
                }
            }
        } else {
            assertStringBuilder.append(IOUtils.LINE_SEPARATOR)
                    .append(format("Search case '%s' failed. Different number of parties: expected <%d> actual <%d>", caseName, expectedResult.size(), result.size()) + IOUtils.LINE_SEPARATOR + assertMessage);
        }
    }

    private void checkCombinedSearch(List<Pair<SearchGroup, Integer>> combinedSearchList, StringBuilder assertStringBuilder) throws Exception {
        EntityQuery partyFilter = new EntityQuery();
        StringBuilder caseName = new StringBuilder();
        String delimiter = "";
        List<Long> resultedIds = null;
        for (Pair<SearchGroup, Integer> atomicSearch : combinedSearchList) {
            SearchGroup searchGroup = atomicSearch.first;
            SearchEntry searchEntry = searchGroup.searchEntries.get(atomicSearch.second);
            // Применяем атомарный фильтр
            searchGroup.searchEntryApplier.applyToFilter(searchGroup, searchEntry, partyFilter);
            // Добавляем название
            caseName.append(delimiter).append(searchGroup.groupName).append(":").append(searchEntry.name);
            delimiter = " + ";
            // Джойним результат
            if (resultedIds == null) {
                resultedIds = new ArrayList<Long>(searchEntry.resultedPartyIds);
            } else {
                joinResult(resultedIds, searchEntry.resultedPartyIds, searchGroup.groupName.equals(SORTING));
            }
        }
        checkEntityQuery(caseName.toString(), partyFilter, resultedIds, assertStringBuilder);
    }

    private void joinResult(List<Long> resultedIds, final List<Long> joinedIds, boolean sorting) {
        if (sorting) {
            // Применяем порядок
            Collections.sort(resultedIds, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return Integer.valueOf(joinedIds.indexOf(o1)).compareTo(Integer.valueOf(joinedIds.indexOf(o2)));
                }
            });
        } else {
            // Берем пересечение
            resultedIds.retainAll(joinedIds);
        }
    }

    private static Long getLong(ITable table, int rowNumber, Column column) throws DataSetException {
        Object value = table.getValue(rowNumber, column.getColumnName());
        return value == null ? null : Long.valueOf(value instanceof String ? (String) value : value.toString());
    }

    private EntityFieldFilter<?> createColumnFilter(String columnName, String query) {
        if ("EMPTY".equals(query)) {
            return new EntityFieldFilter.EmptyValue();
        } else if ("NOT_EMPTY".equals(query)) {
            return new EntityFieldFilter.NotEmptyValue();
        } else if (query.startsWith("RE[")) {
            return new EntityFieldFilter.PatternValue(query.substring(3, query.length() - 1));
        }
        throw new AssertionError(format("Query '%s' unsupported for column '%s'", query, columnName));
    }

    /** Применитель поискового условия */
    private static interface SearchEntryApplier {
        void applyToFilter(SearchGroup searchGroup, SearchEntry searchEntry, EntityQuery entityQuery);
    }

    /** Класс представляем одну колонку эталонного файла (название запроса, сам запрос и ожидаемые идентификаторы контрагентов) */
    private static class SearchEntry {
        public final String name;
        public final String query;
        public final List<Long> resultedPartyIds;

        private SearchEntry(String name, String query, List<Long> resultedPartyIds) {
            this.name = name;
            this.query = query;
            this.resultedPartyIds = resultedPartyIds;
        }
    }

    /** Класс представляем одну страницу эталонного файла */
    private static class SearchGroup {
        public final String groupName;
        public final List<SearchEntry> searchEntries;
        public final SearchEntryApplier searchEntryApplier;

        private SearchGroup(String groupName, List<SearchEntry> searchEntries, SearchEntryApplier searchEntryApplier) {
            this.groupName = groupName;
            this.searchEntries = searchEntries;
            this.searchEntryApplier = searchEntryApplier;
        }
    }

    /** Класс <class>ToComplexSearchDescriptor</class> описывает дескриптор поиска контрагентов */
    public static final class ToComplexSearchDescriptor extends TestCaseDefinition {

        /** Конкретный кейс */
        public final String searchCase;

        public ToComplexSearchDescriptor(String searchCase) {
            this.searchCase = searchCase;
        }
    }
}
