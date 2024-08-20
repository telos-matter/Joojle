package hemmouda.joojle.gui.panes;

import hemmouda.joojle.api.Filter;
import hemmouda.joojle.api.Ranker;
import hemmouda.joojle.api.SignatureForger;
import hemmouda.joojle.api.core.MethodRecord;
import hemmouda.joojle.api.core.MethodScore;
import hemmouda.joojle.api.core.methodinfo.MethodScope;
import hemmouda.joojle.api.core.methodinfo.MethodKind;
import hemmouda.joojle.api.core.methodinfo.MethodVisibility;
import hemmouda.joojle.gui.util.FilterComboBox;
import hemmouda.joojle.gui.util.PlaceholderTextField;
import hemmouda.joojle.gui.Window;
import hemmouda.joojle.gui.WindowPane;
import hemmouda.joojle.gui.icons.Icons;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Map;
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

    private DefaultListModel <String> resultList;
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

        // Where the results are going to be shown
        resultList = new DefaultListModel<>();
        // Create the JScrollPane in one go to avoid using so many variable names
        add(new JScrollPane(new JList<>(resultList)), BorderLayout.CENTER);
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
                Map.of(
                        MethodKind.METHOD, Color.green,
                        MethodKind.CONSTRUCTOR, Color.red
                ),
                this::searchAndFilter);
    }

    private void initVisibilityFilter () {
        visibilityFilter = new FilterComboBox<>(
                MethodVisibility.values(),
                "Any visibility",
                Map.of(
                        MethodVisibility.PUBLIC, Color.green,
                        MethodVisibility.PRIVATE, Color.red,
                        MethodVisibility.PROTECTED, Color.orange
                ),
                this::searchAndFilter);
    }

    private void initScopeFilter () {
        scopeFilter = new FilterComboBox<>(
                MethodScope.values(),
                "Any scope",
                Map.of(
                        MethodScope.INSTANCE, Color.green,
                        MethodScope.STATIC, Color.red
                ),
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
            public void changedUpdate(DocumentEvent e) {

            }


            private void textChanged() {
                searchAndFilter();
            }
        });

        searchField.setPlaceholder("Search and filter from the loaded %d methods and constructors".formatted(loadedMethods.size()));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
    }

    private JButton createHelpButton () {
        JButton button = new JButton(Icons.HELP_ICON);

        button.addActionListener(event -> {
            String help = """
                    Need some help? lol
                    So do I.
                    """;
            JOptionPane.showMessageDialog(null, help, Window.WINDOW_TITLE_PREFIX + " - Help", JOptionPane.INFORMATION_MESSAGE);
        });

        return button;
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
                resultList.addElement(result.method().toString());
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
            MethodKind type = getEnumValue((String) kindFilter.getSelectedItem(), MethodKind.values());
            MethodVisibility visibility = getEnumValue((String) visibilityFilter.getSelectedItem(), MethodVisibility.values());
            MethodScope scope = getEnumValue((String) scopeFilter.getSelectedItem(), MethodScope.values());

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
                kindFilter.getSelectedItem(),
                visibilityFilter.getSelectedItem(),
                scopeFilter.getSelectedItem());
    }

    /**
     * @return the appropriate enum value
     * or <code>null</code> if none match
     */
    private static <T extends Enum<T>> T getEnumValue (String value, T [] enumValues) {
        value = value.toUpperCase();

        for (T enumValue : enumValues) {
            if (enumValue.toString().toUpperCase().equals(value)) {
                return enumValue;
            }
        }

        return null;
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
