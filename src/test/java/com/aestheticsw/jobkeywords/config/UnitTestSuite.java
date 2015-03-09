package com.aestheticsw.jobkeywords.config;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;

@RunWith(Categories.class)
// @ExcludeCategory({ SpringContextTestCategory.class, RestClientTestCategory.class, IntegrationTestCategory.class,
//     DatabaseTestCategory.class })
@IncludeCategory(UnitTestCategory.class)
public class UnitTestSuite {
}
