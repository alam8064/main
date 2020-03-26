package seedu.jelphabot.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.jelphabot.commons.core.LogsCenter;
import seedu.jelphabot.model.calendar.CalendarDate;

/**
 * UI component for calendar view to be displayed.
 */
public class CalendarPanel extends UiPart<Region> {
    private static final String FXML = "CalendarPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CalendarPanel.class);

    private CalendarDate calendarDate;
    private ArrayList<CalendarDayCard> monthDayCards;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthYear;

    public CalendarPanel(CalendarDate calendarDate) {
        super(FXML);
        this.calendarDate = calendarDate;
        monthYear.setText(calendarDate.getMonthName() + ", " + calendarDate.getYear());

        CalendarDate firstDay = calendarDate.getFirstDay();
        fillGridPane(firstDay);
    }

    /**
     * Fills the grid pane of the calendar.
     * @param firstDay The date representing the first day of the month.
     */
    public void fillGridPane(CalendarDate firstDay) {
        int weekIndex = firstDay.getDayOfWeek() - 1;
        int lengthCurrMonth = firstDay.getLengthCurrMonth();
        int lengthPrevMonth = firstDay.getLengthPrevMonth();
        int day = lengthPrevMonth - weekIndex + 1;
        CalendarDate currDate = firstDay.createPrevMonthDate(day);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                CalendarDayCard calendarDayCard = new CalendarDayCard(currDate);
                if (currDate.isThisMonth()) {
                    calendarDayCard.setSameMonth();
                } else {
                    calendarDayCard.setDiffMonth();
                }
                if (currDate.isToday()) {
                    calendarDayCard.highlightToday();
                }
                calendarGrid.add(calendarDayCard.getRoot(), col, row);
                currDate = currDate.dateNextDay();
            }
        }
    }

}
