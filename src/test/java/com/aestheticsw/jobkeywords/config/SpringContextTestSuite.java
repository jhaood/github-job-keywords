package com.aestheticsw.jobkeywords.config;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.aestheticsw.jobkeywords.service.termextractor.impl.fivefilters.FiveFiltersClientTest;

@RunWith(Categories.class)
// @ExcludeCategory({ SpringContextTestCategory.class, RestClientTestCategory.class, IntegrationTestCategory.class,
//     DatabaseTestCategory.class })
@IncludeCategory(SpringContextTestCategory.class)
@SuiteClasses(FiveFiltersClientTest.class)
public class SpringContextTestSuite {
}
