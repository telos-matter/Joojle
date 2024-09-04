package hemmouda.joojle.gui.panes.searchpane;

import hemmouda.joojle.api.Filter;
import hemmouda.joojle.api.QueryHandler;
import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.api.core.MethodScore;
import hemmouda.joojle.api.core.methodinfo.MethodScope;
import hemmouda.joojle.api.core.methodinfo.MethodKind;
import hemmouda.joojle.api.core.methodinfo.MethodVisibility;
import hemmouda.joojle.gui.panes.searchpane.util.Const;
import hemmouda.joojle.gui.panes.searchpane.util.FilterComboBox;
import hemmouda.joojle.gui.panes.searchpane.util.ListCellRenderer;
import hemmouda.joojle.gui.util.MessageWindow;
import hemmouda.joojle.gui.util.PlaceholderTextField;
import hemmouda.joojle.gui.Window;
import hemmouda.joojle.gui.WindowPane;
import hemmouda.joojle.gui.icons.Icons;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.Objects;

/**
 * The main pane that lets the user search
 * and see results
 */
public class SearchPane extends WindowPane {

    private static final String NO_FILTER_PLACEHOLDER = "Search through the %d loaded methods";
    private static final String FILTER_APPLIED_PLACEHOLDER = "Search among the %d filtered results";

    private static final int SEARCH_COOLDOWN_MS = 0;

    /**
     * The loaded methods on which the search
     * and filtering will be preformed
     */
    private final List<MethodRecord> loadedMethods;
    /**
     * The path of the JAR file that has been loaded.
     * Only used to display information
     */
    private final String jarFilePath;

    /**
     * The hash of the last used filters
     */
    private int filtersHash;
    /**
     * The result of the last filter.
     * Used alongside {@link #filtersHash}
     * to not call the filter method twice
     * if the filters didn't change.
     */
    private List<MethodRecord> filteredMethods;

    private DefaultListModel <MethodRecord> resultList;
    private ListCellRenderer listCellRenderer;
    private FilterComboBox <MethodKind> kindFilter;
    private FilterComboBox <MethodVisibility> visibilityFilter;
    private FilterComboBox <MethodScope> scopeFilter;
    private PlaceholderTextField searchField;

    /**
     * When was the last search performed?
     */
    private long lastSearch;
    /**
     * A timer that goes off when
     * the search cooldown elapses.
     */
    private Timer searchTimer;


    public SearchPane(Window parent, String jarFilePath, List <MethodRecord> loadedMethods) {
        super(parent);

        this.jarFilePath = jarFilePath;
        this.loadedMethods = loadedMethods;

        this.filtersHash = -1;
        this.filteredMethods = null;

        this.lastSearch = -1;

        init();
    }

    private void init () {
        // Simple layout
        setLayout(new BorderLayout());

        // Where the filters and search filed is going to be
        JPanel filterAndSearchPanel = new JPanel();
        filterAndSearchPanel.setLayout(new BoxLayout(filterAndSearchPanel, BoxLayout.X_AXIS));

        // The return button first
        filterAndSearchPanel.add(createReturnButton());

        // Then initialize the filters and search field and add them
        initKindFilter();
        filterAndSearchPanel.add(kindFilter);
        initVisibilityFilter();
        filterAndSearchPanel.add(visibilityFilter);
        initScopeFilter();
        filterAndSearchPanel.add(scopeFilter);

        initSearchField();
        filterAndSearchPanel.add(searchField);

        // Add the panel itself
        add(filterAndSearchPanel, BorderLayout.SOUTH);

        // Init resultList where the results are populated
        initResultList();
        // And then create the scroll pane
        add(createScrollPane(), BorderLayout.CENTER);

        // Focus on searchField
        SwingUtilities.invokeLater(() -> {
            searchField.requestFocusInWindow();
        });
    }

    private JButton createReturnButton () {
        JButton button = new JButton(Icons.RETURN_ICON);
        button.addActionListener(event -> {
            parent.showSelectionWindow();
        });
        return button;
    }

    private void initKindFilter() {
        kindFilter = new FilterComboBox<>(
                MethodKind.values(),
                "Any kind",
                Const.METHOD_KIND_COLOR,
                this::searchAndFilter);
    }

    private void initVisibilityFilter () {
        visibilityFilter = new FilterComboBox<>(
                MethodVisibility.values(),
                "Any visibility",
                Const.METHOD_VISIBILITY_COLOR,
                this::searchAndFilter);
    }

    private void initScopeFilter () {
        scopeFilter = new FilterComboBox<>(
                MethodScope.values(),
                "Any scope",
                Const.METHOD_SCOPE_COLOR,
                this::searchAndFilter);
    }

