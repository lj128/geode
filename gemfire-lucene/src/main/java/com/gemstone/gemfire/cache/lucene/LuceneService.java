package com.gemstone.gemfire.cache.lucene;

import java.util.Collection;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.lucene.internal.LuceneServiceImpl;
import com.gemstone.gemfire.internal.cache.extension.Extensible;

/**
 * LuceneService instance is a singleton for each cache. It will be created in cache 
 * constructor and get its reference via {@link GemFireCache#getLuceneService()}.
 * 
 * It provides handle for managing the {@link LuceneIndex} and create the {@link LuceneQuery}
 * via {@link LuceneQueryFactory}
 * 
 * </p>
 * Example: <br>
 * 
 * <pre>
 * At client and server JVM, initializing cache will create the LuceneServiceImpl object, 
 * which is a singleton at each JVM. 
 * 
 * At each server JVM, for data region to create index, create the index on fields with default analyzer:
 * LuceneIndex index = luceneService.createIndex(indexName, regionName, "field1", "field2", "field3"); 
 * or create index on fields with specified analyzer:
 * LuceneIndex index = luceneService.createIndex(indexName, regionName, analyzerPerField);
 * 
 * We can also create index via cache.xml or gfsh.
 * 
 * At client side, create query and run the search:
 * 
 * LuceneQuery query = luceneService.createLuceneQueryFactory().setLimit(200).setPageSize(20)
 * .setResultTypes(SCORE, VALUE, KEY).setFieldProjection("field1", "field2")
 * .create(indexName, regionName, querystring, analyzer);
 * 
 * The querystring is using lucene's queryparser syntax, such as "field1:zhou* AND field2:gzhou@pivotal.io"
 *  
 * LuceneQueryResults results = query.search();
 * 
 * If pagination is not specified:
 * List list = results.getNextPage(); // return all results in one getNextPage() call
 * or if paging is specified:
 * if (results.hasNextPage()) {
 *   List page = results.nextPage(); // return resules page by page
 * }
 * 
 * The item of the list is either the domain object or instance of {@link LuceneResultStruct}
 * </pre>
 * 
 * @author Xiaojian Zhou
 *
 */
public interface LuceneService {
  
  /**
   * Create a lucene index using default analyzer.
   * 
   * @param indexName
   * @param regionPath
   * @param fields
   * @return LuceneIndex object
   */
  public void createIndex(String indexName, String regionPath, String... fields);
  
  /**
   * Create a lucene index using specified analyzer per field
   * 
   * @param indexName index name
   * @param regionPath region name
   * @param analyzerPerField analyzer per field map
   * @return LuceneIndex object
   *
   */
  public void createIndex(String indexName, String regionPath,  
      Map<String, Analyzer> analyzerPerField);

  /**
   * Destroy the lucene index
   * 
   * @param index index object
   */
  public void destroyIndex(LuceneIndex index);
  
  /**
   * Get the lucene index object specified by region name and index name
   * @param indexName index name
   * @param regionPath region name
   * @return LuceneIndex object
   */
  public LuceneIndex getIndex(String indexName, String regionPath);
  
  /**
   * get all the lucene indexes.
   * @return all index objects in a Collection
   */
  public Collection<LuceneIndex> getAllIndexes();

  /**
   * create LuceneQueryFactory
   * @return LuceneQueryFactory object
   */
  public LuceneQueryFactory createLuceneQueryFactory();
}
