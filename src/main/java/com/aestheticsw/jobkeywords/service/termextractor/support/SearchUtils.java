package com.aestheticsw.jobkeywords.service.termextractor.support;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

public class SearchUtils {

    public static Locale lookupLocaleByCountry(String country) {
        List<Locale> localeList = LocaleUtils.languagesByCountry(country);
        if (localeList == null || localeList.size() == 0) {
            throw new IllegalArgumentException("Invalid country code: " + country);
        }
        Locale locale = localeList.get(0);

        // if English isn't the language of the first Locale, then try to find English.
        if (!locale.getLanguage().equals("en")) {
            for (Locale option : localeList) {
                if (option.getLanguage().equals("en")) {
                    return option;
                }
            }
        }
        return locale;
    }
}
