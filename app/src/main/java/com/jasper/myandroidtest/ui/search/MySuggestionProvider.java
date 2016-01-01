package com.jasper.myandroidtest.ui.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    //注意AUTHORITY，这里要跟manifest和searchable中配置得一致
    public final static String AUTHORITY = "com.jasper.myandroidtest.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
