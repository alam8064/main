package seedu.jelphabot.logic.commands;

import static seedu.jelphabot.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.jelphabot.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.jelphabot.testutil.Assert.assertThrows;
import static seedu.jelphabot.testutil.TypicalIndexes.INDEX_THIRD_TASK;
import static seedu.jelphabot.testutil.TypicalTasks.getTypicalJelphaBot;

import org.junit.jupiter.api.Test;

import seedu.jelphabot.commons.core.Messages;
import seedu.jelphabot.commons.core.index.Index;
import seedu.jelphabot.model.Model;
import seedu.jelphabot.model.ModelManager;
import seedu.jelphabot.model.UserPrefs;
import seedu.jelphabot.model.reminder.Reminder;
import seedu.jelphabot.testutil.ReminderBuilder;

public class ReminderCommandTest {

    private Model model = new ModelManager(getTypicalJelphaBot(), new UserPrefs());

    @Test
    public void constructor_nullReminder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ReminderCommand(null, null));
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Reminder reminder = new ReminderBuilder().withIndex("2").build();
        ReminderCommand reminderCommand = new ReminderCommand(INDEX_THIRD_TASK, reminder);

        String expectedMessage = String.format(ReminderCommand.MESSAGE_SUCCESS, INDEX_THIRD_TASK.getOneBased());

        ModelManager expectedModel = new ModelManager(model.getJelphaBot(), new UserPrefs());
        expectedModel.addReminder(reminder);

        assertCommandSuccess(reminderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        int outOfBoundIndex = model.getFilteredTaskList().size() + 1;
        Reminder reminder = new ReminderBuilder().withIndex("" + outOfBoundIndex).build();
        ReminderCommand reminderCommand = new ReminderCommand(Index.fromOneBased(outOfBoundIndex), reminder);

        assertCommandFailure(reminderCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

}