    private void initSearchField () {
        searchField = new PlaceholderTextField();

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}

            private void textChanged() {
                SwingUtilities.invokeLater(() -> {
                    String text = searchField.getText();
                    // If asking for help
                    if (text.equals(Const.HELP_CMD)) {
                        // Clean searchField then show it
                        searchField.setText("");
                        showHelp();
                    // Or if asking to switch colors
                    } else if (text.equals(Const.FLIP_COLOR_CMD)) {
                        // Clean searchField and flip
                        searchField.setText("");
                        flipColor();
                    // Otherwise call for searchAndFilter
                    } else {
                        searchAndFilter();
                    }
                });
            }
        });

        String placeholder = "Type `%s` for help".formatted(Const.HELP_CMD);
        searchField.setPlaceholder(placeholder);
        Font font = searchField.getFont();
        searchField.setFont(new Font(font.getFontName(), font.getStyle(), Const.FONT_SIZE));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
    }

    private void showHelp () {
        MessageWindow.showLargeInfo(Const.HTML_HELP_MESSAGE, " - Help");
    }

    private void flipColor () {
        listCellRenderer.flipColorful();
        // Only need to repaint the JList or ScrollPane
        // but that means keeping a reference to it. So eh.
        repaint();
    }

    private void initResultList() {
        resultList = new DefaultListModel<>();
        // Populate with what we already have
        for (MethodRecord method : loadedMethods) {
            resultList.addElement(method);
        }
    }

    private JScrollPane createScrollPane () {
        listCellRenderer = new ListCellRenderer();

        JList <MethodRecord> list = new JList<>(resultList);
        list.setCellRenderer(listCellRenderer);
        Font font = list.getFont();
        list.setFont(new Font(font.getFontName(), font.getStyle(), Const.FONT_SIZE));

        return new JScrollPane(list);
    }

    /**
     * Searches and filters once the cooldown
     * has elapsed.
     */
    private void searchAndFilter () {
        // If searchTimer is set then do nothing.
        // A search is scheduled to go off
        if (searchTimer != null) {
            return;
        }

        // If not, check if cooldown has elapsed
        long elapsed = System.currentTimeMillis() - lastSearch;
        if (elapsed > SEARCH_COOLDOWN_MS) {
            // If it has elapsed, then search directly
            searchAndFilter0();
        } else {
            // If not, set a timer to go off after
            // the cooldown
            int remainder = (int)(SEARCH_COOLDOWN_MS - elapsed);
            searchTimer = new Timer(remainder, (event) -> {
                searchAndFilter0();
            });
            searchTimer.setRepeats(false);
            searchTimer.start();
        }
    }

    /**
     * What actually does the search
     * and filtering
     */
    private void searchAndFilter0 () {
        // Reset last and searchTimer
        lastSearch = System.currentTimeMillis();
        searchTimer = null;

        SwingUtilities.invokeLater(() -> {
            // Get the query
            String query = searchField.getText();

            // Update the filtered methods
            updateFilteredMethods();

            // Get the ranked results
            var results = QueryHandler.handle(query, filteredMethods);

            // Clear what is displayed
            resultList.removeAllElements();
            // Display the results
            for (MethodScore result : results) {
                resultList.addElement(result.method());
            }
        });
    }

    /**
     * Updates {@link #filteredMethods}
     * if it needs to be updated
     */
    private void updateFilteredMethods() {
        int currentFiltersHash = getFiltersHash();
        if (currentFiltersHash != filtersHash) {

            // Get the filters
            MethodKind type = kindFilter.getSelectedEnum();
            MethodVisibility visibility = visibilityFilter.getSelectedEnum();
            MethodScope scope = scopeFilter.getSelectedEnum();

            // Update
            filteredMethods = Filter.filter(loadedMethods, type, visibility, scope);
            filtersHash = currentFiltersHash;

            // Update placeholder
            String placeholder;
            if (type == null && visibility == null && scope == null) {
                placeholder = NO_FILTER_PLACEHOLDER;
            } else {
                placeholder = FILTER_APPLIED_PLACEHOLDER;
            }
            SwingUtilities.invokeLater(() -> {
                searchField.setPlaceholder(placeholder.formatted(filteredMethods.size()));
                searchField.repaint();
            });
        }
    }

    /**
     * @return a unique hash for the currently
     * selected filters
     */
    private int getFiltersHash () {
        return Objects.hash(
                kindFilter.getSelectedEnum(),
                visibilityFilter.getSelectedEnum(),
                scopeFilter.getSelectedEnum());
    }

    @Override
    public Dimension getPreferredWindowSize() {
        return new Dimension(1000, 600);
    }

    @Override
    public String getPreferredTitle() {
        return jarFilePath;
    }
}
