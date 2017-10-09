package ru.olgak.folks.service.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.ReaderManager;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hflabs.util.core.Pair;
import ru.hflabs.util.io.IOUtils;
import ru.hflabs.util.lucene.LuceneIndexManager;
import ru.hflabs.util.lucene.LuceneModifierUtil;
import ru.hflabs.util.lucene.LuceneUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс <class>SearchIndexAccessor</class> реализует сервис доступа к поисковому индексу
 *
 * @author Nazin Alexander
 */
public class SearchIndexAccessor implements LuceneIndexManager {

    /** Класс логирования */
    private static final Logger LOG = LoggerFactory.getLogger(SearchIndexAccessor.class);

    /** Слушатель удаления неиспользуемых файлов */
    private ReferenceManager.RefreshListener deleteUnusedFilesListener;
    /** Директория индекса: первое значение - директория, второе - путь к директории */
    private Pair<Directory, File> indexDirectory;
    /** Lock доступа к индексу записи */
    private final Lock indexLock;
    /** Индекс записи */
    private IndexWriter indexWriter;

    private Analyzer analyzer;

    public SearchIndexAccessor() {
        this.indexLock = new ReentrantLock();
        this.indexDirectory = new Pair<Directory, File>(new RAMDirectory(), null);
    }


    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public Directory getIndexDirectory() {
        return indexDirectory.first;
    }


    @Override
    public String retrieveIndexName() {
        return indexDirectory.second != null ?
                indexDirectory.second.getPath() :
                indexDirectory.first.toString();
    }

    @Override
    public SearcherManager createSearcherManager() {
        // Проверяем доступность директории индекса
        try {
            SearcherManager manager = new SearcherManager(getIndexDirectory(), null);
            manager.addListener(deleteUnusedFilesListener);
            return manager;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public ReaderManager createReaderManager() {
        // Проверяем доступность директории индекса
        try {
            ReaderManager manager = new ReaderManager(getIndexDirectory());
            manager.addListener(deleteUnusedFilesListener);
            return manager;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public IndexWriter retrieveWriter() {
        // Проверяем доступность директории индекса
        // Выполняем блокировку индекса
        indexLock.lock();
        // Проверяем, что индекс записи создан
        if (indexWriter == null) {
            try {
                indexWriter = new IndexWriter(indexDirectory.first, new IndexWriterConfig(analyzer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Возвращаем индекс записи
        return indexWriter;
    }

    /**
     * Освобождает ресурc
     *
     * @param writer индекс записи
     */
    private void releaseWriter(IndexWriter writer) {
        assert indexWriter == writer : "Released index writer is not equal monitored";
        indexLock.unlock();
    }

    /**
     * Выполняет запись модифицированных данных в индексе
     *
     * @param writer индекс записи
     * @param changesCount количество изменений в индексе
     */
    public void commitWriter(IndexWriter writer, int changesCount) {
        assert indexWriter == writer : "Released index writer is not equal monitored";
        try {
            writer.commit();
        } catch (Throwable ex) {
            indexWriter = null;
            throw new RuntimeException(ex);
        } finally {
            indexLock.unlock();
        }
    }

    /**
     * Выполняет откат модифицированных данных в индексе
     *
     * @param writer индекс записи
     */
    public void rollbackWriter(IndexWriter writer) {
        assert indexWriter == writer : "Released index writer is not equal monitored";
        try {
            writer.rollback();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            indexWriter = null;
            indexLock.unlock();
        }
    }


    @Override
    public void open() throws Exception {
        // Проверяем сущуствование директории индекса и его валидность
        if (!DirectoryReader.indexExists(indexDirectory.first)) {
            // Создаем пустой индекс
            final IndexWriter writer = retrieveWriter();
            try {
                writer.commit();
            } finally {
                commitWriter(writer, -1);
            }
        }
        // Создаем слушателя удаления не используемых файлов
        deleteUnusedFilesListener = new DeleteUnusedFilesListener();
    }

    @Override
    public void close() throws Exception {
        if (indexDirectory != null) {
            indexLock.lock();
            try {
                deleteUnusedFilesListener = null;
                // Закрываем индекс записи
                IOUtils.closeQuietly(indexWriter);
                indexWriter = null;
                // Закрываем директорию индекса
                IOUtils.closeQuietly(indexDirectory.first);
                indexDirectory = null;
            } finally {
                indexLock.unlock();
            }
        }
    }


    /**
     * Класс <class>DeleteUnusedFilesListener</class> реализует слушателя удаления не используемых файлов после закрытия ссылок на директорию индекса
     *
     * @author Nazin Alexander
     */
    private class DeleteUnusedFilesListener implements ReferenceManager.RefreshListener {

        @Override
        public void beforeRefresh() throws IOException {
            // do nothing
        }

        @Override
        public void afterRefresh(boolean didRefresh) throws IOException {
            if (didRefresh && indexLock.tryLock()) {
                try {
                    LuceneModifierUtil.doWithCallback("delete unused reference files", SearchIndexAccessor.this, LuceneModifierUtil.createDeleteUnusedFilesCallback());
                } finally {
                    indexLock.unlock();
                }
            }
        }
    }
}
