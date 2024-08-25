package hemmouda.joojle.gui.panes.searchpane;

import hemmouda.joojle.api.Filter;
import hemmouda.joojle.api.Ranker;
import hemmouda.joojle.api.SignatureForger;
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
import java.util.List;
import java.util.Objects;

/**
 * The main pane that lets the user search
 * and see results
 */
public class SearchPane extends WindowPane {

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
    private FilterComboBox <MethodKind> kindFilter;
    private FilterComboBox <MethodVisibility> visibilityFilter;
    private FilterComboBox <MethodScope> scopeFilter;
    private PlaceholderTextField searchField;


    public SearchPane(Window parent, String jarFilePath, List <MethodRecord> loadedMethods) {
        super(parent);

        this.jarFilePath = jarFilePath;
        this.loadedMethods = loadedMethods;

        this.filtersHash = -1;
        this.filteredMethods = null;

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

        // Then finally, the help button
        filterAndSearchPanel.add(createHelpButton());

        // And ofc add the panel itself
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
                searchAndFilter();
            }
        });

        searchField.setPlaceholder("returnType (param1Type, param2Type <K,V>, ..)");
        Font font = searchField.getFont();
        searchField.setFont(new Font(font.getFontName(), font.getStyle(), Const.FONT_SIZE));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
    }

    private JButton createHelpButton () {
        JButton button = new JButton(Icons.HELP_ICON);

        button.addActionListener(event -> {
            final String helpMessage = """
                    Start writing a query to show the best matching results
                    from the loaded %d methods and constructors. You can
                    also further filter the results according to kind,
                    visibility, and scope.
                    
                    Queries describe the signature of the method you are
                    looking for. Their structure is as follows:
                    returnType (param1Type, param2Type, ..)
                    You should only specify the simple name of a type, and
                    not the fully qualified name. For example, if you are
                    looking for a method that returns a String and takes
                    an array of int, you would type:
                    `String (int [])`, and not: `java.lang.String (int [])`.
                    As for generic types, you would do them same as how you
                    would in the method declaration, but of course without
                    tha parameter name..
                    Like so `List <T> (Map <? extends T, Integer>)` for example.
                    Void return type is specified by `void` and constructors
                    have a return type of the class they are instantiating.
                    
                    More information could be found on:
                    https://github.com/telos-matter/Joojle
                    """.formatted(loadedMethods.size());

            MessageWindow.showInfo(helpMessage, " - Help");
        });

        return button;
    }

    private void initResultList() {
        // Create
        resultList = new DefaultListModel<>();
        // Populate with what we already have
        for (MethodRecord method : loadedMethods) {
            resultList.addElement(method);
        }
    }

    private JScrollPane createScrollPane () {
        JList <MethodRecord> list = new JList<>(resultList);
        list.setCellRenderer(new ListCellRenderer());
        Font font = list.getFont();
        list.setFont(new Font(font.getFontName(), font.getStyle(), Const.FONT_SIZE));

        return new JScrollPane(list);
    }

    private void searchAndFilter () {
        SwingUtilities.invokeLater(() -> {
            // Get the query and simplify it
            String query = searchField.getText();
            query = SignatureForger.simplifyQuery(query);

            // Update the filtered methods
            updateFilteredMethods();

            // Get the ranked results
            var results = Ranker.rank(query, filteredMethods);

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
