package seedu.jelphabot.model.task;

import static seedu.jelphabot.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import seedu.jelphabot.commons.core.index.Index;

/**
 * A container for ObservableList&lt;Task&gt; that splits the TaskList into groups.
 * GroupedByDateTaskList groups Tasks by their module codes.
 * Separation is done over @code{ObservableList} through use of filters.
 * <p>
 */
public class GroupedByModuleTaskList implements GroupedTaskList {
    private final List<SubGroupTaskList> moduleCodeTaskLists = new ArrayList<>();
    private final ObservableSet<ModuleCode> moduleCodes = FXCollections.observableSet();
    private final NumberBinding sizeBinding;

    public GroupedByModuleTaskList(ObservableList<Task> taskList, PinnedTaskList pinnedTasks) {
        requireAllNonNull(taskList);
        moduleCodes.addAll(getUniqueModuleSet(taskList));

        moduleCodeTaskLists.add(pinnedTasks);
        NumberBinding tempSize = Bindings.createIntegerBinding(pinnedTasks::size);
        for (ModuleCode code : moduleCodes) {
            ObservableList<Task> moduleCodeSubList = taskList.filtered(hasModuleCode(code));
            moduleCodeTaskLists.add(new SubGroupTaskList(code.toString(), moduleCodeSubList));
            tempSize = tempSize.add(Bindings.size(moduleCodeSubList));
        }
        this.sizeBinding = tempSize;
    }

    private static HashSet<ModuleCode> getUniqueModuleSet(List<Task> taskList) {
        HashSet<ModuleCode> moduleSet = new HashSet<>();
        for (Task task : taskList) {
            moduleSet.add(task.getModuleCode());
        }
        return moduleSet;
    }

    /**
     * @param moduleCode The ModuleCode to be tested
     * @return a predicate which tests Tasks for the parameter module code.
     */
    private Predicate<Task> hasModuleCode(ModuleCode moduleCode) {
        return task -> task.getModuleCode().equals(moduleCode);
    }

    @Override
    public Iterator<SubGroupTaskList> iterator() {
        return moduleCodeTaskLists.iterator();
    }

    @Override
    public Category getCategory() {
        return Category.MODULE;
    }

    @Override
    public int size() {
        return sizeBinding.intValue();
    }

    @Override
    public Task get(int id) {
        assert id < size();
        for (SubGroupTaskList sublist : moduleCodeTaskLists) {
            if (id < sublist.size()) {
                return sublist.get(id);
            } else {
                id -= sublist.size();
            }
        }
        return null;
    }

    @Override
    public Task get(Index index) {
        return get(index.getZeroBased());
    }
}

